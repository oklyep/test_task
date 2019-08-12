package ru.center_it.test_task.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class WeatherCache {
    private Map<String, WeatherData> weatherDataMap = new HashMap<>();
    private Map<String, WeatherService> weatherServiceMap = new HashMap<>();
    private Map<String, String[]> cities;

    private long expiredMs;

    @Autowired
    public WeatherCache(List<WeatherService> weatherServices,
                        CitiesConfig citiesConfig,
                        @Value("${weather.expiredSec}") int expiredSec) {
        weatherServices.forEach(weatherService -> weatherServiceMap.put(weatherService.getId(), weatherService));
        cities = citiesConfig.getCities();
        expiredMs = TimeUnit.SECONDS.toMillis(expiredSec);
    }

    @Nullable
    public WeatherData getWeatherData(String city, String weatherService) throws IOException {
        String key = city + weatherService;

        WeatherData weatherData = weatherDataMap.get(key);

        if (weatherData == null || weatherData.getExpired() < new Date().getTime()) {
            String[] cityDescription = cities.get(city);
            WeatherService service = weatherServiceMap.get(weatherService);

            if (cityDescription == null || service == null) return null;

            weatherData = service.getWeather(cityDescription[1], cityDescription[2]);
            weatherData.setExpired(new Date().getTime() + expiredMs);
            weatherDataMap.put(key, weatherData);
        }

        return weatherData;
    }
}
