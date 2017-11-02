package com.example.hei.food_calories_ranking;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.hei.food_calories_ranking.MainActivity.mApiLoc;
import static com.example.hei.food_calories_ranking.MainActivity.userSetDistance;


public class MapApiLoader extends AsyncTaskLoader<List<String>> {

    private String TAG = MapApiLoader.class.getSimpleName();

    public static List<String> mapApiRestaurant = new ArrayList<>();
    /** Query URL */
    private String mUrl;

    public MapApiLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<String> loadInBackground() {
        Log.v("TAG", "Loader load in background =_ ");
        HttpHandler sh = new HttpHandler();

        // Making a request to url and getting response
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + mApiLoc +
                "&radius=" + userSetDistance + "&type=restaurant&keyword=fastfood&Gyudon&key=AIzaSyAgE1lUCVlNpi7OyTG6sUzd-CKN-nPeanY";
        String jsonStr = sh.makeServiceCall(url);

        Log.e(TAG, "Response from url: " + jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                // Getting JSON Array node
                JSONArray jsonResult = jsonObj.getJSONArray("results");
                // looping through All JSON objects
                for (int i = 0; i < jsonResult.length(); i++) {
                    JSONObject currentData = jsonResult.getJSONObject(i);
                    String name = currentData.getString("name");
                    Log.v(TAG, "currentData name =_ " + name);

                    if (name.contains("すき家")) {
                        mapApiRestaurant.add("すき家");
                    }
                    if (name.contains("松屋")) {
                        mapApiRestaurant.add("松屋");
                    }
                    if (name.contains("吉野家")) {
                        mapApiRestaurant.add("吉野家");
                    }
                    Log.v(TAG, "mapApiRestaurant :=_ " + mapApiRestaurant);
                }
            } catch (final JSONException e) {
                Log.e(TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(TAG, "Couldn't get json from server.");
        }
        return mapApiRestaurant;
    }
}
