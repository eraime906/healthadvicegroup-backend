package org.healthadvicegroup.endpoint;

import org.healthadvicegroup.endpoint.impl.UsernameHeadEndpoint;
import spark.Request;
import spark.Response;
import spark.utils.Assert;

import java.util.HashMap;

public class EndpointManager {

    private static final HashMap<Class<? extends Endpoint>, Endpoint> endpoints = new HashMap<>();

    // Register endpoints
    static {
        registerEndpoint(new UsernameHeadEndpoint());
    }

    /**
     * Register the provided {@link Endpoint} to {@link #endpoints}
     *
     * @param endpoint the endpoint instance to register
     */
    public static void registerEndpoint(Endpoint endpoint) {
        endpoints.put(endpoint.getClass(), endpoint);
    }

    /**
     * Fetch and execute the provided {@link Class<>} endpoint from {@link #endpoints}
     *
     * @param clazz the endpoint to fetch and execute
     * @param request the web request to handle
     * @param response the resulting web response object
     */
    public static Response executeEndpoint(Class<? extends Endpoint> clazz, Request request, Response response) {
        Assert.isTrue(endpoints.containsKey(clazz), String.format("No endpoint for class %s was found", clazz.getSimpleName()));
        Endpoint endpoint = endpoints.get(clazz);
        return endpoint.handle(request, response);
    }
}
