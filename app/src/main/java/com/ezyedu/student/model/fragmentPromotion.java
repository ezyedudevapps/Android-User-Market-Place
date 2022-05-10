package com.ezyedu.student.model;

public class fragmentPromotion
{
    String tittle;
    String image;
    String hash_id;

    public fragmentPromotion(String tittle, String image, String hash_id) {
        this.tittle = tittle;
        this.image = image;
        this.hash_id = hash_id;
    }

    public String getTittle() {
        return tittle;
    }

    public String getImage() {
        return image;
    }

    public String getHash_id() {
        return hash_id;
    }
}
