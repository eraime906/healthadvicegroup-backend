package org.healthadvicegroup;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.healthadvicegroup.account.AccountManager;
import org.healthadvicegroup.article.ArticleManager;
import org.healthadvicegroup.database.MongoCollectionManager;
import org.healthadvicegroup.endpoint.EndpointManager;
import org.healthadvicegroup.endpoint.impl.UsernameHeadEndpoint;
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
        // Initialise managers
        AccountManager.init();
        ArticleManager.init();

        // Construct API routes
        Spark.head("/account/:username", (response, result) ->
                EndpointManager.executeEndpoint(UsernameHeadEndpoint.class, response, result));

        System.out.println("Started!");
    }

}
