package org.healthadvicegroup.endpoint.impl;

import org.healthadvicegroup.endpoint.Endpoint;
import spark.Request;
import spark.Response;

public class UsernameHeadEndpoint extends Endpoint {

    @Override
    public Response handle(Request request, Response response) {
        response.header("test", "this works!");
        response.status(204);
        return response;
    }
}
