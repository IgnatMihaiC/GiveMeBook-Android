package com.licenta.mihai.givemebook_android.CustomViews.Popups;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSansBold;
import com.licenta.mihai.givemebook_android.CustomViews.RoundBorderButton;
import com.licenta.mihai.givemebook_android.LandingActivity;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Settings;
import com.licenta.mihai.givemebook_android.Models.NetModels.Response.NetStringResponse;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.R;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.licenta.mihai.givemebook_android.Utils.OfflineHandler;
import com.licenta.mihai.givemebook_android.Utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mihai on 07.06.2017.
 */

public class UserSettingsPopup implements DialogInterface.OnDismissListener {

    private Context context;
    private Dialog dialog;
    private RadioGroup emailNotification;
    private RadioGroup pushNotification;
    DialogInterface.OnDismissListener onDismissListener;

    RoundBorderButton logOutButton;

    public UserSettingsPopup(Context context, DialogInterface.OnDismissListener onDismissListener) {
        this.context = context;
        this.onDismissListener = onDismissListener;
        dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setOnDismissListener(this);
        dialog.setContentView(R.layout.profile_setting_dialog);
        dialog.setCanceledOnTouchOutside(true);


    }

    public void init() {
        final TextViewOpenSansBold userName = (TextViewOpenSansBold) dialog.findViewById(R.id.userPopup_userName);
        logOutButton = (RoundBorderButton) dialog.findViewById(R.id.userPopup_logout_button);
        userName.setText(User.getInstance().getCurrentUser().getUsername());
        emailNotification = (RadioGroup) dialog.findViewById(R.id.toggle_email);
        pushNotification = (RadioGroup) dialog.findViewById(R.id.toggle_push);
        if (User.getInstance().getCurrentUser().getSettings().getEmailNotification()) {
            emailNotification.check(R.id.custom_switch_toggle_email_on);
        } else {
            emailNotification.check(R.id.custom_switch_toggle_email_off);
        }

        if (User.getInstance().getCurrentUser().getSettings().getPushNotification()) {
            pushNotification.check(R.id.custom_switch_toggle_push_on);
        } else {
            pushNotification.check(R.id.custom_switch_toggle_push_off);
        }

        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }


    public void showUserPopup() {
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(true);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dismissDateDialog() {
        if (dialog != null) {
            if (dialog.isShowing()) {
                try {
                    dialog.dismiss();
                    dialog = null;
                } catch (final Exception e) {
                    e.printStackTrace();

                } finally {
                    dialog = null;
                }
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.w("UpdateSettings", "OnDismiss");
        final Settings newSettings = new Settings();
        if (emailNotification.getCheckedRadioButtonId() == R.id.custom_switch_toggle_email_on) {
            newSettings.setEmailNotification(true);
        } else {
            newSettings.setEmailNotification(false);
        }
        if (pushNotification.getCheckedRadioButtonId() == R.id.custom_switch_toggle_push_on) {
            newSettings.setPushNotification(true);
        } else {
            newSettings.setPushNotification(false);
        }

        if (allowServerCall(newSettings)) {
            RestClient.networkHandler().updateSettings(User.getInstance().getCurrentUser().getToken(),
                    User.getInstance().getCurrentUser().getUid(), newSettings)
                    .enqueue(new Callback<NetStringResponse>() {
                        @Override
                        public void onResponse(Call<NetStringResponse> call, Response<NetStringResponse> response) {
                            Log.w("UpdateSettings", "Success");
                            User.getInstance().getCurrentUser().setSettings(newSettings);
                        }

                        @Override
                        public void onFailure(Call<NetStringResponse> call, Throwable t) {
                            Log.w("UpdateSettings", "Fail");
                        }
                    });
        }
    }

    private void logoutUser() {
        RestClient.networkHandler().logoutUser(User.getInstance().getCurrentUser().getToken(), User.getInstance().getCurrentUser().getUid())
                .enqueue(new Callback<NetStringResponse>() {
                    @Override
                    public void onResponse(Call<NetStringResponse> call, Response<NetStringResponse> response) {
                        OfflineHandler.getInstance().deleteUsernameAndPassword();
                        Util.openActivityClosingStack(context, LandingActivity.class);
                    }

                    @Override
                    public void onFailure(Call<NetStringResponse> call, Throwable t) {

                    }
                });
    }

    private Boolean allowServerCall(Settings settings) {
        if (settings.getEmailNotification() == User.getInstance().getCurrentUser().getSettings().getEmailNotification())
            if (settings.getPushNotification() == User.getInstance().getCurrentUser().getSettings().getPushNotification()) {
                return false;
            }
        return true;
    }
}
