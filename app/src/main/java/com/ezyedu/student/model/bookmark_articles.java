package com.ezyedu.student.model;

public class bookmark_articles
{
    String hash_id;
    String tittle;
    String image;

    public bookmark_articles(String hash_id, String tittle, String image) {
        this.hash_id = hash_id;
        this.tittle = tittle;
        this.image = image;
    }

    public String getHash_id() {
        return hash_id;
    }

    public String getTittle() {
        return tittle;
    }

    public String getImage() {
        return image;
    }
}
