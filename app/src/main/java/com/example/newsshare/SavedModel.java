package com.example.newsshare;

public class SavedModel {
    private String imageUrl;
    private String newsUrl;
    private String description;
    private String date;
    private int id;

    public SavedModel(String imageUrl, String newsUrl, String description, String date, int id) {
        this.imageUrl = imageUrl;
        this.newsUrl = newsUrl;
        this.description = description;
        this.date = date;
        this.id = id;
    }

    public SavedModel(String imageUrl, String newsUrl, String description, String date) {
        this.imageUrl = imageUrl;
        this.newsUrl = newsUrl;
        this.description = description;
        this.date = date;
    }
    public SavedModel(){}

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
