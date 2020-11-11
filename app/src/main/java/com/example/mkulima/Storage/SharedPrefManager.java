package com.example.mkulima.Storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.mkulima.Model.User;

public class SharedPrefManager {
    private static SharedPrefManager mInstance;
    private Context context;
    private static final String SHARED_PREF_NAME="my_shared_pref_name";

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if (mInstance==null){
            mInstance=new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void saveUser(User user){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("name",user.getName());
        editor.putString("email",user.getEmail());
        editor.putString("gender",user.getGender());
        editor.putString("agegroup",user.getAge_group());
        editor.putString("farmer_type",user.getFarm_run_loc());
        editor.putString("phone",user.getPhone());
        editor.putInt("id",user.getId());
        editor.putString("country",user.getCountry());
        editor.putString("tele_farmer",user.getTele_farmer());
        editor.putString("farm_location",user.getFarm_loc());
        editor.putString("farm_run_location",user.getFarm_run_loc());

        editor.apply();
    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt("id",-1)!=-1;
    }

    public  User getUser(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString("name",null),
                sharedPreferences.getString("email",null),
                sharedPreferences.getString("password",null),
                // sharedPreferences.getInt("phone_number",-1),
                sharedPreferences.getString("farmer_type",null),
                sharedPreferences.getString("country",null),
                sharedPreferences.getString("gender",null),
                sharedPreferences.getString("age_group",null),
                sharedPreferences.getString("tele_farmer",null),
                sharedPreferences.getString("farm_run_loc",null),
                sharedPreferences.getString("farm_loc",null),
                sharedPreferences.getString("phone",null),
                sharedPreferences.getInt("id",-1)
        );
    }

    public void clear(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
