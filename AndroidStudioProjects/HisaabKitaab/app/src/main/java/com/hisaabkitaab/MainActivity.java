package com.hisaabkitaab;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(v -> onLoginClicked());

    }

    private void onLoginClicked() {
        Intent intent = new Intent(this, com.hisaabkitaab.PersonActivity.class);
        startActivity(intent);
        finish();
    }

}
