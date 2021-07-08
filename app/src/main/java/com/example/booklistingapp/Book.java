package com.example.booklistingapp;

import android.graphics.Bitmap;

public class Book {
    private Bitmap image;
    private String description;
    private String tittle;
    private String author;
    private String url;

    public Book(Bitmap image, String tittle, String author, String description, String url) {
        this.image = image;
        this.description= description;
        this.author = author;
        this.tittle = tittle;
        this.url = url;
    }

    public String getTittle() {
        return tittle;
    }

    public String getAuthor() {
        return author;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }
}
