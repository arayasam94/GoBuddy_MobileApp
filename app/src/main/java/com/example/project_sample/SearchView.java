package com.example.project_sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchView extends RecyclerView.ViewHolder {
    View searchView;
    TextView searchSrc;
    TextView searchDestination;
    TextView search_date;
    TextView searchSP;
    ImageView carImage;
    TextView seats_avail;


    public SearchView(View itemView) {

        super(itemView);
        this.searchView = itemView;
        searchSrc = (TextView) itemView.findViewById(R.id.search_source);
        searchDestination = (TextView) itemView.findViewById(R.id.search_destination);
        searchSP = (TextView) itemView.findViewById(R.id.search_price);
        search_date = (TextView) itemView.findViewById(R.id.search_date);
        carImage = (ImageView) itemView.findViewById(R.id.search_image);
        seats_avail=(TextView) itemView.findViewById(R.id.search_seatcount);


    }


    public void setSource(String source) {
        searchSrc.setText(source + "");
    }

    public void setDestination(String dest) {
        searchDestination.setText(dest);
    }

    public void setPrice(Float price) {
        searchSP.setText(price.toString());
    }

    public void setDate(String date) {
        search_date.setText(date);
    }

    public void setImage(String image) {
        byte[] encodeByte = Base64.decode(image, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        carImage.setImageBitmap(bitmap);
    }
    public void setSeatsCount(Integer seatsCount) {
        seats_avail.setText(seatsCount.toString());
    }

}