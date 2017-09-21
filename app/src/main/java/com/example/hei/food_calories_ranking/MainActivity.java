package com.example.hei.food_calories_ranking;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.hei.food_calories_ranking.R.id.current_location_storage;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

// Google map android api key : AIzaSyBrl4wNKulb3yoXN_I7jxpALDhrFiVzFg0
// Google map web service api key : AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String mApiLoc;
    ArrayList<FoodBrandClass> restaurantsList = new ArrayList<>();
    public String resName;

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    Food i1 = new Food("牛丼-並盛", "すき家", 650, 350);
    Food i2 = new Food("ねぎ玉牛丼-並盛", "すき家", 768, 470);
    Food i3 = new Food("牛めし-並盛", "松屋", 709, 290);
    Food i4 = new Food("牛焼肉定食", "松屋", 776, 590);

    ArrayList<Food> foods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startLocationUpdates();

//        // Construct the data source
//        final ArrayList<Food> foods = new ArrayList<>();
//        foods.add(new Food("鮭おにぎり", "Supermarket", 150, 110));
//        foods.add(new Food("親子丼", "Supermarket", 800, 338));
//        foods.add(new Food("牛丼-並盛", "すき家", 650, 350));
//        foods.add(new Food("ねぎ玉牛丼-並盛", "すき家", 768, 470));
//        foods.add(new Food("グラン クラブハウス", "Macdonald", 516, 490));

//
//
//        // Create the adapter to convert the array to views
//        FoodAdapter adapter = new FoodAdapter(this, foods);
//        // Attach the adapter to a ListView
//        ListView listView = (ListView) findViewById(R.id.food_list_view);
//        listView.setAdapter(adapter);

        handler.postDelayed(runnable, 2 * 1000);

    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    Handler handler = new Handler();

    public final Runnable runnable = new Runnable() {

        @Override
        public void run() {
            getApiLoc();
            new GetMapData().execute();
            Log.v("Tag", "mApiLoc =_ " + mApiLoc);


        }
    };

    public String getApiLoc() {
        //Constructor of current location storage.
        TextView getLatLong = (TextView) findViewById(current_location_storage);
        TextView setApiUrl = (TextView) findViewById(R.id.google_api_url);

        // https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=35.7890371,139.8959144&radius=500&type=restaurant&keyword=Gyudon%20Restaurant&key=AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY

        setApiUrl.setText("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
                + getLatLong.getText() + "&radius=500&type=restaurant&keyword=Gyudon%20Restaurant&key=AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY");
        // get text in google_api_url.
        mApiLoc = setApiUrl.getText().toString();
        return mApiLoc;
    }


    // Trigger new location updates at interval
    protected void startLocationUpdates() {

        // Create the location request to start receiving updates
        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        // Create LocationSettingsRequest object using location request
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        // Check whether location settings are satisfied
        // https://developers.google.com/android/reference/com/google/android/gms/location/SettingsClient
        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        settingsClient.checkLocationSettings(locationSettingsRequest);

        // new Google API SDK v11 uses getFusedLocationProviderClient(this)
        getFusedLocationProviderClient(this).requestLocationUpdates(mLocationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        // do work here
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }

    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        // Location Storage
        String latitude = Double.toString(location.getLatitude());
        String longitude = Double.toString(location.getLongitude());

        TextView currentLocationStorage = (TextView) findViewById(current_location_storage);
        currentLocationStorage.setText(latitude + "," + longitude);
        Log.v("Tag", "currentLocation =_ " + currentLocationStorage);
    }

    public class GetMapData extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = mApiLoc;
            String jsonStr = sh.makeServiceCall(url);


            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON Array node
                    JSONArray jsonResult = jsonObj.getJSONArray("results");

                    // looping through All Contacts
                    for (int i = 0; i < jsonResult.length(); i++) {
                        JSONObject currentData = jsonResult.getJSONObject(i);
                        String name = currentData.getString("name");
                        String rating = currentData.getString("rating");

//                        FoodBrandClass restaurant = new FoodBrandClass(name, rating);
//
//                        restaurantsList.add(restaurant);
                        resName = name;
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
            return resName;
        }

            @Override
            protected void onPostExecute (String resName){
//               super.onPostExecute();
//            ListAdapter adapter = new SimpleAdapter(MainActivity.this, restaurantsList,
//                    R.layout.main_list_item, new String[]{ "name","rating"},
//                    new int[]{R.id.food_name, R.id.food_brand});
//            listView.setAdapter(adapter);

                boolean checkSukiya = resName.contains("すき家");

                if (checkSukiya) {
                    foods.add(i1);
                    foods.add(i2);
                    Log.v("Tag", "result =_ contained" + resName);

                }

                // Create the adapter to convert the array to views
                FoodAdapter adapter = new FoodAdapter(MainActivity.this, foods);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.food_list_view);
                listView.setAdapter(adapter);

//
//                Log.v("Tag", "result =_ " + restaurantsList);
            }
        }
    }


