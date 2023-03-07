package org.healthadvicegroup.endpoint;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.healthadvicegroup.Main;
import spark.Request;
import spark.Response;
import spark.Route;

@Getter
public abstract class Endpoint {

    public Endpoint() {
        EndpointManager.registerEndpoint(this);
    }

    /**
     * Used to quickly deserialize HTTP bodies into a key-value format
     *
     * @param body the HTTP body to deserialize
     *
     * @return the deserialized key-value object
     */
    public JsonObject deserializeBody(String body) {
        return (JsonObject) Main.getGSON().fromJson(body, JsonElement.class);
    }

    /**
     * Abstract function implemented by each endpoint to handle a HTTP request
     *
     * @param request the request object
     * @param response the response object
     *
     * @return the finished response object
     */
    public abstract Response handle(Request request, Response response);

}
