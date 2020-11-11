package com.example.mkulima.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mkulima.Interface.ItemClickListener;
import com.example.mkulima.Model.Product;
import com.example.mkulima.ProductDetail;
import com.example.mkulima.R;
import com.example.mkulima.ViewHolder.MoreViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 */
public class FarmInputs extends Fragment {

    public static FarmInputs INSTANCE=null;
    FirebaseDatabase database;
    Query crops;
    private Context contex;

    FirebaseRecyclerOptions<Product> options;
    FirebaseRecyclerAdapter<Product, MoreViewHolder> adapter;
    RecyclerView recyclerview;

    public FarmInputs() {
        database=FirebaseDatabase.getInstance();
        crops=database.getReference("Products").orderByChild("product_type").equalTo("Farm Inputs");

        options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(crops,Product.class)//select all
                .build();
        adapter=new FirebaseRecyclerAdapter<Product, MoreViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final MoreViewHolder menuViewHolder, int i, @NonNull final Product product) {

                Picasso.with(getActivity())
                        .load(product.getImage())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(menuViewHolder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity())
                                        .load(product.getImage())
                                        .error(R.drawable.ic_baseline_terrain_24)
                                        .into(menuViewHolder.imageView, new Callback() {
                                            @Override
                                            public void onSuccess() {

                                            }

                                            @Override
                                            public void onError() {
                                                Log.e("ERRORLiveWallpaper","Could't fetch image");
                                                Toast.makeText(contex, "Could not fetch image", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
                menuViewHolder.txtMenuName.setText(product.getName());
                menuViewHolder.location.setText(product.getLocation());
                menuViewHolder.description.setText(product.getDescription());
                menuViewHolder.price.setText(product.getPrice());
                menuViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get Category id and sends to new A ctivity
                        // Toast.makeText(HomeActivity.this, "" + clickItem.getId(), Toast.LENGTH_SHORT).show();
                        Intent productdetail=new Intent(getContext(), ProductDetail.class);
                        //ecause categoryid is key, we just get key of the selected item
                        productdetail.putExtra("CategoryId",adapter.getRef(position).getKey());
                        productdetail.putExtra("name",product.getName());
                        productdetail.putExtra("price",product.getPrice());
                        productdetail.putExtra("location",product.getLocation());
                        productdetail.putExtra("description",product.getDescription());
                        productdetail.putExtra("date_posted",product.getDate_posted());
                        productdetail.putExtra("img_url",product.getImage());
                        startActivity(productdetail);
                    }
                });

            }

            @NonNull
            @Override
            public MoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_product,parent,false);
                return new MoreViewHolder(v);
            }
        };
    }

    public static FarmInputs getInstance(){
        if (INSTANCE==null)
            INSTANCE=new FarmInputs();
        return INSTANCE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_farm_inputs, container, false);
        recyclerview=view.findViewById(R.id.recycler_category);
        GridLayoutManager gridLayoutManager= new GridLayoutManager(getActivity(),1);
        recyclerview.setLayoutManager(gridLayoutManager);

        setCropsCategory();

        return view;
    }

    private void setCropsCategory() {
        adapter.startListening();
        recyclerview.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        if(adapter !=null)
            adapter.stopListening();
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter !=null)
            adapter.startListening();
    }

}
