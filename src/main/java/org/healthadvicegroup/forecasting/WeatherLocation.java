package org.healthadvicegroup.forecasting;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;

@Getter
public class WeatherLocation {
    private final String locationName;
    private final float latitude;
    private final float longitude;
    private JsonObject weatherData;

    public WeatherLocation(String locationName, float latitude, float longitude) {
        this.locationName = locationName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Called by {@link org.healthadvicegroup.forecasting.apis.OpenWeatherMapHook#updateWeatherData(WeatherLocation)}
     * to update this location's weather data
     *
     * @param object the weather data object to update this location with
     */
    public void updateWeatherData(JsonObject object) {
        this.weatherData = object;
    }

    /**
     * @return shortcut to the main JSON weather data object
     */
    private JsonObject getMainDataObject() {
        return this.weatherData.get("main").getAsJsonObject();
    }

    public String getWeatherDescription() {
        return this.weatherData.get("weather").getAsJsonArray().get(0).getAsJsonObject().get("description").getAsString();
    }

    public double getRealTemperature() {
        return ForecastManager.kelvinToCelsius(this.getMainDataObject().get("temp").getAsDouble());
    }

    public double getFeelsLikeTemperature() {
        return ForecastManager.kelvinToCelsius(this.getMainDataObject().get("feels_like").getAsDouble());
    }

    public int getPressure() {
        return this.getMainDataObject().get("pressure").getAsInt();
    }

    public int getHumidity() {
        return this.getMainDataObject().get("humidity").getAsInt();
    }

    public double getWindSpeed() {
        return this.getWeatherData().get("wind").getAsJsonObject().get("speed").getAsDouble();
    }

    public double getWindBearing() {
        return this.getWeatherData().get("wind").getAsJsonObject().get("deg").getAsDouble();
    }

    public boolean isRaining() {
        return this.getWeatherData().get("rain").getAsJsonObject().get("1h").getAsInt() == 1;
    }

    public int cloudRating() {
        return this.getWeatherData().get("clouds").getAsJsonObject().get("all").getAsInt();
    }
}
