package com.ezyedu.student.model;

public class course_seperate
{
    String category_label;
    String course_title;
    String course_description;
    String course_duration;
    int initial_price;
    int Discount_price;
    String start_date;
    String course_hash_id;
    String ven_logo;
    String ven_name;
    int ven_rating;
    String ven_address;
    int vendor_id;
    int course_rating;
    int total_course_rating;

    public course_seperate(String category_label, String course_title, String course_description, String course_duration, int initial_price, int discount_price, String start_date, String course_hash_id, String ven_logo, String ven_name, int ven_rating, String ven_address, int vendor_id, int course_rating, int total_course_rating) {
        this.category_label = category_label;
        this.course_title = course_title;
        this.course_description = course_description;
        this.course_duration = course_duration;
        this.initial_price = initial_price;
        Discount_price = discount_price;
        this.start_date = start_date;
        this.course_hash_id = course_hash_id;
        this.ven_logo = ven_logo;
        this.ven_name = ven_name;
        this.ven_rating = ven_rating;
        this.ven_address = ven_address;
        this.vendor_id = vendor_id;
        this.course_rating = course_rating;
        this.total_course_rating = total_course_rating;
    }


    public String getCategory_label() {
        return category_label;
    }

    public String getCourse_title() {
        return course_title;
    }

    public String getCourse_description() {
        return course_description;
    }

    public String getCourse_duration() {
        return course_duration;
    }

    public int getInitial_price() {
        return initial_price;
    }

    public int getDiscount_price() {
        return Discount_price;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getCourse_hash_id() {
        return course_hash_id;
    }

    public String getVen_logo() {
        return ven_logo;
    }

    public String getVen_name() {
        return ven_name;
    }

    public int getVen_rating() {
        return ven_rating;
    }

    public String getVen_address() {
        return ven_address;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public int getCourse_rating() {
        return course_rating;
    }

    public int getTotal_course_rating() {
        return total_course_rating;
    }
}
