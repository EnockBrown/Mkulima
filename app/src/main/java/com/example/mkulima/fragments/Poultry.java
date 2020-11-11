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

import com.example.mkulima.DiseaseDetail;
import com.example.mkulima.Interface.ItemClickListener;
import com.example.mkulima.Model.Disease;
import com.example.mkulima.R;
import com.example.mkulima.ViewHolder.DiseaseViewHolder;
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
public class Poultry extends Fragment {

public static Poultry INSTANCE=null;
    FirebaseDatabase database;
    Query cattle;
    private Context contex;

    FirebaseRecyclerOptions<Disease> options;
    FirebaseRecyclerAdapter<Disease, DiseaseViewHolder> adapter;
    RecyclerView recyclerview;
    public Poultry() {
        // Required empty public constructor
        database= FirebaseDatabase.getInstance();
        cattle=database.getReference("Diseases").orderByChild("type").equalTo("POULTRY");

        options = new FirebaseRecyclerOptions.Builder<Disease>()
                .setQuery(cattle,Disease.class)//select all
                .build();
        adapter=new FirebaseRecyclerAdapter<Disease, DiseaseViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final DiseaseViewHolder diseaseViewHolder, int i, @NonNull final Disease disease) {
                Picasso.with(getActivity())
                        .load(disease.getImageLink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(diseaseViewHolder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(getActivity())
                                        .load(disease.getImageLink())
                                        .error(R.drawable.ic_baseline_terrain_24)
                                        .into(diseaseViewHolder.imageView, new Callback() {
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
                diseaseViewHolder.Name.setText(disease.getName());
                diseaseViewHolder.Description.setText(disease.getDescriptions());
                diseaseViewHolder.More.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), ""+disease.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                diseaseViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent i=new Intent(getContext(), DiseaseDetail.class);
                        i.putExtra("CategoryId",adapter.getRef(position).getKey());
                        i.putExtra("name",disease.getName());
                        i.putExtra("description",disease.getDescriptions());
                        i.putExtra("signs",disease.getSigns());
                        i.putExtra("prevention",disease.getPrevention_control());
                        i.putExtra("img_url",disease.getImageLink());
                        startActivity(i);
                        //Toast.makeText(getContext(), ""+disease.getName(), Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @NonNull
            @Override
            public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v =LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_diseases,parent,false);
                return new DiseaseViewHolder(v);
            }
        };
    }

    public static Poultry getInstance(){
        if (INSTANCE==null)
            INSTANCE=new Poultry();
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
        View view= inflater.inflate(R.layout.fragment_poultry, container, false);
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
