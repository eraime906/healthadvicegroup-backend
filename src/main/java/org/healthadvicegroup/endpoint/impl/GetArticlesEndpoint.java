package org.healthadvicegroup.endpoint.impl;

import com.google.gson.JsonObject;
import org.healthadvicegroup.Main;
import org.healthadvicegroup.article.Article;
import org.healthadvicegroup.article.ArticleManager;
import org.healthadvicegroup.endpoint.Endpoint;
import spark.Request;
import spark.Response;

public class GetArticlesEndpoint extends Endpoint {

    @Override
    public Response handle(Request request, Response response) {
        JsonObject json = new JsonObject();
        for (Article article : ArticleManager.getAllArticles()) {
            json.addProperty(article.getId().toString(), String.valueOf(article.toJson(article, Main.getGSON())));
        }
        response.body(json.toString());
        return response;
    }
}
