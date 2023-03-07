package org.healthadvicegroup.endpoint.impl;

import org.healthadvicegroup.endpoint.Endpoint;
import spark.Request;
import spark.Response;
import spark.Route;

public class TestEndpoint extends Endpoint {
    @Override
    public String handle(Request request, Response response) {
        return "done";
    }
}
