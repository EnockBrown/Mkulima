package com.example.mkulima;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mkulima.Common.Common;
import com.example.mkulima.Model.Product;
import com.example.mkulima.Model.Rating;
import com.example.mkulima.Model.Review;
import com.example.mkulima.Storage.SharedPrefManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.Arrays;

public class ProductDetail extends AppCompatActivity implements RatingDialogListener {
    TextView product_name,product_price,location,description,date_posted,review;
    ImageView product_image;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btn_rating;
    //ElegantNumberButton number_button;
    RatingBar ratingBar;

    String productId="";

    FirebaseDatabase database;
    DatabaseReference products;
    DatabaseReference ratingTbl;

    LinearLayout layout_contact_seller;
    Product currentProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
//        String  productId=getIntent().getStringExtra("CategoryId");
//        Toast.makeText(this, ""+foodId, Toast.LENGTH_SHORT).show();

        layout_contact_seller=findViewById(R.id.layout_contact_seller);
        layout_contact_seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone=currentProduct.getPhone();

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
            }
        });
        //FireBase Databases
        database=FirebaseDatabase.getInstance();
        products=database.getReference("Products");
        ratingTbl=database.getReference("ratingTbl");

        //INIT  VIEW
        btn_rating=findViewById(R.id.btn_rating);
        ratingBar =findViewById(R.id.ratingBar);
        review=findViewById(R.id.reviews);
        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reviewDialog();
            }
        });

        //listeners
        btn_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRatingDialo();
            }

        });

        location=findViewById(R.id.location);
        date_posted=findViewById(R.id.date_posted);
        description=findViewById(R.id.description);
        product_price=findViewById(R.id.price);
        product_name=findViewById(R.id.product_name);
        product_image=findViewById(R.id.product_image);

        collapsingToolbarLayout=findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedAppbar);

        if(getIntent() !=null)

            productId=getIntent().getStringExtra("CategoryId");

        if(!productId.isEmpty())
        {
            if(Common.isConnectedToInternet(getBaseContext()))
            {
                getDetailFood(productId);
                getRatingProduct(productId);
            }

            else
            {
                Toast.makeText(ProductDetail.this, "Please Check your internet connection!!", Toast.LENGTH_SHORT).show();
                return;
            }
        }

//        String  p_name=getIntent().getStringExtra("name");
//        String  p_price=getIntent().getStringExtra("price");
//        String  p_location=getIntent().getStringExtra("location");
//        String  p_description=getIntent().getStringExtra("CategoryId");
//        String  p_date_posted=getIntent().getStringExtra("date_posted");
//        String  p_image=getIntent().getStringExtra("img_url");
    }

    private void reviewDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(ProductDetail.this);
        alertDialog.setTitle("One more step!");
        alertDialog.setTitle("Enter your Review");

        final EditText edtAddress= new EditText(ProductDetail.this);
        LinearLayout.LayoutParams lp= new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAddress.setLayoutParams(lp);
        alertDialog.setView(edtAddress);// Add edit text to alert dialog
        alertDialog.setIcon(R.drawable.ic_baseline_rate_review_24);

        FirebaseDatabase database= FirebaseDatabase.getInstance();
       final DatabaseReference reviews=database.getReference("Mkulima_Reviews");

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                reviews.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                            Review review=new Review(
                                    SharedPrefManager.getInstance(ProductDetail.this).getUser().getPhone(),
                                    edtAddress.getText().toString(),
                                    SharedPrefManager.getInstance(ProductDetail.this).getUser().getName(),
                                    currentProduct.getName()
                            );
                          //  reviews.push().setValue(review);
                            reviews.child(edtAddress.getText().toString()).setValue(review);
//                        reviews.child(SharedPrefManager.getInstance(ProductDetail.this).getUser().getPhone())
//                                .setValue(review);

                            Toast.makeText(ProductDetail.this,"Thank you. review Submited successfully",Toast.LENGTH_SHORT).show();
                            finish();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        alertDialog.show();
    }

    private void getRatingProduct(String productId) {
        Query foodRating = ratingTbl.orderByChild("productId").equalTo(productId);

        foodRating.addValueEventListener(new ValueEventListener() {
            int count = 0,sum=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Rating item = postSnapshot.getValue(Rating.class);
                    sum+=Integer.parseInt(item.getRateValue());
                    count++;;
                }
                if(count !=0)
                {
                    float average = sum/count;
                    ratingBar.setRating(average);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    private void getDetailFood(String productId) {
        products.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentProduct =dataSnapshot.getValue(Product.class);

                //set image
                Picasso.with(getBaseContext()).load(currentProduct.getImage())
                        .into(product_image);

                collapsingToolbarLayout.setTitle(currentProduct.getName());
                product_price.setText(currentProduct.getPrice());
                product_name.setText(currentProduct.getName());
                description.setText(currentProduct.getDescription());
                date_posted.setText(currentProduct.getDate_posted());
                location.setText(currentProduct.getLocation());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRatingDialo() {
        new AppRatingDialog.Builder()
                .setPositiveButtonText("Submit")
                .setNegativeButtonText("Cancel")
                .setNoteDescriptions(Arrays.asList("Ver Bad","Not Good","Quite Ok","Very Good","Excellent"))
                .setDefaultRating(1)
                .setTitle("Rate This Commodity")
                .setDescription("Please Select some stars and give us some foodback")
                .setTitleTextColor(R.color.colorPrimary)
                .setDescriptionTextColor(R.color.colorPrimary)
                .setHint("Please write your comment")
                .setHintTextColor(R.color.colorAccent)
                .setCommentTextColor(R.color.white)
                .setCommentBackgroundColor(R.color.colorPrimaryDark)
                .setWindowAnimation(R.style.RatingaDialogFadeAnim)
                .create(ProductDetail.this)
                .show();;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()){

            AlertDialog.Builder dialog=new AlertDialog.Builder(ProductDetail.this);
            dialog.setCancelable(false);
            dialog.setTitle("You need to login before viewing this product");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent =new Intent(ProductDetail.this,Signin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            });
            final AlertDialog alert = dialog.create();
            alert.show();


        }
    }

    @Override
    public void onNegativeButtonClicked() {

    }

    @Override
    public void onPositiveButtonClicked(int value, String comments) {
        //Get Rating and upload to firebase database
        final String phone=SharedPrefManager.getInstance(ProductDetail.this).getUser().getPhone();
        final String Name=SharedPrefManager.getInstance(ProductDetail.this).getUser().getName();
        final Rating rating = new Rating(phone,
                productId,
                String.valueOf(value),
                comments);

        ratingTbl.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(phone).exists())
                {
                    //remove old data ---i can delete or let it be useless functions---
                    ratingTbl.child(phone).removeValue();
                    //update new value
                    ratingTbl.child(phone).setValue(rating);
                }
                else
                {
                    //update new value
                    ratingTbl.child(phone).setValue(rating);
                }
                Toast.makeText(ProductDetail.this, Name+" Thanks for Your Feedback", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onNeutralButtonClicked() {

    }
}