package org.healthadvicegroup.forecasting;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.healthadvicegroup.database.Serializable;
import org.healthadvicegroup.forecasting.apis.TomorrowIOAirQualityHook;

@Getter
public class WeatherLocation implements Serializable<WeatherLocation> {
    private final String locationName;
    private final float latitude;
    private final float longitude;
    private JsonObject weatherData;
    private JsonObject airQualityData;

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
        System.out.println("Updated weather data for " + this.locationName);
    }

    /**
     * Called by {@link TomorrowIOAirQualityHook#updateAirQualityData(WeatherLocation)}
     * to update this location's air quality data
     *
     * @param object the air quality data to update this location with
     */
    public void updateAirQualityData(JsonObject object) {
        this.airQualityData = object;
        System.out.println("Updated air quality data for " + this.locationName);
    }

    /**
     * @return shortcut to the main JSON weather data object
     */
    private JsonObject getMainWeatherDataObject() {
        return this.weatherData.get("main").getAsJsonObject();
    }

    /**
     * @return shortcut to the main JSON air quality data object
     */
    private JsonObject getMainAirQualityDataObject() {
        return this.airQualityData.get("data")
                .getAsJsonObject()
                .get("timelines")
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject()
                .get("intervals")
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject()
                .get("values")
                .getAsJsonObject();
    }

    public String getWeatherDescription() {
        return this.weatherData.get("weather")
                .getAsJsonArray()
                .get(0)
                .getAsJsonObject()
                .get("description")
                .getAsString();
    }

    public double getRealTemperature() {
        return ForecastManager.kelvinToCelsius(this.getMainWeatherDataObject().get("temp").getAsDouble());
    }

    public double getFeelsLikeTemperature() {
        return ForecastManager.kelvinToCelsius(this.getMainWeatherDataObject().get("feels_like").getAsDouble());
    }

    public int getPressure() {
        return this.getMainWeatherDataObject().get("pressure").getAsInt();
    }

    public int getHumidity() {
        return this.getMainWeatherDataObject().get("humidity").getAsInt();
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

    public int getGrassPollenIndex() {
        return this.getMainAirQualityDataObject().get("grassIndex").getAsInt();
    }

    public int getTreePollenIndex() {
        return this.getMainAirQualityDataObject().get("treeIndex").getAsInt();
    }

    public int getWeedPollenIndex() {
        return this.getMainAirQualityDataObject().get("weedIndex").getAsInt();
    }

    @Override
    public JsonElement toJson(WeatherLocation object, Gson gson) {
        JsonObject json = new JsonObject();
        json.addProperty("weather-desc", this.getWeatherDescription());
        json.addProperty("real-temp", this.getRealTemperature());
        json.addProperty("feels-like-temp", this.getFeelsLikeTemperature());
        json.addProperty("pressure", this.getPressure());
        json.addProperty("humidity", this.getHumidity());
        json.addProperty("wind-speed", this.getWindSpeed());
        json.addProperty("wind-bearing", this.getWindBearing());
        json.addProperty("raining", this.isRaining());
        json.addProperty("cloud-rating", this.cloudRating());
        json.addProperty("grass-pollen", this.getGrassPollenIndex());
        json.addProperty("tree-pollen", this.getTreePollenIndex());
        json.addProperty("weed-pollen", this.getWeedPollenIndex());
        return json;
    }
}
