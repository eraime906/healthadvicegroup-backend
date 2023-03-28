package org.healthadvicegroup.endpoint;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.healthadvicegroup.Main;
import spark.Request;
import spark.Response;

@Getter
public abstract class Endpoint {

    public Endpoint() {
        EndpointManager.registerEndpoint(this);
    }

    /**
     * Used to quickly deserialize HTTP bodies into a key-value format
     *
     * @param body the HTTP body to deserialize
     *
     * @return the deserialized key-value object
     */
    public JsonObject bodyToJsonObject(String body) {
        System.out.println("body="+body);
        JsonElement element = Main.getGSON().fromJson(body, JsonElement.class);
        if (element instanceof JsonObject) {
            return (JsonObject) element;
        }
        else if (element instanceof JsonArray) {
            JsonObject jsonObject = new JsonObject();
            JsonArray array = (JsonArray) element;
            for (JsonElement arrayElement : array) {
                JsonObject arrayElementObject = arrayElement.getAsJsonObject();
                for (String key : arrayElementObject.keySet()) {
                    jsonObject.addProperty(key, arrayElementObject.get(key).getAsString());
                }
            }
            return jsonObject;
        }
        throw new UnsupportedOperationException("Unsupported body format");
    }

    /**
     * Abstract function implemented by each endpoint to handle HTTP requests
     *
     * @param request  the request object
     * @param response the response object
     *
     * @return the response body
     */
    public abstract Response handle(Request request, Response response);

}
