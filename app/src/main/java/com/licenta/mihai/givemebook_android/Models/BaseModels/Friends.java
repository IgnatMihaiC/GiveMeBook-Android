package com.licenta.mihai.givemebook_android.Models.BaseModels;

/**
 * Created by mihai on 24.05.2017.
 */

public class Friends {

    private Long fid =0l;
    private String fbid = "";


    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }
}
