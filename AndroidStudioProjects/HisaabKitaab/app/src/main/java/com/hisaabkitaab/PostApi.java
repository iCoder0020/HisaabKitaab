package com.hisaabkitaab;

import com.hisaabkitaab.model.Login;
import com.hisaabkitaab.model.LoginReply;
import com.hisaabkitaab.model.PaymentUserReply;
import com.hisaabkitaab.model.Token;
import com.hisaabkitaab.model.User;
import com.hisaabkitaab.model.UserReply;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

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

    @FormUrlEncoded
    @POST("/user")
    Call<UserReply> getUserID(@Field("type") String type, @Field("username") String username);

    @GET("/payment_user/")
    Call<PaymentUserReply> getPaymentUser(@Field("type") String type, @Field("userid") int userid);

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