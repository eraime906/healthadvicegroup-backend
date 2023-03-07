package org.healthadvicegroup.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import java.util.HashMap;

public class MongoCollectionManager {

    private static final HashMap<String, MongoCollectionWrapper> cache = new HashMap<>();
    @SuppressWarnings("FieldCanBeLocal")
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    /**
     * Called on startup to initialise the database connection
     */
    public static void init() {
        mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://yandiya:vybe2205jiE176Et@healthadvicegroup.yvqvbdz.mongodb.net/?retryWrites=true&w=majority"));
        database = mongoClient.getDatabase("healthadvicegroup");
        System.out.println("Connected to the database");
    }

    /**
     * Static getter to fetch {@link MongoCollectionWrapper} instances from {@link #cache}
     * Collections that don't yet have a wrapper constructed will be made and then cached
     *
     * @param collectionName the name of the collection to fetch
     *
     * @return the collection wrapper
     */
    public static MongoCollectionWrapper getCollectionWrapper(String collectionName) {
        return cache.computeIfAbsent(collectionName, val -> new MongoCollectionWrapper(database, collectionName));
    }
}
