package com.sict.android.lovecooking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.sict.android.lovecooking.Model.LoginModel;
import com.sict.android.lovecooking.Model.LoginResponse;
import com.sict.android.lovecooking.Model.UserAuth;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.ApplicationActivityServices;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {

    private EditText email,password;
    private MaterialButton btnLogin,btnRegister;
    ApplicationActivityServices applicationActivityServices ;
    private String url;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor ;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getElementId();
        applicationActivityServices = RetrofitClient.getRetrofit().create(ApplicationActivityServices.class);
        sharedPreferences = getApplicationContext().getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        url = sharedPreferences.getString("url","http://192.168.0.101:8000/");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                finish();startActivity(intent);
            }
        });
    }

    private void login() {
        String email_login = email.getText().toString();
        String password_login = password.getText().toString();
        Call<LoginResponse> loginCall = applicationActivityServices.getLogin(new LoginModel(email_login,password_login));
        loginCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    Toast.makeText(LoginActivity.this,
                            "Login successfully\nGetting user information",
                            Toast.LENGTH_LONG).show();
                    editor.putString("token",response.body().getToken());
                    editor.apply();
                    String token = response.body().getToken();
                    authorizationUser(token);
                }else{
                    Toast.makeText(LoginActivity.this,
                            "Login information not correct",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Serve could not response",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void authorizationUser(String token) {
        Call<UserAuth> userAuthCall = applicationActivityServices.getAuth("Bearer "+token);
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

                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        startActivity(intent);finish();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Token is not correct\nCheck your login information and try again\n"
                            +response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserAuth> call, Throwable t) {
                Toast.makeText(LoginActivity.this,
                        "Serve could not response" +
                                "\nCould not authorization login activity",
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getElementId() {
        email = (EditText)findViewById(R.id.email);
        password = (EditText)findViewById(R.id.password);
        btnLogin = (MaterialButton)findViewById(R.id.btnlogin);
        btnRegister = (MaterialButton)findViewById(R.id.register);
    }

}