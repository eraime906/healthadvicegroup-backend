package org.healthadvicegroup.endpoint.impl;

import org.healthadvicegroup.Main;
import org.healthadvicegroup.endpoint.Endpoint;
import org.healthadvicegroup.forecasting.ForecastManager;
import org.healthadvicegroup.forecasting.WeatherLocation;
import spark.Request;
import spark.Response;

public class GetLocationDataEndpoint extends Endpoint {
    @Override
    public Response handle(Request request, Response response) {
        WeatherLocation location = ForecastManager.getLocation(request.params(":location"));
        if (location == null) {
            response.body("Invalid location name");
            response.status(404);
            return response;
        }
        response.body(location.toJson(location, Main.getGSON()).toString());
        return response;
    }
}
