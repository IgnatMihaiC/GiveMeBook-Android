package com.licenta.mihai.givemebook_android.Models.BaseModels;

/**
 * Created by mihai on 24.05.2017.
 */

public class Preferences {

    private Long pid = 0l;
    private String pname = "";

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
