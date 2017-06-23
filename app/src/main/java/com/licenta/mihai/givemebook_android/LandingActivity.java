package com.licenta.mihai.givemebook_android;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.BorderEditText;
import com.licenta.mihai.givemebook_android.Models.BaseModels.UserModel;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetLoginReply;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.Singletons.User;
import com.licenta.mihai.givemebook_android.Utils.OfflineHandler;
import com.licenta.mihai.givemebook_android.Utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

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

    @BindView(R.id.landing_fbLoginButton)
    LoginButton loginButton;


    CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email", "user_photos", "public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("LoginFB", accessToken);
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Util.showObjectLog(response.toString());
                                Log.w("LoginFB", object.toString());
                                proceedRegister(object);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Log.w("LoginFB", "Cancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.w("LoginFB", "Error");
            }
        });

        initRotationAnim();

    }

    private void proceedRegister(JSONObject object) {
        final UserModel userModel = new UserModel();
        try {

            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                userModel.setPhotoUrl(profile_pic.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            if (object.has("id"))
                userModel.setFbID(object.getString("id"));
            if (object.has("first_name"))
                userModel.setUsername(object.getString("first_name"));
            if (object.has("last_name"))
                userModel.setUsername(userModel.getUsername() + " " + object.getString("last_name"));
            if (object.has("email"))
                userModel.setEmail(object.getString("email"));

            RestClient.networkHandler().registerFacebook(userModel)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            proceedFBLogin(response.body());
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {

                        }
                    });
        } catch (JSONException e) {
            Log.d("LoginFB", "Error parsing JSON");
        }
    }

    private void proceedFBLogin(UserModel userModel) {
        NetLoginReply netLoginReply = new NetLoginReply();
        netLoginReply.setEmail(userModel.getEmail());
        netLoginReply.setPassword(userModel.getPassword());
        sendLogin(null, netLoginReply);
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


    public void buildLogin(final View view) {
        NetLoginReply netLoginReply = new NetLoginReply();
        if (emailEditText.getText().toString().isEmpty()) {
            emailEditText.setError("Please insert email");
            return;
        }
        if (passwordEditText.getText().toString().isEmpty()) {
            passwordEditText.setError("Please insert password");
        }

        netLoginReply.setEmail(emailEditText.getText().toString().trim());
        netLoginReply.setPassword(Util.sha1Hash(passwordEditText.getText().toString().trim()));
        ((CircularProgressButton) view).startAnimation();
        sendLogin((CircularProgressButton) view, netLoginReply);

    }

    private void sendLogin(final CircularProgressButton view, NetLoginReply netLoginReply) {
        RestClient.networkHandler().loginUser(netLoginReply)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        if (response.code() == 200) {
                            User.getInstance().setCurrentUser(response.body());
                            Util.showObjectLog(response.body());
                            if (view != null) view.revertAnimation();

                            OfflineHandler.getInstance().storeUsername(response.body().getEmail());
                            OfflineHandler.getInstance().storePassword(response.body().getPassword());

                            Util.openActivityClosingParent(LandingActivity.this, MainActivity.class);
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable throwable) {
                        Snackbar.make(parentLayout, "Invalid email or password", BaseTransientBottomBar.LENGTH_LONG).show();
                        if (view != null) view.revertAnimation();
                    }
                });
    }


    @OnClick({R.id.landing_loginButton, R.id.landing_registerButton, R.id.landing_fbLoginButton})
    public void interaction(View view) {
        switch (view.getId()) {
            case R.id.landing_loginButton:
                buildLogin(view);
                break;
            case R.id.landing_registerButton:
                Util.openActivity(LandingActivity.this, RegisterActivity.class);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,
                resultCode, data);
    }

}
