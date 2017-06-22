package com.licenta.mihai.givemebook_android.Models.BaseModels;

/**
 * Created by mihai on 20.06.2017.
 */

public class Recommendations {
    private Long rid = 0l;
    private UserModel user;
    private Book book;



    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}
