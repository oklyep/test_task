package ru.center_it.test_task.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.center_it.test_task.weather.CitiesConfig;
import ru.center_it.test_task.weather.WeatherService;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableConfigurationProperties(CitiesConfig.class)
@AutoConfigureMockMvc
public class WeatherControllerTest {
    @Autowired
    private CitiesConfig citiesConfig;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    List<WeatherService> weatherServiceList;

    @Test
    public void integrationRequestWeather() throws Exception {
        String city = citiesConfig.getCities().keySet().iterator().next();

        for (WeatherService weatherService : weatherServiceList)
            mockMvc
                    .perform(get(String.format("/weather?city=%s&weatherService=%s", city, weatherService.getId())))
                    .andExpect(status().isOk());
    }
}
