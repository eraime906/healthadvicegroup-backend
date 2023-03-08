package org.healthadvicegroup.forecasting;

import org.healthadvicegroup.forecasting.apis.OpenWeatherMapHook;
import org.healthadvicegroup.forecasting.apis.TomorrowIOAirQualityHook;

import java.util.HashSet;
import java.util.Timer;
import java.util.TimerTask;

public class ForecastManager {

    private static final HashSet<WeatherLocation> weatherLocations = new HashSet<WeatherLocation>(){{
        add(new WeatherLocation("Southampton", 50.904968f, -1.403230f));
    }};

    public static void init() {
        // Update data every 30 min
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (WeatherLocation weatherLocation : weatherLocations) {
                    OpenWeatherMapHook.updateWeatherData(weatherLocation);
                    TomorrowIOAirQualityHook.updateAirQualityData(weatherLocation);
                }
            }
        }, 0, 1000 * 60 * 30);
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }
}
