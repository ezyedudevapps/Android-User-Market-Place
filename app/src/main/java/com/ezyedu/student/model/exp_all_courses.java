package com.ezyedu.student.model;

public class exp_all_courses
{
    String course_sub_category_id;
    String title;
    String description;

    String is_discount;

    String is_installment_available;
    String duration;
    String status;
    String hash_id;
    private  String created_at;

    private  String image;
    private  String ccreated_at;
    private  String chash_id;
    private  String small;
    private String original;

    private String vname;
    private String address;

    private String latitude;
    private String longitude;
    private String logo;
    private String email;
    private String website;
    private String phone;

    private String vcreated_at;
    private String vhash_id;

    private String automated_message;
    private String video;
    private String vsmall;
    private String voriginal;

    private String average_rate;

    private String gname;

    private String caname;

    private String dname;

    private String cclabel;
    private String cccreated_at;
    private String cchash_id;
    public Double current_page,id,price,discount_price,cid,
            courses_id,vid,is_active,price_range,is_chatting_allowed,total_review,gid,caid,did,ccid;


    public exp_all_courses(String course_sub_category_id, String title, String description, String is_discount, String is_installment_available, String duration, String status, String hash_id, String created_at, String image, String ccreated_at, String chash_id, String small, String original, String vname, String address, String latitude, String longitude, String logo, String email, String website, String phone, String vcreated_at, String vhash_id, String automated_message, String video, String vsmall, String voriginal, String average_rate, String gname, String caname, String dname, String cclabel, String cccreated_at, String cchash_id, Double current_page, Double id, Double price, Double discount_price, Double cid, Double courses_id, Double vid, Double is_active, Double price_range, Double is_chatting_allowed, Double total_review, Double gid, Double caid, Double did, Double ccid) {
        this.course_sub_category_id = course_sub_category_id;
        this.title = title;
        this.description = description;
        this.is_discount = is_discount;
        this.is_installment_available = is_installment_available;
        this.duration = duration;
        this.status = status;
        this.hash_id = hash_id;
        this.created_at = created_at;
        this.image = image;
        this.ccreated_at = ccreated_at;
        this.chash_id = chash_id;
        this.small = small;
        this.original = original;
        this.vname = vname;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.logo = logo;
        this.email = email;
        this.website = website;
        this.phone = phone;
        this.vcreated_at = vcreated_at;
        this.vhash_id = vhash_id;
        this.automated_message = automated_message;
        this.video = video;
        this.vsmall = vsmall;
        this.voriginal = voriginal;
        this.average_rate = average_rate;
        this.gname = gname;
        this.caname = caname;
        this.dname = dname;
        this.cclabel = cclabel;
        this.cccreated_at = cccreated_at;
        this.cchash_id = cchash_id;
        this.current_page = current_page;
        this.id = id;
        this.price = price;
        this.discount_price = discount_price;
        this.cid = cid;
        this.courses_id = courses_id;
        this.vid = vid;
        this.is_active = is_active;
        this.price_range = price_range;
        this.is_chatting_allowed = is_chatting_allowed;
        this.total_review = total_review;
        this.gid = gid;
        this.caid = caid;
        this.did = did;
        this.ccid = ccid;
    }


    public String getCourse_sub_category_id() {
        return course_sub_category_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getIs_discount() {
        return is_discount;
    }

    public String getIs_installment_available() {
        return is_installment_available;
    }

    public String getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }

    public String getHash_id() {
        return hash_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getImage() {
        return image;
    }

    public String getCcreated_at() {
        return ccreated_at;
    }

    public String getChash_id() {
        return chash_id;
    }

    public String getSmall() {
        return small;
    }

    public String getOriginal() {
        return original;
    }

    public String getVname() {
        return vname;
    }

    public String getAddress() {
        return address;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLogo() {
        return logo;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhone() {
        return phone;
    }

    public String getVcreated_at() {
        return vcreated_at;
    }

    public String getVhash_id() {
        return vhash_id;
    }

    public String getAutomated_message() {
        return automated_message;
    }

    public String getVideo() {
        return video;
    }

    public String getVsmall() {
        return vsmall;
    }

    public String getVoriginal() {
        return voriginal;
    }

    public String getAverage_rate() {
        return average_rate;
    }

    public String getGname() {
        return gname;
    }

    public String getCaname() {
        return caname;
    }

    public String getDname() {
        return dname;
    }

    public String getCclabel() {
        return cclabel;
    }

    public String getCccreated_at() {
        return cccreated_at;
    }

    public String getCchash_id() {
        return cchash_id;
    }

    public Double getCurrent_page() {
        return current_page;
    }

    public Double getId() {
        return id;
    }

    public Double getPrice() {
        return price;
    }

    public Double getDiscount_price() {
        return discount_price;
    }

    public Double getCid() {
        return cid;
    }

    public Double getCourses_id() {
        return courses_id;
    }

    public Double getVid() {
        return vid;
    }

    public Double getIs_active() {
        return is_active;
    }

    public Double getPrice_range() {
        return price_range;
    }

    public Double getIs_chatting_allowed() {
        return is_chatting_allowed;
    }

    public Double getTotal_review() {
        return total_review;
    }

    public Double getGid() {
        return gid;
    }

    public Double getCaid() {
        return caid;
    }

    public Double getDid() {
        return did;
    }

    public Double getCcid() {
        return ccid;
    }
}
