package com.ezyedu.student.model;

public class SearchInstitutionClass
{
    String learn_type;
    String tittle;
    String address;
    Double rating;
    String image;
    String hashid;
    Integer id;
    int total_review;

    public SearchInstitutionClass(String learn_type, String tittle, String address, Double rating, String image, String hashid, Integer id, int total_review) {
        this.learn_type = learn_type;
        this.tittle = tittle;
        this.address = address;
        this.rating = rating;
        this.image = image;
        this.hashid = hashid;
        this.id = id;
        this.total_review = total_review;
    }

    public String getLearn_type() {
        return learn_type;
    }

    public String getTittle() {
        return tittle;
    }

    public String getAddress() {
        return address;
    }

    public Double getRating() {
        return rating;
    }

    public String getImage() {
        return image;
    }

    public String getHashid() {
        return hashid;
    }

    public Integer getId() {
        return id;
    }

    public int getTotal_review() {
        return total_review;
    }
}
