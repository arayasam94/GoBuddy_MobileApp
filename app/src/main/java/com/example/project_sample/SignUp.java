




package com.example.project_sample;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.project_sample.MainActivity;
import com.example.project_sample.R;
import com.example.project_sample.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.utilities.Validation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    Bitmap imageBitmap;
    private EditText firstname;
    private EditText lastname;
    private RadioGroup radio_group;
    private RadioButton radio_sex;
    private EditText email;
    private EditText password;
    private EditText confirm_password;
    private EditText phone;
    private Button profile_btn;
    private Button sign_up_btn;
    private Bitmap profilePic_bitMap;
    private ImageView profile_pic;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private DatabaseReference db_ref_profile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        radio_group = (RadioGroup) findViewById(R.id.radio_group);
        //radio_sex = (RadioButton) findViewById(R.id.male_radio);
        email = (EditText) findViewById(R.id.email_address_su);
        password = (EditText) findViewById(R.id.password);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        phone = (EditText) findViewById(R.id.phone);
        profile_btn = (Button) findViewById(R.id.upload_profilepic_button);
        sign_up_btn = (Button) findViewById(R.id.signup_button);
        profile_pic = (ImageView) findViewById(R.id.profilePic);

        //Initializing firebase authentication instance
        mAuth = FirebaseAuth.getInstance();
        sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Validation()) {
                    String Email = email.getText().toString().trim();
                    String Password = password.getText().toString().trim();
                    int radioId = radio_group.getCheckedRadioButtonId();
                    radio_sex = findViewById(radioId);
                    //creating account on firebase
                    mAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    try {
                                        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                            @Override
                                            public void onSuccess(AuthResult authResult) {
                                                String currentUID=authResult.getUser().getUid();//get registered user id
                                                String userID = currentUID;
                                                String fName = firstname.getText().toString();
                                                String lName = lastname.getText().toString();
                                                String sex = radio_sex.getText().toString();
                                                String emailID = email.getText().toString();
                                                String phoneNo = phone.getText().toString();
                                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                                imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                                String imageEncoded = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
                                                Profile_Structure profileInfo = new Profile_Structure(currentUID, fName, lName, sex, emailID, phoneNo, imageEncoded);
                                                Map<String, Object> profile_data = profileInfo.toMap();
                                                Map<String, Object> profile_user = new HashMap<>();
                                                //updating user profile details to database
                                                db_ref_profile = FirebaseDatabase.getInstance().getReference("GoBuddyData").child("users").child(currentUID);
                                                profile_user.put("profile", profileInfo);
                                                db_ref_profile.updateChildren(profile_user);
                                                Toast.makeText(SignUp.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                                                Intent signup = new Intent(SignUp.this, LoginActivity.class);
                                                startActivity(signup);
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUp.this, "Could not register", Toast.LENGTH_SHORT).show();

                        }
                    });
                }






            }
        });
        //selecting profile photo from galler or camera
        profile_btn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                        SignUp.this);
                myAlertDialog.setTitle("Upload Profile Picture");
                myAlertDialog.setMessage("Choose from Camera/Gallery");
                //choose photo from gallery
                myAlertDialog.setPositiveButton("Gallery",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.addCategory(Intent.CATEGORY_OPENABLE);
                                intent.setType("image/*");
                                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);


                            }
                        });
                //choose photo from camera
                myAlertDialog.setNegativeButton("Camera",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 0);
                            }
                        });
                myAlertDialog.show();
            }

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //opening camera
        if (requestCode == 0 && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");

            profile_pic.setImageBitmap(imageBitmap);
            profile_btn.setText("Change Profile Image");//updating profile picture

        }
        //opening gallery
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            // Get the path from the Uri
            final String path = getPathFromURI(selectedImageUri);
            if (path != null) {
                File f = new File(path);
                selectedImageUri = Uri.fromFile(f);
            }
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);//converting picture from gallery to bitmap
            } catch (IOException e) {
                e.printStackTrace();
            }

            profile_pic.setImageBitmap(imageBitmap);
            profile_btn.setText("Change Image");//updating profile picture


        }
    }

    private String getPathFromURI(Uri contentUri) {

        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;

    }

    public boolean Validation() {

        int flag = 0;

        String fName = firstname.getText().toString().trim();
        String lname = lastname.getText().toString().trim();
        //String radio_sex = sex_radio/
        String Email = email.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String CnfPassword = confirm_password.getText().toString().trim();
        String phoneNo = phone.getText().toString().trim();

        int radioId = radio_group.getCheckedRadioButtonId();
        radio_sex = findViewById(radioId);

        Toast.makeText(this, radio_sex.getText(), Toast.LENGTH_SHORT);

        if (TextUtils.isEmpty(fName)) {
            Toast.makeText(this, "Please enter First Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(lname)) {
            Toast.makeText(this, "Please enter Last Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, "Please enter Email Address", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(Password)) {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (Password.length() < 6) {
            Toast.makeText(this, "Password length should be 6 characters", Toast.LENGTH_LONG).show();
            return false;
        }
        if (TextUtils.isEmpty(CnfPassword)) {
            Toast.makeText(this, "Please Confirm Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!CnfPassword.equals(Password)) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(phoneNo)) {
            Toast.makeText(this, "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


//        profile_pic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(getPackageManager()) != null) { // check this
//                    startActivityForResult(takePictureIntent, 1);
//                }
//            }
//
//        });

}

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0 && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            profilePic_bitMap = (Bitmap) extras.get("data");
//
//            profile_pic.setImageBitmap(profilePic_bitMap);
//            profile_btn.setText("Change Image");
//
//        }
//        if (requestCode == 1 && resultCode == RESULT_OK) {
//
//
//            Uri selectedImageUri = data.getData();
//            // Get the path from the Uri
//            final String path = getPathFromURI(selectedImageUri);
//            if (path != null) {
//                File f = new File(path);
//                selectedImageUri = Uri.fromFile(f);
//            }
//            // Set the image in ImageView
//            profile_pic.setImageURI(selectedImageUri);
//            // carImage.setImageBitmap(selectedImageUri);
//            profile_btn.setText("Change Image");
//
//
//
//        }}
//
//    private String getPathFromURI(Uri contentUri) {
//
//        String res = null;
//        String[] proj = {MediaStore.Images.Media.DATA};
//        Cursor cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
//        if (cursor.moveToFirst()) {
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            res = cursor.getString(column_index);
//        }
//        cursor.close();
//        return res;
//
//    }

