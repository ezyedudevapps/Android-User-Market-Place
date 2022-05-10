package com.ezyedu.student.model;

public class OrderInfo
{
    String course_id;
    String course_name;
    String course_image;
    Double course_amount;
    int course_qty;

    public OrderInfo(String course_id, String course_name, String course_image, Double course_amount, int course_qty) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.course_image = course_image;
        this.course_amount = course_amount;
        this.course_qty = course_qty;
    }

    public String getCourse_id() {
        return course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_image() {
        return course_image;
    }

    public Double getCourse_amount() {
        return course_amount;
    }

    public int getCourse_qty() {
        return course_qty;
    }
}
