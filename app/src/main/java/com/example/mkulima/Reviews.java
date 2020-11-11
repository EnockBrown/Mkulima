package com.example.mkulima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mkulima.Common.Common;
import com.example.mkulima.Model.Request;
import com.example.mkulima.Model.Review;
import com.example.mkulima.ViewHolder.RequestViewHolder;
import com.example.mkulima.ViewHolder.ReviewsViewHoler;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Reviews extends AppCompatActivity {

    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "Mkulima_Reviews";

    FirebaseRecyclerAdapter<Review, ReviewsViewHoler> adapter;
    FirebaseRecyclerOptions<Review> options;
    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        initToolbar();

        //Firebase
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Mkulima_Reviews");

        //Load Menu
        recyclerView=findViewById(R.id.recycler_reviews);
        //recycler_menu.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,1));

        if(Common.isConnectedToInternet(getBaseContext()))

            loadReiews();

        else
        {
            Toast.makeText(getBaseContext(), "Please Check your internet connection!!", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void loadReiews() {
        Log.e("home","Inside loadmenu");

        final ProgressDialog mDialog = new ProgressDialog(Reviews.this);
        mDialog.setMessage("Please Wait...");
        mDialog.show();
        options = new FirebaseRecyclerOptions.Builder<Review>()
                .setQuery(requests,Review.class)//select all
                .build();
        mDialog.dismiss();
        Log.e("home","After Options");
        adapter=new FirebaseRecyclerAdapter<Review, ReviewsViewHoler>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ReviewsViewHoler reviewViewHolder, int i, @NonNull final Review review) {
                reviewViewHolder.txtproductname.setText(review.getProduct_name());
                reviewViewHolder.txreview.setText(review.getReview());
                reviewViewHolder.txtname.setText(review.getName());
                reviewViewHolder.txtPhone.setText(review.getPhone());

            }

            @NonNull
            @Override
            public ReviewsViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.e("home","Inside Request hoder");
                mDialog.dismiss();
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_reviews,parent,false);
                return new ReviewsViewHoler(itemView);
            }
        };
        adapter.notifyDataSetChanged();
        // mDialog.dismiss();
        //adapter=new MyAdapter(MainActivity.this,helper.retrieve());
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        //swipeRefreshLayout.setRefreshing(false);
        Log.e("home","last laodmenu");

    }
    @Override
    protected void onResume() {
        super.onResume();
        //   fab.setCount(new Database(this).getCountCart());}
        loadReiews();
        adapter.startListening();
    }
    @Override
    protected void onStart() {
        Log.e("home","Inside onStart");
        super.onStart();
        loadReiews();
        adapter.startListening();
    }

    private void initToolbar() {
        Toolbar toolbar=findViewById(R.id.results);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Reviews");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}