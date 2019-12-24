package com.example.project_sample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
/*to populate rides fragments*/
public class RidesView extends RecyclerView.ViewHolder {
    View searchView;
    TextView searchSrc;
    TextView searchDestination;
    TextView search_date;
    TextView searchSP;
    ImageView carImage;
    TextView driver;



    public RidesView(View itemView) {
        super(itemView);
        this.searchView = itemView;
        searchSrc = (TextView) itemView.findViewById(R.id.r_source);
        searchDestination = (TextView) itemView.findViewById(R.id.r_destination);
        searchSP = (TextView) itemView.findViewById(R.id.r_price);
        search_date = (TextView) itemView.findViewById(R.id.r_date);
        carImage = (ImageView) itemView.findViewById(R.id.r_image);
        driver=(TextView) itemView.findViewById(R.id.r_user);
    }
public  void  setDriver(String driverName){driver.setText(driverName);}
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

}