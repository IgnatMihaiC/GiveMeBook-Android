package com.licenta.mihai.givemebook_android.Models.BaseModels;

/**
 * Created by mihai on 24.05.2017.
 */

public class Settings {

    private Long sid = 0l;

    private Boolean emailNotification = false;

    private Boolean pushNotification = false;


    public Long getSid() {
        return sid;
    }

    public void setSid(Long sid) {
        this.sid = sid;
    }

    public Boolean getEmailNotification() {
        return emailNotification;
    }

    public void setEmailNotification(Boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public Boolean getPushNotification() {
        return pushNotification;
    }

    public void setPushNotification(Boolean pushNotification) {
        this.pushNotification = pushNotification;
    }
}
