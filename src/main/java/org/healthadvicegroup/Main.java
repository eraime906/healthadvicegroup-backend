package org.healthadvicegroup;


import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.healthadvicegroup.account.AccountManager;
import org.healthadvicegroup.article.ArticleManager;
import org.healthadvicegroup.database.MongoCollectionManager;
import org.healthadvicegroup.endpoint.EndpointManager;
import org.healthadvicegroup.endpoint.JsonTransformer;
import org.healthadvicegroup.endpoint.impl.*;
import org.healthadvicegroup.forecasting.ForecastManager;
import spark.ResponseTransformer;
import spark.Spark;

import java.util.Arrays;

public class Main {

    @Getter private static Gson GSON;

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
        /**
         * Used to check whether an endpoint exists or not
         *
         * @see UsernameValidityEndpoint
         */
        Spark.head("/account/:username", (request, response) ->
                EndpointManager.executeEndpoint(UsernameValidityEndpoint.class, request, response));

        /**
         * Used to validate whether the provided account credentials are valid or not
         */
        Spark.post("account/validate", (request, response) ->
                EndpointManager.executeEndpoint(CredentialsValidationEndpoint.class, request, response).body());

        /**
         * Used to create a new account
         *
         * @see AccountCreationEndpoint
         */
        Spark.post("account/create", (request, response) ->
                EndpointManager.executeEndpoint(AccountCreationEndpoint.class, request, response));

        /**
         * Used to fetch all articles from {@link ArticleManager}
         *
         * @see GetArticlesEndpoint
         */
        Spark.get("/articles", (request, response) ->
                EndpointManager.executeEndpoint(GetArticlesEndpoint.class, request, response).body());

        /**
         * Used to fetch a list of supported locations
         *
         * @see GetLocationsEndpoint
         */
        Spark.get("/locations", (request, response) ->
                EndpointManager.executeEndpoint(GetLocationsEndpoint.class, request, response).body());

        /**
         * Used to fetch data about a location
         *
         * @see GetLocationDataEndpoint
         */
        Spark.get("/location/:location", (request, response) ->
                EndpointManager.executeEndpoint(GetLocationDataEndpoint.class, request, response).body());

        System.out.println("Backend Service Started");
    }
}
