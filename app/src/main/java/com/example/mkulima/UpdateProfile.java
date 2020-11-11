package com.example.mkulima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkulima.Common.Common;
import com.example.mkulima.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

public class UpdateProfile extends AppCompatActivity {

    TextView username;
    //CountryCodePicker ccp;
    CountryCodePicker ccp;
    CheckBox buyer,seller,both;
    RadioButton male,female,yes,no, ckb1,ckb2,ckb3,ckb4;
    Button btnupdate;
    RadioGroup radio_gende,radio_tele,radio_age;
    MaterialEditText farm_loc,farm_run_loc;

    String Farmer_type="";
    String Telefarmer="";
    String ageGroup="";
    String gender="";
    String country="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        final String  usrname=getIntent().getStringExtra("name");
        final String  usremail=getIntent().getStringExtra("email");
        final String  usrpassword=getIntent().getStringExtra("password");
        final String usrphone=  getIntent().getStringExtra("phone");
        final int id= Integer.parseInt(getIntent().getStringExtra("phone"));
        //final String  usrphone=getIntent().getStringExtra("phone");

        farm_loc=findViewById(R.id.farm_locatiom);
        farm_run_loc=findViewById(R.id.farm_running_place);
        buyer=findViewById(R.id.ckb_buyer);
        seller=findViewById(R.id.ckb_seller);
        both=findViewById(R.id.ckb_both);
        male=findViewById(R.id.ckb_male);
        female=findViewById(R.id.ckb_female);
        yes=findViewById(R.id.tele_yes);
        no=findViewById(R.id.tele_no);
        ckb1=findViewById(R.id.age12_19);
        ckb2=findViewById(R.id.age20_34);
        ckb3=findViewById(R.id.age35_49);
        ckb4=findViewById(R.id.age50);
        radio_tele=findViewById(R.id.radio_group_tele);
        radio_age=findViewById(R.id.radio_age_group);
        radio_gende=findViewById(R.id.radio_group_gender);
        btnupdate=findViewById(R.id.btnupdate);

        ccp =  findViewById(R.id.countrycode);
        username=findViewById(R.id.username);
        username.setText("Welcome "+usrname+",");


        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                country=ccp.getSelectedCountryName();
            }
        });
        // final String country =ccp.getSelectedCountryName();
        // Toast.makeText(this, "Your Country is  "+country, Toast.LENGTH_SHORT).show();

        //init Firebae
        final FirebaseDatabase database =FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(UpdateProfile.this);
                    mDialog.setMessage("Loading...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(String.valueOf(usrphone)).exists()){
                                mDialog.dismiss();
                                Toast.makeText(UpdateProfile.this, "Phone Number is already registered", Toast.LENGTH_SHORT).show();
                            }else{
                                mDialog.dismiss();
                                User user = new User(usrname,usremail, usrpassword,Farmer_type,country,
                                        gender,ageGroup ,Telefarmer,farm_run_loc.getText().toString(),farm_loc.getText().toString(),usrphone,id);
                                table_user.child(String.valueOf(usrphone)).setValue(user);
                                showDialog();
                                Toast.makeText(UpdateProfile.this, "updated up successfully", Toast.LENGTH_SHORT).show();
                                Intent i=new Intent(UpdateProfile.this,Signin.class);
                                startActivity(i);
                                // finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(UpdateProfile.this, "Please Check your internet connection", Toast.LENGTH_SHORT).show();
                }



            }
        });




    }

    private void showDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateProfile.this);
        dialog.setCancelable(false);
        dialog.setTitle("Congratulations!");
        dialog.setMessage("We are lucky to have you! " +
                "We do verify sellers/buers and some items in the market place. " +
                "You can always talk to us via " +
                "vinmutriz@gmail.com");
        dialog.setPositiveButton("OK" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String m="";
        String f="";
        switch (view.getId()){
            case R.id.ckb_male:
                if (checked) {
                    m = "Male";
                    //  Toast.makeText(this, "you have selected " + m, Toast.LENGTH_SHORT).show( );
                }
                break;
            case R.id.ckb_female:
                if (checked){
                    f = "Female";
                    //  Toast.makeText(this, "you have selected " + f, Toast.LENGTH_SHORT).show( );
                }

        }

        gender =m+f;
    }

    public void onClickAgeGroup(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String age12 = "";
        String age20="";
        String age35="";
        String age50="";
        switch (view.getId()){
            case R.id.age12_19:
                if (checked){
                    age12 ="12-19 years";
                    //   Toast.makeText(this, ""+age12, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.age20_34:
                if (checked){
                    age20="20-34 Years";
                    //  Toast.makeText(this, ""+age20, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.age35_49:
                if (checked){
                    age35 ="35-49 Years ";
                    // Toast.makeText(this, ""+age35, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.age50:
                if (checked){
                    age50="50 yeas and above";
                    //Toast.makeText(this, ""+age50, Toast.LENGTH_SHORT).show();
                }
        }
        ageGroup= age12+age20+age35+age50;
    }

    public void onClickTelephoneFarmers(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        String teleYes="";
        String teleNo="";
        switch (view.getId()){
            case R.id.tele_yes:
                if (checked){
                    teleYes="Yes";
                    // Toast.makeText(this, ""+teleYes, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tele_no:
                if (checked){
                    teleNo="No";
                    // Toast.makeText(this, ""+teleNo, Toast.LENGTH_SHORT).show();
                }
        }

        Telefarmer=teleNo+teleYes;
    }

    public void onClickFarmerType(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        String buyer="";
        String seller="";
        String both="";
        switch (view.getId()){
            case R.id.ckb_buyer:
                if (checked) {
                    buyer= "Buyer";
                    //  Toast.makeText(this, "you have selected " + buyer, Toast.LENGTH_SHORT).show( );
                }
                break;
            case R.id.ckb_seller:
                if (checked){
                    seller = "Seller";
                    // Toast.makeText(this, "you have selected " + seller, Toast.LENGTH_SHORT).show( );
                }
            case R.id.ckb_both:
                if (checked){
                    both="Both";
                    //  Toast.makeText(this, ""+both, Toast.LENGTH_SHORT).show();
                }

        }

        Farmer_type =buyer+" "+seller+" "+both;
    }
}