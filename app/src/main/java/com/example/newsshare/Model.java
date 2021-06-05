package com.example.newsshare;

public class Model {
    private String content;
    private String imageUrl;
    private String contentUrl;
    private String date;

    public Model(String content, String imageUrl,String contentUrl,String date) {
        this.content = content;
        this.imageUrl = imageUrl;
        this.contentUrl = contentUrl;
        this.date = date;
    }

    public String getDate() {
        return date;
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
