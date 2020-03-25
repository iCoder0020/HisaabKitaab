package com.example.hisaabkitaab;

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

import com.google.android.material.textfield.TextInputLayout;

import com.example.hisaabkitaab.model.Login;
import com.example.hisaabkitaab.model.User;
import com.google.gson.GsonBuilder;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    private AppPreferences preferences;

    private TextInputLayout textUsernameLayout;
    private TextInputLayout textPasswordLayout;
    private Button loginButton;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        preferences = new AppPreferences(this);
//        if (preferences.isLoggedIn()) {
//            startMainActivity();
//            finish();
//            return;
//        }

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

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(PostApi.root)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        PostApi postApi = retrofit.create(PostApi.class);

        Login login = new Login(username, password);
        Call<User> call = postApi.login(login);

        call.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.w("Login Read: ",new GsonBuilder().setPrettyPrinting().create().toJson(response));
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        String token = response.body().getToken();

//                        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor prefLoginEdit = preferences.edit();
//                        prefLoginEdit.putBoolean("loggedin", true);
//                        prefLoginEdit.putString("token", token);
//                        prefLoginEdit.commit();

//                        Toast.makeText(getContext(), token, Toast.LENGTH_SHORT).show();
                        performLogin();
                    }
                } else {
                    showErrorDialog();
//                    Toast.makeText(getContext(), "login no correct :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                //showErrorDialog();
//                Toast.makeText(getActivity(), "error :(", Toast.LENGTH_SHORT).show();
            }
        });


        // if right ->performLogin();
        // if wrong -> showErrorDialog();

    }

    private void performLogin() {
        // preferences.setLoggedIn(true);

        textPasswordLayout.setEnabled(false);
        textUsernameLayout.setEnabled(false);
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startMainActivity();
            finish();
        }, 2000);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, com.example.hisaabkitaab.MainActivity.class);
        startActivity(intent);
    }

}