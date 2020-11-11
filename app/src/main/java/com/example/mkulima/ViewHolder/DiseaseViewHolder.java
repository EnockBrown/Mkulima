package com.example.mkulima.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkulima.Interface.ItemClickListener;
import com.example.mkulima.R;

public class DiseaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Name,Description,More;
    public ImageView imageView;

    private ItemClickListener itemClickListener;
    public DiseaseViewHolder(@NonNull View itemView) {
        super(itemView);
        Name=itemView.findViewById(R.id.name);
        Description=itemView.findViewById(R.id.description);
        More=itemView.findViewById(R.id.more);
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
