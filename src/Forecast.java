public class Forecast {
    String time;
    Double rain;
    Double temperature;
    String humidity;
    public Forecast(String time,Double rain, Double temperature,String humidity)
    {
        this.time = time;
        this.rain = rain;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public String getTime() {
        return time;
    }

    public Double getRain() {
        return rain;
    }

    public Double getTemperature() {
        return temperature;
    }

    public String getHumidity() {
        return humidity;
    }
}
