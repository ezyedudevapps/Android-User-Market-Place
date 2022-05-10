package com.ezyedu.student.model;

public class Maps
{
    Double Latitude;
    Double Longitude;
    String name;

    public Maps(Double latitude, Double longitude, String name) {
        Latitude = latitude;
        Longitude = longitude;
        this.name = name;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public String getName() {
        return name;
    }
}
