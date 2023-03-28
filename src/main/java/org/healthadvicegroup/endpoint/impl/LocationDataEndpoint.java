package org.healthadvicegroup.endpoint.impl;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import org.healthadvicegroup.Main;
import org.healthadvicegroup.endpoint.Endpoint;
import org.healthadvicegroup.forecasting.WeatherLocation;
import org.healthadvicegroup.forecasting.apis.OpenWeatherMapHook;
import org.healthadvicegroup.forecasting.apis.TomorrowIOAirQualityHook;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class LocationDataEndpoint extends Endpoint {

    @Override
    @SneakyThrows
    public Response handle(Request request, Response response) {
        float lat, lon;
        try {
            lat = Float.parseFloat(request.params(":lat"));
            lon = Float.parseFloat(request.params(":lon"));
        } catch (NumberFormatException ex) {
            response.body("Malformed or missing lat or lon parameter");
            response.status(400);
            return response;
        }
        WeatherLocation customLocation = new WeatherLocation("custom", lat, lon);
        new Thread(() -> {
            OpenWeatherMapHook.updateWeatherData(customLocation);
            TomorrowIOAirQualityHook.updateAirQualityData(customLocation);
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(500);
        response.body(customLocation.toJson(customLocation, Main.getGSON()).toString());
        return response;
    }
}

