package com.example.mkulima.ViewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mkulima.Interface.ItemClickListener;
import com.example.mkulima.R;

public class ReviewsViewHoler extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtPhone, txreview, txtname,txtproductname;

    private ItemClickListener itemClickListener;
    public ReviewsViewHoler(@NonNull View itemView) {
        super(itemView);
        txtPhone=itemView.findViewById(R.id.phone);
        txtname=itemView.findViewById(R.id.name);
        txreview=itemView.findViewById(R.id.review);
        txtproductname=itemView.findViewById(R.id.p_name);
    }

    @Override
    public void onClick(View view) {

    }
}
