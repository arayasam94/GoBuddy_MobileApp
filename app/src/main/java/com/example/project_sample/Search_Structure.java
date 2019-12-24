package com.example.project_sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.util.Date;

public class Search_Structure {

    private String source, destination;
    private Float price;
    private String date;
    private String image;
    private Integer seats_avail;
    private String postID;
    private String info;
    private String startTime;
    private String driverID;
    private String description;

    public Search_Structure() {

    }
    public Search_Structure(String postID, String info, String source, String destination, String date, String startTime, Float price, Integer seats_av, String image, String driverID, String desc) {

        this.source = source;
        this.destination = destination;
        this.price = price;
        this.date = date;
        this.image=image;
        this.info=info;
        this.seats_avail=seats_av;
        this.postID=postID;
        this.driverID=driverID;
        this.startTime=startTime;
        this.description=desc;

    }

    public Integer getSeats_avail() {
        return seats_avail;
    }

    public void setSeats_avail(Integer seats_avail) {
        this.seats_avail = seats_avail;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getDriverID() {
        return driverID;
    }

    public void setDriverID(String uid) {
        this.driverID = uid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
