package com.savijan.pcbuilder;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.ImageViewHolder>  implements View.OnClickListener{

    private Context mContext;
    private List<Upload> mUploads;
    private FragmentManager fragmentManager;
//    private String category;


    public imageAdapter(Context context, List<Upload> uploads, FragmentManager fm){
        mContext = context;
        mUploads = uploads;
        fragmentManager = fm;
    }

    @NonNull
    @NotNull
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false);

        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageViewHolder holder, int position) {

        Upload currentUpload = mUploads.get(position);

//        category = currentUpload.getCategory();

        holder.name.setText(currentUpload.getName());
        holder.description.setText(currentUpload.getDescription());
        holder.categoryCom.setText(currentUpload.getCategory());

        Picasso.get()
                .load(currentUpload.getImageUrl())
                .into(holder.imageView);

        holder.itemView.setOnClickListener(this::onClick);

    }


    @Override
    public int getItemCount() {
        return mUploads.size();
    }

    @Override
    public void onClick(View v) {

        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        TextView tvDescription = (TextView) v.findViewById(R.id.tvDiscription);
        TextView categoryCom = (TextView) v.findViewById(R.id.categoryCom);
        String name = tvName.getText().toString();
        String description = tvDescription.getText().toString();
        String category = categoryCom.getText().toString();

//        Toast.makeText(v.getContext(), name, Toast.LENGTH_SHORT).show();


        Fragment componentFrag = new ViewComponentFragment(name, description, category);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.fragment_conteiner, componentFrag);
        ft.commit();

    }

    public class ImageViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageView;
        public TextView description;
        public TextView name;
        public TextView categoryCom;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
            description = itemView.findViewById(R.id.tvDiscription);
            name = itemView.findViewById(R.id.tvName);
            categoryCom = itemView.findViewById((R.id.categoryCom));

        }

    }

}
