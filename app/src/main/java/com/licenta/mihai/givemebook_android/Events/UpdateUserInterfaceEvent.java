package com.licenta.mihai.givemebook_android.Events;

/**
 * Created by mihai on 22.06.2017.
 */

public class UpdateUserInterfaceEvent {

    private String type;

    public UpdateUserInterfaceEvent(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
