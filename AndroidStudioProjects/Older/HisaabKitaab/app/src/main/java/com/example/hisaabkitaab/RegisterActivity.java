package com.example.hisaabkitaab;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hisaabkitaab.model.Login;
import com.example.hisaabkitaab.model.PostReply;
import com.example.hisaabkitaab.model.Register;
import com.example.hisaabkitaab.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout textNameLayout;
    private TextInputLayout textEmailLayout;
    private TextInputLayout textUsernameLayout;
    private TextInputLayout textPasswordLayout;
    private TextInputLayout textRepeatPasswordLayout;

    private Button loginButton;
    private Button registerButton;

    private ProgressBar progressBar;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        textNameLayout = findViewById(R.id.textNameLayout);
        textEmailLayout = findViewById(R.id.textEmailLayout);
        textUsernameLayout = findViewById(R.id.textUsernameLayout);
        textPasswordLayout = findViewById(R.id.textPasswordLayout);
        textRepeatPasswordLayout = findViewById(R.id.textRepeatPasswordLayout);

        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(v -> onLoginClicked());
        registerButton.setOnClickListener(v -> onRegisterClicked());

        textNameLayout
                .getEditText()
                .addTextChangedListener(createTextWatcher(textNameLayout));

        textEmailLayout
                .getEditText()
                .addTextChangedListener(createTextWatcher(textEmailLayout));

        textUsernameLayout
                .getEditText()
                .addTextChangedListener(createTextWatcher(textUsernameLayout));

        textPasswordLayout
                .getEditText()
                .addTextChangedListener(createTextWatcher(textPasswordLayout));

        textRepeatPasswordLayout
                .getEditText()
                .addTextChangedListener(createTextWatcher(textRepeatPasswordLayout));

    }

    private void onLoginClicked() {
        startLoginActivity();
        finish();
    }

    private void onRegisterClicked() {
        String name = textNameLayout.getEditText().getText().toString();
        String email = textEmailLayout.getEditText().getText().toString();
        String username = textUsernameLayout.getEditText().getText().toString();
        String password = textPasswordLayout.getEditText().getText().toString();
        String repeatpassword = textRepeatPasswordLayout.getEditText().getText().toString();

        Boolean checks = true;

        if (name.isEmpty()) {
            checks = false;
            textNameLayout.setError("Name must not be empty");
        }
        if (email.isEmpty()) {
            checks = false;
            textEmailLayout.setError("Email must not be empty");
        }
        if (username.isEmpty()) {
            checks = false;
            textUsernameLayout.setError("Username must not be empty");
        }
        if (password.isEmpty()) {
            checks = false;
            textPasswordLayout.setError("Password must not be empty");
        }
        if (repeatpassword.isEmpty()) {
            checks = false;
            textRepeatPasswordLayout.setError("Repeat Password must not be empty");
        }
        if (!repeatpassword.equals(password)) {
            checks = false;
            textRepeatPasswordLayout.getEditText().setText("");
            textPasswordLayout.getEditText().setText("");
            showErrorDialog("Passwords doesn't match");
        }
        if(!email.endsWith("@goa.bits-pilani.ac.in")){
            checks = false;
            textEmailLayout.getEditText().setText("");
            showErrorDialog("Email should be from BITS Goa");
        }
        if(checks) {
            Log.v("click login", "entring tryLogin");
            tryRegister(name, email, username, password);
        }
    }

    private void tryRegister(
            final String name,
            final String email,
            final String username,
            final String password)
    {

        User userModel = new User(1, email, username, password, "randomise");
        Call<ResponseBody> call = RetrofitClient.getInstance().getPostApi().registrationUser(userModel);

        call.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.w("Response from server: ",new GsonBuilder().setPrettyPrinting().create().toJson(response));


                if (response.isSuccessful()) {
                    if (response.body() != null) {

                        //String token = response.body().getToken();
                        try {
                            Log.w("hey baby", response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        showSuccessDialog(username);
                        performRegister();
                    }
                } else {
                    try {
                        String error = response.errorBody().string();
                        Log.w("hey baby", error);
                        showErrorDialog(error);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    showErrorDialog("Error received from Server");
//                    Toast.makeText(getContext(), "login no correct :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v("see this: ", t.getMessage());
                showErrorDialog("Cannot Connect to Server");
//                Toast.makeText(getActivity(), "error :(", Toast.LENGTH_SHORT).show();
            }
        });


        // if right ->performLogin();
        // if wrong -> showErrorDialog();

    }

    private void performRegister(){

        textNameLayout.setEnabled(false);
        textEmailLayout.setEnabled(false);
        textRepeatPasswordLayout.setEnabled(false);
        textPasswordLayout.setEnabled(false);
        textUsernameLayout.setEnabled(false);

        registerButton.setVisibility(View.INVISIBLE);
        loginButton.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startLoginActivity();
            finish();
        }, 2000);
    }

    private void showErrorDialog(String error) {
        new AlertDialog.Builder(this)
                .setTitle("Register Error")
                .setMessage(error)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void showSuccessDialog(String username){
        new AlertDialog.Builder(this)
                .setTitle("Account Created")
                .setMessage("Your account with the username "
                        .concat(username)
                        .concat(" has been successfully created!"))
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, com.example.hisaabkitaab.LoginActivity.class);
        startActivity(intent);
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

}
