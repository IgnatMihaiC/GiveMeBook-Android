package com.licenta.mihai.givemebook_android;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.licenta.mihai.givemebook_android.Models.BaseModels.UserModel;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetLoginReply;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.licenta.mihai.givemebook_android.Utils.AppSettings;
import com.licenta.mihai.givemebook_android.Utils.OfflineHandler;
import com.licenta.mihai.givemebook_android.Utils.Util;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!OfflineHandler.getInstance().isUsernameStored()) {
                    Util.openActivityClosingParent(SplashScreen.this, LandingActivity.class);
                } else {
                    autoLogin();
                }
            }
        }, AppSettings.SPLASH_DISPLAY_LENGTH);
    }


    private void autoLogin() {
        NetLoginReply netLoginReply = new NetLoginReply();
        netLoginReply.setEmail(OfflineHandler.getInstance().recoverUsername());
        netLoginReply.setPassword(OfflineHandler.getInstance().recoverPassword());
        RestClient.networkHandler().loginUser(netLoginReply)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.isSuccessful()) {
                            User.getInstance().setCurrentUser(response.body());
                            Util.showObjectLog(response.body());
                            Util.openActivityClosingParent(SplashScreen.this, MainActivity.class);
                        }else{
                            Util.openActivityClosingParent(SplashScreen.this, LandingActivity.class);
                            OfflineHandler.getInstance().deleteUsernameAndPassword();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable throwable) {


                    }
                });
    }

}
