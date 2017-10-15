package com.example.hei.food_calories_ranking;


import android.support.annotation.NonNull;

import java.util.Comparator;

public class Food implements Comparator<Food>, Comparable<Food>{

    public Food(){}

    private String mFoodName;
    private String mFoodBrand;
    private int mCalories;
    private int mPrice;
    private int mRating;
    private int mRatingResId;
    private int mImageViewId;

    // Create new Food object.
    public Food(String foodName, String foodBrand, int foodCalories, int foodPrice, int vegRating, int ratingResId,
    int imageViewId) {

        mFoodName = foodName;
        mFoodBrand = foodBrand;
        mCalories = foodCalories;
        mPrice = foodPrice;
        mRating = vegRating;
        mRatingResId = ratingResId;
        mImageViewId = imageViewId;
    }

    public String getFoodName() {return mFoodName;}
    public String getFoodBrand() {return mFoodBrand;}
    public int getFoodCalories() {return mCalories;}
    public int getFoodPrice() {return mPrice;}
    public int getVegRating() {return mRating;}
    public int getRatingResId() {return mRatingResId;}
    public int getImageViewId() {return mImageViewId;}

    @Override
    public int compareTo(@NonNull Food o) {
        return 0;
    }
    // Overriding the compare method to sort the age
    @Override
    public int compare(Food o1, Food o2) {
        return o2.mRating - o1.mRating;
    }
}
