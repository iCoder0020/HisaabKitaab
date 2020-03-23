package com.example.hisaabkitaab;

import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {
    private static final String KEY_LOGIN_STATE = "key_login_state";

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
}
