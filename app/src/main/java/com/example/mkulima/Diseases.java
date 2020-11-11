package com.example.mkulima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.mkulima.ViewHolder.DiseasesFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class Diseases extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diseases);
        initToolbar();

        viewPager=findViewById(R.id.viewpager);
        DiseasesFragmentAdapter adapter = new DiseasesFragmentAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(adapter);

        tabLayout=findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void initToolbar() {
        Toolbar toolbar=findViewById(R.id.results);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Good Practices");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}