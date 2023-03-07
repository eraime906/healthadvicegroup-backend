package org.healthadvicegroup.article;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import org.bson.Document;
import org.healthadvicegroup.Main;
import org.healthadvicegroup.database.Serializable;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
public class Article implements Serializable<Article> {

    private final UUID id;
    private final String title;
    private final String blurb;
    private final String text;
    private final HashSet<EnumArticleTag> tags = new HashSet<>();

    public Article(Document document) {
        this.id = document.get("_id", UUID.class);
        this.title = document.getString("title");
        this.blurb = document.getString("blurb");
        this.text = document.getString("text");
        this.tags.addAll(Main.getGSON().fromJson(document.getString("tags"), new TypeToken<HashSet<EnumArticleTag>>(){}.getType()));

    }

    @Override
    public Document toDocument(Article object, Gson gson) {
        return new Document("_id", object.id.toString())
                .append("title", object.title)
                .append("blurb", object.blurb)
                .append("text", object.text)
                .append("tags", gson.toJson(this.tags));
    }

    @Override
    public JsonElement toJson(Article object, Gson gson) {
        JsonObject json = new JsonObject();
        json.addProperty("title", object.title);
        json.addProperty("blurb", object.blurb);
        json.addProperty("text", object.text);
        json.addProperty("tags", gson.toJson(object.tags));
        return json;
    }
}
