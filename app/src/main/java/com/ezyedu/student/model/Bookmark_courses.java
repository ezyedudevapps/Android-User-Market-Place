package com.ezyedu.student.model;

public class Bookmark_courses
{
    String image;
    String Duration;
    String Data;
    String Courser_Name;
    String vendor_name;
    String Hash_id;

    public Bookmark_courses(String image, String duration, String data, String courser_Name, String vendor_name, String hash_id) {
        this.image = image;
        Duration = duration;
        Data = data;
        Courser_Name = courser_Name;
        this.vendor_name = vendor_name;
        Hash_id = hash_id;
    }

    public String getImage() {
        return image;
    }

    public String getDuration() {
        return Duration;
    }

    public String getData() {
        return Data;
    }

    public String getCourser_Name() {
        return Courser_Name;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public String getHash_id() {
        return Hash_id;
    }
}
