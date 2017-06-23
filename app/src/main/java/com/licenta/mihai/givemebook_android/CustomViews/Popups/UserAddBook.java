package com.licenta.mihai.givemebook_android.CustomViews.Popups;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.licenta.mihai.givemebook_android.CustomViews.CustomText.BorderEditText;
import com.licenta.mihai.givemebook_android.Models.NetModels.Response.NetStringResponse;
import com.licenta.mihai.givemebook_android.Network.RestClient;
import com.licenta.mihai.givemebook_android.R;
import com.licenta.mihai.givemebook_android.Singletons.User;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by mihai on 19.06.2017.
 */

public class UserAddBook {
    private Context context;
    private Dialog dialog;
    private DialogInterface.OnDismissListener dismissListener;
    private CircularProgressButton sendButton,dismissButton;
    private BorderEditText titleEditText, authorEditText, descriptionEditText, categoryEditText;

    public UserAddBook(Context context, DialogInterface.OnDismissListener dismissListener) {
        this.context = context;
        this.dismissListener = dismissListener;
    }

    public void init() {
        dialog = new Dialog(context, R.style.CustomAlertDialog);
        dialog.setContentView(R.layout.add_book_layout);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        dialog.setOnDismissListener(dismissListener);


        titleEditText = (BorderEditText) dialog.findViewById(R.id.add_book_title_editText);
        authorEditText = (BorderEditText) dialog.findViewById(R.id.add_book_author_editText);
        descriptionEditText = (BorderEditText) dialog.findViewById(R.id.add_book_description_editText);
        categoryEditText = (BorderEditText) dialog.findViewById(R.id.add_book_categorie_editText);
        sendButton = (CircularProgressButton) dialog.findViewById(R.id.add_book_send_button);
        dismissButton = (CircularProgressButton) dialog.findViewById(R.id.add_book_dismiss_button);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();

            }
        });
    }

    public void showUserPopup() {
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendData() {
        if (titleEditText.getText().toString().isEmpty()) {
            titleEditText.setError("Must be filled");
            return;
        }
        if (authorEditText.getText().toString().isEmpty()) {
            authorEditText.setError("Must be filled");
            return;
        }
        if (descriptionEditText.getText().toString().isEmpty()) {
            descriptionEditText.setError("Must be filled");
            return;
        }
        if (categoryEditText.getText().toString().isEmpty()) {
            categoryEditText.setError("Must be filled");
            return;
        }

        RequestBody title = RequestBody.create(MediaType.parse("text/plain"), titleEditText.getText().toString());
        RequestBody author = RequestBody.create(MediaType.parse("text/plain"), authorEditText.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), descriptionEditText.getText().toString());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", categoryEditText.getText().toString());
        JsonArray jsonElements = new JsonArray();
        jsonElements.add(jsonObject);
        RequestBody categories = RequestBody.create(MediaType.parse("text/plain"), jsonElements.toString());
        RestClient.networkHandler().addBook(
                User.getInstance().getCurrentUser().getToken(),
                User.getInstance().getCurrentUser().getUid(),
                title,
                author,
                description,
                categories,
                null)
                .enqueue(new Callback<NetStringResponse>() {
                    @Override
                    public void onResponse(Call<NetStringResponse> call, Response<NetStringResponse> response) {
                        dismissDialog();
                    }

                    @Override
                    public void onFailure(Call<NetStringResponse> call, Throwable t) {
//                        dismissDialog();
                    }
                });
    }

    public void dismissDialog() {
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
}
