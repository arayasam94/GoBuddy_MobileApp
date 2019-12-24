package com.example.project_sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_sample.ui.main.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPost extends AppCompatActivity {
    TextView vp_src;
    TextView vp_dst;
    TextView vp_date;
    TextView vp_time;
    TextView vp_price;
    ImageView vp_carimage;
    ImageView vp_driver;
    Spinner vp_spinner;
    Button vp_book;
    TextView vp_desc;
    private DatabaseReference databasePostAd;
    private DatabaseReference databaseUserAd;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String postID;
    private Bundle dataBundle;
    private String src;
    private String dst;
    private Float price;
    private String date;
    private String time;
    private Integer seatsAvail;
    private String carImage;
    private String desc;
    private String info;
    private String role;
    private String driverID;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        vp_src = (TextView) findViewById(R.id.vp_src);
        vp_dst = (TextView) findViewById(R.id.vp_dst);
        vp_date = (TextView) findViewById(R.id.vp_date);
        vp_price = (TextView) findViewById(R.id.vp_price);
        vp_time = (TextView) findViewById(R.id.vp_time);
        vp_carimage = (ImageView) findViewById(R.id.vp_carImage);
        vp_driver = (ImageView) findViewById(R.id.vp_driver_image);
        vp_spinner = (Spinner) findViewById(R.id.vp_seats);
        vp_book = (Button) findViewById(R.id.vp_book);
        vp_desc = (TextView) findViewById(R.id.vp_desc);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userID = user.getUid();
        populateView();

        vp_driver.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openProfile();
            }
        });
        vp_book.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                bookRide();
            }
        });
    }

    private void populateView() {

        //vp_spinner.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);
        postID = getIntent().getStringExtra("postID");
        dataBundle = getIntent().getBundleExtra("bundle");
        src = dataBundle.getString("source");
        dst = dataBundle.getString("destination");
        price = dataBundle.getFloat("price");
        date = dataBundle.getString("date");
        time = dataBundle.getString("time");
        seatsAvail = dataBundle.getInt("seats");
        carImage = dataBundle.getString("image");
        info = dataBundle.getString("info");
        role = dataBundle.getString("role");
        driverID = dataBundle.getString("driverID");
        desc = dataBundle.getString("description");
        String[] seatsList = new String[seatsAvail];

        DatabaseReference profileDetails = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users").child(driverID).child("profile");

        Query query;

        query = profileDetails.orderByChild("profile");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String driverpic;
                driverpic = dataSnapshot.child("profile_pic").getValue().toString();
                byte[] encodeByte = Base64.decode(driverpic, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);//converting image to bitmap
                vp_driver.setImageBitmap(bitmap);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        for (int i = 0; i < seatsList.length; i++) {
            seatsList[i] = (String.valueOf((i + 1)));
        }
        byte[] encodeByte = Base64.decode(carImage, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        byte[] driver = Base64.decode(carImage, Base64.DEFAULT);
        Bitmap driverImage = BitmapFactory.decodeByteArray(driver, 0, encodeByte.length);
        if (seatsAvail == 0 || driverID.equals(userID)) {
            vp_book.setEnabled(false);
            vp_book.setBackgroundColor(getColor(R.color.common_google_signin_btn_text_dark_disabled));

        }
        //


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, seatsList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vp_spinner.setAdapter(aa);
        vp_spinner.setSelected(true);
        vp_carimage.setImageBitmap(bitmap);
        vp_src.setText(src);
        vp_dst.setText(dst);
        vp_date.setText(date);
        vp_time.setText(time);
        vp_price.setText(price.toString());
        vp_desc.setText(desc);
        vp_driver.setImageBitmap(driverImage);

        vp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Integer seats = Integer.parseInt(vp_spinner.getSelectedItem().toString());
                vp_price.setText(String.valueOf(price * seats));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }
    //navigationg to driver's profile from View_post page
    private void openProfile() {
        Intent intent = new Intent(this, Driver_profile.class);
        intent.putExtra("driverID", driverID);
        startActivity(intent);

    }

    private void bookRide() {
        databasePostAd = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("posts");
        databaseUserAd = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users");

        String info = src + "_" + dst + "_" + date;
        String userId = user.getUid();
        Integer seats_booked = Integer.parseInt(vp_spinner.getSelectedItem().toString());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String key = databaseUserAd.push().getKey();
        bookRide_structure trip_ad = new bookRide_structure(postID, info, src, dst, date, time, price, seats_booked, carImage, userId, driverID, desc);
        Map<String, Object> bookRide = trip_ad.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> userUpdates = new HashMap<>();
        Map<String, Object> passengerUpdate = new HashMap<>();
        passengerUpdate.put(userId, seats_booked);
        databasePostAd.child(postID).child("seatsAvail").setValue(seatsAvail - seats_booked);
        databasePostAd.child(postID).child("passengers").updateChildren(passengerUpdate);
        userUpdates.put("/" + userId + "/rides_passenger/" + key, bookRide);
        databaseUserAd.updateChildren(userUpdates);//updating booked post.

        Toast.makeText(ViewPost.this, "Seats successfully booked", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fragmentNumber", "yourrides"); //navigating to your rides fragment
        startActivity(intent);


    }


}
