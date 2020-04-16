package com.hisaabkitaab;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String KEY_LOGIN_STATE = "key_login_state";
    private static final String KEY_LOGIN_TOKEN = "key_login_token";

    private SharedPreferences preferences;

    AppPreferences(Context context){
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
        return preferences.getString(KEY_LOGIN_TOKEN, "");
    }
}
