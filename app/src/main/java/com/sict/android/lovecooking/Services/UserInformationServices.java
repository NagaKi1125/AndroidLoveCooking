package com.sict.android.lovecooking.Services;

import com.sict.android.lovecooking.Model.Dish;
import com.sict.android.lovecooking.Model.FollowLikedList;
import com.sict.android.lovecooking.Model.Menu;
import com.sict.android.lovecooking.Model.MenuList;
import com.sict.android.lovecooking.Model.UserInfo;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserInformationServices {

    //user infor
    //http://lovecooking.herokuapp.com/api/user-info
    @GET("user-info")
    Call<UserInfo> getUserInfo(@Header("Authorization") String authToken);

    //user menu List
    //http://lovecooking.herokuapp.com/api/user-menu
    @GET("user-menu")
    Call<List<MenuList>> getMenuList(@Header("Authorization") String authToken);

    @GET("user-menu-spinner")
    Call<List<Menu>> getMenu(@Header("Authorization") String authToken);

    @GET("user-dishes")
    Call<List<Dish>> getUserDishes(@Header("Authorization") String authToken);

    //user follow list and liked dishes list
    //http://lovecooking.herokuapp.com/api/follow-liked
    @GET("follow-liked")
    Call<FollowLikedList> getFollowLikedList(@Header("Authorization") String authToken);

}
