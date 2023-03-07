package org.healthadvicegroup;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.healthadvicegroup.account.AccountManager;
import org.healthadvicegroup.article.ArticleManager;
import org.healthadvicegroup.database.MongoCollectionManager;
import org.healthadvicegroup.endpoint.EndpointManager;
import org.healthadvicegroup.endpoint.impl.AccountCreationEndpoint;
import org.healthadvicegroup.endpoint.impl.GetArticlesEndpoint;
import org.healthadvicegroup.endpoint.impl.UsernameValidityEndpoint;
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


        // Construct API routes
        /**
         * Used to check whether an endpoint exists or not
         *
         * @see UsernameValidityEndpoint
         */
        Spark.head("/account/:username", (response, result) ->
                EndpointManager.executeEndpoint(UsernameValidityEndpoint.class, response, result));

        /**
         * Used to create a new account
         *
         * @see AccountCreationEndpoint
         */
        Spark.post("account/create", (response, result) ->
                EndpointManager.executeEndpoint(AccountCreationEndpoint.class, response, result));

        /**
         * Used to fetch all articles from {@link ArticleManager}
         *
         * @see GetArticlesEndpoint
         */
        Spark.get("/articles", (response, result) ->
                EndpointManager.executeEndpoint(GetArticlesEndpoint.class, response, result));

        System.out.println("Backend Service Started");
    }

}
