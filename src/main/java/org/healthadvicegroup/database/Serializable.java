package org.healthadvicegroup.database;

import com.google.gson.*;
import org.bson.Document;
import org.healthadvicegroup.Main;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public interface Serializable<T> extends JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    default JsonElement serialize(T object, Type ignored, JsonSerializationContext ignored_) {
        return this.toJson(object, Main.getGSON());
    }

    @Override
    default T deserialize(JsonElement json, Type ignored, JsonDeserializationContext ignored_) throws JsonParseException {
        return this.fromJson(json);
    }

    /**
     * Overridable function to implement JSON deserialization
     *
     * @param json the JSON data to deserialize
     *
     * @return the deserialized object
     */
    default T fromJson(JsonElement json) {
        throw new UnsupportedOperationException("This function hasn't been implemented in this class");
    }

    /**
     * Overridable function to implement JSON serialization
     *
     * @param object the {@link T} object to serialize
     *
     * @return the serialized object
     */
    default public JsonElement toJson(T object, Gson gson) {
        throw new UnsupportedOperationException("This function hasn't been implemented in this class");
    }

    /**
     * Overridable function to implement document serialization
     *
     * @param object the {@link T} object to serialize
     *
     * @return the serialized object
     */
    default public Document toDocument(T object){
        throw new UnsupportedOperationException("This function hasn't been implemented in this class");
    }

    /**
     * Serialize a list of {@link T} objects to a list of documents
     *
     * @param objects the documents to serialize
     *
     * @return the list of documents
     */
    default List<Document> toDocumentList(List<T> objects) {
        List<Document> documents = new ArrayList<>();
        for (T object : objects) {
            documents.add(this.toDocument(object));
        }
        return documents;
    }

}
