package com.example.project_sample;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


public class SearchFragment extends Fragment  {
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    final Calendar myCalendar = Calendar.getInstance();
    Toolbar toolbar;
    AutocompleteSupportFragment srcSearch;
    AutocompleteSupportFragment dstSearch;
    EditText dateSearch;
    Button btnSearch;
    String src;
    String dest;

    DatabaseReference databaseSearchTrip;
    FirebaseRecyclerAdapter<Search_Structure, SearchView> recyclerAdapter;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.search_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);
        toolbar = (Toolbar) v.findViewById(R.id.toolbarSearch);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        dateSearch = (EditText) v.findViewById(R.id.dateSearch);
        layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        btnSearch = (Button) v.findViewById(R.id.btn_search);


        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), "AIzaSyDBw9jXVB3Kd-UhKvw1XtKgCFUICux_sZw");
        }

        // Initialize the AutocompleteSupportFragment.
        srcSearch = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.srcSearch);
        srcSearch.setHint("Enter Source");
        dstSearch = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.dstSearch);
        dstSearch.setHint("Enter Destination");
        srcSearch.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        dstSearch.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        srcSearch.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                src = place.getName();
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.

                Log.i(TAG, "An error occurred: " + status);
            }
        });
        dstSearch.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                dest = place.getName();
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Validation
                if((src==null ||src.equals("")) || (dest==null || dest.equals("")) || (dateSearch.getText().toString().equals(""))){
                    Toast.makeText(getActivity(), "Enter all the fields", Toast.LENGTH_LONG).show();
                    return;
                }
                // TODO Auto-generated method stub
                databaseSearchTrip = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("posts");
                Query query ;
                String info=src+"_"+dest+"_"+dateSearch.getText().toString();
                query = databaseSearchTrip.orderByChild("info").equalTo(info);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Toast.makeText(getActivity(),"No trips found",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //creating new Search_structure model with data from firebase
                  final FirebaseRecyclerOptions<Search_Structure> options =
                        new FirebaseRecyclerOptions.Builder<Search_Structure>()
                                .setQuery(query, new SnapshotParser<Search_Structure>() {
                                    @NonNull
                                    @Override
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

                recyclerAdapter = new FirebaseRecyclerAdapter<Search_Structure, SearchView>(options) {
                    @Override
                    public int getItemCount() {
                        return getSnapshots().size();
                    }

                    @NonNull

                    @Override
                    public SearchView onCreateViewHolder(ViewGroup viewGroup, final int i) {
                        View view = LayoutInflater.from(viewGroup.getContext())
                                .inflate(R.layout.search_list, viewGroup, false);

                        return new SearchView(view);
                    }

                    @Override
                    protected void onBindViewHolder(final SearchView holder, final int position, final Search_Structure model) {
                        holder.setSource(model.getSource());
                        holder.setDestination(model.getDestination());
                        holder.setPrice(model.getPrice());
                        holder.setDate(model.getDate());
                        holder.setImage(model.getImage());
                        holder.setSeatsCount(model.getSeats_avail());

                        /*sending data in content to view_post page*/
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent view_post = new Intent(getActivity(), ViewPost.class);
                                Bundle postBundle=new Bundle();


                                String postID=model.getPostID();
                                String date=model.getDate();
                                String dest=model.getDestination();
                                String src=model.getSource();
                                Float price=model.getPrice();
                                String image=model.getImage();
                                String info=model.getInfo();
                                String description=model.getDescription();
                                Integer seatsAvail=model.getSeats_avail();
                                String startTime=model.getStartTime();
                                String userID=model.getDriverID();

                                view_post.putExtra("postID",postID);
                                postBundle.putString("source", src);
                                postBundle.putString("destination",dest);
                                postBundle.putFloat("price",price);
                                postBundle.putString("date",date);
                                postBundle.putString("time",startTime);
                                postBundle.putInt("seats",seatsAvail);
                                postBundle.putString("image",image);
                                postBundle.putString("info",info);
                                postBundle.putString("driverID",userID);
                                postBundle.putString("description",description);


                                view_post.putExtra("bundle",postBundle);
                                startActivity(view_post);

                            }
                        });

                    }


                };
                recyclerAdapter.startListening();
                recyclerView.setAdapter(recyclerAdapter);
            }

        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();// updating date to view
            }

        };

        dateSearch.setOnClickListener(new View.OnClickListener() {// onclick of date picker

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);// to disable past dates
                datePickerDialog.show();
            }
        });


        return v;
    }


    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //format of the date
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dateSearch.setText(sdf.format(myCalendar.getTime()));//updating date
    }


}
