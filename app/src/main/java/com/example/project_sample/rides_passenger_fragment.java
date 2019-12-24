package com.example.project_sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class rides_passenger_fragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    DatabaseReference databaseSearchTrip;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseRecyclerAdapter<bookRide_structure, RidesView> recyclerAdapter;
    Button openMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rides_passenger_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rp_recycle);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        fetchRides();
        return view;
    }
    private void fetchRides(){

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        final String userId=user.getUid();
        databaseSearchTrip = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users").child(userId).child("rides_passenger");
        Query query ;
        query = databaseSearchTrip.orderByChild("rides_passenger");
        //fetching rides details from firebase
        FirebaseRecyclerOptions<bookRide_structure> options =
                new FirebaseRecyclerOptions.Builder<bookRide_structure>()
                        .setQuery(query, new SnapshotParser<bookRide_structure>() {
                            @NonNull
                            @Override
                            //creating data model
                            public bookRide_structure parseSnapshot(DataSnapshot snapshot) {
                                 return new bookRide_structure(snapshot.child("postID").getValue().toString(),
                                        snapshot.child("info").getValue().toString(),
                                        snapshot.child("source").getValue().toString(),
                                        snapshot.child("destination").getValue().toString(),
                                        snapshot.child("date_journey").getValue().toString(),
                                        snapshot.child("time_journey").getValue().toString(),
                                        Float.parseFloat(snapshot.child("paid").getValue().toString()),
                                        Integer.parseInt(snapshot.child("seatsBooked").getValue().toString()),
                                        snapshot.child("image").getValue().toString(),
                                        snapshot.child("userID").getValue().toString(),
                                        snapshot.child("driverID").getValue().toString(),
                                        snapshot.child("description").getValue().toString());
                            }
                        })
                        .build();

        recyclerAdapter = new FirebaseRecyclerAdapter<bookRide_structure, RidesView>(options) {
            @NonNull
            @Override
            public RidesView onCreateViewHolder(ViewGroup viewGroup, final int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.rides_list, viewGroup, false);



                return new RidesView(view);
            }


            @Override
            protected void onBindViewHolder(final RidesView holder, final int position, final bookRide_structure model) {
                holder.setSource(model.getBookRide_Structure_source());
                holder.setDestination(model.getBookRide_Structure_destination());
                holder.setPrice(model.getBookRide_Structure_paid());
                holder.setDate(model.getBookRide_Structure_date());
                holder.setImage(model.getBookRide_Structure_carImage());
                // query to fetch name of the driver
                DatabaseReference profileDetails = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users").child(model.getBookRide_Structure_driverID()).child("profile");
                Query query;

                query = profileDetails.orderByChild("profile");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                       holder.setDriver(dataSnapshot.child("firstName").getValue().toString());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });















                holder.setDriver(model.getBookRide_Structure_driverID());
                holder.itemView.findViewById(R.id.openMap).setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        String driverID=model.getBookRide_Structure_driverID();
                        Intent map = new Intent(getActivity(), MapsActivity.class);
                        map.putExtra("driverID",driverID);
                        startActivity(map);
                    }
                });
            }
        };
        recyclerAdapter.startListening();
        recyclerView.setAdapter(recyclerAdapter);
    }
}
