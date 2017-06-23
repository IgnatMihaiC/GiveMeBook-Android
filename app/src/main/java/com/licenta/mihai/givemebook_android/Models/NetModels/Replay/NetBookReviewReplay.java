package com.licenta.mihai.givemebook_android.Models.NetModels.Replay;

/**
 * Created by mihai on 23.06.2017.
 */

public class NetBookReviewReplay {

    private String reviewContent;
    private Long userID;

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }
}
