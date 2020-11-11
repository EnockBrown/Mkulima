package com.example.mkulima.ViewHolder;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mkulima.fragments.Cattle;
import com.example.mkulima.fragments.Goats;
import com.example.mkulima.fragments.Poultry;

public class DiseasesFragmentAdapter extends FragmentPagerAdapter {
    private Context context;

    public DiseasesFragmentAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position==0)
            return Cattle.getInstance();
        else if (position==1)
            return Goats.getInstance();
        else if (position==2)
            return Poultry.getInstance();
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Cattle";
            case 1:
                return "Goats";
            case 2:
                return "Poultry";
        }
        return "";
    }
}
