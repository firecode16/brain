package com.brain.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.brain.R;

public class LoginActivity extends AppCompatActivity {

    private Intent navigation = null;
    protected Button btnSignIn;
    private final boolean response = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnSignIn = findViewById(R.id.btnSignIn);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response) {
                    navigation = new Intent(LoginActivity.this, HomeActivity.class);
                    LoginActivity.this.startActivity(navigation);
                }
            }
        });
    }

    public void viewRegisterClicked(View view) {
        navigation = new Intent(LoginActivity.this, RegisterActivity.class);
        LoginActivity.this.startActivity(navigation);
    }

}