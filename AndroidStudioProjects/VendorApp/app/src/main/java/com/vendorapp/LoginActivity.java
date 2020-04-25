package com.vendorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.content.SharedPreferences;
import android.widget.Toast;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.vendorapp.model.LoginReply;
import com.google.android.material.textfield.TextInputLayout;

import com.vendorapp.model.Login;
import com.vendorapp.model.ToGetUserID;
import com.vendorapp.model.Token;
import com.vendorapp.model.User;
import com.google.gson.GsonBuilder;
import com.vendorapp.model.UserReply;


import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private AppPreferences preferences;

    private TextInputLayout textUsernameLayout;
    private TextInputLayout textPasswordLayout;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = new AppPreferences(this);
        if (preferences.isLoggedIn()) {
            startMainActivity();
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        textUsernameLayout = findViewById(R.id.textUsernameLayout);
        textPasswordLayout = findViewById(R.id.textPasswordLayout);
        loginButton = findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(v -> onLoginClicked());

        textUsernameLayout
                .getEditText()
                .addTextChangedListener(createTextWatcher(textUsernameLayout));

        textPasswordLayout
                .getEditText()
                .addTextChangedListener(createTextWatcher(textPasswordLayout));
    }

    private void onLoginClicked() {
        String username = textUsernameLayout.getEditText().getText().toString();
        String password = textPasswordLayout.getEditText().getText().toString();
        boolean checks = true;
        if (username.isEmpty()) {
            checks = false;
            textUsernameLayout.setError("Username must not be empty");
        }
        if (password.isEmpty()) {
            checks = false;
            textPasswordLayout.setError("Password must not be empty");
        }
        if(checks) {
            textPasswordLayout.setEnabled(false);
            textUsernameLayout.setEnabled(false);
            loginButton.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            Log.v("click login", "entring tryLogin");
            tryLogin(username, password);
        }
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Login Failed")
                .setMessage("Username or password is not correct. Please try again.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private TextWatcher createTextWatcher(final TextInputLayout textSomeLayout) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textSomeLayout.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // not needed
            }
        };
    }

    private void tryLogin(final String username, final String password){

        Log.v("hel", username.concat(password));

        Login login = new Login(username, password);
        Call<LoginReply> call = RetrofitClient.getInstance().getPostApi().login(login);
        Log.v("hel", username.concat(password));
        call.enqueue(new Callback<LoginReply>() {
            @Override
            public void onResponse(Call<LoginReply> call, Response<LoginReply> response) {
                //Log.w("Login Read: ",new GsonBuilder().setPrettyPrinting().create().toJson(response));
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String token = response.body().getToken();
                        preferences.setUserName(username);
                        Log.v("hey baby:",token);
                        preferences.setToken(token);
                        performLogin();
                    }
                } else {
                    showErrorDialog();
                    textPasswordLayout.setEnabled(true);
                    textUsernameLayout.setEnabled(true);
                    loginButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
//                    Toast.makeText(getContext(), "login no correct :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginReply> call, Throwable t) {
                //showErrorDialog();
//                Toast.makeText(getActivity(), "error :(", Toast.LENGTH_SHORT).show();
            }
        });


        // if right ->performLogin();
        // if wrong -> showErrorDialog();

    }

    private void performLogin() {

        ToGetUserID model = new ToGetUserID(preferences.getUserName());
        Log.w("Username: ",preferences.getUserName());

        Call<UserReply> call = RetrofitClient.getInstance().getPostApi().getUserID(model);

        call.enqueue(new Callback<UserReply>() {

            @Override
            public void onResponse(Call<UserReply> call, Response<UserReply> response) {
                Log.w("Login Read: ","Response Recieved!");
                Log.w("Login Read: ",Integer.toString(response.code()));
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        int id = 0;

                        id = response.body().getId();
                        preferences.setId(id);
                        Log.v("hey baby:",Integer.toString(id));

                        preferences.setLoggedIn(true);
//
//                        textPasswordLayout.setEnabled(false);
//                        textUsernameLayout.setEnabled(false);
//                        loginButton.setVisibility(View.INVISIBLE);
//                        progressBar.setVisibility(View.VISIBLE);

                        Handler handler = new Handler();
                        handler.postDelayed(() -> {
                            startMainActivity();
                            finish();
                        }, 2000);
                    }
                } else {
                    showErrorDialog();
                    textPasswordLayout.setEnabled(true);
                    textUsernameLayout.setEnabled(true);
                    loginButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
//                    Toast.makeText(getContext(), "login no correct :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserReply> call, Throwable t) {
                //showErrorDialog();
//                Toast.makeText(getActivity(), "error :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, com.vendorapp.MainActivity.class);
        startActivity(intent);
    }

}