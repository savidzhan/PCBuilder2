package com.savijan.pcbuilder;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewListFragment extends Fragment{

    private String categoryName;
    private ListView lvMain;
    private TextView tvOut;
    private ListView lvComponents;
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Map<String, Object> m;
    private SimpleAdapter sAdapter;
    private DatabaseReference sDateBase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    int img = R.drawable.ic_launcher_background;
    private View v;

    public ViewListFragment(String categoryName) {
        this.categoryName = categoryName;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view_list, container, false);

        init();

        getDataFromDB();

        return v;
    }

    private void init() {
        tvOut = (TextView) v.findViewById(R.id.tvOut);
        lvMain = (ListView) v.findViewById(R.id.lvComponents);

        sDateBase = FirebaseDatabase.getInstance().getReference(categoryName);
        tvOut.setText("Вы выбрали категорию: " + categoryName);

        storage = FirebaseStorage.getInstance();
    }

    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(data.size()>0){data.clear(); }

                for(DataSnapshot ds : snapshot.getChildren()){
//                    storageReference = storage.getReferenceFromUrl("gs://pcbuilder-savijan.appspot.com/cpu").child( ds.child("img").getValue()+".png");
                    m = new HashMap<String, Object>();
                    m.put("name", ds.child("name").getValue());
                    m.put("description", ds.child("description").getValue());
                    m.put("image", img);
                    assert m != null;
                    data.add(m);
                }
                String[] from = {"name", "description", "image"};
                int[] to = {R.id.tvName, R.id.tvDiscription, R.id.ivImg};

                SimpleAdapter sAdapter = new SimpleAdapter(getContext(), data, R.layout.components,from, to);
                lvMain.setAdapter(sAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        sDateBase.addValueEventListener(vListener);

    }

}
