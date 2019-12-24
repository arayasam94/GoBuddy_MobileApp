package com.example.project_sample;


import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationManager;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;


import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;

import java.util.HashMap;
import java.util.Map;


public class MylocationListener implements LocationListener {
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference databaseUser;
    String userId;

        @Override
        public void onLocationChanged(final Location location) {


            if (location != null) {



                Log.v("Location", "IN ON LOCATION CHANGE, lat=" + location.getLatitude() + ", lon=" + location.getLongitude());
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                 userId = user.getUid();
                databaseUser = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users");
                databaseUser.runTransaction(new Transaction.Handler() {
                    @NonNull
                    @Override
                    public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                        Map<String, Object> userUpdates = new HashMap<>();
                        Map<String, Object> parent = new HashMap<>();
                        userUpdates.put("latitude", location.getLatitude());
                        userUpdates.put("longitude", location.getLongitude());
                        parent.put("/" + userId + "/location", userUpdates);
                        databaseUser.updateChildren(parent);
                        return  null;
                    }

                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                    }
                });


            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
//    LocationManager locationManager = (LocationManager) MapsActivity.getSystemService(Context.LOCATION_SERVICE);
//       locationManager.requestLocationUpdates(locationProvide, 0, 0, locationListener);
//    Location lastLocation=locationManager.getLastKnownLocation(locationProvide);


};











