package com.licenta.mihai.givemebook_android.Models.BaseModels;

import java.util.List;

/**
 * Created by mihai on 23.05.2017.
 */

public class UserModel {

    public long uid;
    public String username;
    public String email;
    public long type;
    public String photoUrl;
    public String token;
    public String createdAt;
    public String updatedAt;

    private List<Preferences> preferences;


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
}
