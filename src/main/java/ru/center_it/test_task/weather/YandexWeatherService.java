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
public class YandexWeatherService implements WeatherService {

    @Value("${weather.tokens.yandex}")
    private String token;

    @Override
    public String getId() {
        return "yandex";
    }

    @Override
    public String getName() {
        return "Яндекс Погода";
    }

    @Override
    public WeatherData getWeather(String lat, String lon) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(String.format("https://api.weather.yandex.ru/v1/forecast?lat=%s&lon=%s", lat, lon))
                .header("X-Yandex-API-Key", token)
                .build();

        String json;
        try (Response response = client.newCall(request).execute()) {
            json = response.body().string();
        }

        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        return new WeatherData(
                JsonPath.read(document, "$.fact.temp"),
                JsonPath.read(document, "$.fact.wind_speed"),
                JsonPath.read(document, "$.fact.humidity"));
    }
}
