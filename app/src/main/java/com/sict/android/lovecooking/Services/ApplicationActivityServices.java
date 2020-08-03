package com.sict.android.lovecooking.Services;

import com.sict.android.lovecooking.Model.Comment;
import com.sict.android.lovecooking.Model.LoginModel;
import com.sict.android.lovecooking.Model.LoginResponse;
import com.sict.android.lovecooking.Model.SearchFilter;
import com.sict.android.lovecooking.Model.User;
import com.sict.android.lovecooking.Model.UserAuth;
import com.sict.android.lovecooking.Model.UserCreate;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApplicationActivityServices  {

    //https://lovecooking.herokuapp.com/api/login
    @POST("login")
    Call<LoginResponse> getLogin(@Body LoginModel loginModel);

    //https://lovecooking.herokuapp.com/api/auth
    @GET("auth")
    Call<UserAuth> getAuth(@Header("Authorization") String authToken);

    //https://lovecooking.herokuapp.com/api/register
    @POST("register")
    Call<User> getRegister(@Body UserCreate userCreate);

    @GET("search")
    Call<List<SearchFilter>> getSearchResult(@Query("data") String searchData);

}
