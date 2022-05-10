package com.ezyedu.student.model;

public class Promo_one_new
{
    String image;
    String tittle;
    String duration;
    String logo;
    String vtittle;
    String vaddress;
    Double rating;
    String description_content;
    String start_at;
    String finish_at;
    int vendor_id;


    public Promo_one_new(String image, String tittle, String duration, String logo, String vtittle, String vaddress, Double rating, String description_content, String start_at, String finish_at, int vendor_id) {
        this.image = image;
        this.tittle = tittle;
        this.duration = duration;
        this.logo = logo;
        this.vtittle = vtittle;
        this.vaddress = vaddress;
        this.rating = rating;
        this.description_content = description_content;
        this.start_at = start_at;
        this.finish_at = finish_at;
        this.vendor_id = vendor_id;
    }


    public String getImage() {
        return image;
    }

    public String getTittle() {
        return tittle;
    }

    public String getDuration() {
        return duration;
    }

    public String getLogo() {
        return logo;
    }

    public String getVtittle() {
        return vtittle;
    }

    public String getVaddress() {
        return vaddress;
    }

    public Double getRating() {
        return rating;
    }

    public String getDescription_content() {
        return description_content;
    }

    public String getStart_at() {
        return start_at;
    }

    public String getFinish_at() {
        return finish_at;
    }

    public int getVendor_id() {
        return vendor_id;
    }
}
