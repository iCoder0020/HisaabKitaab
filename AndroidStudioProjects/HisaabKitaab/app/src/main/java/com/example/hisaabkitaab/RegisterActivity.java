package com.example.hisaabkitaab;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hisaabkitaab.model.Login;
import com.example.hisaabkitaab.model.PostReply;
import com.example.hisaabkitaab.model.Register;
import com.example.hisaabkitaab.model.User;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.GsonBuilder;

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

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(PostApi.root)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();

        PostApi postApi = retrofit.create(PostApi.class);

        Register register = new Register(name, email, username, password);
        Call<PostReply> call = postApi.registrationUser(register);

        call.enqueue(new Callback<PostReply>() {

            @Override
            public void onResponse(Call<PostReply> call, Response<PostReply> response) {
                Log.w("Response from server: ",new GsonBuilder().setPrettyPrinting().create().toJson(response));
                Log.w("hey baby", response.message());

                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //String token = response.body().getToken();

//                        SharedPreferences preferences = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor prefLoginEdit = preferences.edit();
//                        prefLoginEdit.putBoolean("loggedin", true);
//                        prefLoginEdit.putString("token", token);
//                        prefLoginEdit.commit();

//                        Toast.makeText(getContext(), token, Toast.LENGTH_SHORT).show();
                        //performLogin();
                    }
                } else {
                    showErrorDialog("Error received from Server");
//                    Toast.makeText(getContext(), "login no correct :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostReply> call, Throwable t) {
                Log.v("see this: ", t.getMessage());
                showErrorDialog("Cannot Connect to Server");
//                Toast.makeText(getActivity(), "error :(", Toast.LENGTH_SHORT).show();
            }
        });


        // if right ->performLogin();
        // if wrong -> showErrorDialog();

    }

    private void showErrorDialog(String error) {
        new AlertDialog.Builder(this)
                .setTitle("Register Error")
                .setMessage(error)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void startLoginActivity() {
        finish();
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
