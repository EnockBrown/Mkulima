package com.example.mkulima.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkulima.Interface.ItemClickListener;
import com.example.mkulima.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

    public TextView product_name,product_price,product_location;
    public ImageView product_image;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);
        product_name=itemView.findViewById(R.id.product_name);
        product_image=itemView.findViewById(R.id.product_image);
        product_price=itemView.findViewById(R.id.product_price);
        product_location=itemView.findViewById(R.id.product_location);


        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v,getAdapterPosition(),false);
    }
}