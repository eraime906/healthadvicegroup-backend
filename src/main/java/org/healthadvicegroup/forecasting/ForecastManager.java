package org.healthadvicegroup.forecasting;

import org.healthadvicegroup.forecasting.apis.OpenWeatherMapHook;
import org.healthadvicegroup.forecasting.apis.TomorrowIOAirQualityHook;

import java.util.*;

public class ForecastManager {

    private static final HashSet<WeatherLocation> weatherLocations = new HashSet<WeatherLocation>(){{
        add(new WeatherLocation("Southampton", 50.904968f, -1.403230f));
    }};

    public static void init() {
        // Update data every 30 min
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Updating location data");
                for (WeatherLocation weatherLocation : weatherLocations) {
                    OpenWeatherMapHook.updateWeatherData(weatherLocation);
                    TomorrowIOAirQualityHook.updateAirQualityData(weatherLocation);
                }
            }
        }, 0, 1000 * 60 * 30);
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
     *
     * @param name the name of the location to fetch
     *
     * @return the location, if one exists
     */
    public static WeatherLocation getLocation(String name) {
        for (WeatherLocation weatherLocation : weatherLocations) {
            if (weatherLocation.getLocationName().equals(name)) {
                return weatherLocation;
            }
        }
        return null;
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
}
