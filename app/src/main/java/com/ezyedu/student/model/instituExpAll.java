package com.ezyedu.student.model;

public class instituExpAll
{
    String groupName;
    int vendor_id;
    String venor_name;
    String vendor_logo;
    String address;
    int total_review;

    public instituExpAll(String groupName, int vendor_id, String venor_name, String vendor_logo, String address, int total_review) {
        this.groupName = groupName;
        this.vendor_id = vendor_id;
        this.venor_name = venor_name;
        this.vendor_logo = vendor_logo;
        this.address = address;
        this.total_review = total_review;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public String getVenor_name() {
        return venor_name;
    }

    public String getVendor_logo() {
        return vendor_logo;
    }

    public String getAddress() {
        return address;
    }

    public int getTotal_review() {
        return total_review;
    }
}
