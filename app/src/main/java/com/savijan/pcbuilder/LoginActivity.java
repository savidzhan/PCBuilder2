package com.savijan.pcbuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savijan.pcbuilder.Model.Users;
import com.savijan.pcbuilder.Prevalent.Prevalent;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {

    private Button btnLoginIn;
    private EditText etLoginName, etPassword;
    private ProgressDialog loadingBar;
    private CheckBox chbRememberMe;
    private TextView adminLink, notAdminLink;
    private String parenDBName = "Users";
    boolean userIsAdmin = false;
    int adminNum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        etLoginName = (EditText) findViewById(R.id.etLoginName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btnLoginIn = (Button) findViewById(R.id.btnLoginIn);
        chbRememberMe = (CheckBox) findViewById(R.id.rememberMe);
        adminLink = (TextView) findViewById(R.id.admin_panel_link);
        notAdminLink = (TextView) findViewById(R.id.not_admin_panel_link);
        Paper.init(this);
        loadingBar = new ProgressDialog(this);

        btnLoginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        adminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminLink.setVisibility(View.INVISIBLE);
                notAdminLink.setVisibility(View.VISIBLE);
                parenDBName = "Admins";
                btnLoginIn.setText("Вход для админа");
                userIsAdmin = true;
            }
        });

        notAdminLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notAdminLink.setVisibility(View.INVISIBLE);
                adminLink.setVisibility(View.VISIBLE);
                parenDBName = "Users";
                btnLoginIn.setText("Войти");
                userIsAdmin = false;
            }
        });


    }

    private void loginUser() {
        String loginName = etLoginName.getText().toString();
        String password = etPassword.getText().toString();

        if(TextUtils.isEmpty(loginName)||TextUtils.isEmpty(password)){
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
        } else{

            loadingBar.setTitle("Вход в приложение");
            loadingBar.setMessage("Пожалуйста, подождите...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateLogin(loginName, password);
            
        }

    }

    private void ValidateLogin(String loginName, String password) {

        if(chbRememberMe.isChecked()){
            Paper.book().write(Prevalent.UserLoginKey, loginName);
            Paper.book().write(Prevalent.UserPasswordKey, password);

        }

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parenDBName).child(loginName).exists()){

                    Users usersData = snapshot.child(parenDBName).child(loginName).getValue(Users.class);

                    if(usersData.getLogin().equals(loginName)){
                        if(usersData.getPassword().equals(password)){

                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Вход прошел успешно!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("userIsAdmin", userIsAdmin);
                            intent.putExtra("name", loginName);
                            startActivity(intent);

                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Неверный пароль!", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Такого логина не существует!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}