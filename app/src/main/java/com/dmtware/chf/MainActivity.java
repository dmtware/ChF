package com.dmtware.chf;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dmtware.chf.model.Token;
import com.dmtware.chf.network.AppController;
import com.google.gson.Gson;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends ActionBarActivity implements LoginFragment.LoginListener {

    public final String CF_URL = "http://forgedemo.channelforge.com/forgeapi/v1/Identity/SessionToken";
    public final static String EXTRA_MESSAGE = "com.dmtware.chf.MESSAGE";
    private String currentToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_json) {
            Toast.makeText(this, "About what? ;)", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // this gets called by the Login Fragment when the usr clicks button
    @Override
    public void getLoginDetails(String userName, String password) {
        getToken2(userName, password);
    }

    // Login button
    public void getToken(String userName, String password){

       // byte[] loginBytes = ("TomaszUser" + ":" + "Tomcat39").getBytes();

        // generate basic authentication
        byte[] loginBytes = (userName + ":" + password).getBytes();
        final StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        // POST request
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.POST, CF_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Token myToken = new Gson().fromJson(response, Token.class);

                TokenFragment tokenFragment = (TokenFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
                tokenFragment.setTokenText("TOKEN: " + myToken.sessionToken);

                currentToken = myToken.sessionToken;

            }
        // if error
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity.this, "The user name or password is incorrect", Toast.LENGTH_LONG).show();

            }
            // headers
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("CF-VersionInfo", "AppKey=C1A379CE-C0C0-43E9-91C7-9E80F5AC6452;AppVersion=1.0.0;OS=Windows;Platform=DellUA=Stuff");
                params.put("Authorization", loginBuilder.toString());
                return params;
            }
        };
        queue.add(request);
    }

    // Login button
    public void getToken2(String userName, String password){

        // byte[] loginBytes = ("TomaszUser" + ":" + "Tomcat39").getBytes();

        // generate basic authentication
        byte[] loginBytes = (userName + ":" + password).getBytes();
        final StringBuilder loginBuilder = new StringBuilder()
                .append("Basic ")
                .append(Base64.encodeToString(loginBytes, Base64.DEFAULT));

        // POST request

        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        // Progress dialog
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, CF_URL, null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                Token myToken = new Gson().fromJson(String.valueOf(response), Token.class);

                TokenFragment tokenFragment = (TokenFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
                tokenFragment.setTokenText("TOKEN: " + myToken.sessionToken);

                currentToken = myToken.sessionToken;
                pDialog.hide();

            }
            // if error
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pDialog.hide();
                Toast.makeText(MainActivity.this, "The user name or password is incorrect", Toast.LENGTH_LONG).show();
            }
            // headers
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("CF-VersionInfo", "AppKey=C1A379CE-C0C0-43E9-91C7-9E80F5AC6452;AppVersion=1.0.0;OS=Windows;Platform=DellUA=Stuff");
                params.put("Authorization", loginBuilder.toString());
                return params;
            }
        };
        // add to the queue
        AppController.getInstance().addToRequestQueue(request, tag_json_obj);
    }
}