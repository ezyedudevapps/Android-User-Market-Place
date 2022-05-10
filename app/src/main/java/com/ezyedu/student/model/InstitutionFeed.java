package com.ezyedu.student.model;

public class InstitutionFeed
{
    String image;
    String tittle;
    String date;
    String hash_id;

    public InstitutionFeed(String image, String tittle, String date, String hash_id) {
        this.image = image;
        this.tittle = tittle;
        this.date = date;
        this.hash_id = hash_id;
    }

    public String getImage() {
        return image;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDate() {
        return date;
    }

    public String getHash_id() {
        return hash_id;
    }
}
