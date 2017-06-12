package com.licenta.mihai.givemebook_android.Models.NetModels.Response;

/**
 * Created by mihai on 09.06.2017.
 */

public class NetStringResponse {

    private String message;

    public NetStringResponse(String message) {
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
