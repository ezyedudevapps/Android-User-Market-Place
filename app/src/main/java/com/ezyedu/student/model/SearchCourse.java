package com.ezyedu.student.model;

public class SearchCourse
{
    String Hash_id;
    String Duration;
    String created_at;
    String name;
    String tittle;
    String image;

    public SearchCourse(String hash_id, String duration, String created_at, String name, String tittle, String image) {
        Hash_id = hash_id;
        Duration = duration;
        this.created_at = created_at;
        this.name = name;
        this.tittle = tittle;
        this.image = image;
    }

    public String getHash_id() {
        return Hash_id;
    }

    public String getDuration() {
        return Duration;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getName() {
        return name;
    }

    public String getTittle() {
        return tittle;
    }

    public String getImage() {
        return image;
    }
}
