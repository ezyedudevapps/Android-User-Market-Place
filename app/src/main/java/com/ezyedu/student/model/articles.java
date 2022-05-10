package com.ezyedu.student.model;

public class articles
{
    String image;
    String tittle;
    String hashid;

    public articles(String image, String tittle, String hashid) {
        this.image = image;
        this.tittle = tittle;
        this.hashid = hashid;
    }

    public String getHashid()
    {
        return hashid;
    }
    public String getImage() {
        return image;
    }

    public String getTittle() {
        return tittle;
    }
}