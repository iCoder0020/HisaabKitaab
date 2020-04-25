package com.hisaabkitaab;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hisaabkitaab.adapter.MainAdapter;
import com.hisaabkitaab.adapter.PersonAdapter;
import com.hisaabkitaab.model.Friend;
import com.hisaabkitaab.model.FriendList;
import com.hisaabkitaab.model.Payment;
import com.hisaabkitaab.model.PaymentList;

public class PersonActivity extends AppCompatActivity {

    private ImageView topBar;
    private TextView textName;
    private FloatingActionButton addButton;
    private TextView textPayment;
    private ImageView imageBack;

    private PersonAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private Friend friend;

    private static final String EXTRAS_FRIEND = "EXTRAS_FRIEND";
    private static final String EXTRAS_PAYMENT = "EXTRAS_PAYMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        topBar = findViewById(R.id.imageMain);

        textName = findViewById(R.id.textName);
        textPayment = findViewById(R.id.textPayment);
        imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> finish());

        addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(v -> newPayment());

        adapter = new PersonAdapter( payment ->
                PaymentActivity.startPaymentActivity(this, payment));


        RecyclerView recyclerView = findViewById(R.id.recyclerViewPerson);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

//        refreshLayout = findViewById(R.id.refreshPerson);
//        refreshLayout.setOnRefreshListener(this::getNewRecords(Friend friend));

        showData(getIntent().getExtras().getParcelable(EXTRAS_FRIEND));

    }

    public static void startPersonActivity(Activity activity, Friend friend) {
        Intent intent = new Intent(activity, PersonActivity.class);
        intent.putExtra(EXTRAS_FRIEND, friend);
        activity.startActivity(intent);
    }

    private void newPayment() {
        Payment p = new Payment(0.0,0.0,0, friend.getId(),"",friend.getUsername(),"");
        Intent intent = new Intent(this, com.hisaabkitaab.PaymentActivity.class);
        intent.putExtra(EXTRAS_PAYMENT, p);
        startActivity(intent);
    }

    private void getNewRecords(Friend friend) {
        //refreshLayout.setRefreshing(true);
        //List<int> friendsIDs = getFriendIDs();

        Log.w("PersonActivity", friend.getUsername());
        Log.w("PersonActivity", Integer.toString(friend.getPayments().getPayments().size()));
        this.friend = friend;
        runOnUiThread(() -> {
            adapter.submitList(friend.getPayments().getPayments());
        });
//        refreshLayout.setRefreshing(false);
    }

    private void showData(Friend friend){

        textName.setText(friend.getUsername());
        textName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textName.setTextColor(Color.WHITE);

        String str = "";

        if(friend.getAmount() < 0){
            str = "You Lent : "+Double.toString((friend.getAmount()*-1));
        }
        else{
            str = "You Borrowed : "+Double.toString(friend.getAmount());
        }
        textPayment.setText(str);
        textPayment.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textPayment.setTextColor(Color.WHITE);

        getNewRecords(friend);

    }

}
