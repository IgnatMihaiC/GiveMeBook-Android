package com.licenta.mihai.givemebook_android.Network;

import android.preference.Preference;

import com.licenta.mihai.givemebook_android.Models.BaseModels.BookReview;
import com.licenta.mihai.givemebook_android.Models.BaseModels.BookState;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Interactions;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Preferences;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Recommendations;
import com.licenta.mihai.givemebook_android.Models.BaseModels.Settings;
import com.licenta.mihai.givemebook_android.Models.BaseModels.SharedUser;
import com.licenta.mihai.givemebook_android.Models.BaseModels.UserModel;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetBookReviewReplay;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetBookStateReplay;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetInteractions;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetLoginReply;
import com.licenta.mihai.givemebook_android.Models.NetModels.Replay.NetPreferences;
import com.licenta.mihai.givemebook_android.Models.NetModels.Response.NetStringResponse;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @POST("api/user/registerFacebook")
    Call<UserModel> registerFacebook(@Body UserModel userModel);

    @POST("api/user/updatePassword/{id}")
    Call<NetStringResponse> changePassword(@Header("Authorization") String token, @Path("id") Long userID, @Body RequestBody password);

    @Multipart
    @POST("/api/user/updatePhoto")
    Call<NetStringResponse> updatePhoto(@Header("Authorization") String token, @Part("id") Long userID, @Part MultipartBody.Part profilePhoto);

    @POST("api/user/login")
    Call<UserModel> loginUser(@Body NetLoginReply netLoginReply);

    @POST("api/user/logout/{id}")
    Call<NetStringResponse> logoutUser(@Header("Authorization") String token, @Path("id") Long userID);

    @POST("api/user/updateSettings/{id}")
    Call<NetStringResponse> updateSettings(@Header("Authorization") String token, @Path("id") Long userID, @Body Settings settings);

    @POST("api/user/updatePreferences/{id}")
    Call<List<Preferences>> updatePreferences(@Header("Authorization") String token, @Path("id") Long userID, @Body List<NetPreferences> preferences);

    @GET("api/user/getUser/{id}")
    Call<SharedUser> getUserById(@Header("Authorization") String token, @Path("id") Long userID);

    @Multipart
    @POST("api/book/addBook")
    Call<NetStringResponse> addBook(@Header("Authorization") String token,
                                    @Part("userID") Long userID,
                                    @Part("bookTitle") RequestBody bookTitle,
                                    @Part("author") RequestBody author,
                                    @Part("description") RequestBody description,
                                    @Part("categories") RequestBody categories,
                                    @Part("cover_photo") File file);

    @GET("api/recommendation/get/{id}")
    Call<List<Recommendations>> getRecommendations(@Header("Authorization") String token, @Path("id") Long userID);

    @POST("api/book/addBookReview/{id}")
    Call<NetStringResponse> addBookReview(@Header("Authorization") String token, @Path("id") Long bookID, @Body NetBookReviewReplay bookReview);

    @POST("api/user/addInteraction")
    Call<Interactions> addInteractions(@Header("Authorization") String token, @Body NetInteractions interactions);

    @POST("api/user/removeInteraction")
    Call<NetStringResponse> removeInteraction(@Header("Authorization") String token, @Body NetInteractions interactions);


    @POST("api/booktouser/addBookState")
    Call<NetStringResponse> addBookState(@Header("Authorization") String token, @Body NetBookStateReplay bookStateReplay);

    @POST("api/booktouser/removeBookState")
    Call<NetStringResponse> removeBookState(@Header("Authorization") String token, @Body NetBookStateReplay bookStateReplay);

    @GET("api/booktouser/getBookStateByUser/{id}")
    Call<List<BookState>> getBookStateByUser(@Header("Authorization") String token, @Path("id") Long uid);



}
