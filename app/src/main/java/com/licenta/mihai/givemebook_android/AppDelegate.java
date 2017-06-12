package com.licenta.mihai.givemebook_android;

import android.app.Application;
import android.content.Context;

/**
 * Created by mihai on 12.06.2017.
 */

public class AppDelegate extends Application {

    static Context myContext;

    @Override
    public void onCreate() {
        super.onCreate();
        myContext = getApplicationContext();
    }

    public static Context getMyContext() {
        return myContext;
    }
}
