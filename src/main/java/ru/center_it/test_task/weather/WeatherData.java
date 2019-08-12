package ru.center_it.test_task.weather;

public class WeatherData {
    private Number humidity;
    private Number temp;
    private Number wind;

    private long expired;

    public WeatherData(Number temp, Number wind, Number humidity) {
        this.temp = temp;
        this.wind = wind;
        this.humidity = humidity;
    }

    public Number getHumidity() {
        return humidity;
    }

    public Number getTemp() {
        return temp;
    }

    public Number getWind() {
        return wind;
    }

    long getExpired() {
        return expired;
    }

    void setExpired(long expired) {
        this.expired = expired;
    }
}
