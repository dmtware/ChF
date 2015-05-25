package com.dmtware.chf;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.dmtware.chf.model.Data;
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


public class CatalogActivity extends ActionBarActivity {

    ListView listView;
    List<String> groups;

    List<Data> dataList;

    public final String CF_URL = "http://forgedemo.channelforge.com/forgeapi/v1/Content/Groups";
    public static String GROUP_NAME = "com.dmtware.chf.GROUP";
    public static String URL_GROUP = "com.dmtware.chf.URL";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // initialize listview
        listView = (ListView)findViewById(R.id.listView);

        //Get data
        getMediaGroups();

        // listview on click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showOffers(position);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_json) {
            Toast.makeText(this, dataList.toString(), Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Get media groups
    public void getMediaGroups(){
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                CF_URL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d("On Response", response.toString());

                try {
                    JSONObject jObject = new JSONObject(response.toString());
                    JSONArray jArray = jObject.getJSONArray("Data");

                    // deserialize generic collection
                    Type listType = new TypeToken<ArrayList<Data>>() {}.getType();
                    dataList = new Gson().fromJson(String.valueOf(jArray), listType);

                    // get friendly names of the groups for the list
                    groups = new ArrayList<String>();
                    for (int i = 0; i < dataList.size(); i++){
                        groups.add(dataList.get(i).getFriendlyName());
                    }
                    // add friendly names to the listview
                    ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(CatalogActivity.this, R.layout.row_layout, groups);
                    listView.setAdapter(itemsAdapter);

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
    // displays the content of the media group category
    private void showOffers(int position){

        // get the group name
        String groupName = dataList.get(position).getName();

        // create intent and pass group name
        Intent showOffersIntent = new Intent(this, OffersActivity.class);
        showOffersIntent.putExtra(GROUP_NAME, groupName);
        startActivity(showOffersIntent);
    }
}