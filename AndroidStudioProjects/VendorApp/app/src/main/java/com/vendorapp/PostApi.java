package com.vendorapp;

import com.vendorapp.model.AddPayment;
import com.vendorapp.model.FriendIDList;
import com.vendorapp.model.Login;
import com.vendorapp.model.LoginReply;
import com.vendorapp.model.PaymentList;
import com.vendorapp.model.PostReply;
import com.vendorapp.model.SendQuery;
import com.vendorapp.model.ToGetUserID;
import com.vendorapp.model.Token;
import com.vendorapp.model.User;
import com.vendorapp.model.Register;
import com.vendorapp.model.UserReply;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PostApi {

    //Login and get token
    @POST("/auth/jwt/create/")
    Call<LoginReply> login(@Body Login login);

    //Register
    @POST("/auth/users/")
    Call<ResponseBody> registrationUser(@Body User userModel);

    //Update profile for details
    @PUT("/api/accounts/all-profiles/")
    Call<ResponseBody> updateProfile(@Body User userModel);


    //Get User Details
    @GET("/auth/users/me")
    Call<UserReply> getUser(@Body Token tokenModel);

    // Get UserID from UserName
    @POST("/user/")
    Call<UserReply> getUserID(@Body ToGetUserID model);

    // Get all friends ID
    @POST("/friend/")
    Call<FriendIDList> getFriendIDList(@Body SendQuery model);

    // Create new Payment
    @POST("/payment_user/")
    Call<ResponseBody> newPayment(@Body AddPayment model);

    // Create new Payment
    @POST("/payment_user/")
    Call<PaymentList> getPaymentList(@Body SendQuery model);


//    @GET("post/list/")
//    Call<List<PostModel>> getListPost();
//
//    @GET("post/{id}/")
//    Call<PostModel> getPost(@Path(value = "id", encoded = true) String id);
//
//    @POST("add/")
//    Call<PostModel> addPost(@Header("Authorization")  String authToken, @Body PostModel postModel);
//
//    @GET("profile/list/")
//    Call<List<PostModel>> getProfileList(@Header("Authorization")  String authToken);
//
//
//    @PUT("profile/edit/{id}/")
//    Call<PostModel> updatePost(@Header("Authorization")  String authToken, @Body PostModel postModelUpdate, @Path(value = "id", encoded = true) String id);
//
//
//    @DELETE("profile/delete/{id}/")
//    Call<List<PostModel>> deletePost(@Header("Authorization")  String authToken, @Path(value = "id", encoded = true) String id);
//
//
//    @GET("category/list/")
//    Call<List<CategoryModel>> getAllCategory();
//
//    @GET("list/{id}/")
//    Call<List<CategoryModel>> getCategoryById(@Path(value = "id", encoded = true) Integer id);

}