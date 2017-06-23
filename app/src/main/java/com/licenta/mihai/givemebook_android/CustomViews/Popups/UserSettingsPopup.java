package com.licenta.mihai.givemebook_android.CustomViews.Popups;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.facebook.login.LoginManager;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.BorderEditText;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.TextViewOpenSansBold;
import com.licenta.mihai.givemebook_android.CustomViews.RoundBorderButton;
import com.licenta.mihai.givemebook_android.Events.ActionEvent;
import com.licenta.mihai.givemebook_android.LandingActivity;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Settings;
import com.licenta.mihai.givemebook_android.Models.NetModels.Response.NetStringResponse;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.R;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.licenta.mihai.givemebook_android.Utils.OfflineHandler;
import com.licenta.mihai.givemebook_android.Utils.UploadPhoto;
import com.licenta.mihai.givemebook_android.Utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mihai on 07.06.2017.
 */

public class UserSettingsPopup implements DialogInterface.OnDismissListener {

    private Context context;
    private Dialog dialog;
    private RadioGroup pushNotification;
    private BorderEditText newPassword;


    private RoundBorderButton changePassword;
    private RoundBorderButton changePhoto;
    private RoundBorderButton logOutButton;

    private String changePasswordButtonState = "first";
    public UploadPhoto uploadPhoto = new UploadPhoto();

    public UserSettingsPopup(Context context) {
        this.context = context;
        dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setOnDismissListener(this);
        dialog.setContentView(R.layout.profile_setting_dialog);
        dialog.setCanceledOnTouchOutside(true);


    }

    public void init() {
        final TextViewOpenSansBold userName = (TextViewOpenSansBold) dialog.findViewById(R.id.userPopup_userName);
        logOutButton = (RoundBorderButton) dialog.findViewById(R.id.userPopup_logout_button);
        changePassword = (RoundBorderButton) dialog.findViewById(R.id.userPopup_changePassword_button);
        changePhoto = (RoundBorderButton) dialog.findViewById(R.id.userPopup_changePhoto_button);

        userName.setText(User.getInstance().getCurrentUser().getUsername());
        pushNotification = (RadioGroup) dialog.findViewById(R.id.toggle_push);
        newPassword = (BorderEditText) dialog.findViewById(R.id.userPopup_changePassword_editText);
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
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changePasswordButtonState.equals("first")) {
                    newPassword.setVisibility(View.VISIBLE);
                    changePassword.setText("Send");
                    changePasswordButtonState = "second";
                } else {
                    if (newPassword.getText().toString().isEmpty()) {
                        newPassword.setVisibility(View.GONE);
                        changePassword.setText("Change Password");

                    } else {
                        final String pass = Util.sha1Hash(newPassword.getText().toString());
                        RequestBody usernameBody = RequestBody.create(MediaType.parse("text/plain"), pass);
                        RestClient.networkHandler().changePassword(User.getInstance().getCurrentUser().getToken(), User.getInstance().getCurrentUser()
                                .getUid(), usernameBody)
                                .enqueue(new Callback<NetStringResponse>() {
                                    @Override
                                    public void onResponse(Call<NetStringResponse> call, Response<NetStringResponse> response) {
                                        if (response.isSuccessful()) {
                                            User.getInstance().getCurrentUser().setPhotoUrl(pass);
                                            OfflineHandler.getInstance().storePassword(pass);
                                            newPassword.setVisibility(View.GONE);
                                            changePassword.setText("Change Password");
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<NetStringResponse> call, Throwable t) {

                                    }
                                });
                    }
                    changePasswordButtonState = "first";
                }

            }
        });

        changePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto.setContext(context);
                uploadPhoto.clearPhotoDir();
                uploadPhoto.defaultSelectMethodDialog();

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
        EventBus.getDefault().post(new ActionEvent("menu"));
    }

    private void logoutUser() {
        LoginManager.getInstance().logOut();
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

    public void isPictureReady() {
        File file = new File(uploadPhoto.getPicturePathEdited());
        final RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        RestClient.networkHandler().updatePhoto(User.getInstance().getCurrentUser().getToken(), User.getInstance().getCurrentUser().getUid(), filePart)
                .enqueue(new Callback<NetStringResponse>() {
                    @Override
                    public void onResponse(Call<NetStringResponse> call, Response<NetStringResponse> response) {
                        if (response.isSuccessful()) {
                            Util.showShortToast(context, "Success");
                            User.getInstance().getCurrentUser().setPhotoUrl(response.body().getMessage());
                            EventBus.getDefault().post(new ActionEvent("userPhoto"));
                        } else {
                            Util.showShortToast(context, "FailI");
                        }
                    }

                    @Override
                    public void onFailure(Call<NetStringResponse> call, Throwable t) {
                        Util.showShortToast(context, "FailE");
                    }
                });
    }

    private Boolean allowServerCall(Settings settings) {
        if (settings.getPushNotification() == User.getInstance().getCurrentUser().getSettings().getPushNotification()) {
            return false;
        }
        return true;
    }

}
