package com.example.project_sample;

import android.provider.ContactsContract;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Profile_Structure {

    private String userID;
    private String firstName;
    private String lastName;
    private String sex;
    private String email_id;
    private String phone_no;
    private String profile_pic;

    public Profile_Structure()
    {}

    // Need to add profile Pic
    public Profile_Structure(String userID, String firstName, String lastName, String sex, String email_id, String phone_no,String profile_pic)
    {
        this.userID=userID;
        this.firstName=firstName;
        this.lastName=lastName;
        this.sex=sex;
        this.email_id=email_id;
        this.phone_no=phone_no;
        this.profile_pic=profile_pic;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("firstname", firstName);
        result.put("lastname", lastName);
        result.put("sex", sex);
        result.put("emailid", email_id);
        result.put("phoneno", phone_no);
        result.put("profile_image",profile_pic);
        //result.put("profile_pic",profile_pic);

        return result;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
