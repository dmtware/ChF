package com.dmtware.chf;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.dmtware.chf.network.AppController;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by tzay on 24/05/2015.
 */
public class OffersAdapter extends ArrayAdapter<String> {

    public OffersAdapter(Context context, ArrayList<String> offers) {
        super(context, R.layout.row_offer, offers);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View theView = theInflater.inflate(R.layout.row_offer, parent, false);

        // get string and extract name and img url
        String offer = getItem(position);
        int index = offer.indexOf("|");
        String offerName = offer.substring(0, (index - 1));
        String offerImage = offer.substring(index + 3, offer.indexOf(","));

        Log.i("My Log", offerImage);

        TextView theTextView = (TextView) theView.findViewById(R.id.textOffer);

        theTextView.setText(offerName);

        // img
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView imgNetWorkView = (NetworkImageView) theView.findViewById(R.id.image);
        imgNetWorkView.setImageUrl(offerImage, imageLoader);

        return theView;
    }
}
