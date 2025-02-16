package com.brain.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.brain.R;

public class ProfileActivity extends Activity {

    private Intent navigation = null;
    protected View btnSignIn;
    private final boolean response = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnSignIn = findViewById(R.id.btnBackHome);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (response) {
                    navigation = new Intent(ProfileActivity.this, HomeActivity.class);
                    ProfileActivity.this.startActivity(navigation);
                }
            }
        });
    }
}
