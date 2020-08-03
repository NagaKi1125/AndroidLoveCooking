package com.sict.android.lovecooking.Remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    //private static final String BASE_URL = "http://192.168.43.129:8000/api/";
    private static final String BASE_URL = "http://192.168.0.101:8000/api/";
    //private static final String BASE_URL = "http://lovecooking.herokuapp.com/api/";

    public static Retrofit getRetrofit(){
        if(retrofit == null){
            retrofit = new retrofit2.Retrofit.Builder()
                            .baseUrl(BASE_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
        }
        return retrofit;
    }
}
