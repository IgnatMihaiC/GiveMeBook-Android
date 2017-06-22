package com.licenta.mihai.givemebook_android.Models.BaseModels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihai on 12.06.2017.
 */

public class Book {

    private Long bId = 0l;
    private String title = "";
    private String author = "";
    private String description = "";
    private String cover_photo = "";

    private List<Categories> categories = new ArrayList<>();
    private List<BookReview> bookReviews = new ArrayList<>();

    public Long getbId() {
        return bId;
    }

    public void setbId(Long bId) {
        this.bId = bId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover_photo() {
        return cover_photo;
    }

    public void setCover_photo(String cover_photo) {
        this.cover_photo = cover_photo;
    }

    public List<Categories> getCategories() {
        return categories;
    }

    public void setCategories(List<Categories> categories) {
        this.categories = categories;
    }

    public List<BookReview> getBookReviews() {
        return bookReviews;
    }

    public void setBookReviews(List<BookReview> bookReviews) {
        this.bookReviews = bookReviews;
    }
}
