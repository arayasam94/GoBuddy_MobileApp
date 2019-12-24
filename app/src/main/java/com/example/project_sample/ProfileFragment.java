package com.example.project_sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_sample.ui.login.LoginActivity;
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

import org.w3c.dom.Text;

public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    FirebaseRecyclerAdapter<Search_Structure, SearchView> recyclerAdapter;

    DatabaseReference profileDetails;

    private TextView welcomeName;
    private TextView fullName;
    private TextView user_sex;
    private TextView emailID;
    private TextView phoneNo;
    private TextView user_ID;
    private ImageView profile_pic;
    private Button logout;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.profile_fragment, container, false);

        welcomeName = (TextView) v.findViewById(R.id.welcomeName);
        fullName = (TextView) v.findViewById(R.id.profileView_fullName);
        user_sex = (TextView) v.findViewById(R.id.profileView_sex);
        emailID = (TextView) v.findViewById(R.id.profileView_email);
        phoneNo = (TextView) v.findViewById(R.id.profileView_phoneNo);
        user_ID = (TextView) v.findViewById(R.id.userID);
        profile_pic = (ImageView) v.findViewById(R.id.profileView_pic);
        String uid = null;
        logout = (Button) v.findViewById(R.id.button_logout);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            uid = user.getUid();
            user_ID.setText(uid);
        }
        //retrieving user's details from firebase
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
                profilepic = dataSnapshot.child("profile_pic").getValue().toString();
                byte[] encodeByte = Base64.decode(profilepic, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);//convert image string to bitmap
                profile_pic.setImageBitmap(bitmap);
                welcomeName.setText(f_name);
                fullName.setText(f_name + " " + l_name);
                user_sex.setText(_sex);
                emailID.setText(email);
                phoneNo.setText(phone);
                user_ID.setText(u_ID);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //logout from app
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Toast.makeText(v.getContext(), "Logout successful", Toast.LENGTH_SHORT);
                Intent login = new Intent(getActivity(), LoginActivity.class);
                startActivity(login);
            }
        });
        return v;
    }
}
