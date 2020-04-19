package com.hisaabkitaab;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class MainActivity extends AppCompatActivity {

    private AppPreferences preferences;
    private Button button;
    private MaterialToolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setText("Go to Arshdeep");
        button.setOnClickListener(v -> onLoginClicked());

        toolbar = findViewById(R.id.toolbar);

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.logout) {
                performLogout();
            }
            return false;
        });

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
        Intent intent = new Intent(this, com.hisaabkitaab.StartupActivity.class);
        startActivity(intent);
    }

}
