package com.savijan.pcbuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewListActivity extends AppCompatActivity {
    ListView lvMain;
    TextView tvOut;
    private ListView lvComponents;
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Map<String, Object> m;
    private SimpleAdapter sAdapter;
    private DatabaseReference sDateBase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String TABLE_KEY;
    int img = R.drawable.ic_launcher_background;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list);

        init();
        getDataFromDB();



    }

    private void init() {
        tvOut = (TextView) findViewById(R.id.tvOut);
        lvMain = (ListView) findViewById(R.id.lvComponents);
        Intent intent = getIntent();
        TABLE_KEY = intent.getStringExtra("category");

        sDateBase = FirebaseDatabase.getInstance().getReference(TABLE_KEY);
        tvOut.setText("Вы выбрали категорию: " + TABLE_KEY);

        storage = FirebaseStorage.getInstance();




    }

    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(data.size()>0){data.clear(); }

                for(DataSnapshot ds : snapshot.getChildren()){
                    storageReference = storage.getReferenceFromUrl("gs://pcbuilder-savijan.appspot.com/cpu").child( ds.child("img").getValue()+".png");
                    m = new HashMap<String, Object>();
                    m.put("name", ds.child("name").getValue());
                    m.put("description", ds.child("description").getValue());
                    m.put("image", img);
                    assert m != null;
                    data.add(m);
                }
                String[] from = {"name", "description", "image"};
                int[] to = {R.id.tvName, R.id.tvDiscription, R.id.ivImg};

                SimpleAdapter sAdapter = new SimpleAdapter(ViewListActivity.this, data, R.layout.components,from, to);
                lvMain.setAdapter(sAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        sDateBase.addValueEventListener(vListener);

    }

}