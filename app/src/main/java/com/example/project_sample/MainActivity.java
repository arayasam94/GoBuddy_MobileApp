package com.example.project_sample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public MylocationListener listener;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private LocationManager locationManager;
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseUser;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottom_nav = findViewById(R.id.bottom_nav_view); //binding bottom navigation
        bottom_nav.setOnNavigationItemSelectedListener(navListener);
        if (getIntent().hasExtra("fragmentNumber")) {//navigation to rides_passenger_fragment from booking page
            if (getIntent().getStringExtra("fragmentNumber").equals("yourrides")) {
                Bundle bundle = new Bundle();
                bundle.putString("tab", "ridespassenger");
                YourRidesFragment yourrides = new YourRidesFragment();
                yourrides.setArguments(bundle);
                ((BottomNavigationView) findViewById(R.id.bottom_nav_view)).setSelectedItemId(R.id.nav_your_rides);

                getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, yourrides).commit();//navigating to YourRidesFragment
            }

        } else
            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, new HomeFragment()).commit();//default fragment
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //check  permissions to access device location

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        /*access users current location*/
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Task<Location> task = mFusedLocationClient.getLastLocation();// fetch the last known location

        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    mAuth = FirebaseAuth.getInstance();
                    user = mAuth.getCurrentUser();
                    String userId = user.getUid();
                    //updating current location coordinates of the user to the database on launch of the application.
                    databaseUser = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users");
                    Map<String, Object> userUpdates = new HashMap<>();
                    Map<String, Object> parent = new HashMap<>();
                    userUpdates.put("latitude", location.getLatitude());
                    userUpdates.put("longitude", location.getLongitude());
                    parent.put("/" + userId + "/location", userUpdates);
                    databaseUser.updateChildren(parent);
                }

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            Fragment selectedFrag = null;
            // navigating to fragments with bottom navigation menu
            switch (menuItem.getItemId()) {
                case R.id.nav_home:
                    selectedFrag = new HomeFragment();
                    break;

                case R.id.nav_search:
                    selectedFrag = new SearchFragment();
                    break;

                case R.id.nav_post_ad:
                    selectedFrag = new PostAdFragment();
                    break;

                case R.id.nav_your_rides:
                    selectedFrag = new YourRidesFragment();
                    break;

                case R.id.nav_profile:
                    selectedFrag = new ProfileFragment();
                    break;

            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragement_container, selectedFrag).commit();

            return true;
        }
    };
}
