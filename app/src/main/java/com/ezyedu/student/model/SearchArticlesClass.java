package com.ezyedu.student.model;

public class SearchArticlesClass
{
    String image;
    String tittle;
    String hashid;

    public SearchArticlesClass(String image, String tittle, String hashid) {
        this.image = image;
        this.tittle = tittle;
        this.hashid = hashid;
    }

    public String getImage() {
        return image;
    }

    public String getTittle() {
        return tittle;
    }

    public String getHashid() {
        return hashid;
    }
}
