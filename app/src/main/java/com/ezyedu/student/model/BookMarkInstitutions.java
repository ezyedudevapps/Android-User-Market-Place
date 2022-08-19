package com.ezyedu.student.model;

public class BookMarkInstitutions {
    int vendor_id;
    String name;
    String logo;
    String address;

    public BookMarkInstitutions(int vendor_id, String name, String logo, String address) {
        this.vendor_id = vendor_id;
        this.name = name;
        this.logo = logo;
        this.address = address;
    }

    public int getVendor_id() {
        return vendor_id;
    }

    public String getName() {
        return name;
    }

    public String getLogo() {
        return logo;
    }

    public String getAddress() {
        return address;
    }
}
