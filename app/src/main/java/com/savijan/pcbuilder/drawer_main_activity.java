package com.savijan.pcbuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private boolean isLoginned;

    private Button btnLoginInto, btnRegisterInto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        ((LinearLayout) findViewById(R.id.authIsTrue)).setVisibility(View.VISIBLE);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


//        btnLoginInto = (Button) findViewById(R.id.btnLoginInto);
////        btnRegisterInto = (Button) findViewById(R.id.btnRegisterInto);

//        btnRegisterInto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new RegisterFragment()).commit();
//
//            }
//        });
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Processors")).commit();

//        afterLoginned();

    }

    public void afterLoginned(){

        isLoginned = getIntent().getExtras().getBoolean("isLoginned");
        if(isLoginned == true){
            Toast.makeText(this, "Залогинился", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void goToLoginAfterReg(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new LoginFragment()).commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        switch (item.getItemId()){
//            case R.id.btnRegisterInto:
//                item.setVisible(false);
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new RegisterFragment()).commit();
//                break;
//            case R.id.btnLoginInto:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new LoginFragment()).commit();
//                break;
            case R.id.nav_category_all:
                break;
            case R.id.nav_category_cpu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Processors")).commit();
                break;
            case R.id.nav_category_mom:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Motherboards")).commit();
                break;
            case R.id.nav_category_ram:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("RAM")).commit();
                break;
            case R.id.nav_category_gpu:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Videocards")).commit();
                break;
            case R.id.nav_category_hdd:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Hdddisks")).commit();
                break;
            case R.id.nav_category_ssd:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Ssddiskd")).commit();
                break;
            case R.id.nav_category_case:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Cases")).commit();

                break;
            case R.id.nav_category_supply:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Supply")).commit();

                break;
            case R.id.nav_category_cooler:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewListFragment("Coolers")).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}