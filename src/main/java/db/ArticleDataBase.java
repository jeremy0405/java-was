package db;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import model.Article;

public class ArticleDataBase {
    private static final Map<Long, Article> articles = new HashMap<>();
    private static final AtomicLong sequence = new AtomicLong();

    static {
        articles.put(sequence.incrementAndGet(), new Article(1L, LocalDate.now(), "user1", "안녕하세요"));
        articles.put(sequence.incrementAndGet(), new Article(2L, LocalDate.now(), "user2", "안녕하세요 안녕하세요 안녕하세요"));
    }

    public static void postArticle(Article article) {
        Long id = sequence.incrementAndGet();
        article.setId(id);
        articles.put(id, article);
    }

    public static Article findArticleById(Long id) {
        return articles.get(id);
    }

    public static Collection<Article> findAll() {
        return articles.values();
    }
}
