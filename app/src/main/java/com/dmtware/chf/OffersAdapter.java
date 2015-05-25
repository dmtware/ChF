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

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by tzay on 24/05/2015.
 */
public class OffersAdapter extends ArrayAdapter<String> {

    public OffersAdapter(Context context, ArrayList<String> offers) {
        super(context, R.layout.row_offer ,offers);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View theView = theInflater.inflate(R.layout.row_offer, parent, false);

        String offer = getItem(position);

        int index = offer.indexOf("|");

        String offerName = offer.substring(0, (index - 1));

        String offerImage = offer.substring(index+3, offer.indexOf(","));

        Log.i("My Log", offerImage);

        TextView theTextView = (TextView) theView.findViewById(R.id.textOffer);

        theTextView.setText(offerName);

        ImageView theImageView = (ImageView) theView.findViewById(R.id.image);

        new ImageDownloader(theImageView).execute(offerImage);

        return theView;
    }

    class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public ImageDownloader(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
