package ru.center_it.test_task.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.center_it.test_task.weather.CitiesConfig;
import ru.center_it.test_task.weather.WeatherCache;
import ru.center_it.test_task.weather.WeatherData;
import ru.center_it.test_task.weather.WeatherService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WeatherController {
    private Map<String, String> cities = new HashMap<>();
    private Map<String, String> weatherServices = new HashMap<>();
    private WeatherCache weatherCache;

    @Autowired
    public WeatherController(List<WeatherService> weatherServices,
                             CitiesConfig citiesConfig,
                             WeatherCache weatherCache) {
        weatherServices.forEach(weatherService -> this.weatherServices.put(weatherService.getId(), weatherService.getName()));
        citiesConfig.getCities().forEach((name, description) -> this.cities.put(name, description[0]));
        this.weatherCache = weatherCache;
    }

    @GetMapping("/cities")
    public Map<String, String> getCities() {
        return cities;
    }

    @GetMapping("/weatherServices")
    public Map<String, String> getWeatherServices() {
        return weatherServices;
    }

    @GetMapping("/weather")
    public WeatherData getWeather(@RequestParam String city,
                                  @RequestParam String weatherService) throws IOException {
        return weatherCache.getWeatherData(city, weatherService);
    }
}
