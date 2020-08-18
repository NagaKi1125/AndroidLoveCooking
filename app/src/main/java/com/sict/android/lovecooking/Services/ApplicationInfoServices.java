package com.sict.android.lovecooking.Services;

import com.sict.android.lovecooking.Model.Category;
import com.sict.android.lovecooking.Model.Comment;
import com.sict.android.lovecooking.Model.Dish;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApplicationInfoServices {

    //https://lovecooking.herokuapp.com/api/dishes
    @GET("dishes")
    Call<List<Dish>> getAllDish();

    //https://lovecooking.herokuapp.com/api/dishes/{id}
    @GET("dishes/{id}")
    Call<Dish> getDishInfo(@Path("id") String id);

    //https://lovecooking.herokuapp.com/api/dishes-get-edit/{id}
    @GET("dishes-get-edit/{id}")
    Call<Dish> getDishEditInfo(@Path("id") String id);

    //categories
    //https://lovecooking.herokuapp.com/api/categories
    @GET("categories")
    Call<List<Category>> getCategory();

    @GET("dishes/{id}/comment")
    Call<List<Comment>> getDishComment(@Path("id") String dish_id);


}
