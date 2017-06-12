package com.licenta.mihai.givemebook_android.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.licenta.mihai.givemebook_android.AppDelegate;

/**
 * Created by mihai on 12.06.2017.
 */


public class OfflineHandler {

    private static OfflineHandler ourInstance = new OfflineHandler();

    public static OfflineHandler getInstance() {
        return ourInstance;
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String AppPREFERENCES = "ApplicationData";

    private String kStoreEmail = "userName";
    private String kStorePassword = "userPassword";

    private OfflineHandler() {
        Context applicationContext = AppDelegate.getMyContext();
        sharedPreferences = applicationContext.getSharedPreferences(AppPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    //region Storing Methods for Objects
    public void storeUsername(String email) {
        editor.putString(kStoreEmail, email);
        editor.commit();
    }

    public void storePassword(String password) {
        editor.putString(kStorePassword, password);
        editor.commit();
    }

    public String recoverUsername() {
        return sharedPreferences.getString(kStoreEmail, "none");
    }

    public String recoverPassword() {
        return sharedPreferences.getString(kStorePassword, "none");
    }

    public void deleteUsernameAndPassword() {
        editor.remove(kStoreEmail);
        editor.remove(kStorePassword);
        editor.commit();
        editor.apply();
    }
    public Boolean isUsernameStored() {
        return !recoverUsername().equals("none");
    }
}
