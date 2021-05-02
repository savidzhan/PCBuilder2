package com.savijan.pcbuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegUser;
    private EditText etName, etSurname, etLoginName, etAddress, etPhoneNumber, etRegPass;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etSurname = (EditText) findViewById(R.id.etSurname);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etPhoneNumber = (EditText) findViewById(R.id.etPhoneNumber);
        loadingBar = new ProgressDialog(this);

        btnRegUser = (Button) findViewById(R.id.btnRegUser);
        btnRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }
        });
    }
    private void CreateAccount() {
        String loginName = etLoginName.getText().toString();
        String regPass = etRegPass.getText().toString();
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String userAddress = etAddress.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        if(TextUtils.isEmpty(loginName)||
                TextUtils.isEmpty(regPass)||
                TextUtils.isEmpty(name)||
                TextUtils.isEmpty(surname)||
                TextUtils.isEmpty(userAddress)||
                TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("Создание аккаунта");
            loadingBar.setMessage("Пожалуйста, подождите...");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateLogin(loginName, regPass, name, surname,userAddress, phoneNumber);

        }

    }

    private void ValidateLogin(String loginName, String regPass, String name, String surname, String userAddress, String phoneNumber) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!(snapshot.child("Users").child(loginName).exists())){

                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("login", loginName);
                    userDataMap.put("password", regPass);
                    userDataMap.put("name", name);
                    userDataMap.put("surname", surname);
                    userDataMap.put("address", userAddress);
                    userDataMap.put("phone", phoneNumber);

                    RootRef.child("Users").child(loginName).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Ошибка!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else {
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Логин уже занят!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }
}