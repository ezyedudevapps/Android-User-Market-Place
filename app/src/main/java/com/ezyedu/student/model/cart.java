package com.ezyedu.student.model;

public class cart
{
    String course_hash_id;
    String title;
    Double price;
    Double discount_price;
    String start_date;
    String image;
    String vendor_name;


    public cart(String course_hash_id, String title, Double price, Double discount_price, String start_date, String image, String vendor_name) {
        this.course_hash_id = course_hash_id;
        this.title = title;
        this.price = price;
        this.discount_price = discount_price;
        this.start_date = start_date;
        this.image = image;
        this.vendor_name = vendor_name;
    }

    public String getCourse_hash_id() {
        return course_hash_id;
    }

    public String getTitle() {
        return title;
    }

    public Double getPrice() {
        return price;
    }

    public Double getDiscount_price() {
        return discount_price;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getImage() {
        return image;
    }

    public String getVendor_name() {
        return vendor_name;
    }
}
