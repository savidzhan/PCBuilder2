package com.savijan.pcbuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class drawer_main_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView txtTest;

    private Button btnLoginInto, btnRegisterInto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        txtTest = findViewById(R.id.txtTest);

        btnLoginInto = (Button) findViewById(R.id.btnLoginInto);
        btnRegisterInto = (Button) findViewById(R.id.btnRegisterInto);

//        btnRegisterInto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new RegisterFragment()).commit();
//
//            }
//        });

    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.btnRegisterInto:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new RegisterFragment()).commit();
                break;
            case R.id.btnLoginInto:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new LoginFragment()).commit();
                break;
            case R.id.nav_category_all:
                txtTest.setText("nav_category_all");
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.nav_category_cpu:
                txtTest.setText("nav_category_cpu");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Processors")).commit();
                break;
            case R.id.nav_category_mom:
                txtTest.setText("nav_category_mom");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Motherboards")).commit();
                break;
            case R.id.nav_category_ram:
                txtTest.setText("nav_category_ram");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("RAM")).commit();
                break;
            case R.id.nav_category_gpu:
                txtTest.setText("nav_category_gpu");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Videocards")).commit();
                break;
            case R.id.nav_category_hdd:
                txtTest.setText("nav_category_hdd");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Hdddisks")).commit();
                break;
            case R.id.nav_category_ssd:
                txtTest.setText("nav_category_ssd");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Ssddiskd")).commit();
                break;
            case R.id.nav_category_case:
                txtTest.setText("nav_category_case");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Cases")).commit();

                break;
            case R.id.nav_category_supply:
                txtTest.setText("nav_category_supply");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Supply")).commit();

                break;
            case R.id.nav_category_cooler:
                txtTest.setText("nav_category_cooler");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Coolers")).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}