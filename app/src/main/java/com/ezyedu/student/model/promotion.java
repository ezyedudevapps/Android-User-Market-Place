package com.ezyedu.student.model;

public class promotion
{
    String tittle;
    String image;
    String Hash_id;

    public promotion(String tittle, String image,String hash_id) {
        this.tittle = tittle;
        this.image = image;
        this.Hash_id = hash_id;
    }

    public String getTittle() {
        return tittle;
    }

    public String getImage() {
        return image;
    }
    public String getHash_id()
    {
        return Hash_id;
    }
}
