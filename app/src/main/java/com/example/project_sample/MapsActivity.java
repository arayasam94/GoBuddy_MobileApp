
package com.example.project_sample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener  {

    public GoogleMap mMap;

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 1;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    //location and storage permissions required to access device location.
    private static final String[] REQUIRED_SDK_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    DatabaseReference databaseSearchTrip;
    FloatingActionButton close;
    String driverID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        checkPermissions();
        close=(FloatingActionButton) findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("fragmentNumber","yourrides"); //navigation on click of back button  from maps activity to your rides fragment
                startActivity(intent);
            }
        });
       Intent intent = getIntent();
       driverID=intent.getStringExtra("driverID");// get driver's ID

        // get the SupportMapFragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        // get notified when the map is ready to be used.
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // permission to access location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);// enable current location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        //to track user's current location
        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));
    }

        repeatRetrieve();

    }
/*fetch the driver's location and add a marker on map */
    private void repeatRetrieve() {
        databaseSearchTrip = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users").child(driverID);
        databaseSearchTrip.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().toString().equals("location")) {

                Double latitude=Double.parseDouble(dataSnapshot.child("latitude").getValue().toString());
                Double longitude=Double.parseDouble(dataSnapshot.child("longitude").getValue().toString());
                mMap.clear();
                       mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude,longitude))
                            .title("Driver's location").icon(BitmapDescriptorFactory.fromResource(R.drawable.map_car)));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mMap.clear();
                if(dataSnapshot!=null) {
                    Double latitude = Double.parseDouble(dataSnapshot.child("location").child("latitude").getValue().toString());
                    Double longitude = Double.parseDouble(dataSnapshot.child("location").child("longitude").getValue().toString());
                    mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(latitude,longitude))
                            .title("Driver's location").icon(BitmapDescriptorFactory.fromResource(R.drawable.car_marker)));

                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    protected void checkPermissions() {
        final List<String> requiredPermissions = new ArrayList<String>();
        /*check and request for all the required permissions*/
        for (final String permission : REQUIRED_SDK_PERMISSIONS) {
            final int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(permission);
            }
        }
        if (!requiredPermissions.isEmpty()) {
            // request all missing permissions
            final String[] permissions = requiredPermissions
                    .toArray(new String[requiredPermissions.size()]);
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            final int[] grantResults = new int[REQUIRED_SDK_PERMISSIONS.length];
            Arrays.fill(grantResults, PackageManager.PERMISSION_GRANTED);
            onRequestPermissionsResult(REQUEST_CODE_ASK_PERMISSIONS, REQUIRED_SDK_PERMISSIONS,
                    grantResults);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                for (int index = permissions.length - 1; index >= 0; --index) {
                    if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                        //leave the application if required permissions are not granted
                        return;
                    }
                }
                // continue if all permissions are granted

                break;
        }
    }
}

