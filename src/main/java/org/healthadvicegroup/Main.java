package org.healthadvicegroup;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.healthadvicegroup.account.AccountManager;
import org.healthadvicegroup.database.MongoCollectionManager;
import org.healthadvicegroup.database.MongoCollectionWrapper;
import org.healthadvicegroup.endpoint.EndpointManager;
import org.healthadvicegroup.endpoint.impl.TestEndpoint;
import spark.Spark;

public class Main {

    @Getter private static Gson GSON;

    public static void main(String[] args) {
        // Setup gson
        GSON = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        // Initialise database connection
        MongoCollectionManager.init();
        // Initialise account manager
        AccountManager.init();

        // Construct API routes
        Spark.get("/test", (response, result) ->
                EndpointManager.executeEndpoint(TestEndpoint.class, response, result));
    }

}
