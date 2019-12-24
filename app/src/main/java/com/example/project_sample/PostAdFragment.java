package com.example.project_sample;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

public class PostAdFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    String[] seats = {"1", "2", "3"};//number of seats for posting ad

    final Calendar myCalendar = Calendar.getInstance();
    Button post_btn;
    Toolbar toolbar;
    AutocompleteSupportFragment post_src;
    AutocompleteSupportFragment post_dst;
    EditText post_date;
    EditText post_price;
    EditText post_time;
    Spinner seats_avail;
    EditText post_description;
    Button imgCar;
    DatabaseReference databasePostAd;
    DatabaseReference databaseUserAd;
    StorageReference storageReference;
    FirebaseStorage storage;
    String source;
    String dest;
    ImageView carImage;
    Bitmap imageBitmap;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.post_ad_fragment, container, false);
        toolbar = (Toolbar) v.findViewById(R.id.toolbarSearch);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        seats_avail = (Spinner) v.findViewById(R.id.ad_seats_available);
        post_date = (EditText) v.findViewById(R.id.post_date);
        post_price = (EditText) v.findViewById(R.id.post_price);
        seats_avail.setOnItemSelectedListener(this);
        post_btn = (Button) v.findViewById(R.id.btn_post_ad);
        imgCar = (Button) v.findViewById(R.id.btnImgCar);
        carImage = (ImageView) v.findViewById(R.id.imgCarContain);
        post_description = (EditText) v.findViewById(R.id.p_carDescription);
        //choose image of car from camera or gallery
        imgCar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                        getActivity());
                myAlertDialog.setTitle("Upload Vehicle's Picture");
                myAlertDialog.setMessage("Selct from Camera/Gallery");

                myAlertDialog.setPositiveButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

                            }
                        });

                myAlertDialog.setNegativeButton("Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {

                                Intent intent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);

                                startActivityForResult(intent,
                                        0);

                            }
                        });
                myAlertDialog.show();

            }

        });


        //Creating the ArrayAdapter instance having the seats list
        ArrayAdapter aa = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item, seats);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        if (!Places.isInitialized()) {
            Places.initialize(getActivity().getApplicationContext(), "AIzaSyDBw9jXVB3Kd-UhKvw1XtKgCFUICux_sZw");
        }

        // Initialize the AutocompleteSupportFragment.
        post_src = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.post_src);
        post_src.setHint("Enter Source");
        post_dst = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.post_dst);
        post_dst.setHint("Enter Destination");

        // Specify the types of required place data to fetch.
        post_src.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
        post_dst.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));


        // Set up a PlaceSelectionListener to handle the response.
        post_src.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                source = place.getName();
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
        post_dst.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                dest = place.getName();
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });


        //Setting the ArrayAdapter data on the Spinner
        seats_avail.setAdapter(aa);


        post_time = (EditText) v.findViewById(R.id.post_time);
        //select time
        post_time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        post_time.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        //initialize date picker
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        //select date
        post_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);// disable past dates
                datePickerDialog.show();

            }
        });

        /*Validation - all the fields must be filled*/
        post_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((source == null || source.equals("")) || (dest == null || dest.equals("")) || (post_date.getText().toString().equals("")) ||
                        (post_time.getText().toString().equals("")) || post_price.getText().toString().equals("") || post_description.getText().toString().equals("")
                        || post_description.getText().toString().equals("") || carImage.getDrawable() == null) {

                    Toast.makeText(getActivity(), "Enter all the fields", Toast.LENGTH_LONG).show();
                    return;
                }
                databasePostAd = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("posts");
                databaseUserAd = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users");
                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference();
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                String src = source;
                String dstn = dest;
                String doj = post_date.getText().toString();
                String strtTime = post_time.getText().toString().trim();
                Float pricePSeat = Float.parseFloat(post_price.getText().toString().trim());
                Integer seats_av = Integer.parseInt(seats_avail.getSelectedItem().toString());
                String info = src + "_" + dstn + "_" + doj;
                String desc = post_description.getText().toString();
                String userId = user.getUid();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                //String id = databasePostAd.push().getKey();
                String key = databasePostAd.child("posts").push().getKey();
                PostAd_Structure trip_ad = new PostAd_Structure(key, info, src, dstn, doj, strtTime, pricePSeat, seats_av, imageEncoded, userId, desc);
                Map<String, Object> postValues = trip_ad.toMap();
                //databasePostAd.child(key).setValue(trip_ad);
                Map<String, Object> childUpdates = new HashMap<>();
                Map<String, Object> userUpdates = new HashMap<>();
                childUpdates.put(key, postValues);//update post in all the posts section on firebase - 'posts'
                userUpdates.put("/" + userId + "/rides_driver/" + key, postValues);//update post under userID - 'users'

                databasePostAd.updateChildren(childUpdates);
                databaseUserAd.updateChildren(userUpdates);
                Toast.makeText(getActivity(), "Ad successfully posted", Toast.LENGTH_LONG).show();
                Fragment yourrides = new YourRidesFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragement_container, yourrides);
                ((BottomNavigationView) getActivity().findViewById(R.id.bottom_nav_view)).setSelectedItemId(R.id.nav_your_rides);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });


        return v;

    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        post_date.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            carImage.setImageBitmap(imageBitmap);
            imgCar.setText("Change Image");

        }
        if (requestCode == 1 && resultCode == RESULT_OK) {


            Uri selectedImageUri = data.getData();
            // Get the path from the Uri
            final String path = getPathFromURI(selectedImageUri);
            if (path != null) {
                File f = new File(path);
                selectedImageUri = Uri.fromFile(f);
            }
            // Set the image in ImageView

            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //use the bitmap as you like
            carImage.setImageBitmap(imageBitmap);
            imgCar.setText("Change Image");


        }
    }

    private String getPathFromURI(Uri contentUri) {

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;

    }

}
