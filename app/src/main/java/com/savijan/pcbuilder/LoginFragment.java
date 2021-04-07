package com.savijan.pcbuilder;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.savijan.pcbuilder.Model.Users;
import com.savijan.pcbuilder.Prevalent.Prevalent;

import org.jetbrains.annotations.NotNull;

import io.paperdb.Paper;

public class LoginFragment extends Fragment {

    private View v;

    private Button btnLoginIn;
    private EditText etLoginName, etPassword;
    private ProgressDialog loadingBar;
    private CheckBox chbRememberMe;
    public boolean isLoginned = false;

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.login_fragment, container, false);

        etLoginName = (EditText) v.findViewById(R.id.etLoginName);
        etPassword = (EditText) v.findViewById(R.id.etPassword);
        btnLoginIn = (Button) v.findViewById(R.id.btnLoginIn);
        chbRememberMe = (CheckBox) v.findViewById(R.id.rememberMe);
        Paper.init(v.getContext());
        loadingBar = new ProgressDialog(v.getContext());


        btnLoginIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        return v;
    }

    private void loginUser() {
        String loginName = etLoginName.getText().toString();
        String password = etPassword.getText().toString();

        if(TextUtils.isEmpty(loginName)||TextUtils.isEmpty(password)){
            Toast.makeText(v.getContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
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
                if(snapshot.child("Users").child(loginName).exists()){

                    Users usersData = snapshot.child("Users").child(loginName).getValue(Users.class);

                    if(usersData.getLogin().equals(loginName)){
                        if(usersData.getPassword().equals(password)){


                            loadingBar.dismiss();
                            Toast.makeText(v.getContext(), "Вход прошел успешно!", Toast.LENGTH_SHORT).show();
                            isLoginned = true;
                            Intent intent = new Intent(v.getContext(), drawer_main_activity.class);
                            intent.putExtra("name", loginName);
                            intent.putExtra("isLoginned", true);
                            startActivity(intent);

                        }
                        else {
                            loadingBar.dismiss();
                            Toast.makeText(v.getContext(), "Неверный пароль!", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    loadingBar.dismiss();
                    Toast.makeText(v.getContext(), "Такого логина не существует!", Toast.LENGTH_SHORT).show();
                    Fragment regFrag = new RegisterFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_conteiner, regFrag);
                    ft.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}
