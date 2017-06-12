package com.licenta.mihai.givemebook_android;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.licenta.mihai.givemebook_android.CustomViews.CustomText.BorderEditText;
import com.licenta.mihai.givemebook_android.Models.BaseModels.UserModel;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetLoginReply;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.licenta.mihai.givemebook_android.Utils.OfflineHandler;
import com.licenta.mihai.givemebook_android.Utils.Util;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingActivity extends AppCompatActivity {

    @BindView(R.id.imageLogo)
    ImageView imageLogo;

    @BindView(R.id.landing_login_Username)
    BorderEditText emailEditText;

    @BindView(R.id.landing_login_Password)
    BorderEditText passwordEditText;

    @BindView(R.id.landingLayoutParent)
    ConstraintLayout parentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRotationAnim();

    }

    private void initRotationAnim() {
        ObjectAnimator scaleDown;
        scaleDown = ObjectAnimator.ofFloat(imageLogo,
                "rotationY", 0f, 360f);
        scaleDown.setDuration(600);
        scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
        scaleDown.setRepeatMode(ObjectAnimator.REVERSE);
        //scaleDown.start();
    }


    public void sendLogin(final View view) {
        NetLoginReply netLoginReply = new NetLoginReply();
        if (emailEditText.getText().toString().isEmpty()) {
            emailEditText.setError("Please insert email");
            return;
        }
        if (passwordEditText.getText().toString().isEmpty()) {
            passwordEditText.setError("Please insert password");
        }

        netLoginReply.setEmail(emailEditText.getText().toString().trim());
        netLoginReply.setPassword(passwordEditText.getText().toString().trim());
        ((CircularProgressButton) view).startAnimation();
        RestClient.networkHandler().loginUser(netLoginReply)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        User.getInstance().setCurrentUser(response.body());
                        Util.showObjectLog(response.body());
                        ((CircularProgressButton) view).revertAnimation();

                        OfflineHandler.getInstance().storeUsername(emailEditText.getText().toString());
                        OfflineHandler.getInstance().storePassword(passwordEditText.getText().toString());

                        Util.openActivityClosingParent(LandingActivity.this, MainActivity.class);
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable throwable) {
                        Snackbar.make(parentLayout, "Invalid email or password", BaseTransientBottomBar.LENGTH_LONG).show();
                        ((CircularProgressButton) view).revertAnimation();
                    }
                });

    }


    @OnClick({R.id.landing_loginButton, R.id.landing_registerButton, R.id.landing_fbLoginButton})
    public void interaction(View view) {
        switch (view.getId()) {
            case R.id.landing_loginButton:
                sendLogin(view);
                break;
            case R.id.landing_registerButton:
                Util.openActivity(LandingActivity.this, RegisterActivity.class);
                break;
        }
    }
}
