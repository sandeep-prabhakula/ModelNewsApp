package com.example.newsshare;

public class Model {
    private String content;
    private String imageUrl;

    public Model(String content, String imageUrl) {
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
