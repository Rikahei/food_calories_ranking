package com.example.hei.food_calories_ranking;

import android.Manifest;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.hei.food_calories_ranking.MapApiLoader.mapApiRestaurant;
import static com.example.hei.food_calories_ranking.R.id.current_location_storage;
import static com.google.android.gms.location.LocationServices.getFusedLocationProviderClient;

// Google map android api key : AIzaSyBrl4wNKulb3yoXN_I7jxpALDhrFiVzFg0
// Google map web service api key : AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY
// https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=35.7890371,139.8959144
// &radius=500&type=restaurant&keyword=Gyudon%20Restaurant&key=AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY

public class MainActivity extends AppCompatActivity implements LoaderCallbacks <List<String>>{

    private String TAG = MainActivity.class.getSimpleName();
    // Variable for api location.
    public static String mApiLoc;
    // Variable for SeekBar set distance.
    public static String userSetDistance;
    // Variable for SeekBar
    private SeekBar seekBar;
    private TextView seekBarValue;

    // Location request variable
    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    private String MAP_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + mApiLoc +
            "&radius=" + userSetDistance + "&type=restaurant&keyword=fastfood&Gyudon&key=AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY";

    // Arraylist constructor for calories data.
    Food sukiya_i1 = new Food("牛丼-並盛", "すき家", 650, 350, 0, R.drawable.dish_ratio_00, R.drawable.gyuu_don_001);
    Food sukiya_i2 = new Food("ねぎ玉牛丼-並盛", "すき家", 768, 470, 30, R.drawable.dish_ratio_30, R.drawable.gyuu_don_002);
    Food sukiya_i3 = new Food("豚丼-並盛", "すき家", 700, 4350, 0, R.drawable.dish_ratio_30, R.drawable.gyuu_don_004);
    Food matsuya_i1 = new Food("牛めし-並盛", "松屋", 709, 290, 0, R.drawable.dish_ratio_00, R.drawable.gyuu_don_001);
    Food matsuya_i2 = new Food("牛焼肉定食", "松屋", 776, 590, 30, R.drawable.dish_ratio_30, R.drawable.gyuu_don_003);
    Food matsuya_i3 = new Food("豚バラ生姜焼定食-並盛", "松屋", 835, 590, 30, R.drawable.dish_ratio_30, R.drawable.gyuu_don_003);
    Food yoshinoya_i1 = new Food("豚生姜焼き定食-並盛", "吉野家", 627, 490, 30, R.drawable.dish_ratio_30 , R.drawable.gyuu_don_003);
    Food yoshinoya_i2 = new Food("秋のベジ牛定食-並盛", "吉野家", 651, 590, 50, R.drawable.dish_ratio_50, R.drawable.gyuu_don_s001);
    Food yoshinoya_i3 = new Food("牛丼-並盛", "吉野家", 669, 380, 0, R.drawable.dish_ratio_00, R.drawable.gyuu_don_001);

    // Arraylist constructor for array adapter.
    ArrayList<Food> foods = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createLocationRequest();

        seekBar = (SeekBar) findViewById(R.id.distance_seekbar);
        seekBarValue = (TextView) findViewById(R.id.seekbar_textview);
        seekBar.setOnSeekBarChangeListener( new OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mapApiRestaurant.clear();
                foods.clear();
                // Get user location
                getApiLoc();
                // Get user set distance
                getUserSetDistance();
                // Force load MapApiLoader
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(0, null, MainActivity.this);
                loaderManager.initLoader(0, null, MainActivity.this).forceLoad();
            }
        });

        // Get last know location
        mFusedLocationClient = getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
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
                        }
                    }
                });

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        handler.postDelayed(runnable, 1 * 1000);
    }

    protected void createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    // Delay run handler
    Handler handler = new Handler();
    public final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            // Clear arraylist
            mapApiRestaurant.clear();
            foods.clear();
            // Get user location
            getApiLoc();
            // Get user set distance
            getUserSetDistance();
            // process AsyncTask to get api information
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(0, null, MainActivity.this).forceLoad();
        }
    };

    // get user location method
    public String getApiLoc() {
        //Constructor of current location storage.
        TextView getLatLong = (TextView) findViewById(current_location_storage);
        mApiLoc = getLatLong.getText().toString();
        return mApiLoc;
    }

    // get user spinner distance
    public String getUserSetDistance() {
        TextView seekbarDistance = (TextView) findViewById(R.id.seekbar_textview);
        userSetDistance = seekbarDistance.getText().toString();
        return userSetDistance;
    }

    @Override
    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
        Log.v("TAG", "Loader on createloader =_ ");
        return new MapApiLoader(this, MAP_API_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<String>> loader, List<String> mapApiRestaurant) {

            // Algorithms for update UI
            if (mapApiRestaurant.contains("すき家")) {
                foods.add(sukiya_i1);
                foods.add(sukiya_i2);
                foods.add(sukiya_i3);
            }
            if (mapApiRestaurant.contains("松屋")) {
                foods.add(matsuya_i1);
                foods.add(matsuya_i2);
                foods.add(matsuya_i3);
            }

            if (mapApiRestaurant.contains("吉野家")) {
                foods.add(yoshinoya_i1);
                foods.add(yoshinoya_i2);
                foods.add(yoshinoya_i3);
            }
        Log.v(TAG, "onLoadFinisher=_ " );

        // Sorts the array list
        Collections.sort(foods);
        // Sorts the array list using comparator
        Collections.sort(foods, new Food());

        // Create the adapter to convert the array to views
        FoodAdapter adapter = new FoodAdapter(MainActivity.this, foods);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.food_list_view);
        listView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<List<String>> loader) {
        // Clear arraylist
        mapApiRestaurant.clear();
        foods.clear();
        Log.v(TAG, "onLoaderReset=_ " );
    }
}


