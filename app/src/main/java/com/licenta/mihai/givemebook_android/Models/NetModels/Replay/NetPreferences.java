package com.licenta.mihai.givemebook_android.Models.NetModels.Replay;

/**
 * Created by mihai on 22.06.2017.
 */

public class NetPreferences {

    private String pname = "";

    public NetPreferences(String pname) {
        this.pname = pname;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
