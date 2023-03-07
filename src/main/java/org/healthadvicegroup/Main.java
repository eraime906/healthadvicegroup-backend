package org.healthadvicegroup;


import org.healthadvicegroup.database.MongoCollectionWrapper;
import org.healthadvicegroup.endpoint.EndpointManager;
import org.healthadvicegroup.endpoint.impl.TestEndpoint;
import spark.Spark;

public class Main {

    public static void main(String[] args) {
        // Initialise database connection
        MongoCollectionWrapper.init();

        // Construct API routes
        Spark.get("/test", (response, result) ->
                EndpointManager.executeEndpoint(TestEndpoint.class, response, result));
    }

}
