package com.example.mkulima.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkulima.Interface.ItemClickListener;
import com.example.mkulima.R;

public class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtMenuName,price,location;
    public ImageView imageView;

    private ItemClickListener itemClickListener;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);

        txtMenuName=itemView.findViewById(R.id.name);
        price=itemView.findViewById(R.id.price);
        location=itemView.findViewById(R.id.location);
        imageView=itemView.findViewById(R.id.menu_image);

        itemView.setOnClickListener(this);

    }
    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}

