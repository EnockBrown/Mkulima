package com.example.mkulima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.example.mkulima.ViewHolder.RequestViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BuyerRequest extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference requst;
    RecyclerView recycler_requests;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerOptions<Request> options;
    FirebaseRecyclerAdapter<Request, RequestViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_request);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent I=new Intent(BuyerRequest.this,NewBuyerRequest.class);
                startActivity(I);
            }
        });

        database=FirebaseDatabase.getInstance();
        requst=database.getReference("Requests");

        //Load Menu
        recycler_requests=findViewById(R.id.recycler_requests);
        //recycler_menu.setHasFixedSize(true);
        recycler_requests.setLayoutManager(new GridLayoutManager(this,1));

        if(Common.isConnectedToInternet(getBaseContext()))

            loadRequests();

        else
        {
            Toast.makeText(getBaseContext(), "Please Check your internet connection!!", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //   fab.setCount(new Database(this).getCountCart());}
        loadRequests();
        adapter.startListening();
    }
    @Override
    protected void onStart() {
        Log.e("home","Inside onStart");
        super.onStart();
        loadRequests();
        adapter.startListening();
    }
    private void loadRequests() {
        Log.e("home","Inside loadmenu");

        final ProgressDialog mDialog = new ProgressDialog(BuyerRequest.this);
        mDialog.setMessage("Please Wait...");
        mDialog.show();
        options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requst,Request.class)//select all
                .build();
        mDialog.dismiss();
        Log.e("home","After Options");
        adapter=new FirebaseRecyclerAdapter<Request, RequestViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull RequestViewHolder requestViewHolder, int i, @NonNull final Request request) {
                requestViewHolder.Name.setText(request.getName());
                requestViewHolder.Description.setText(request.getDescription());
                requestViewHolder.Location.setText(request.getLocation());
                requestViewHolder.UserNmae.setText(request.getUserName());
                requestViewHolder.Date.setText(request.getDate_posted());
                requestViewHolder.layout_call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone=request.getPhone();

                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                        startActivity(intent);
                        //Toast.makeText(BuyerRequest.this, ""+phone, Toast.LENGTH_SHORT).show();
                    }
                });
                requestViewHolder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(BuyerRequest.this, "Share activity", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.e("home","Inside Request hoder");
                mDialog.dismiss();
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_request,parent,false);
                return new RequestViewHolder(itemView);
            }
        };
        adapter.notifyDataSetChanged();
        // mDialog.dismiss();
        //adapter=new MyAdapter(MainActivity.this,helper.retrieve());
        recycler_requests.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        //swipeRefreshLayout.setRefreshing(false);
        Log.e("home","last laodmenu");
    }
}