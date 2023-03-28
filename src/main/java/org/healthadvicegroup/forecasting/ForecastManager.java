package org.healthadvicegroup.forecasting;

import org.healthadvicegroup.forecasting.apis.OpenWeatherMapHook;
import org.healthadvicegroup.forecasting.apis.TomorrowIOAirQualityHook;

import java.util.*;

public class ForecastManager {

    private static final HashSet<WeatherLocation> weatherLocations = new HashSet<WeatherLocation>(){{
        add(new WeatherLocation("Southampton", 50.904968f, -1.403230f));
        add(new WeatherLocation("London", 51.507200f, 0.127600f));
        add(new WeatherLocation("Manchester", 53.480800f, 2.242600f));
        add(new WeatherLocation("Birmingham", 52.486200f, 1.890400f));
        add(new WeatherLocation("Plymouth", 50.375500f, 4.142700f));
        add(new WeatherLocation("Edinburgh", 55.953300f, 3.188300f));
    }};

    public static void init() {
        // Update data every 60 min
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Updating location data");
                for (WeatherLocation weatherLocation : weatherLocations) {
                    OpenWeatherMapHook.updateWeatherData(weatherLocation);
                    if (!TomorrowIOAirQualityHook.rateLimited) {
                        TomorrowIOAirQualityHook.updateAirQualityData(weatherLocation);
                    }
                }
            }
        }, 0, 1000 * 60 * 60);
    }

    /**
     * @return a list of all supported location names
     */
    public static List<String> getLocationNames() {
        List<String> locations = new ArrayList<>();
        for (WeatherLocation location : weatherLocations) {
            locations.add(location.getLocationName());
        }
        return locations;
    }

    /**
     * Fetch a {@link WeatherLocation} by name
     * <p>
     * This method is case-sensitive as exact names are provided by {@link #getLocationNames()}
     *
     * @param name the name of the location to fetch
     *
     * @return the location, if one exists with the provided name
     */
    public static WeatherLocation getLocation(String name) {
        for (WeatherLocation weatherLocation : weatherLocations) {
            if (weatherLocation.getLocationName().equals(name)) {
                return weatherLocation;
            }
        }
        return null;
    }

    /**
     * Quick and accurate conversion from Kelvin to Celsius
     * <p>
     * This is used because the temperature returned by the weather API records data in Kelvin
     *
     * @param kelvin the Kelvin temperature to convert
     *
     * @return the provided Kelvin temperature in Celsius
     */
    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
}
