package com.vendorapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonActivity extends AppCompatActivity {

    ImageView topBar;
    TextView textName;
    TextView textPayment;
    ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        topBar = findViewById(R.id.imageMain);

        textName = findViewById(R.id.textName);
        textName.setText("Arshdeep Singh");
        textName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        textPayment = findViewById(R.id.textPayment);
        textPayment.setText("To Pay: 500");
        textPayment.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        imageBack = findViewById(R.id.imageBack);
        imageBack.setOnClickListener(v -> finish());

    }

}
