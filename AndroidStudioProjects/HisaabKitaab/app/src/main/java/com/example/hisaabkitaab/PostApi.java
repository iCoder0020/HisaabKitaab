package com.example.hisaabkitaab;

import com.example.hisaabkitaab.model.Login;
import com.example.hisaabkitaab.model.PostReply;
import com.example.hisaabkitaab.model.User;
import com.example.hisaabkitaab.model.Register;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PostApi {


    String root = "http://192.168.43.55:8000/";
//    String root = "http://127.0.0.1:8000/";


    String base_local = root + "api/v1/";
    String BASE_URL = base_local + "";
    String POST_URL = base_local + "post/";
    String API_URL = root + "api/v1/";


    //Login and get token
    @POST("api-token-auth/")
    Call<User> login(@Body Login login);

    //Register
    @POST("account/")
    Call<PostReply> registrationUser(@Body Register register);

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