package com.vendorapp;

import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.vendorapp.model.FriendsListCallback;
import com.vendorapp.model.PaymentList;
import com.vendorapp.model.SendQuery;
import com.vendorapp.model.UserReply;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://aristaheroku.herokuapp.com/";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if(mInstance == null){
            mInstance = new RetrofitClient();
        }
        return mInstance;
    }

    public PostApi getPostApi(){
        return retrofit.create(PostApi.class);
    }

}
