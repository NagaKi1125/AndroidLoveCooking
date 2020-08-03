package com.sict.android.lovecooking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sict.android.lovecooking.Model.UserAuth;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.ApplicationActivityServices;
import com.sict.android.lovecooking.Services.UserInformationServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApplicationActivityServices applicationActivityServices;
    private ProgressBar progressBar;
    Handler handler;
    private TextView tv_work;


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences = this.getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        applicationActivityServices = RetrofitClient.getRetrofit().create(ApplicationActivityServices.class);
        getElementID();
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new CheckTokenForLogin().execute();
            }
        },3000);

    }

    @SuppressLint("StaticFieldLeak")
    private class CheckTokenForLogin extends AsyncTask<Void,String,Void>{
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            Call<UserAuth> userAuthCall = applicationActivityServices.getAuth(
                    "Bearer "+sharedPreferences.getString("token","null"));
            userAuthCall.enqueue(new Callback<UserAuth>() {
                @Override
                public void onResponse(Call<UserAuth> call, Response<UserAuth> response) {
                    if(response.isSuccessful()){
                        if(response.body().getLevel() == 0){
                            Toast.makeText(getApplicationContext(),
                                    "Your account have been disabled\n" +
                                            "Please contact admin to have furthor information ",
                                    Toast.LENGTH_LONG).show();
                        }else{
                            editor.putString("full_name",response.body().getName());
                            editor.putString("user_name",response.body().getUsername());
                            editor.putInt("id",response.body().getId());
                            editor.putInt("level",response.body().getLevel());
                            editor.putString("dish_liked",response.body().getDishLiked());
                            editor.putString("follow",response.body().getFollow());
                            editor.apply();
                            tv_work.setText("Xin chào "+response.body().getName()+"\nĐang chuyển hướng");

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(SplashActivity.this,MainActivity.class);
                                    startActivity(intent);finish();
                                }
                            },2000);

                        }
                    }else{
                        tv_work.setText("Chuyển đến đăng nhập");

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                                startActivity(intent);finish();
                            }
                        },1000);
                    }
                }

                @Override
                public void onFailure(Call<UserAuth> call, Throwable t) {
                    Toast.makeText(SplashActivity.this,
                            "Serve could not response",
                            Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(SplashActivity.this,LoginActivity.class);
                    startActivity(intent);finish();
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    private void getElementID(){
        progressBar = (ProgressBar)findViewById(R.id.progress);
        tv_work = (TextView)findViewById(R.id.progress_work);
    }
}