package com.ezyedu.student.model;

public class Save_Orders
{
    String Order_id;
    String ven_name;
    Double price;

    public Save_Orders(String order_id, String ven_name, Double price) {
        Order_id = order_id;
        this.ven_name = ven_name;
        this.price = price;
    }

    public String getOrder_id() {
        return Order_id;
    }

    public String getVen_name() {
        return ven_name;
    }

    public Double getPrice() {
        return price;
    }
}
