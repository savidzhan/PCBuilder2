package com.savijan.pcbuilder;

import com.savijan.pcbuilder.drawer_main_activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class RegisterFragment extends Fragment {

    private View v;

    private Button btnRegUser;
    private EditText etName, etSurname, etLoginName, etAddress, etPhoneNumber, etRegPass;
    ProgressDialog loadingBar;

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.register_fragment, container, false);

        etLoginName = (EditText) v.findViewById(R.id.etRegLoginName);
        etRegPass = (EditText) v.findViewById(R.id.etRegPass);
        etName = (EditText) v.findViewById(R.id.etName);
        etSurname = (EditText) v.findViewById(R.id.etSurname);
        etAddress = (EditText) v.findViewById(R.id.etAddress);
        etPhoneNumber = (EditText) v.findViewById(R.id.etPhoneNumber);
        loadingBar = new ProgressDialog(v.getContext());
        FragmentManager fragmentManager;

        btnRegUser = (Button) v.findViewById(R.id.btnRegUser);
        btnRegUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateAccount();
            }

        });

        return v;
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
            Toast.makeText(v.getContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(v.getContext(), "Регистрация прошла успешно!", Toast.LENGTH_SHORT).show();
//                                        Intent intent = new Intent(v.getContext(), LoginFragment.class);
//                                        startActivity(intent);
                                        Fragment logFrag = new LoginFragment();
                                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                                        ft.replace(R.id.fragment_conteiner, logFrag);
                                        ft.commit();

                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(v.getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else {
                    loadingBar.dismiss();
                    Toast.makeText(v.getContext(), "Логин уже занят!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }


}
