package com.example.mkulima.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkulima.R;

public class RequestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView Name,Description,Location,UserNmae,Date;
    public LinearLayout share,layout_call;


    public RequestViewHolder(@NonNull View itemView) {
        super(itemView);

        Name=itemView.findViewById(R.id.name);
        Description=itemView.findViewById(R.id.description);
        Location=itemView.findViewById(R.id.location);
        UserNmae=itemView.findViewById(R.id.call);
        Date=itemView.findViewById(R.id.date);
        layout_call=itemView.findViewById(R.id.layout_call);
        share=itemView.findViewById(R.id.layout_share);
    }

    @Override
    public void onClick(View v) {

    }
}
