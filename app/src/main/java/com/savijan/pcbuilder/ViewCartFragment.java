package com.savijan.pcbuilder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class ViewCartFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private DatabaseReference sDateBase;
    private FirebaseStorage storage;
    private View v;
    private TextView tvOut;

    private RecyclerView mRecyclerView;
    private imageAdapterCart mAdapter;
    private List<Upload> mUploads;

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_cart, container, false);

        init();

        return v;

    }

    private void init() {
        mRecyclerView = v.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUploads = new ArrayList<>();

        tvOut = (TextView) v.findViewById(R.id.tvOut);

        Paper.init(getContext());
        List<String> components = Paper.book().getAllKeys();


        sDateBase = FirebaseDatabase.getInstance().getReference("Categories");
        sDateBase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for (String item : components) {
                    for(DataSnapshot postSnapshot : snapshot.getChildren()){

                        if(postSnapshot.child(item).exists()){
                            Upload upload = postSnapshot.child(item).getValue(Upload.class);
                            mUploads.add(upload);
                        }


                    }
                }

                    mAdapter = new imageAdapterCart(getContext(), mUploads, getFragmentManager());
                    mRecyclerView.setAdapter(mAdapter);


                }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        storage = FirebaseStorage.getInstance();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
