package com.licenta.mihai.givemebook_android.Network;

import com.licenta.mihai.givemebook_android.Models.BaseModels.Settings;
import com.licenta.mihai.givemebook_android.Models.BaseModels.UserModel;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetLoginReply;
import com.licenta.mihai.givemebook_android.Models.NetModels.Response.NetStringResponse;

import java.io.File;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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

    @POST("api/user/logout/{id}")
    Call<NetStringResponse> logoutUser(@Header("Authorization") String token, @Path("id") Long userID);

    @POST("/api/user/updateSettings/{id}")
    Call<NetStringResponse> updateSettings(@Header("Authorization") String token, @Path("id") Long userID, @Body Settings settings);

}
