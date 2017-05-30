package com.licenta.mihai.givemebook_android;

import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.licenta.mihai.givemebook_android.Models.BaseModels.UserModel;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetRegisterReply;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.Utils.Util;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {


    //region getViews
    @BindView(R.id.register_toolbar)
    Toolbar registerToolbar;

    @BindView(R.id.register_parentLayout)
    ConstraintLayout parentLayout;

    @BindView(R.id.register_username_editText)
    EditText usernameEditText;

    @BindView(R.id.register_email_editText)
    EditText emailEditText;

    @BindView(R.id.register_password_editText)
    EditText passwordEditText;

    @BindView(R.id.register_repassword_editText)
    EditText repasswordEditText;

    @BindView(R.id.register_username_inputLayout)
    TextInputLayout usernameInputLayout;

    @BindView(R.id.register_email_inputLayout)
    TextInputLayout emailInputLayout;

    @BindView(R.id.register_password_inputLayout)
    TextInputLayout passwordInputLayout;

    @BindView(R.id.register_repassword_inputLayout)
    TextInputLayout repasswordInputLayout;
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initComponents();
        releaseInput();
    }

    private void initComponents() {
        setSupportActionBar(registerToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void releaseInput() {
        usernameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                usernameInputLayout.setError(null);
            }
        });
        emailEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emailInputLayout.setError(null);
            }
        });
        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordInputLayout.setError(null);
            }
        });
        repasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                repasswordInputLayout.setError(null);
            }
        });
    }

    public void registerUser(final View view) {

        if (usernameEditText.getText().toString().isEmpty()) {
            usernameInputLayout.setError("Field cannot be left blank.");
            return;
        }
        if (emailEditText.getText().toString().isEmpty()) {
            emailInputLayout.setError("Field cannot be left blank.");
            return;
        }
        if (!Util.validateEmail(emailEditText.getText().toString())) {
            emailInputLayout.setError("Invalid email address.");
            return;
        }
        if (passwordEditText.getText().toString().isEmpty()) {
            passwordInputLayout.setError("Field cannot be left blank.");
            return;
        }
        if (repasswordEditText.getText().toString().isEmpty()) {
            repasswordInputLayout.setError("Field cannot be left blank.");
            return;
        }


        if (!passwordEditText.getText().toString().equals(repasswordEditText.getText().toString())) {
            passwordInputLayout.setError("Passwords do not match.");
            return;
        }
        RequestBody username = RequestBody.create(MediaType.parse("text/plain"), usernameEditText.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), emailEditText.getText().toString());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"), passwordEditText.getText().toString());
        ((CircularProgressButton) view).startAnimation();
        RestClient.networkHandler().registerUser(username, email,
                password, null)
                .enqueue(new Callback<UserModel>() {
                    @Override
                    public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                        Snackbar.make(parentLayout, "Success", BaseTransientBottomBar.LENGTH_LONG).show();
                        ((CircularProgressButton) view).revertAnimation();
                        Util.openActivityClosingParent(RegisterActivity.this,LandingActivity.class);
                    }

                    @Override
                    public void onFailure(Call<UserModel> call, Throwable t) {
                        Snackbar.make(parentLayout, "Something went wrong", BaseTransientBottomBar.LENGTH_LONG).show();
                        ((CircularProgressButton) view).revertAnimation();
                    }
                });

    }


    @OnClick({R.id.register_toolbar_back, R.id.register_continue_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_toolbar_back:
                finish();
                break;
            case R.id.register_continue_button:
                registerUser(view);
                break;
        }
    }
}
