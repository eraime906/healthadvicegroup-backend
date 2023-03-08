package org.healthadvicegroup;


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
        Spark.head("/account/:username", (request, result) ->
                EndpointManager.executeEndpoint(UsernameValidityEndpoint.class, request, result));

        /**
         * Used to create a new account
         *
         * @see AccountCreationEndpoint
         */
        Spark.post("account/create", (request, result) ->
                EndpointManager.executeEndpoint(AccountCreationEndpoint.class, request, result));

        /**
         * Used to fetch all articles from {@link ArticleManager}
         *
         * @see GetArticlesEndpoint
         */
        Spark.get("/articles", (request, result) ->
                EndpointManager.executeEndpoint(GetArticlesEndpoint.class, request, result).body());

        /**
         * Used to fetch a list of supported locations
         *
         * @see GetLocationsEndpoint
         */
        Spark.get("/locations", (request, result) ->
                EndpointManager.executeEndpoint(GetLocationsEndpoint.class, request, result).body());

        /**
         * Used to fetch data about a location
         *
         * @see GetLocationDataEndpoint
         */
        Spark.get("/location/:location", (request, result) ->
                EndpointManager.executeEndpoint(GetLocationDataEndpoint.class, request, result).body());

        System.out.println("Backend Service Started");
    }
}
