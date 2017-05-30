package com.licenta.mihai.givemebook_android.Models.NetModels.Replay;

import java.io.File;

/**
 * Created by mihai on 24.05.2017.
 */

public class NetRegisterReply {

    private String username = "";
    private String password = "";
    private String email = "";
    private File photo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public File getPhoto() {
        return photo;
    }

    public void setPhoto(File photo) {
        this.photo = photo;
    }
}
