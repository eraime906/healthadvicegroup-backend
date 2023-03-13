package org.healthadvicegroup.endpoint;

import org.healthadvicegroup.endpoint.impl.*;
import spark.Request;
import spark.Response;
import spark.utils.Assert;
import sun.security.krb5.Credentials;

import java.sql.SQLOutput;
import java.util.HashMap;

public class EndpointManager {

    private static final HashMap<Class<? extends Endpoint>, Endpoint> endpoints = new HashMap<>();

    // Register endpoints
    static {
        registerEndpoint(new UsernameValidityEndpoint());
        registerEndpoint(new AccountCreationEndpoint());
        registerEndpoint(new GetArticlesEndpoint());
        registerEndpoint(new GetLocationsEndpoint());
        registerEndpoint(new GetLocationDataEndpoint());
        registerEndpoint(new CredentialsValidationEndpoint());
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
     *
     * @return the {@link Response} from the endpoint to be sent back
     */
    public static Response executeEndpoint(Class<? extends Endpoint> clazz, Request request, Response response) {
        Assert.isTrue(endpoints.containsKey(clazz), String.format("No endpoint for class %s was found", clazz.getSimpleName()));
        Endpoint endpoint = endpoints.get(clazz);
        response.type("application/json");
        System.out.printf("Executing %s\n", endpoint.getClass().getSimpleName());
        return endpoint.handle(request, response);
    }
}
