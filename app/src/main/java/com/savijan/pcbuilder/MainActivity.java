package com.savijan.pcbuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savijan.pcbuilder.Model.Users;
import com.savijan.pcbuilder.Prevalent.Prevalent;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnRegister, btnLogin;
    private String parentDBName = "Users";
    private ProgressDialog loadingBar;
    private boolean userIsAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this::onClick);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this::onClick);

        loadingBar = new ProgressDialog(this);

        Paper.init(this);

        String UserLoginKey = Paper.book().read(Prevalent.UserLoginKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if(UserLoginKey != "" && UserPasswordKey != ""){
            if(!TextUtils.isEmpty(UserLoginKey) && !TextUtils.isEmpty(UserPasswordKey)){

                loadingBar.setTitle("Вход в приложение");
                loadingBar.setMessage("Пожалуйста, подождите...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

                ValidateUser(UserLoginKey, UserPasswordKey);
            }
        }

    }

    private void ValidateUser(String loginName, String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child(parentDBName).child(loginName).exists()){

                    Users usersData = snapshot.child(parentDBName).child(loginName).getValue(Users.class);

                    if(usersData.getLogin().equals(loginName)){
                        if(usersData.getPassword().equals(password)){

                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Вход прошел успешно!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            intent.putExtra("userIsAdmin", userIsAdmin);
                            intent.putExtra("name", loginName);
                            startActivity(intent);

                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Неверный пароль!", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Такого логина не существует!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.btnLogin:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnRegister:
                intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}