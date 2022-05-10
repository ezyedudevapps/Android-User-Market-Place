package com.ezyedu.student.model;

public class First_model {
    Integer icon;
    String head_text;
    String body_text1;
    String body_text2;
    String body_text3;

    public First_model(Integer icon,String head_text,String body_text1,String body_text2,String body_text3)
    {
        this.icon = icon;
        this.head_text = head_text;
        this.body_text1 = body_text1;
        this.body_text2 = body_text2;
        this.body_text3 = body_text3;
    }

    public Integer getIcon() {
        return icon;
    }

    public String getHead_text() {
        return head_text;
    }

    public String getBody_text1() {
        return body_text1;
    }

    public String getBody_text2() {
        return body_text2;
    }

    public String getBody_text3() {
        return body_text3;
    }
}
