package com.example.hei.food_calories_ranking;


public class FoodBrandClass {

    public FoodBrandClass(){}

    private String mFoodBrand;
    private String mRating;

    public FoodBrandClass(String FoodBrand, String rating){
        FoodBrand = mFoodBrand;
        rating = mRating;
    }

    public String getFoodBrand(){ return mFoodBrand;}
    public String getRating() { return mRating;}
}
