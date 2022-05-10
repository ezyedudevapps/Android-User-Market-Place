package com.ezyedu.student.model;

public class Institution_Groups
{
    int Group_id;
    String Group_name;
    String image;

    public Institution_Groups(int group_id, String group_name, String image) {
        Group_id = group_id;
        Group_name = group_name;
        this.image = image;
    }


    public int getGroup_id() {
        return Group_id;
    }

    public String getGroup_name() {
        return Group_name;
    }

    public String getImage() {
        return image;
    }
}
