package ru.center_it.test_task.weather;

import java.io.IOException;

public interface WeatherService {
    String getId();

    String getName();

    WeatherData getWeather(String lat, String lon) throws IOException;
}
