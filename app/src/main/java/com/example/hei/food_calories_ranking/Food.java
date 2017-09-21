package com.example.hei.food_calories_ranking;


public class Food {

    public Food(){}

    private String mFoodName;
    private String mFoodBrand;
    private int mCalories;
    private int mPrice;

    // Create new Food object.
    public Food(String foodName, String foodBrand, int foodCalories, int foodPrice) {

        mFoodName = foodName;
        mFoodBrand = foodBrand;
        mCalories = foodCalories;
        mPrice = foodPrice;
    }

    public String getFoodName() {return mFoodName;}
    public String getFoodBrand() {return mFoodBrand;}
    public int getFoodCalories() {return mCalories;}
    public int getFoodPrice() {return mPrice;}
}
