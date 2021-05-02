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

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;


public class RegisterFragment extends Fragment {

    private View v;

    private Button btnRegUser;
    private EditText etName, etSurname, etAddress, etPhoneNumber;
    private List<String> components;
//    ProgressDialog loadingBar;

    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.register_fragment, container, false);

        etName = (EditText) v.findViewById(R.id.etName);
        etSurname = (EditText) v.findViewById(R.id.etSurname);
        etAddress = (EditText) v.findViewById(R.id.etAddress);
        etPhoneNumber = (EditText) v.findViewById(R.id.etPhoneNumber);

        Paper.init(getContext());
        components = Paper.book().getAllKeys();
//        loadingBar = new ProgressDialog(v.getContext());
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
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String userAddress = etAddress.getText().toString();
        String phoneNumber = etPhoneNumber.getText().toString();

        if(TextUtils.isEmpty(name)||
                TextUtils.isEmpty(surname)||
                TextUtils.isEmpty(userAddress)||
                TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(v.getContext(), "Заполните все поля!", Toast.LENGTH_SHORT).show();
        } else {

            ValidateLogin(name, surname, userAddress, phoneNumber);

        }
    }

    private void ValidateLogin(String name, String surname, String userAddress, String phoneNumber) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    HashMap<String, Object> userDataMap = new HashMap<>();
                    HashMap<String, Object> productData = new HashMap<>();

                    userDataMap.put("name", name);
                    userDataMap.put("surname", surname);
                    userDataMap.put("address", userAddress);
                    userDataMap.put("phone", phoneNumber);
                    for(String item : components){
                        productData.put(item, "1");
                    }

                    RootRef.child("Orders").child(name).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
//                                        loadingBar.dismiss();
//                                        Toast.makeText(v.getContext(), "Переход к оплате", Toast.LENGTH_SHORT).show();
                                        RootRef.child("Orders").child(name).child("Products").updateChildren(productData);
//                                        Intent intent = new Intent(v.getContext(), LoginFragment.class);
//                                        startActivity(intent);
                                        getFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new PayFragment()).commit();

                                    } else {
//                                        loadingBar.dismiss();
                                        Toast.makeText(v.getContext(), "Ошибка!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

    }


}
