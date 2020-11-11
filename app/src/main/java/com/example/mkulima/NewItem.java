package com.example.mkulima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkulima.Common.Common;
import com.example.mkulima.Model.Product;
import com.example.mkulima.Storage.SharedPrefManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class NewItem extends AppCompatActivity {
    TextView selectImage,UploadImage,date;
    ImageView preview_img,date_image;
    Button btn_upload;
    private Button[] btn = new Button[4];
    private Button btn_unfocus;
    private int[] btn_id = {R.id.crops, R.id.livestock, R.id.farminputs, R.id.others};
    Uri saveUri;
    Uri downloadUrl;
    Context context;
    MaterialEditText name, price, description;
    String farmerType = "";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    LinearLayout linearLayout;
    DatePickerDialog picker;

    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(NewItem.this, Signin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        initToolbar();

        //init firebase
        final FirebaseDatabase database =FirebaseDatabase.getInstance();
        final DatabaseReference table_products=database.getReference("Products");

        //getting current date
        long currentTimeMillis = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("MMM MM dd, yyyy h:mm a");
        String dateString = sdf.format(currentTimeMillis);
        date=findViewById(R.id.date);
        date.setText(dateString);

        name=findViewById(R.id.item_name);
        price=findViewById(R.id.item_price);
        description=findViewById(R.id.item_description);
        linearLayout=findViewById(R.id.linear_layout_date);
        date_image=findViewById(R.id.imgdate);
        selectImage=findViewById(R.id.selectImage);
        UploadImage=findViewById(R.id.UploadImage);
        preview_img=findViewById(R.id.imagePreview);
        btn_upload=findViewById(R.id.btnUpload);

        radioButton();
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(NewItem.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        date_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(NewItem.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        UploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage();
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String nm=name.getText().toString().trim();
                final String dsc=description.getText().toString().trim();
                final String prc=price.getText().toString().trim();
                final String dt=date.getText().toString().trim();
                final String url =downloadUrl.toString();

                final String phn =SharedPrefManager.getInstance(NewItem.this).getUser().getPhone();

                if (nm.isEmpty()){
                    name.setError("Name is required or empty");
                    name.requestFocus();
                    return;
                }
                if (dsc.isEmpty()){
                    description.setError("Description is required or empty");
                    description.requestFocus();
                    return;
                }
                if (prc.isEmpty()){
                    price.setError("Priceis Empty or is required");
                    price.requestFocus();
                    return;
                }
                if (dt.isEmpty()){
                    date.setError("Date id Required or is Empty");
                    date.requestFocus();
                    return;
                }
                if (url.isEmpty()){
                    Toast.makeText(context, "No Image Selected", Toast.LENGTH_SHORT).show();
                }

                if (Common.isConnectedToInternet(NewItem.this)){
                    final ProgressDialog mDialog = new ProgressDialog(NewItem.this);
                    mDialog.setMessage("Uploading product...");
                    mDialog.show();

                    table_products.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            mDialog.dismiss();
                            Product product=new Product(nm,url,dsc,prc,"Ongata Rongai",dt,farmerType,phn );
                            table_products.child(nm).setValue(product);
                            //showDialog();
                            Toast.makeText(NewItem.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewItem.this,HomeActivity.class));
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else {
                    Toast.makeText(NewItem.this, "Kindly check your internet connection", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void radioButton() {
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

    private void setFocus(Button btn_unfocus, Button btn_focus){
        btn_unfocus.setTextColor(Color.rgb(49, 50, 51));
        btn_unfocus.setBackgroundColor(Color.rgb(207, 207, 207));
        btn_focus.setTextColor(Color.rgb(255, 255, 255));
        btn_focus.setBackgroundColor(Color.rgb(3, 106, 150));
        this.btn_unfocus = btn_focus;
    }

    private void uploadImage() {
        if(saveUri != null)
        {
            final ProgressDialog mDialog = new ProgressDialog(this);
            mDialog.setMessage("Uploading");
            mDialog.show();
            Common.storage= FirebaseStorage.getInstance();
            Common.storageReffrence=Common.storage.getReference().child("images");
            String imageName = UUID.randomUUID().toString();
            final StorageReference imageFolder = Common.storageReffrence.child("images/"+imageName);
            imageFolder.putFile(saveUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            mDialog.dismiss();
                            Toast.makeText(NewItem.this,"Uploaded!!",Toast.LENGTH_SHORT).show();


                            imageFolder.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    downloadUrl = uri;
                                    String urrrr=downloadUrl.toString();
                                    Toast.makeText(NewItem.this, ""+urrrr, Toast.LENGTH_SHORT).show();
                                    //product.setImage(urrrr);

                                    showImage(downloadUrl);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mDialog.dismiss();
                            Toast.makeText(NewItem.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0* taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mDialog.setMessage("uploaded "+progress+"%");
                        }
                    });

        }
    }

    private void showImage(Uri downloadUrl) {
        if (downloadUrl !=null){
            int width= Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.with(this)
                    .load(downloadUrl)
                    .resize(width,width*2/3)
                    .centerCrop()
                    .into(preview_img);
        }
    }

    private void chooseImage() {
        Intent intent =new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select picture"), Common.PICK_IMAGE_REQUEST);
    }

    private void initToolbar() {
        Toolbar toolbar=findViewById(R.id.results);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("What are you growing/rearing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode==Common.PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data !=null && data.getData() != null)
        {
            saveUri = data.getData();
            //btnSelect.setText("Image selected");
            Toast.makeText(this, "Image Selected", Toast.LENGTH_SHORT).show();
        }
    }

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
}