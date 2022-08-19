package com.ezyedu.student.model;

public class articesSeperate
{
    String image;
    String type;
    String heading;
    String Author;
    String date;
    String description;
    String Hash_id;
    String label;
    String bookmark;

    public articesSeperate(String image, String type, String heading, String author, String date, String description, String hash_id, String label, String bookmark) {
        this.image = image;
        this.type = type;
        this.heading = heading;
        Author = author;
        this.date = date;
        this.description = description;
        Hash_id = hash_id;
        this.label = label;
        this.bookmark = bookmark;
    }

    public String getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public String getHeading() {
        return heading;
    }

    public String getAuthor() {
        return Author;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getHash_id() {
        return Hash_id;
    }

    public String getLabel() {
        return label;
    }

    public String getBookmark() {
        return bookmark;
    }
}
