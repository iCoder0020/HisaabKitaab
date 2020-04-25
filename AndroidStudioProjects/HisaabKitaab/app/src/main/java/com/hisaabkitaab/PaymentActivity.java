package com.hisaabkitaab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.hisaabkitaab.model.AddPayment;
import com.hisaabkitaab.model.Login;
import com.hisaabkitaab.model.LoginReply;
import com.hisaabkitaab.model.Payment;
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

    private RadioGroup radioGroup;

    private Payment payment;

    private static final String EXTRAS_PAYMENT = "EXTRAS_PAYMENT";

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

        radioGroup = findViewById(R.id.radioGroup);

        textUsernameLayout.setEnabled(true);
        //textUsernameLayout.getEditText().setText("arsh99");
        textDescriptionLayout.setEnabled(true);
        //textDescriptionLayout.getEditText().setText("Food King");
        textAmountLayout.setEnabled(true);
        //textAmountLayout.getEditText().setText("5000");

        toolbar = findViewById(R.id.toolbarPayment);

        //setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> finish());

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.paymentDone) {
                performDone();
            }
            return false;
        });

        showData(getIntent().getExtras().getParcelable(EXTRAS_PAYMENT));

    }

    private void performDone() {

        if(payment.getId() == 0){
            showErrorDialog("Payment is not Saved Yet, Save the Payment First.");
            return;
        }

        AddPayment paymentobj = new AddPayment(payment.getTotalAmount(), payment.getLendedAmount(), payment.getBorrowerID(), payment.getLenderID() ,payment.getDescription());

        paymentobj.setId(payment.getId());
        paymentobj.setType("update");
        paymentobj.setStatus("D");

        Call call = RetrofitClient.getInstance().getPostApi().updatePayment(paymentobj);

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
                    //Log.w("PaymentActivityError: ", response.errorBody());
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

    private void showData(Payment payment){

        this.payment = payment;

        if(payment.getBorrowerID() == 0 && payment.getLenderID() == 0){

        }
        else if(payment.getBorrowerID() == 0){
            textUsernameLayout.getEditText().setText(payment.getLender_username());
        }
        else{
            if(payment.getBorrowerID() == preferences.getId()){
                textUsernameLayout.getEditText().setText(payment.getLender_username());
                radioGroup.check(R.id.radioBorrow);
            }
            else{
                textUsernameLayout.getEditText().setText(payment.getBorrower_username());
                radioGroup.check(R.id.radioLent);
            }

            textDescriptionLayout.getEditText().setText(payment.getDescription());
            textAmountLayout.getEditText().setText(Double.toString(payment.getLendedAmount()));
        }
    }

    public static void startPaymentActivity(Activity activity, Payment payment) {
        Intent intent = new Intent(activity, PaymentActivity.class);
        intent.putExtra(EXTRAS_PAYMENT, payment);
        activity.startActivity(intent);
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
        if(radioGroup.getCheckedRadioButtonId() == View.NO_ID){
            checks = false;
            Toast.makeText(PaymentActivity.this, "No Payment Type Selected", Toast.LENGTH_LONG).show();
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
                        performSave(username, id, amount, description);
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



    private void performSave(String username, int id, double amount, String description){

        AddPayment paymentobj;

        if(radioGroup.getCheckedRadioButtonId() == R.id.radioBorrow){
            Log.w("PaymentActivity", "Borrowed Save");
            paymentobj = new AddPayment(amount, amount, preferences.getId(), id, description);
        }
        else{
            Log.w("PaymentActivity", "Lent Save");
            paymentobj = new AddPayment(amount, amount, id, preferences.getId(), description);
        }

        Call call;

        Log.w("PaymentActivity", Integer.toString(payment.getId()));

        if(payment.getId() == 0){
            call = RetrofitClient.getInstance().getPostApi().newPayment(paymentobj);
        }
        else{
            paymentobj.setId(payment.getId());
            paymentobj.setType("update");
            call = RetrofitClient.getInstance().getPostApi().updatePayment(paymentobj);
        }

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
                } else {
                    showErrorDialog("Server Error!");
                    //Log.w("PaymentActivityError: ", response.errorBody());
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
