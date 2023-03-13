package org.healthadvicegroup.database;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import lombok.Getter;
import org.bson.Document;

@Getter
public class MongoCollectionWrapper {

    private final MongoDatabase database;
    private final MongoCollection<Document> collection;

    MongoCollectionWrapper(MongoDatabase database, String collectionName) {
        this.database = database;
        this.collection = database.getCollection(collectionName);
    }

    /**
     * Save to {@link #collection} the provided {@link Document}
     *
     * @param document the document to save
     */
    public void saveDocument(Document document) {
        this.collection.replaceOne(Filters.eq("_id", document.get("_id")), document, new ReplaceOptions().upsert(true));
    }

    /**
     * Delete the document with the provided _id
     *
     * @param _id the id of the document to delete
     */
    public void deleteDocument(String _id) {
        this.collection.deleteOne(Filters.eq("_id", _id));
    }

    /**
     * Return the {@link Document} with the provided _id
     *
     * @param _id the _id of the document to fetch
     *
     * @return the document, or an empty one if none was found
     */
    public Document getDocument(String _id) {
        Document document = this.collection.find(Filters.eq("_id", _id)).first();
        return document == null ? new Document() : document;
    }

    /**
     * @return All documents in this collection
     */
    public FindIterable<Document> getAllDocuments() {
        return this.collection.find();
    }

}
