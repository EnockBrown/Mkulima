package com.example.mkulima.ViewHolder;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mkulima.fragments.Crops;
import com.example.mkulima.fragments.FarmInputs;
import com.example.mkulima.fragments.Livestock;
import com.example.mkulima.fragments.Others;

public class MyFragmentAdapter extends FragmentPagerAdapter {
    private Context context;

    public MyFragmentAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0)
            return Crops.getInstance();

        else if (position==1)
            return Livestock.getInstance();

        else if (position==2)
            return FarmInputs.getInstance();

        else if (position==3)
            return Others.getInstance();

        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return "Crops";
            case 1:
                return "Livestock";
            case 2:
                return "Farm Inputs";
            case 3:
                return "Others";
        }
        return "";
    }
}
