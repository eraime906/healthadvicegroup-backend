package org.healthadvicegroup.article;

import org.bson.Document;
import org.healthadvicegroup.database.MongoCollectionManager;
import org.healthadvicegroup.database.MongoCollectionWrapper;

import java.util.*;

public class ArticleManager {
    private static final HashMap<UUID, Article> articleCache = new HashMap<>();
    private static final MongoCollectionWrapper articleCollection = MongoCollectionManager.getCollectionWrapper("articles");

    public static void init() {
        // Deserialize existing articles from the database
        long start = System.currentTimeMillis();
        for (Document document : articleCollection.getAllDocuments()) {
            try {
                articleCache.put(UUID.fromString(document.getString("_id")), new Article(document));
            } catch (Exception ex) { // catch all possible exceptions when deserializing
                System.out.printf("Failed to deserialize article with id %s\n", document.get("_id"));
            }
        }
        System.out.printf("Deserialized %s articles in %sms", articleCache.size(), System.currentTimeMillis() - start);
    }

    /**
     * Return a {@link Collection} of all articles in {@link #articleCache}
     *
     * @return all articles
     */
    public static Collection<Article> getAllArticles() {
        return articleCache.values();
    }

    /**
     * Fetch the {@link Article} from {@link #articleCache} with the provided id
     * <p>
     * @see #getArticle(UUID) (UUID)
     *
     * @param id the article id to fetch
     *
     * @return the article, if it exists
     */
    public static Article getArticle(String id) {
        return getArticle(UUID.fromString(id));
    }

    /**
     * Fetch the {@link Article} from {@link #articleCache} with the provided id
     *
     * @param id the article id to fetch
     *
     * @return the article, if it exists
     */
    public static Article getArticle(UUID id) {
        return articleCache.get(id);
    }
}
