package com.licenta.mihai.givemebook_android.Events;

/**
 * Created by mihai on 22.06.2017.
 */

public class ActionEvent {

    private String type;

    public ActionEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
