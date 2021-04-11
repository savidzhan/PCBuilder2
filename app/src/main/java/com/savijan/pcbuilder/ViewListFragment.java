package com.savijan.pcbuilder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class ViewListFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private String categoryName;
    private ListView lvMain;
    private TextView tvOut;
    private ImageView image;
    private ListView lvComponents;
    ArrayList<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
    private Map<String, Object> m;
    private SimpleAdapter sAdapter;
    private DatabaseReference sDateBase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private View v;
    private int imageCom;

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
        lvMain.setOnItemClickListener(this::onItemSelected);

        sDateBase = FirebaseDatabase.getInstance().getReference(categoryName);
        tvOut.setText("Вы выбрали категорию: " + categoryName);

        storage = FirebaseStorage.getInstance();
    }

    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(data.size()>0){data.clear(); }
                String comImage;

                for(DataSnapshot ds : snapshot.getChildren()){
//                    storageReference = storage.getReferenceFromUrl("gs://pcbuilder-savijan.appspot.com/cpu").child( ds.child("img").getValue()+".png");
                    m = new HashMap<String, Object>();
                    m.put("category", categoryName);
                    m.put("name", ds.child("name").getValue());
                    m.put("description", ds.child("description").getValue());
                    comImage = ds.child("img").getValue().toString();
//                    m.put("image", imageCom);

//                    getImageFromFirebase(comImage, image);
                    assert m != null;
                    data.add(m);

                }
                String[] from = {"name", "description", "category"};
                int[] to = {R.id.tvName, R.id.tvDiscription, R.id.categoryCom};

                sAdapter = new SimpleAdapter(getContext(), data, R.layout.components,from, to);
                lvMain.setAdapter(sAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        sDateBase.addValueEventListener(vListener);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) view.findViewById(R.id.tvDiscription);
        String name = tvName.getText().toString();
        String description = tvDescription.getText().toString();


        Fragment componentFrag = new ViewComponentFragment(name, description, categoryName);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_conteiner, componentFrag);
        ft.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getImageFromFirebase(String imageUri, ImageView img){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("cpu/" + imageUri + ".png");

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                img.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
