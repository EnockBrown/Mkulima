package com.example.mkulima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkulima.Common.Common;
import com.example.mkulima.Model.Disease;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DiseaseDetail extends AppCompatActivity {

    String diseaseId="";
    String  nm="";
    TextView name,signs,prevention,description;
    ImageView image;
    FirebaseDatabase database;
    DatabaseReference disease;

    Disease currentDisease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        initToolbar();
        nm =getIntent().getStringExtra("name");
        name=findViewById(R.id.disease_name);
        signs=findViewById(R.id.signs);
        prevention=findViewById(R.id.prevention);
        description=findViewById(R.id.disease_description);
        image=findViewById(R.id.imag);

        database=FirebaseDatabase.getInstance();
        disease=database.getReference("Diseases");

        if(getIntent() !=null)

            diseaseId=getIntent().getStringExtra("CategoryId");

        if(!diseaseId.isEmpty())
        {
            if(Common.isConnectedToInternet(getBaseContext()))
            {
                getDetails(diseaseId);
            }

            else
            {
                Toast.makeText(DiseaseDetail.this, "Please Check your internet connection!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    private void getDetails(String diseaseId) {
        disease.child(diseaseId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentDisease=dataSnapshot.getValue(Disease.class);
                //set image
                Picasso.with(getBaseContext()).load(currentDisease.getImageLink())
                        .into(image);

                name.setText(currentDisease.getName());
                description.setText(currentDisease.getDescriptions());
                signs.setText(currentDisease.getSigns());
                prevention.setText(currentDisease.getPrevention_control());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initToolbar() {
        Toolbar toolbar=findViewById(R.id.results);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(nm);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}