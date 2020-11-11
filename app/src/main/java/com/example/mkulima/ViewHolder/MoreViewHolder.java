package com.example.mkulima.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkulima.Interface.ItemClickListener;
import com.example.mkulima.R;

public class MoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuName,price,location,description;
    public ImageView imageView;
    private ItemClickListener itemClickListener;

    public MoreViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMenuName=itemView.findViewById(R.id.name);
        description=itemView.findViewById(R.id.description);
        price=itemView.findViewById(R.id.price);
        location=itemView.findViewById(R.id.location);
        imageView=itemView.findViewById(R.id.image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}
