package org.healthadvicegroup.article;

import lombok.Getter;
import org.bson.Document;
import org.healthadvicegroup.database.Serializable;

import java.util.ArrayList;
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
        this.id = UUID.fromString(document.getString("_id"));
        this.title = document.getString("title");
        this.blurb = document.getString("blurb");
        this.text = document.getString("text");

        // Deserialize article tags
        List<String> tagList = document.getList("tags", String.class);
        if (tagList != null) {
            for (String tagName : tagList) {
                this.tags.add(EnumArticleTag.valueOf(tagName));
            }
        }

    }

    @Override
    public Document toDocument(Article object) {
        Document document = new Document("_id", object.id.toString())
                .append("title", object.title)
                .append("blurb", object.blurb)
                .append("text", object.text);

        // Serialize article tags
        List<String> tags = new ArrayList<>();
        for (EnumArticleTag tag : object.getTags()) {
            tags.add(tag.name());
        }
        document.append("tags", tags);
        return document;
    }
}
