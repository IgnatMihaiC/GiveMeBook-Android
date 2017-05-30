package com.licenta.mihai.givemebook_android.Network;

import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetLoginReply;
import com.licenta.mihai.givemebook_android.Models.BaseModels.UserModel;

import java.io.File;

import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by mihai on 23.05.2017.
 */

public interface API {

    @Multipart
    @POST("api/user/register")
    Call<UserModel> registerUser(@Part("username") RequestBody username,
                                 @Part("email") RequestBody email,
                                 @Part("password") RequestBody password,
                                 @Part("photo") File file);

    @POST("api/user/login")
    Call<UserModel> loginUser(@Body NetLoginReply netLoginReply);

}
