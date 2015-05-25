package com.dmtware.chf;
/*

 */
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dmtware.chf.model.Offer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OffersActivity extends ActionBarActivity {

    public final String CF_URL = "http://forgedemo.channelforge.com/forgeapi/v1/Content/Groups";

    public static String EXTRA_URL = "com.dmtware.chf.MESSAGE";
    public static String EXTRA_ID = "com.dmtware.chf.ID";
    public static String EXTRA_POSITION = "com.dmtware.chf.POSITION";


    String groupUrl, currentGroupName;

    ListView listViewOffers;

    List<Offer> offerList;
    JSONArray jArray;

    ArrayList<String> offers;
    ArrayList<Integer> ids;

    ListAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);

        // initialize widgets
        listViewOffers = (ListView)findViewById(R.id.listViewOffers);

        // get group name
        Intent intent = getIntent();
        currentGroupName = intent.getStringExtra(CatalogActivity.GROUP_NAME);

        // refactor URL
        groupUrl = CF_URL + "/" + currentGroupName + "/Entities?PageIndex=0&PageSize=10";

        // gets offers from the server to the list
        getOffers();

        listViewOffers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showDetails(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_offers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_json) {
            Toast.makeText(this, jArray.toString(), Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Get offers
    public void getOffers(){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                groupUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("On Response", response.toString());

                try {
                    JSONObject jObject = new JSONObject(response.toString());
                    jArray = jObject.getJSONArray("Data");

                    // deserialize generic collection
                    Type listType = new TypeToken<ArrayList<Offer>>() {}.getType();
                    offerList = new Gson().fromJson(String.valueOf(jArray), listType);

                    // string array of offers and links to image
                    offers = new ArrayList<String>();
                    ids = new ArrayList<Integer>();
                    for (int i = 0; i < offerList.size(); i++){
                        offers.add(offerList.get(i).toString());
                        ids.add(offerList.get(i).getId());
                    }

                    // add friendly names to the listview
                    //ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(OffersActivity.this, R.layout.row_layout, offers);

                    itemsAdapter = new OffersAdapter(OffersActivity.this, offers);
                    listViewOffers.setAdapter(itemsAdapter);

                    //urlTextView.setText(jArray.toString());
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

    // displays detail activity
    private void showDetails(int position) {
        // get the offer id
        String offerId = "" + ids.get(position);

        // create intent and pass group name
        Intent showOfferDetailIntent = new Intent(this, OfferDetailActivity.class);
        showOfferDetailIntent.putExtra(EXTRA_URL, groupUrl);
        showOfferDetailIntent.putExtra(EXTRA_ID, offerId);
        showOfferDetailIntent.putExtra(EXTRA_POSITION, ""+position);
        startActivity(showOfferDetailIntent);
    }
}