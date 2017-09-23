package com.example.hei.food_calories_ranking;

import android.location.Location;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.hei.food_calories_ranking.R.id.current_location_storage;

// Google map android api key : AIzaSyBrl4wNKulb3yoXN_I7jxpALDhrFiVzFg0
// Google map web service api key : AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();
    private String mApiLoc;

    private LocationRequest mLocationRequest;
    private LocationProvider mLocationProvider;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mRequestingLocationUpdates;

    private long UPDATE_INTERVAL = 10 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */


    Food i1 = new Food("牛丼-並盛", "すき家", 650, 350);
    Food i2 = new Food("ねぎ玉牛丼-並盛", "すき家", 768, 470);
    Food i3 = new Food("牛めし-並盛", "松屋", 709, 290);
    Food i4 = new Food("牛焼肉定食", "松屋", 776, 590);
    Food i5 = new Food("豚生姜焼き定食-並盛", "吉野家", 627, 490);
    Food i6 = new Food("秋のベジ牛定食-並盛", "吉野家", 651, 590);

    ArrayList<Food> foods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                        // New location has now been determined
                        // Location Storage
                        String latitude = Double.toString(location.getLatitude());
                        String longitude = Double.toString(location.getLongitude());

                        TextView currentLocationStorage = (TextView) findViewById(current_location_storage);
                        currentLocationStorage.setText(latitude + "," + longitude);
                        Log.v("Tag", "currentLocation =_ " + currentLocationStorage);
                        }
                    }
                });

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());


//        startLocationUpdates();

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


    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

        // https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=35.7890371,139.8959144&radius=500&type=restaurant&keyword=Gyudon%20Restaurant&key=AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY

//        setApiUrl.setText("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
//                + getLatLong.getText() + "&radius=1000&type=restaurant&keyword=Gyudon%20Restaurant&key=AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY");
        // get text in google_api_url.
        mApiLoc = getLatLong.getText().toString();
        return mApiLoc;
    }

    public class GetMapData extends AsyncTask<String, Void, String> {

        String apiResult = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(MainActivity.this, "Json Data is downloading", Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + mApiLoc
                    +"&radius=1000&type=restaurant&keyword=Gyudon%20Restaurant&key=AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY";
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

                        if (name.contains("すき家")){
                            apiResult = apiResult + "すき家";
                        }
                        if (name.contains("松屋")){
                            apiResult = apiResult + "松屋";
                        }
                        if (name.contains("吉野家")){
                            apiResult = apiResult + "吉野家";
                        }

//                        FoodBrandClass restaurant = new FoodBrandClass(name, rating);
//                        restaurantsList.add(restaurant);

                        Log.d("Tag", "forLoop =_ " + name);

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
            return apiResult;
        }

            @Override
            protected void onPostExecute (String apiResult){
//               super.onPostExecute();

                Log.d("Tag", "apiResult =_ " + apiResult);

//                boolean checkSukiya = restaurantsList.toString().contains("すき家");
//                boolean checkMatsuya = restaurantsList.contains("松屋");
//                int arrayListSize = restaurantsList.size();

//                Log.v("Tag", "result =_ contained " + checkSukiya);
//                Log.d("Tag", "result =_ contained " + checkMatsuya);
//                Log.d("Tag", "result =_ Size " + arrayListSize);

//                for (int i = 0; i<arrayListSize; i++){
//                    Log.v ("Tag", "for_loop =_ " + restaurantsList.get(i).getRating()) ;
//                }

                if (apiResult.contains("すき家")) {
                    foods.add(i1);
                    foods.add(i2);
                }

                if (apiResult.contains("松屋")) {
                    foods.add(i3);
                    foods.add(i4);
                }

                if (apiResult.contains("吉野家")) {
                    foods.add(i5);
                    foods.add(i6);
                }

                // Create the adapter to convert the array to views
                FoodAdapter adapter = new FoodAdapter(MainActivity.this, foods);
                // Attach the adapter to a ListView
                ListView listView = (ListView) findViewById(R.id.food_list_view);
                listView.setAdapter(adapter);
            }
    }
}

