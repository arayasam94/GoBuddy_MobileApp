package com.example.project_sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Driver_profile extends AppCompatActivity {
    private DatabaseReference profileDetails;
    TextView name;
    ImageView profileView_pic;
    TextView profileView_fullName;
    TextView profileView_sex;
    TextView profileView_email;
    TextView profileView_phoneNo;
    FloatingActionButton sms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() { //back navigation to View_post page
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        name=(TextView) findViewById(R.id.welcomeName);
        profileView_pic=(ImageView) findViewById(R.id.profileView_pic);
        profileView_fullName=(TextView) findViewById(R.id.profileView_fullName);
        profileView_sex=(TextView) findViewById(R.id.profileView_sex);
        profileView_email=(TextView) findViewById(R.id.profileView_email);
        profileView_phoneNo=(TextView) findViewById(R.id.profileView_phoneNo);
        sms=(FloatingActionButton) findViewById(R.id.sms);
        String uid=getIntent().getStringExtra("driverID");
        //fetching driver's details from firebase
        profileDetails = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users").child(uid).child("profile");

        Query query;

        query = profileDetails.orderByChild("profile");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String u_ID;
                String f_name;
                String l_name;
                String _sex;
                String email;
                String phone;
                String profilepic;

                u_ID = dataSnapshot.child("userID").getValue().toString();
                f_name = dataSnapshot.child("firstName").getValue().toString();
                l_name = dataSnapshot.child("lastName").getValue().toString();
                _sex = dataSnapshot.child("sex").getValue().toString();
                email = dataSnapshot.child("email_id").getValue().toString();
                phone = dataSnapshot.child("phone_no").getValue().toString();
                profilepic=dataSnapshot.child("profile_pic").getValue().toString();
                byte[] encodeByte = Base64.decode(profilepic, Base64.DEFAULT);//converting image string to bitmap
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

                profileView_pic.setImageBitmap(bitmap);

                name.setText(f_name);
                profileView_fullName.setText(f_name+" "+l_name);
                profileView_sex.setText(_sex);
                profileView_email.setText(email);
                profileView_phoneNo.setText(phone);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //contact module: contact driver through sms
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:"+profileView_phoneNo.getText());//driver's number
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                intent.putExtra("sms_body", "");
                startActivity(intent);//open SMS
            }
        });
    }
}
