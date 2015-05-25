package com.dmtware.chf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class OfferDetailActivity extends ActionBarActivity {

    JSONArray jArray;
    JSONObject pickedOffer;

    String groupUrl, offerId, positionStr;

    int position;

    TextView textViewDetailId, getTextViewDetailName;
    ImageView imageViewDetil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detail);

        // initialize views
        textViewDetailId = (TextView)findViewById(R.id.textViewDetailId);
        getTextViewDetailName = (TextView)findViewById(R.id.textViewDetailName);
        imageViewDetil = (ImageView)findViewById(R.id.imageViewDetail);

        // get data
        Intent intent = getIntent();
        groupUrl = intent.getStringExtra(OffersActivity.EXTRA_URL);
        offerId = intent.getStringExtra(OffersActivity.EXTRA_ID);
        positionStr = intent.getStringExtra(OffersActivity.EXTRA_POSITION);

        position = Integer.parseInt(positionStr);

        System.out.println(positionStr);

        getOfferDetails();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offer_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, pickedOffer.toString(), Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Get offer details
    public void getOfferDetails(){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                groupUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("On Response", response.toString());

                try {
                    JSONObject jObject = new JSONObject(response.toString());
                    jArray = jObject.getJSONArray("Data");

                    pickedOffer = (JSONObject) jArray.get(position);
                    System.out.println(pickedOffer.toString());

                    int id = pickedOffer.getInt("ID");
                    String name = pickedOffer.getString("Name");

                    textViewDetailId.setText("" + id);
                    getTextViewDetailName.setText(name);

                    // get image url
                    JSONArray mediaArray = pickedOffer.getJSONArray("Media");
                    JSONObject mediaObject = mediaArray.getJSONObject(0);
                    String imgUrl = mediaObject.getString("Resource");

                    System.out.println(imgUrl);

                    new ImageDownloader(imageViewDetil).execute(imgUrl);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("CF-VersionInfo", "AppKey=C1A379CE-C0C0-43E9-91C7-9E80F5AC6452;AppVersion=1.0.0;OS=Windows;Platform=DellUA=Stuff");
                params.put("CF-Project", "Tomasz");
                params.put("Accept-Language", "en-US,en;q=0.8");
                return params;
            }
        };
        queue.add(jsonObjReq);
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
