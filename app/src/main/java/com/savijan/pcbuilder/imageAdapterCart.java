package com.savijan.pcbuilder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.paperdb.Paper;

public class imageAdapterCart extends RecyclerView.Adapter<imageAdapterCart.ImageViewHolder>  implements View.OnClickListener{

    private Context mContext;
    private List<Upload> mUploads;
    private FragmentManager fragmentManager;
//    private String category;


    public imageAdapterCart(Context context, List<Upload> uploads, FragmentManager fm){
        mContext = context;
        mUploads = uploads;
        fragmentManager = fm;
    }

    @NonNull
    @NotNull
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_image_cart, parent, false);

        Paper.init(mContext);

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

        String nameToDelete = holder.name.getText().toString();

        holder.deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Paper.book().delete(nameToDelete);

                mUploads.remove(position);

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mUploads.size()+1);

                holder.itemView.setVisibility(View.GONE);

            }
        });

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
        public Button deleteItem;

        public ImageViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view_upload);
            description = itemView.findViewById(R.id.tvDiscription);
            name = itemView.findViewById(R.id.tvName);
            categoryCom = itemView.findViewById((R.id.categoryCom));
            deleteItem = itemView.findViewById((R.id.deleteItem));

        }

    }

}
