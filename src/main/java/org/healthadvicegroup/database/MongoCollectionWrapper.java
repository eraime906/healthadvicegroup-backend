package org.healthadvicegroup.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;

import java.util.HashMap;

public class MongoCollectionWrapper {

    private static final HashMap<String, MongoCollectionWrapper> cache = new HashMap<>();
    private static MongoClient mongoClient;

    private String collectionName;

    public MongoCollectionWrapper(String collectionName) {
        this.collectionName = collectionName;
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
        return cache.computeIfAbsent(collectionName, val -> new MongoCollectionWrapper(collectionName));
    }

    /**
     * Called on startup to initialise the database connection
     */
    public static void init() {
        mongoClient = new MongoClient(new MongoClientURI("string"));
    }
}
