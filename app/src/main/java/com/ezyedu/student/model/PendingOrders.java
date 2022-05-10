package com.ezyedu.student.model;

public class PendingOrders
{
    int order_id;
    String order_ref_id;
    String vendor_name;
    String vendor_images;
    Double final_amount;
    int status;
    String date;

    public PendingOrders(int order_id, String order_ref_id, String vendor_name, String vendor_images, Double final_amount, int status, String date) {
        this.order_id = order_id;
        this.order_ref_id = order_ref_id;
        this.vendor_name = vendor_name;
        this.vendor_images = vendor_images;
        this.final_amount = final_amount;
        this.status = status;
        this.date = date;
    }

    public int getOrder_id() {
        return order_id;
    }

    public String getOrder_ref_id() {
        return order_ref_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }

    public String getVendor_images() {
        return vendor_images;
    }

    public Double getFinal_amount() {
        return final_amount;
    }

    public int getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
