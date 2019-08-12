package ru.center_it.test_task.weather;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties(CitiesConfig.class)
public class WeatherCacheTest {

    private int requests = 0;

    @Autowired
    private CitiesConfig citiesConfig;

    private void sleep(int ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException ignored) {
        }
    }


    private WeatherService mockService = new WeatherService() {
        @Override
        public String getId() {
            return "mock";
        }

        @Override
        public String getName() {
            return "mock";
        }

        @Override
        public WeatherData getWeather(String lat, String lon) {
            requests++;
            return new WeatherData(0, 0, 0);
        }
    };

    /**
     * После истечения заданного интервала времени происходит повторный запрос данных
     */
    @Test
    public void recordShouldExpire() throws IOException {

        String city = citiesConfig.getCities().keySet().iterator().next();
        WeatherCache weatherCache = new WeatherCache(Collections.singletonList(mockService), citiesConfig, 1);

        weatherCache.getWeatherData(city, mockService.getId());
        assertThat("Запрос при пустом кэше", requests, is(1));
        weatherCache.getWeatherData(city, mockService.getId());
        assertThat("В кэше есть свежая запись, запрос не делается", requests, is(1));
        sleep(1000);
        weatherCache.getWeatherData(city, mockService.getId());
        assertThat("После истечения срока должен быть повторный запрос", requests, is(2));
    }


    @Test
    public void shouldReturnNullIfCityOrServiceNotFound() throws IOException {
        WeatherCache weatherCache = new WeatherCache(Collections.singletonList(mockService), citiesConfig, 1);
        WeatherData weatherData = weatherCache.getWeatherData("notExists", "notExists");
        assertNull(weatherData);
    }
}