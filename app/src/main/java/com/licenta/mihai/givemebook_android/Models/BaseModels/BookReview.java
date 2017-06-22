package com.licenta.mihai.givemebook_android.Models.BaseModels;

/**
 * Created by mihai on 12.06.2017.
 */

public class BookReview {

    private Long idBookReview;
    private Long userID;
    private String reviewContent;

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getIdBookReview() {
        return idBookReview;
    }

    public void setIdBookReview(Long idBookReview) {
        this.idBookReview = idBookReview;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}
