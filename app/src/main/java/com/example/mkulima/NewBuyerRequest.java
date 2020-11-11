package com.example.mkulima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkulima.Common.Common;
import com.example.mkulima.Model.Request;
import com.example.mkulima.Storage.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rilixtech.widget.countrycodepicker.Country;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NewBuyerRequest extends AppCompatActivity {

    private Button[] btn = new Button[4];
    private Button btn_unfocus,btnupdate;
    private int[] btn_id = {R.id.crops, R.id.livestock, R.id.farminputs, R.id.others};
    Button btn1;
    DatePickerDialog picker;
    TextView date;
    LinearLayout layout;
    MaterialEditText item_name,description_detail,delivery_location;
    String farmerType="";
    String country="";
    CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_buyer_request);

        //getting current date
        long currentTimeMillis = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        String dateString = sdf.format(currentTimeMillis);
        date=findViewById(R.id.date);
        date.setText(dateString);
        item_name=findViewById(R.id.item_name);
        description_detail=findViewById(R.id.description_detail);
        delivery_location=findViewById(R.id.delivery_location);

        btn1=findViewById(R.id.btnpostorder);
        layout=findViewById(R.id.layout_date);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(NewBuyerRequest.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        ccp=findViewById(R.id.countrycode);
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                country=ccp.getSelectedCountryName();
            }
        });
        //init firebase
        final FirebaseDatabase database =FirebaseDatabase.getInstance();
        final DatabaseReference table_requests=database.getReference("Requests");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String itemName=item_name.getText().toString().trim();
                final String itemDescription=description_detail.getText().toString().trim();
                final String DeliveryLocation=country+", "+delivery_location.getText().toString().trim();
                final String itemDate=date.getText().toString().trim();
                final String phone= String.valueOf(SharedPrefManager.getInstance(NewBuyerRequest.this).getUser().getPhone());
                final String usrname=SharedPrefManager.getInstance(NewBuyerRequest.this).getUser().getName();
                //txtFullName.setText(SharedPrefManager.getInstance(HomeActivity.this).getUser().getName());
                //Toast.makeText(NewBuyerRequest.this, ""+farmerType, Toast.LENGTH_SHORT).show();

                if (itemName.isEmpty()){
                    item_name.setError("Product name is Required");
                    item_name.requestFocus();
                    return;
                }

                if (itemDescription.isEmpty()){
                    description_detail.setError("Product Description is Required");
                    description_detail.requestFocus();
                    return;
                }
                if (DeliveryLocation.isEmpty()){
                    delivery_location.setError("Delivery location is Required or Empty");
                    delivery_location.requestFocus();
                    return;
                }

                if (Common.isConnectedToInternet(NewBuyerRequest.this)){
                    final ProgressDialog mDialog = new ProgressDialog(NewBuyerRequest.this);
                    mDialog.setMessage("Posting Item...");
                    mDialog.show();

                    table_requests.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mDialog.dismiss();
                            Request request=new Request(itemDate,itemDescription,phone,DeliveryLocation,itemName,farmerType,usrname);
                            table_requests.child(itemName).setValue(request);
                            Toast.makeText(NewBuyerRequest.this, "Request Updated Sucessfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(NewBuyerRequest.this, "Check your internet connections", Toast.LENGTH_SHORT).show();
                }
            }
        });

        initToolbar();

        //init buttons
        for(int i = 0; i < btn.length; i++){
            btn[i] =  findViewById(btn_id[i]);
            btn[i].setBackgroundColor(Color.rgb(207, 207, 207));
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //boolean checked = ((Button) v).isChecked();
                    boolean checked=((Button) v).isClickable();
                    String crops="";
                    String livestock="";
                    String FarmInputs="";
                    String Others="";

                    switch (v.getId()){
                        case R.id.crops:
                            setFocus(btn_unfocus, btn[0]);
                            if (checked)
                                crops="Crops";
                            break;
                        case R.id.livestock:
                            setFocus(btn_unfocus,btn[1]);
                            if (checked)
                                livestock="Livestock";
                            break;
                        case R.id.farminputs:
                            setFocus(btn_unfocus,btn[2]);
                            if (checked)
                                FarmInputs="Farm Inputs";
                            break;
                        case R.id.others:
                            setFocus(btn_unfocus,btn[3]);
                            if (checked)
                                Others="Others";
                            break;
                    }

                    farmerType=crops+livestock+FarmInputs+Others;
                }
            });
        }
        btn_unfocus = btn[0];
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

    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus = btn_focus;
    }

    private void initToolbar() {
        Toolbar toolbar=findViewById(R.id.results);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("New Buyer Request");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
