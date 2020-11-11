package com.example.mkulima;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hbb20.CountryCodePicker;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Signup extends AppCompatActivity {

    Button btnsignup;
    TextView txt;
    MaterialEditText name,email,phone,password;

    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        name=findViewById(R.id.edtname);
        email=findViewById(R.id.edtemail);
        phone=findViewById(R.id.edtphone);
        password=findViewById(R.id.edtpassword);
        // ccp = (CountryCodePicker) findViewById(R.id.country_code);


        //Toast.makeText(this, "your phone Number is "+userphone, Toast.LENGTH_SHORT).show();

        //  Toast.makeText(this, ""+username, Toast.LENGTH_SHORT).show();
        btnsignup=findViewById(R.id.btnsignup);
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showDialog();


                //Toast.makeText(Signup.this, "Your phone number is"+userphone, Toast.LENGTH_SHORT).show();
            }

            private void showDialog() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Signup.this);
                dialog.setCancelable(false);
                dialog.setTitle("Welcome "+name.getText().toString().trim());
                dialog.setMessage("You are Now part of " +
                        "Mkulima Hodari Community");
                dialog.setPositiveButton("OK" , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //Action for "OK".
                        final String username=name.getText().toString().trim();
                        String useremail=email.getText().toString().trim();
                        String userpassword=password.getText().toString().trim();
                        String phn = phone.getText().toString().trim();
//                        String ccd=ccp.getSelectedCountryCodeWithPlus();
//                        final String userphone=ccd+"-"+phn;

                        Intent i =new Intent(Signup.this,UpdateProfile.class);
                        i.putExtra("name",username);
                        i.putExtra("email",useremail);
                        i.putExtra("password",userpassword);
                        i.putExtra("phone",phn);
                        startActivity(i);
                    }
                });

                final AlertDialog alert = dialog.create();
                alert.show();
            }
        });
    }
}