package com.sict.android.lovecooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sict.android.lovecooking.Adapter.FollowListAdapter;
import com.sict.android.lovecooking.Model.FollowLikedList;
import com.sict.android.lovecooking.Model.User;
import com.sict.android.lovecooking.Model.UserInfo;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.UserActivityServices;
import com.sict.android.lovecooking.Services.UserInformationServices;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoActivity extends AppCompatActivity {

    RecyclerView followRecyclerView;
    FollowListAdapter followListAdapter;
    private TextView name,username,gender,birth,address,level,join;
    private CircleImageView avatar;
    //private String url = "http://192.168.43.129:8000/";
    private String url = "http://192.168.0.101:8000/";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    UserInformationServices userInformationServices;
    UserActivityServices userActivityServices;
    private String token,UserFullname = "Love Cooking";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        getElementId();
        sharedPreferences = this.getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userInformationServices = RetrofitClient.getRetrofit().create(UserInformationServices.class);
        userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);

        token = "Bearer "+ sharedPreferences.getString("token","null");

        followListAdapter = new FollowListAdapter();
        followRecyclerView.setHasFixedSize(true);
        followRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getUserInfoCall();
        getUserFollowCall();

        //create action bar menu
        getSupportActionBar();
        getSupportActionBar().setTitle(UserFullname);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_sharp_home_white_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.user_toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Intent intent = new Intent(UserInfoActivity.this,MainActivity.class);
                finish();startActivity(intent);
                return true;
            case R.id.acton_edit_info:
                Intent intent1 = new Intent(UserInfoActivity.this,UserInfoEditActivity.class);
                finish();startActivity(intent1);
                return true;
            case R.id.action_edit_password:
                alertDialogChangePassword();
                return true;
            case R.id.action_logout:
                logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void alertDialogChangePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đổi mật khẩu mới");
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_change_password,null,false);
        EditText oldpass = view.findViewById(R.id.oldPass);
        EditText newpass = view.findViewById(R.id.newPass);
        EditText re_newpass = view.findViewById(R.id.re_newPass);
        builder.setView(view);
        builder.setPositiveButton("Đổi mật khẩu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(newpass.getText().toString().equals(re_newpass.getText().toString())){
                    changePassword(re_newpass.getText().toString(),oldpass.getText().toString());
                }else{
                    Toast.makeText(UserInfoActivity.this,
                            "Nhập lại mật khẩu mới không khớp",
                            Toast.LENGTH_LONG).show();
                }
            }
        }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void changePassword(String newPass, String oldPass) {
        Call<ResponseBody> changePassword = userActivityServices.changePassword(token,newPass,oldPass);
        changePassword.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(UserInfoActivity.this,
                            "Password changed successfully\n"+
                            "Please re-login",
                            Toast.LENGTH_LONG).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(UserInfoActivity.this,LoginActivity.class);
                            finish();startActivity(intent);
                        }
                    },2000);
                }else{
                    Toast.makeText(UserInfoActivity.this,
                            "Change password failed \n"+
                            "Please try again later",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this,
                        "Serve could not response\n"+t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void logout() {
        Call<ResponseBody> userLogout = userActivityServices.logout(token);
        userLogout.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(UserInfoActivity.this,LoginActivity.class);
                    finish();startActivity(intent);
                    editor.putString("token","null");
                    editor.apply();
                }else {
                    Toast.makeText(UserInfoActivity.this,
                            "Token not correct\n"+response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this,
                        "Serve could not response\n"+t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUserFollowCall() {
        Call<FollowLikedList>  followLikedListCall = userInformationServices.getFollowLikedList(token);
        followLikedListCall.enqueue(new Callback<FollowLikedList>() {
            @Override
            public void onResponse(Call<FollowLikedList> call, Response<FollowLikedList> response) {
                if (response.isSuccessful()){
                    String[] follows = response.body().getFollowListNames().split("_");
                    List<String> followList = new ArrayList<>();
                    followList.addAll(Arrays.asList(follows));
                    followListAdapter.setData(UserInfoActivity.this,followList);
                    followRecyclerView.setAdapter(followListAdapter);
                }else{
                    Toast.makeText(UserInfoActivity.this,
                            "Token not correct",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FollowLikedList> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this,
                        "Serve could not response",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUserInfoCall() {
        Call<UserInfo> userInfoCall = userInformationServices.getUserInfo(token);
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.isSuccessful()){
                    UserFullname = response.body().getFullname();
                    name.setText(response.body().getFullname());
                    username.setText(response.body().getUsername());
                    String gen="";
                    if(response.body().getGender()==0) {
                        gen = "Đầu bếp nam";
                    }else{
                        gen = "Đầu bếp nữ";
                    }
                    gender.setText(gen);
                    birth.setText(response.body().getBirthday());
                    address.setText(response.body().getAddress());
                    Picasso.get().load(url+response.body().getAvatar()).into(avatar);
                    join.setText("Tôi tham gia từ: "+response.body().getCreatedAt());
                    String lev="";
                    if(response.body().getLevel()==2){
                        lev = "Người dùng thành viên";
                    }else{
                        lev = "Quản trị viên";
                    }
                    level.setText("Quyền truy cập: "+lev);
                }else{
                    Toast.makeText(UserInfoActivity.this,
                            "Token not correct",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(UserInfoActivity.this,
                        "Serve could not response",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getElementId() {
        followRecyclerView = (RecyclerView)findViewById(R.id.follow);
        name = (TextView)findViewById(R.id.name);
        username = (TextView)findViewById(R.id.username);
        gender = (TextView)findViewById(R.id.gender);
        birth = (TextView)findViewById(R.id.birthday);
        address = (TextView)findViewById(R.id.address);
        level = (TextView)findViewById(R.id.level);
        join = (TextView)findViewById(R.id.join);
        avatar = (CircleImageView)findViewById(R.id.avatar);
    }
    @Override
    public void onBackPressed() {
        back();
    }
    public void back(){
        Intent intent = new Intent(UserInfoActivity.this,MainActivity.class);
        finish();startActivity(intent);
    }


}