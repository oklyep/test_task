package ru.center_it.test_task.weather;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "weather")
public class CitiesConfig {
    private Map<String, String[]> cities;

    public Map<String, String[]> getCities() {
        return this.cities;
    }

    public void setCities(Map<String, String[]> cities) {
        this.cities = cities;
    }
}
