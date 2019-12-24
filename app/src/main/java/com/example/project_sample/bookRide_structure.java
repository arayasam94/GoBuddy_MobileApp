package com.example.project_sample;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class bookRide_structure {

    private String bookRide_Structure_postID;
    private String bookRide_Structure_source;
    private String bookRide_Structure_destination;
    private String bookRide_Structure_date;
    private String bookRide_Structure_startTime;
    private Float bookRide_Structure_paid;
    private Integer bookRide_Structure_seatsBooked;
    private String bookRide_Structure_carImage;
    private String bookRide_Structure_userID;
    private String bookRide_Structure_info;
    private String bookRide_Structure_driverID;
    private String bookRide_Structure_description;

    public bookRide_structure() {
    }


    public bookRide_structure(String postID, String info, String source, String destination, String date, String startTime, Float price, Integer seats_booked, String image, String uid, String driverID, String desc) {
        this.bookRide_Structure_postID = postID;
        this.bookRide_Structure_info = info;
        this.bookRide_Structure_source = source;
        this.bookRide_Structure_destination = destination;
        this.bookRide_Structure_date = date;
        this.bookRide_Structure_startTime = startTime;
        this.bookRide_Structure_userID = uid;
        this.bookRide_Structure_driverID = driverID;
        this.bookRide_Structure_carImage = image;
        this.bookRide_Structure_paid = price;
        this.bookRide_Structure_seatsBooked = seats_booked;
        this.bookRide_Structure_description = desc;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("source", bookRide_Structure_source);
        result.put("destination", bookRide_Structure_destination);
        result.put("postID", bookRide_Structure_postID);
        result.put("date_journey", bookRide_Structure_date);
        result.put("paid", bookRide_Structure_paid);
        result.put("time_journey", bookRide_Structure_startTime);
        result.put("seatsBooked", bookRide_Structure_seatsBooked);
        result.put("image", bookRide_Structure_carImage);
        result.put("userID", bookRide_Structure_userID);
        result.put("info", bookRide_Structure_info);
        result.put("driverID", bookRide_Structure_driverID);
        result.put("description", bookRide_Structure_description);

        return result;
    }


    public String getBookRide_Structure_postID() {
        return bookRide_Structure_postID;
    }

    public void setBookRide_Structure_postID(String bookRide_Structure_postID) {
        this.bookRide_Structure_postID = bookRide_Structure_postID;
    }

    public String getBookRide_Structure_source() {
        return bookRide_Structure_source;
    }

    public void setBookRide_Structure_source(String bookRide_Structure_source) {
        this.bookRide_Structure_source = bookRide_Structure_source;
    }

    public String getBookRide_Structure_destination() {
        return bookRide_Structure_destination;
    }

    public void setBookRide_Structure_destination(String bookRide_Structure_destination) {
        this.bookRide_Structure_destination = bookRide_Structure_destination;
    }

    public String getBookRide_Structure_date() {
        return bookRide_Structure_date;
    }

    public void setBookRide_Structure_date(String bookRide_Structure_date) {
        this.bookRide_Structure_date = bookRide_Structure_date;
    }

    public String getBookRide_Structure_startTime() {
        return bookRide_Structure_startTime;
    }

    public void setBookRide_Structure_startTime(String bookRide_Structure_startTime) {
        this.bookRide_Structure_startTime = bookRide_Structure_startTime;
    }

    public Float getBookRide_Structure_paid() {
        return bookRide_Structure_paid;
    }

    public void setBookRide_Structure_paid(Float bookRide_Structure_paid) {
        this.bookRide_Structure_paid = bookRide_Structure_paid;
    }

    public Integer getBookRide_Structure_seatsBooked() {
        return bookRide_Structure_seatsBooked;
    }

    public void setBookRide_Structure_seatsBooked(Integer bookRide_Structure_seatsBooked) {
        this.bookRide_Structure_seatsBooked = bookRide_Structure_seatsBooked;
    }

    public String getBookRide_Structure_carImage() {
        return bookRide_Structure_carImage;
    }

    public void setBookRide_Structure_carImage(String bookRide_Structure_carImage) {
        this.bookRide_Structure_carImage = bookRide_Structure_carImage;
    }

    public String getBookRide_Structure_userID() {
        return bookRide_Structure_userID;
    }

    public void setBookRide_Structure_userID(String bookRide_Structure_userID) {
        this.bookRide_Structure_userID = bookRide_Structure_userID;
    }

    public String getBookRide_Structure_info() {
        return bookRide_Structure_info;
    }

    public void setBookRide_Structure_info(String bookRide_Structure_info) {
        this.bookRide_Structure_info = bookRide_Structure_info;
    }


    public String getBookRide_Structure_driverID() {
        return bookRide_Structure_driverID;
    }

    public void setBookRide_Structure_driverID(String getBookRide_Structure_driverID) {
        this.bookRide_Structure_driverID = getBookRide_Structure_driverID;
    }

    public String getBookRide_Structure_description() {
        return bookRide_Structure_description;
    }

    public void setBookRide_Structure_description(String bookRide_Structure_description) {
        this.bookRide_Structure_description = bookRide_Structure_description;
    }


}

