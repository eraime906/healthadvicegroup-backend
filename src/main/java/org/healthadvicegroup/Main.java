package org.healthadvicegroup;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.healthadvicegroup.account.AccountManager;
import org.healthadvicegroup.article.ArticleManager;
import org.healthadvicegroup.database.MongoCollectionManager;
import org.healthadvicegroup.endpoint.EndpointManager;
import org.healthadvicegroup.endpoint.impl.*;
import org.healthadvicegroup.forecasting.ForecastManager;

import static spark.Spark.*;

public class Main {

    @Getter
    private static Gson GSON;

    @SuppressWarnings("DanglingJavadoc") // Used to document endpoints
    public static void main(String[] args) {
        // Setup gson
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        // Initialise managers
        MongoCollectionManager.init();
        AccountManager.init();
        ArticleManager.init();
        ForecastManager.init();


        // Construct API routes
        path("/", () -> {
            before("/*", (request, response) -> {
                System.out.printf("API call to %s from %s\n", request.url(), request.host());
            });
            path("/account", () -> {
                head("/:username", (request, response) -> EndpointManager.executeEndpoint(UsernameValidityEndpoint.class, request, response));
                post("/validate", (request, response) -> EndpointManager.executeEndpoint(CredentialsValidationEndpoint.class, request, response).body());
                post("/create", (request, response) -> EndpointManager.executeEndpoint(AccountCreationEndpoint.class, request, response).body());
            });
            get("/articles", (request, response) -> EndpointManager.executeEndpoint(GetArticlesEndpoint.class, request, response).body());
            get("/locations", (request, response) -> EndpointManager.executeEndpoint(GetLocationsEndpoint.class, request, response).body());
            get("/location/:location", (request, response) -> EndpointManager.executeEndpoint(GetLocationDataEndpoint.class, request, response).body());

        });

        System.out.println("Backend Service Started");
    }
}
