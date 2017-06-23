package com.licenta.mihai.givemebook_android.Models.BaseModels;

/**
 * Created by mihai on 19.06.2017.
 */

public class BookState {

    private Long bsId;
    private Integer type;
    private Book book;


    public Long getBsId() {
        return bsId;
    }

    public void setBsId(Long bsId) {
        this.bsId = bsId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

}
