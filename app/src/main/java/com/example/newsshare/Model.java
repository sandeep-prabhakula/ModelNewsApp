package com.example.newsshare;

public class Model {
    private String content;
    private String imageUrl;
    private String contentUrl;

    public Model(String content, String imageUrl,String contentUrl) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.contentUrl = contentUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
