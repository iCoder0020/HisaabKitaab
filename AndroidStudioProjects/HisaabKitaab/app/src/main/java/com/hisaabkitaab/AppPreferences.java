package com.hisaabkitaab;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String KEY_LOGIN_STATE = "key_login_state";
    private static final String KEY_LOGIN_TOKEN = "key_login_token";
    private static final String KEY_NAME = "key_name";
    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_EMAIL = "key_email";
    private static final String KEY_PHONE_NUMBER = "key_phone_number";
    private static final String KEY_USER_ID = "key_id";


    private SharedPreferences preferences;

    public AppPreferences(Context context){
        preferences = context.getSharedPreferences("hisaab-kitaab", Context.MODE_PRIVATE);
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(KEY_LOGIN_STATE, false);
    }

    public void setLoggedIn(boolean loggedIn){
        preferences.edit().putBoolean(KEY_LOGIN_STATE, loggedIn).apply();
    }

    public void setToken(String token){
        preferences.edit().putString(KEY_LOGIN_TOKEN, token).apply();
    }

    public String getToken(){
        return preferences.getString(KEY_LOGIN_TOKEN, null);
    }

    public void setName(String name){
        preferences.edit().putString(KEY_NAME, name).apply();
    }

    public String getName(){
        return preferences.getString(KEY_NAME, null);
    }

    public void setEmail(String email){
        preferences.edit().putString(KEY_EMAIL, email).apply();
    }

    public String getEmail(){
        return preferences.getString(KEY_EMAIL, null);
    }

    public void setPhoneNumber(String phoneNumber){
        preferences.edit().putString(KEY_PHONE_NUMBER, phoneNumber).apply();
    }

    public String getPhoneNumber(){
        return preferences.getString(KEY_PHONE_NUMBER, null);
    }

    public void setUserName(String username){
        preferences.edit().putString(KEY_USERNAME, username).apply();
    }

    public String getUserName(){
        return preferences.getString(KEY_USERNAME, null);
    }

    public void setId(int id){
        preferences.edit().putInt(KEY_USER_ID, id).apply();
    }

    public int getId(){
        return preferences.getInt(KEY_USER_ID, 0);
    }

    public void logout() {
        SharedPreferences.Editor preferencesEditor = preferences.edit();
        preferencesEditor.clear();
        preferencesEditor.apply();
    }

}
