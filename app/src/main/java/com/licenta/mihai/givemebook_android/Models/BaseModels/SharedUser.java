package com.licenta.mihai.givemebook_android.Models.BaseModels;

import java.util.List;

/**
 * Created by mihai on 19.06.2017.
 */

public class SharedUser {
    private String username;
    private String photoUrl;
    private String fbID;
    private List<Interactions> friends;
    private List<BookState> bookStates;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public List<Interactions> getInteractions() {
        return friends;
    }

    public void setInteractions(List<Interactions> friends) {
        this.friends = friends;
    }

    public List<BookState> getBookStates() {
        return bookStates;
    }

    public void setBookStates(List<BookState> bookStates) {
        this.bookStates = bookStates;
    }
}
