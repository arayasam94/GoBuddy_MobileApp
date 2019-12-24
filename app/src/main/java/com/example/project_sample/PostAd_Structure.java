package com.example.project_sample;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class PostAd_Structure {

    private String postAd_Structure_postID;
    private String postAd_Structure_source;
    private String postAd_Structure_destination;
    private String postAd_Structure_date;
    private String postAd_Structure_startTime;
    private Float postAd_Structure_pricePSeat;
    private Integer postAd_Structure_seatsAvailable;
    private String postAd_Structure_carImage;
    private String postAd_Structure_userID;
    private String postAd_Structure_info;
    private String postAd_Structure_desc;

    public PostAd_Structure() {
    }

    public PostAd_Structure(String postID, String info, String postSource, String postDestination, String date, String startTime, Float pricePSeat, Integer seats_av, String image, String uid, String desc) {
        this.postAd_Structure_postID = postID;
        this.postAd_Structure_source = postSource;
        this.postAd_Structure_destination = postDestination;
        this.postAd_Structure_date = date;
        this.postAd_Structure_startTime = startTime;
        this.postAd_Structure_pricePSeat = pricePSeat;
        this.postAd_Structure_seatsAvailable = seats_av;
        this.postAd_Structure_carImage = image;
        this.postAd_Structure_userID = uid;
        this.postAd_Structure_info = info;
        this.postAd_Structure_desc = desc;
    }

/*hashmap for database structure*/
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("source", postAd_Structure_source);
        result.put("destination", postAd_Structure_destination);
        result.put("postID", postAd_Structure_postID);
        result.put("date_journey", postAd_Structure_date);
        result.put("seatPrice", postAd_Structure_pricePSeat);
        result.put("time_journey", postAd_Structure_startTime);
        result.put("seatsAvail", postAd_Structure_seatsAvailable);
        result.put("image", postAd_Structure_carImage);
        result.put("userID", postAd_Structure_userID);
        result.put("info", postAd_Structure_info);
        result.put("description", postAd_Structure_desc);

        return result;
    }

    public String getPostID() {
        return postAd_Structure_postID;
    }

    public String getPostSource() {
        return postAd_Structure_source;
    }

    public String getPostDestination() {
        return postAd_Structure_destination;
    }

    public String getDate() {
        return postAd_Structure_date;
    }

    public String getStartTime() {
        return postAd_Structure_startTime;
    }

    public Float getPricePSeat() {
        return postAd_Structure_pricePSeat;
    }

    public Integer getSeats_av() {
        return postAd_Structure_seatsAvailable;
    }

    public String getImage() {
        return postAd_Structure_carImage;
    }

    public String getPostAd_Structure_userID() {
        return postAd_Structure_userID;
    }

    public void setPostAd_Structure_userID(String postAd_Structure_userID) {
        this.postAd_Structure_userID = postAd_Structure_userID;
    }

    public String getPostAd_Structure_info() {
        return postAd_Structure_info;
    }

    public String getGetPostAd_Structure_desc() {
        return postAd_Structure_desc;
    }

    public void setGetPostAd_Structure_desc(String getPostAd_structure_desc) {
        this.postAd_Structure_desc = getPostAd_structure_desc;
    }

}
