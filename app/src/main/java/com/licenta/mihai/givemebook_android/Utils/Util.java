package com.licenta.mihai.givemebook_android.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mihai on 23.05.2017.
 */

public class Util {

    //region Activity manager Region
    public static void openActivity(Context ctx, Class c) {
        Intent intent = new Intent(ctx, c);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void openActivityClosingStack(Context ctx, Class c) {
        Intent intent = new Intent(ctx, c);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }

    public static void openActivityClosingParent(Context ctx, Class c) {
        Intent intent = new Intent(ctx, c);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
        ((Activity) ctx).finish();
    }
    //endregion

    //region Toast Region
    public static void showToast(Context ctx, String message) {
        if (ctx != null) {
            try {
                Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showToast(Context ctx, int message) {
        if (ctx != null) {
            try {
                Toast.makeText(ctx, ctx.getResources().getString(message), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void showShortToast(Context ctx, String message) {
        if (ctx != null) {
            try {
                Toast.makeText(ctx, message, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //endregion

    //region Show Log Region
    public static void showObjectLog(Object o) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(o);
        customInfoLog("GSON Object", "Content", json);
    }

    public static void showObjectLog(String objectName, Object o) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(o);
        customInfoLog("GSON " + objectName, "^", json);
    }

    public static void customInfoLog(String activityName, String viewId, int infoMessage) {
        Log.i("--->", " \n");
        Log.i("--->", activityName + "\n---------------------------------------------");
        Log.i("--->" + viewId + "       ", Integer.toString(infoMessage));
        Log.i("--->", "---------------------------------------------\n");
        Log.i("--->", " ");
    }

    public static void customInfoLog(String activityName, String viewId, String infoMessage) {
        Log.i("--->", " \n");
        Log.i("--->", activityName + "\n---------------------------------------------");
        Log.i("--->" + viewId + "       ", infoMessage);
        Log.i("--->", "---------------------------------------------\n");
        Log.i("--->", " ");
    }


    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

}
