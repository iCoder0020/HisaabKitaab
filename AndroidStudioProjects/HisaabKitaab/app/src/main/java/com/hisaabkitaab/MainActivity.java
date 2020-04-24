package com.hisaabkitaab;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.hisaabkitaab.AppPreferences;
import com.hisaabkitaab.adapter.MainAdapter;
import com.hisaabkitaab.model.FriendIDList;
import com.hisaabkitaab.model.FriendList;
import com.hisaabkitaab.model.Payment;
import com.hisaabkitaab.model.PaymentList;
import com.hisaabkitaab.model.SendQuery;
import com.hisaabkitaab.model.ToGetUserID;
import com.hisaabkitaab.model.UserReply;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private AppPreferences preferences;
    private Button button;
    private FloatingActionButton addButton;
    private MaterialToolbar toolbar;
    private PaymentList payments;
    private FriendList friends;
    private MainAdapter adapter;
    private SwipeRefreshLayout refreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = new AppPreferences(this);

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> newPayment());

//        button = findViewById(R.id.button);
//        button.setText("Go to Arshdeep");
//        button.setOnClickListener(v -> onLoginClicked());

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(item -> {
            finish();
        });

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                performLogout();
            }
            return false;
        });

        adapter = new MainAdapter();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this::getNewRecords);

        getNewRecords();


    }

    private void getNewRecords(){
        refreshLayout.setRefreshing(true);
        payments = new PaymentList();
        friends = new FriendList();

        //List<int> friendsIDs = getFriendIDs();
        getAllLendedPayments();
        refreshLayout.setRefreshing(false);
    }

    private void printAllPayments() {
        Log.w("PaymentToFriend","Entered");

        for(Payment p: payments.getPayments()){
            if(p.getBorrowerID() == preferences.getId()){
                if(!friends.checkFriend(p.getLenderID())){
                    friends.addFriend(p.getLenderID(), p.getLender_username(), 0);
                }
                friends.getFriend(p.getLenderID()).addPayment(p);
            }
            else{
                if(!friends.checkFriend(p.getBorrowerID())){
                    friends.addFriend(p.getBorrowerID(), p.getBorrower_username(), 0);
                }
                friends.getFriend(p.getBorrowerID()).addPayment(p);
            }
        }

        Log.w("PaymentToFriend",Integer.toString(friends.getFriendList().size()));

        runOnUiThread(() -> {
            adapter.submitList(friends.getFriendList());
        });
    }

    private void getAllLendedPayments() {

        SendQuery model = new SendQuery("lended_to", preferences.getId());

        Call<PaymentList> call = RetrofitClient.getInstance().getPostApi().getPaymentList(model);

        call.enqueue(new Callback<PaymentList>() {

            @Override
            public void onResponse(Call<PaymentList> call, Response<PaymentList> response) {
                Log.w("Friends Read: ","Response Recieved!");
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //Toast.makeText(MainActivity.this, "Payments Recieved!", Toast.LENGTH_LONG).show();
                        payments.addPayments(response.body().getPayments());
                        //Log.w("Size of List Recieved: ", Integer.toString(response.body().getPayments().size()));
                        getAllBorrowedPayments();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error in Loading Payments!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentList> call, Throwable t) {
                //showErrorDialog();
//                Toast.makeText(getActivity(), "error :(", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAllBorrowedPayments() {

        SendQuery model = new SendQuery("borrowed_from", preferences.getId());

        Call<PaymentList> call = RetrofitClient.getInstance().getPostApi().getPaymentList(model);

        call.enqueue(new Callback<PaymentList>() {

            @Override
            public void onResponse(Call<PaymentList> call, Response<PaymentList> response) {
                Log.w("Friends Read: ","Response Recieved!");
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        //Toast.makeText(MainActivity.this, "Payments Recieved!", Toast.LENGTH_LONG).show();
                        payments.addPayments(response.body().getPayments());
                        //Log.w("Size of List Recieved: ", Integer.toString(response.body().getPayments().size()));
                        printAllPayments();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Error in Loading Payments!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentList> call, Throwable t) {
                //showErrorDialog();
//                Toast.makeText(getActivity(), "error :(", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void newPayment() {
        Intent intent = new Intent(this, com.hisaabkitaab.PaymentActivity.class);
        startActivity(intent);
    }

    private void onLoginClicked() {
        Intent intent = new Intent(this, com.hisaabkitaab.PersonActivity.class);
        startActivity(intent);
    }

    private void performLogout() {
        String[] items = {"Title", "Date"};
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirm Logout?")
                .setMessage("By Logging out, you will lose your local data.")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logoutAccount();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Aborting mission...");
                    }
                })
                .show();
    }

    private void logoutAccount() {
        preferences = new AppPreferences(this);
        preferences.logout();
        Handler handler = new Handler();
        startStartUpActivity();
        finish();
    }

    private void startStartUpActivity() {
        Intent intent = new Intent(this, com.hisaabkitaab.LoginActivity.class);
        startActivity(intent);
    }

}
