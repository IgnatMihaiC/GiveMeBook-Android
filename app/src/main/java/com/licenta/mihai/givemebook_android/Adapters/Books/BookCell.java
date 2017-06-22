package com.licenta.mihai.givemebook_android.Adapters.Books;

import com.licenta.mihai.givemebook_android.Models.BaseModels.Book;

/**
 * Created by mihai on 12.06.2017.
 */

public class BookCell {

    private Book book;
    private String userPhoto = "";

    public BookCell(Book book, String userPhoto) {
        this.book = book;
        this.userPhoto = userPhoto;
    }

    public BookCell(Book books) {
        this.book = books;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }
}
