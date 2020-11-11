package com.example.mkulima;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.mkulima.Storage.SharedPrefManager;

public class Profile extends AppCompatActivity {
    TextView Name,Phone,City,Country,Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initToolbar();

        Name=findViewById(R.id.name);
        Phone=findViewById(R.id.phone);
        City=findViewById(R.id.city);
        Country=findViewById(R.id.country);
        Email=findViewById(R.id.email);

        Name.setText(SharedPrefManager.getInstance(Profile.this).getUser().getName());
        Phone.setText(SharedPrefManager.getInstance(Profile.this).getUser().getPhone());
        City.setText(SharedPrefManager.getInstance(Profile.this).getUser().getFarm_loc());
        Country.setText(SharedPrefManager.getInstance(Profile.this).getUser().getCountry());
        Email.setText(SharedPrefManager.getInstance(Profile.this).getUser().getEmail());
    }

    private void initToolbar() {
    }
}