package org.healthadvicegroup.endpoint.impl;

import org.healthadvicegroup.Main;
import org.healthadvicegroup.endpoint.Endpoint;
import org.healthadvicegroup.forecasting.ForecastManager;
import spark.Request;
import spark.Response;

public class GetLocationsEndpoint extends Endpoint {

    @Override
    public Response handle(Request request, Response response) {
        response.body(Main.getGSON().toJson(ForecastManager.getLocationNames()));
        return response;
    }
}
