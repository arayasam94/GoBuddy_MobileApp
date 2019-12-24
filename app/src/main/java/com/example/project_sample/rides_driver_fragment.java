package com.example.project_sample;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class rides_driver_fragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    DatabaseReference databaseSearchTrip;
    Toolbar toolbar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseRecyclerAdapter<Search_Structure, RidesView> recyclerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ride_driver_fragment, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.rd_recycle);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        fetchRides();
        return view;
    }
    /*fetch all rides of user - as driver*/
    private void fetchRides(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String userId=user.getUid();
        databaseSearchTrip = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users").child(userId).child("rides_driver");
                Query query ;
                query = databaseSearchTrip.orderByChild("rides_driver");//fetch data from database
                //fetching data and populating it in recycler view
                FirebaseRecyclerOptions<Search_Structure> options =
                        new FirebaseRecyclerOptions.Builder<Search_Structure>()
                                .setQuery(query, new SnapshotParser<Search_Structure>() {
                                    @NonNull
                                    @Override
                                    //creating new model with data from firebase
                                    public Search_Structure parseSnapshot(DataSnapshot snapshot) {
                                        return new Search_Structure(snapshot.child("postID").getValue().toString(),
                                                snapshot.child("info").getValue().toString(),
                                                snapshot.child("source").getValue().toString(),
                                                snapshot.child("destination").getValue().toString(),
                                                snapshot.child("date_journey").getValue().toString(),
                                                snapshot.child("time_journey").getValue().toString(),
                                                Float.parseFloat(snapshot.child("seatPrice").getValue().toString()),
                                                Integer.parseInt(snapshot.child("seatsAvail").getValue().toString()),
                                                snapshot.child("image").getValue().toString(),
                                                snapshot.child("userID").getValue().toString(),
                                                snapshot.child("description").getValue().toString());
                                    }
                                })
                                .build();
        //initializing recycler adapter
        recyclerAdapter = new FirebaseRecyclerAdapter<Search_Structure, RidesView>(options) {
            @NonNull
            @Override
            public RidesView onCreateViewHolder(ViewGroup viewGroup, final int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rides_list, viewGroup, false);
                return new RidesView(view);
            }

            @Override
            protected void onBindViewHolder(final RidesView holder, final int position, final Search_Structure model) {
                holder.setSource(model.getSource());
                holder.setDestination(model.getDestination());
                holder.setPrice(model.getPrice());
                holder.setDate(model.getDate());
                holder.setImage(model.getImage());
                holder.itemView.findViewById(R.id.openMap).setVisibility(View.GONE);//hiding track button for driver
            }
        };
        recyclerAdapter.startListening();
        recyclerView.setAdapter(recyclerAdapter);
    }
}
