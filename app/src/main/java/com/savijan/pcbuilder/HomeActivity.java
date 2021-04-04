package com.savijan.pcbuilder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {

    private Button btnCategory, btnAddComponent;
    private String parentDBName, userName;
    private TextView btnLogout, homeWelcome;
    private boolean userIsAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        userName = intent.getStringExtra("name");
        userIsAdmin = intent.getBooleanExtra("userIsAdmin", userIsAdmin);

        homeWelcome = (TextView) findViewById(R.id.homeWelcome);
        homeWelcome.setText("Добро пожаловать " + userName);

        btnAddComponent = (Button) findViewById(R.id.btnAddComponent);

        if(userIsAdmin){
            btnAddComponent.setVisibility(View.VISIBLE);
        }

        btnAddComponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, ComponentAddActivity.class);
                startActivity(intent);
            }
        });

        btnLogout = (TextView) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Paper.book().destroy();

                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnCategory = findViewById(R.id.btnCategory);
        btnCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
                startActivity(intent);
            }
        });

    }
}