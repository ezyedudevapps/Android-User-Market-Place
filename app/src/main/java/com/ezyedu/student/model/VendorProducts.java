package com.ezyedu.student.model;

public class VendorProducts
{
    Integer id;
    String course_title;
    String course_hash_id;
    String course_duration;
    String start_date;
    String institution;
    String courses_image;
    Integer category_id;
    String category_label;

    public VendorProducts(Integer id, String course_title, String course_hash_id, String course_duration, String start_date, String institution, String courses_image, Integer category_id, String category_label) {
        this.id = id;
        this.course_title = course_title;
        this.course_hash_id = course_hash_id;
        this.course_duration = course_duration;
        this.start_date = start_date;
        this.institution = institution;
        this.courses_image = courses_image;
        this.category_id = category_id;
        this.category_label = category_label;
    }


    public Integer getId() {
        return id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public String getCourse_hash_id() {
        return course_hash_id;
    }

    public String getCourse_duration() {
        return course_duration;
    }

    public String getStart_date() {
        return start_date;
    }

    public String getInstitution() {
        return institution;
    }

    public String getCourses_image() {
        return courses_image;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public String getCategory_label() {
        return category_label;
    }


}
