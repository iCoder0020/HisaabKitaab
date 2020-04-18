package com.hisaabkitaab;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartupActivity extends AppCompatActivity {

    private AppPreferences preferences;
    private Button loginButton;
    private Button registerButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new AppPreferences(this);
//        if (preferences.isLoggedIn()) {
//            startMainActivity();
//            finish();
//            return;
//        }

        setContentView(R.layout.activity_startup);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        loginButton.setOnClickListener(v -> onLoginClicked());
        registerButton.setOnClickListener(v -> onRegisterClicked());


    }

    private void onLoginClicked() {
        startLoginActivity();
    }

    private void onRegisterClicked() {
        startRegisterActivity();
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, com.hisaabkitaab.MainActivity.class);
        startActivity(intent);
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, com.hisaabkitaab.LoginActivity.class);
        startActivity(intent);
    }

    private void startRegisterActivity() {
        Intent intent = new Intent(this, com.hisaabkitaab.RegisterActivity.class);
        startActivity(intent);
    }

}
