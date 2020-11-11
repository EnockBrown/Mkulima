package com.example.mkulima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkulima.Common.Common;
import com.example.mkulima.Model.User;
import com.example.mkulima.Storage.SharedPrefManager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class Signin extends AppCompatActivity {

    MaterialEditText phone,password;
    ImageView image;
    Button btn;
    TextView txt1,txt2;

    FirebaseDatabase database;
    DatabaseReference table_user;

    @Override
    protected void onStart() {
        super.onStart();
        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent i = new Intent(Signin.this, NewItem.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        phone=findViewById(R.id.phone);
        password=findViewById(R.id.password);
        image=findViewById(R.id.show_pass);
        btn=findViewById(R.id.btnsignin);
        txt1=findViewById(R.id.forgot_password);
        txt2=findViewById(R.id.login_using_phone);


        //INIT FIREBASE DATABSE;
        database=FirebaseDatabase.getInstance();
        table_user=database.getReference("User");


        txt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usrphone= phone.getText().toString().trim();
                final String usrpass=password.getText().toString().trim();


                if (usrphone.isEmpty()){
                    phone.setError("Phone is Required");
                    phone.requestFocus();
                    return;
                }
                if (usrphone.length()>10){
                    password.setError("Invalid phone number");
                    password.requestFocus();
                    return;
                }

                if (usrpass.isEmpty()){
                    password.setError("Password is Required");
                    password.requestFocus();
                    return;
                }
                if (usrpass.length()<6){
                    password.setError("Password should be atleast 6 characters long");
                    password.requestFocus();
                    return;
                }

                if (Common.isConnectedToInternet(getBaseContext())){

                    final ProgressDialog mDialog = new ProgressDialog(Signin.this);
                    mDialog.setMessage("Please Wait...");
                    mDialog.show();

                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.child(usrphone).exists()){
                                //GET USER INFORMATION
                                mDialog.dismiss();
                                User user = dataSnapshot.child(usrphone).getValue(User.class);
                                user.setPhone(usrphone);

                                if (user.getPassword().equals(usrpass)){

                                    SharedPrefManager.getInstance(Signin.this)
                                            .saveUser(user);

                                    Intent homeIntent = new Intent(Signin.this, HomeActivity.class);
                                    // Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();

                                    table_user.removeEventListener(this);
                                }
                                else{
                                    Toast.makeText(Signin.this, "Wron Phone number or password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                mDialog.dismiss();
                                Toast.makeText(Signin.this, "User doesnt exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else{
                    Toast.makeText(Signin.this, "Please Check Your Internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showdialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(Signin.this);
        dialog.setCancelable(false);
        LayoutInflater inflater = LayoutInflater.from(this);
        View forgot_pwd=inflater.inflate(R.layout.layout_forgot_pwd,null);
        dialog.setView(forgot_pwd);
        dialog.setPositiveButton("RESET" , new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        final AlertDialog alert = dialog.create();
        alert.show();
    }
}