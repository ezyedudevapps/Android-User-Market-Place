package com.ezyedu.student.model;

public class fragmentArticle
{
    String name;
    String image;
    String hash_id;
    String bookmarks;

    public fragmentArticle(String name, String image, String hash_id, String bookmarks) {
        this.name = name;
        this.image = image;
        this.hash_id = hash_id;
        this.bookmarks = bookmarks;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getHash_id() {
        return hash_id;
    }

    public String getBookmarks() {
        return bookmarks;
    }
}
