package com.licenta.mihai.givemebook_android.Models.NetModels.Replay;

/**
 * Created by mihai on 23.06.2017.
 */

public class NetBookStateReplay {
    private Long userID;
    private Long bookID;

    private Integer type;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
