package org.healthadvicegroup.endpoint;

import lombok.Getter;
import spark.Request;
import spark.Response;
import spark.Route;

@Getter
public abstract class Endpoint {

    public Endpoint() {
        EndpointManager.registerEndpoint(this);
    }

    public abstract Response handle(Request request, Response response);

}
