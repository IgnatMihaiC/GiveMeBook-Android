package com.licenta.mihai.givemebook_android.Models.BaseModels;

import java.util.List;

/**
 * Created by mihai on 23.05.2017.
 */

public class UserModel {

    public long uid;
    public String username;
    public String email;
    public String password;
    public long type;
    public String photoUrl;
    public String token;
    public String createdAt;
    public String updatedAt;
    public String fbID;

    private List<Preferences> preferences;
    private Settings settings;
    private List<Interactions> interactions;
//    private List<Recommendations> recommendations;
    private List<BookState> bookStates;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Preferences> getPreferences() {
        return preferences;
    }

    public void setPreferences(List<Preferences> preferences) {
        this.preferences = preferences;
    }

    public Settings getSettings() {
        return settings;
    }

    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public List<Interactions> getInteractions() {
        return interactions;
    }

    public void setInteractions(List<Interactions> interactions) {
        this.interactions = interactions;
    }

    public List<BookState> getBookStates() {
        return bookStates;
    }

   public void setBookStates(List<BookState> bookStates) {
        this.bookStates = bookStates;
    }

    public String getFbID() {
        return fbID;
    }

    public void setFbID(String fbID) {
        this.fbID = fbID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
