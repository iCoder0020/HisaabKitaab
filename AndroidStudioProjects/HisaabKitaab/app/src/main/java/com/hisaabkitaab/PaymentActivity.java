package com.hisaabkitaab;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.hisaabkitaab.model.AddPayment;
import com.hisaabkitaab.model.Login;
import com.hisaabkitaab.model.LoginReply;
import com.hisaabkitaab.model.ToGetUserID;
import com.hisaabkitaab.model.UserReply;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentActivity extends AppCompatActivity {
    private AppPreferences preferences;
    private Button saveButton;
    private MaterialToolbar toolbar;

    private TextInputLayout textUsernameLayout;
    private TextInputLayout textAmountLayout;
    private TextInputLayout textDescriptionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        preferences = new AppPreferences(this);

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> onSaveClicked());

        textUsernameLayout = findViewById(R.id.textUsernameLayout);
        textDescriptionLayout = findViewById(R.id.textDescriptionLayout);
        textAmountLayout = findViewById(R.id.textAmountLayout);

        textUsernameLayout.setEnabled(true);
        //textUsernameLayout.getEditText().setText("arsh99");
        textDescriptionLayout.setEnabled(true);
        //textDescriptionLayout.getEditText().setText("Food King");
        textAmountLayout.setEnabled(true);
        //textAmountLayout.getEditText().setText("5000");

        toolbar = findViewById(R.id.toolbar);

        //setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> finish());

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.delete) {
                performDelete();
            }
            return false;
        });

    }

    private void setPaymentScreen(String username, String description, String amount){
        textUsernameLayout.getEditText().setText(username);
        textDescriptionLayout.getEditText().setText(description);
        textAmountLayout.getEditText().setText(amount);
    }

    private void performDelete() {
        // TODO Delete the record
    }

    private void onSaveClicked() {
        String username = textUsernameLayout.getEditText().getText().toString();
        String amount = textAmountLayout.getEditText().getText().toString();
        String description = textDescriptionLayout.getEditText().getText().toString();
        double doublenumber = 0;
        boolean checks = true;
        if (username.isEmpty()) {
            checks = false;
            textUsernameLayout.setError("Username must not be empty");
        }
        if (amount.isEmpty()) {
            checks = false;
            textAmountLayout.setError("Amount must not be empty");
        }
        if(description.isEmpty()){
            checks = false;
            textDescriptionLayout.setError("Description mus not be empty");
        }
        if(isNumeric(amount)){
            doublenumber = Double.parseDouble(amount);
        }
        else{
            checks = false;
            textAmountLayout.setError("Amount should be a number");
        }
        if(checks) {
            Log.v("click login", "entring tryLogin");
            trySave(username, doublenumber, description);
        }
    }

    private void trySave(String username, double amount, String description) {

        ToGetUserID model = new ToGetUserID(username);

        Call<UserReply> call = RetrofitClient.getInstance().getPostApi().getUserID(model);

        call.enqueue(new Callback<UserReply>() {

            @Override
            public void onResponse(Call<UserReply> call, Response<UserReply> response) {
                Log.w("Login Read: ", "Response Recieved!");
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        int id = 0;
                        id = response.body().getId();
                        performSave(id, amount, description);
                    }
                } else {
                    showErrorDialog("UserName Not Valid");
                    textUsernameLayout.setError("Not Valid");
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



    private void performSave(int id, double amount, String description){

        AddPayment paymentobj = new AddPayment(amount, amount, id, preferences.getId(), description);
        Call call = RetrofitClient.getInstance().getPostApi().newPayment(paymentobj);

        //Log.v("hel", username.concat(password));

        call.enqueue(new Callback<LoginReply>() {
            @Override
            public void onResponse(Call<LoginReply> call, Response<LoginReply> response) {
                //Log.w("Login Read: ",new GsonBuilder().setPrettyPrinting().create().toJson(response));
                if (response.isSuccessful()) {
                    Toast.makeText(PaymentActivity.this, "Payment Saved!", Toast.LENGTH_LONG).show();
                    Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        finish();
                    }, 1000);
//                    if (response.body() != null) {
//
//                    }
                } else {
                    showErrorDialog("Server Error!");
//                    Toast.makeText(getContext(), "login no correct :(", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginReply> call, Throwable t) {
                //showErrorDialog();
//                Toast.makeText(getActivity(), "error :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showErrorDialog(String messgae) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(messgae)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
