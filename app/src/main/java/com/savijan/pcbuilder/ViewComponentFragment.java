package com.savijan.pcbuilder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class ViewComponentFragment extends Fragment {

    private String componentName, componentDescription, componentCategory;
    private TextView tvNameCom, tvDesCom, tvCatCom, tvPriceCom;
    private ImageView image;
    private DatabaseReference sDateBase;
//    int img = R.drawable.ic_baseline_flip_camera_ios_24;
    private View v;

    public ViewComponentFragment(String componentName, String componentDescription, String componentCategory) {
        this.componentCategory = componentCategory;
        this.componentName = componentName;
        this.componentDescription = componentDescription;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_view_component, container, false);

        init();

        getDataFromDB();


// Load the image using Glide

        return v;
    }

    private void init() {

        sDateBase = FirebaseDatabase.getInstance().getReference(componentCategory);
        image = (ImageView) v.findViewById(R.id.imageComponent);
        tvCatCom = (TextView) v.findViewById(R.id.categoryComponent);
        tvNameCom = (TextView) v.findViewById(R.id.nameComponent);
        tvDesCom = (TextView) v.findViewById(R.id.descriptionComponent);
        tvPriceCom = (TextView) v.findViewById(R.id.priceComponent);

        tvCatCom.setText(componentCategory);
//        tvNameCom.setText(componentName);
//        tvDesCom.setText(componentDescription);



    }

    private void getDataFromDB(){
        ValueEventListener vListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String comName, comDes, comPrice, comImage;
                comName = snapshot.child(componentName).child("name").getValue().toString();
                comDes = snapshot.child(componentName).child("description").getValue().toString();
                comPrice = snapshot.child(componentName).child("price").getValue().toString();
                comImage = snapshot.child(componentName).child("img").getValue().toString();

                getImageFromFirebase(comImage);


                tvNameCom.setText(comName);
                tvDesCom.setText(comDes);
                tvPriceCom.setText(comPrice);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        sDateBase.addValueEventListener(vListener);

    }

    public void getImageFromFirebase(String imageUri){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("cpu/" + imageUri + ".png");

        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                image.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
