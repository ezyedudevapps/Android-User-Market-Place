package com.ezyedu.student.model;

public class Cart_Details {
    String hash_id;
    int[] qty;

    public Cart_Details(String hash_id, int[] qty) {
        this.hash_id = hash_id;
        this.qty = qty;
    }

    public String getHash_id() {
        return hash_id;
    }

    public int[] getQty() {
        return qty;
    }
}
