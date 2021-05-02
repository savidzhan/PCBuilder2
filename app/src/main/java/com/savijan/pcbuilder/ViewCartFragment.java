package com.savijan.pcbuilder;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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

public class ViewCartFragment extends Fragment{

    private DatabaseReference sDateBase;
    private FirebaseStorage storage;
    private View v;
    private TextView tvCartOut;
//    private ImageButton updateCart;
    private Button btnOrder;
    private LinearLayout llTextOut;

    private RecyclerView mRecyclerView;
    private imageAdapterCart mAdapter;
    private List<Upload> mUploads;
//    private FragmentManager fragmentManager;

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

//        updateCart = (ImageButton) v.findViewById(R.id.btnUpdateCartView);
//        updateCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new ViewCartFragment()).commit();
//            }
//        });

        llTextOut = (LinearLayout) v.findViewById(R.id.llTextOut);
        ViewGroup.LayoutParams params = llTextOut.getLayoutParams();
// Changes the height and width to the specified *pixels*
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;

        tvCartOut = (TextView) v.findViewById(R.id.tvCartOut);
        btnOrder = (Button) v.findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_conteiner, new RegisterFragment()).commit();
            }
        });

        Paper.init(getContext());
//        Paper.book().destroy();
        List<String> components = Paper.book().getAllKeys();

        if(components.isEmpty()){
            tvCartOut.setText("В корзине пусто!");
            btnOrder.setVisibility(View.GONE);
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            llTextOut.setLayoutParams(params);
        }

//        if(mAdapter.getItemCount() == 0){
//            tvCartOut.setText("В корзине пусто!");
//        }


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

}
