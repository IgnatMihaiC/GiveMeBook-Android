package com.licenta.mihai.givemebook_android.Adapters.Friend;

/**
 * Created by mihai on 12.06.2017.
 */

public class FriendListCell {

    private String userName;
    private String photoUrl;


    public FriendListCell() {
    }

    public FriendListCell(String userName, String photoUrl) {
        this.userName = userName;
        this.photoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCircleImageView() {
        return photoUrl;
    }

    public void setCircleImageView(String circleImageView) {
        this.photoUrl = circleImageView;
    }
}
