package model;

import java.time.LocalDate;

public class Article {
    private Long id;
    private LocalDate date;
    private String userId;
    private String content;

    public Article(LocalDate date, String userId, String content) {
        this.date = date;
        this.userId = userId;
        this.content = content;
    }

    public Article(Long id, LocalDate date, String userId, String content) {
        this(date, userId, content);
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }
}
