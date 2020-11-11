package com.example.mkulima;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.mkulima.Common.Common;
import com.example.mkulima.Interface.ItemClickListener;
import com.example.mkulima.Interface.OnFirebaseDataChanged;
import com.example.mkulima.Model.Product;
import com.example.mkulima.Storage.SharedPrefManager;
import com.example.mkulima.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnFirebaseDataChanged {


    FirebaseDatabase database;
    DatabaseReference product;

    TextView txtFullName,email;
    LinearLayout buyer,seller,more_items;

    RecyclerView recycler_menu;
    FirebaseRecyclerOptions<Product> options;
    FirebaseRecyclerAdapter<Product, MenuViewHolder> adapter;
    FirebaseRecyclerAdapter<Product,MenuViewHolder> searchadapter;
    FirebaseRecyclerOptions<Product> search_options;
    MaterialSearchBar materialSearchBar;
    List<String> suggestList = new ArrayList<>();


    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //init layout managers
        more_items=findViewById(R.id.moreItems);
        more_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,MoreActivity.class));
            }
        });
        buyer= findViewById(R.id.layout_sell);
        seller=  findViewById(R.id.layout_buyer_request);
        buyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,NewItem.class));
            }
        });
        seller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,BuyerRequest.class));
            }
        });
        //INIT FireBase
        database=FirebaseDatabase.getInstance();
        product=database.getReference("Products");

        Paper.init(this);

        //Load Menu
        recycler_menu=findViewById(R.id.recycler_product);
        //recycler_menu.setHasFixedSize(true);
        recycler_menu.setLayoutManager(new GridLayoutManager(this,2));
        if(Common.isConnectedToInternet(getBaseContext()))

            loadMenu();

        else
        {
            Toast.makeText(getBaseContext(), "Please Check your internet connection!!", Toast.LENGTH_SHORT).show();
            return;
        }

        //searchbar
        materialSearchBar = (MaterialSearchBar)findViewById(R.id.searchBar);
        materialSearchBar.setTextColor(R.color.black);
        materialSearchBar.setHint("Enter Your Product");
        materialSearchBar.setSpeechMode(true);
        loadSuggest();//to load suggested list from the firebase
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<String>();
                for(String search:suggestList)
                {
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When search bar is closed
                //Returns original suggest adapter
                if(!enabled)
                    recycler_menu.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                //when search finish
                //show result of search adapter
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });


        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Button lg=findViewById(R.id.login);
        Button register=findViewById(R.id.signup);
        if (SharedPrefManager.getInstance(HomeActivity.this).isLoggedIn()){
            lg.setVisibility(View.GONE);
        }else{
            lg.setVisibility(View.VISIBLE);
        }
        if (SharedPrefManager.getInstance(HomeActivity.this).isLoggedIn()){
            register.setVisibility(View.GONE);
        }else{
            register.setVisibility(View.VISIBLE);
        }
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(HomeActivity.this, "This is Login Button", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(HomeActivity.this,Signin.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(HomeActivity.this,Signup.class);
                startActivity(intent);
                //Toast.makeText(HomeActivity.this, "This is Register Button", Toast.LENGTH_SHORT).show();
            }
        });


        //SET Name for user
        View headerView=navigationView.getHeaderView(0);
        txtFullName=headerView.findViewById(R.id.name);
        txtFullName.setText(SharedPrefManager.getInstance(HomeActivity.this).getUser().getName());
        email=headerView.findViewById(R.id.email);
        email.setText(SharedPrefManager.getInstance(this).getUser().getEmail());
        ImageView logout=headerView.findViewById(R.id.logout);
        if (SharedPrefManager.getInstance(HomeActivity.this).isLoggedIn()){
            logout.setVisibility(View.VISIBLE);
        }else{
            logout.setVisibility(View.GONE);
        }
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    Toast.makeText(HomeActivity.this, "Loged out successfully", Toast.LENGTH_SHORT).show();
                SharedPrefManager.getInstance(HomeActivity.this).clear();
                startActivity(new Intent(HomeActivity.this,HomeActivity.class));
            }
        });

        // you can even change only one from above to and keep the other one normally
//        navigationView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
//        headerView.getBackground().setColorFilter(0x80000000, PorterDuff.Mode.MULTIPLY);
    }

    private void startSearch(CharSequence text) {
        search_options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(product.orderByChild("name").equalTo(text.toString()),Product.class)//select all
                //. foodList.orderByChild("name").equalTo(text.toString())//compare names
                .build();
        searchadapter=new FirebaseRecyclerAdapter<Product, MenuViewHolder>(search_options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder viewHolder, int i, @NonNull final Product product) {
                Log.e("home","Inside onBind");
                //  mDialog.dismiss();
                viewHolder.txtMenuName.setText(product.getName());
                viewHolder.price.setText(product.getPrice());
                viewHolder.location.setText(product.getLocation());
                Picasso.with(getBaseContext()).load(product.getImage())
                        .into(viewHolder.imageView);
                //  endListener();
                final Product clickItem =product;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get Category id and sends to new A ctivity
                        // Toast.makeText(HomeActivity.this, "" + clickItem.getId(), Toast.LENGTH_SHORT).show();
                        Intent productdetail=new Intent(HomeActivity.this,ProductDetail.class);
                        //ecause categoryid is key, we just get key of the selected item
                        productdetail.putExtra("CategoryId",adapter.getRef(position).getKey());
                        productdetail.putExtra("name",product.getName());
                        productdetail.putExtra("price",product.getPrice());
                        productdetail.putExtra("location",product.getLocation());
                        productdetail.putExtra("description",product.getDescription());
                        productdetail.putExtra("date_posted",product.getDate_posted());
                        productdetail.putExtra("img_url",product.getImage());
                        startActivity(productdetail);
                    }
                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                mDialog.dismiss();
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item,parent,false);
                return new MenuViewHolder(itemView);
            }
        };
        searchadapter.notifyDataSetChanged();
        // mDialog.dismiss();
        //adapter=new MyAdapter(MainActivity.this,helper.retrieve());
        recycler_menu.setAdapter(searchadapter);
        searchadapter.startListening();
        searchadapter.notifyDataSetChanged();
    }

    private void loadSuggest() {
        product.orderByChild("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                        {
                            Product item =postSnapshot.getValue(Product.class);
                            suggestList.add(item.getName());//add name of product to suggest list
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    //
    @Override
    protected void onResume() {
        super.onResume();
        //   fab.setCount(new Database(this).getCountCart());}
        loadMenu();
        adapter.startListening();
    }
    @Override
    protected void onStart() {
        Log.e("home","Inside onStart");
        super.onStart();
        loadMenu();
        adapter.startListening();
    }
    //
//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        adapter.stopListening();
//    }
    private void loadMenu() {
        Log.e("home","Inside loadmenu");

        final ProgressDialog mDialog = new ProgressDialog(HomeActivity.this);
        mDialog.setMessage("Please Wait...");
        mDialog.show();
        options = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(product,Product.class)//select all
                .build();
        mDialog.dismiss();
        Log.e("home","After Options");
        adapter =new FirebaseRecyclerAdapter<Product, MenuViewHolder>(options) {
            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                Log.e("home","Inside MenuView");
                mDialog.dismiss();
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.product_item,parent,false);
                return new MenuViewHolder(itemView);
            }

            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder viewHolder, int i, @NonNull final Product product) {
                Log.e("home","Inside onBind");
                //  mDialog.dismiss();
                viewHolder.txtMenuName.setText(product.getName());
                viewHolder.price.setText(product.getPrice());
                viewHolder.location.setText(product.getLocation());
                Picasso.with(getBaseContext()).load(product.getImage())
                        .into(viewHolder.imageView);
                //  endListener();
                final Product clickItem =product;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Get Category id and sends to new A ctivity
                        // Toast.makeText(HomeActivity.this, "" + clickItem.getId(), Toast.LENGTH_SHORT).show();
                        Intent productdetail=new Intent(HomeActivity.this,ProductDetail.class);
                        //ecause categoryid is key, we just get key of the selected item
                        productdetail.putExtra("CategoryId",adapter.getRef(position).getKey());
                        productdetail.putExtra("name",product.getName());
                        productdetail.putExtra("price",product.getPrice());
                        productdetail.putExtra("location",product.getLocation());
                        productdetail.putExtra("description",product.getDescription());
                        productdetail.putExtra("date_posted",product.getDate_posted());
                        productdetail.putExtra("img_url",product.getImage());
                        startActivity(productdetail);
                    }
                });
            }

        };

        adapter.notifyDataSetChanged();
        // mDialog.dismiss();
        //adapter=new MyAdapter(MainActivity.this,helper.retrieve());
        recycler_menu.setAdapter(adapter);
        adapter.startListening();
        adapter.notifyDataSetChanged();
        //swipeRefreshLayout.setRefreshing(false);
        Log.e("home","last laodmenu");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent i=new Intent(HomeActivity.this,HomeActivity.class);
            startActivity(i);
            //Toast.makeText(this, "This Is home button", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_gallery) {
            Intent i=new Intent(HomeActivity.this,BuyerRequest.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(HomeActivity.this,MoreActivity.class));

        } else if (id == R.id.nav_tools) {
            startActivity(new Intent(HomeActivity.this,Diseases.class));

        } else if (id == R.id.account) {
            Intent intent=new Intent(HomeActivity.this,MyProfile.class);
            startActivity(intent);

        } else if (id == R.id.contactus) {
            Intent intent=new Intent(Intent.ACTION_SEND);
            String[] recipients={"mailto@gmail.com"};
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT,"Subject text here...");
            intent.putExtra(Intent.EXTRA_TEXT,"Body of the content here...");
            intent.putExtra(Intent.EXTRA_CC,"VinnMutriz@gmail.com");
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        }
        else if (id==R.id.diseases){
            startActivity(new Intent(HomeActivity.this, Diseases.class));
        }
        else if (id==R.id.reviews){
           // Toast.makeText(this, "Reviews", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(HomeActivity.this, Reviews.class));
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void dataChanged() {

        adapter.notifyDataSetChanged();
    }
}