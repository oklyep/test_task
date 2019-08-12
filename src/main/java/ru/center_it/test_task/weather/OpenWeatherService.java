package ru.center_it.test_task.weather;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OpenWeatherService implements WeatherService {

    @Value("${weather.tokens.openweather}")
    private String token;

    @Override
    public String getId() {
        return "openweather";
    }

    @Override
    public String getName() {
        return "openweathermap.org";
    }

    @Override
    public WeatherData getWeather(String lat, String lon) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(String.format("http://api.openweathermap.org/data/2.5/weather?units=metric&appid=%s&lat=%s&lon=%s", token, lat, lon))
                .build();

        String json;

        try (Response response = client.newCall(request).execute()) {
            json = response.body().string();
        }

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        return new WeatherData(
                JsonPath.read(document, "$.main.temp"),
                JsonPath.read(document, "$.wind.speed"),
                JsonPath.read(document, "$.main.humidity"));
    }
}
