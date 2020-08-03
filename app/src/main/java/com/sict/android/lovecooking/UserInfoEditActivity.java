package com.sict.android.lovecooking;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.sict.android.lovecooking.Model.UserInfo;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.UserActivityServices;
import com.sict.android.lovecooking.Services.UserInformationServices;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserInfoEditActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 1;
    private CircleImageView avatar;
    private EditText name,username,address;
    private TextView birthday,txtava;
    private Spinner genderSpinner;
    private DatePicker datePicker;
    private LinearLayout btnLayout;
    private MaterialButton btnChoose,btnCancel,btnUpdate;
    private ImageButton back;
    UserInformationServices userInformationServices;
    UserActivityServices userActivityServices;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private int genderRetrieve = 0;
    //private String url = "http://192.168.43.129:8000/";
    private String url = "http://192.168.0.101:8000/";

    private String avatarImage = "null",birthdayDate="00-00-0000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);

        getElementId();

        userInformationServices = RetrofitClient.getRetrofit().create(UserInformationServices.class);
        userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);
        sharedPreferences = this.getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        datePicker.setVisibility(View.GONE);
        btnLayout.setVisibility(View.GONE);

        getUserInfo();

        txtava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoEditActivity.this,UserInfoActivity.class);
                finish();startActivity(intent);
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.VISIBLE);
            }
        });
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.GONE);
                btnLayout.setVisibility(View.GONE);
                String date = datePicker.getDayOfMonth()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getYear();
                birthdayDate = datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth();
                birthday.setText(date);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.GONE);
                btnLayout.setVisibility(View.GONE);
            }
        });

        ArrayAdapter<CharSequence> genderAdapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.gender,android.R.layout.simple_spinner_item);
        genderAdapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(genderAdapterSpinner);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderRetrieve = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(UserInfoEditActivity.this,
//                        "Bearer "+sharedPreferences.getString("token","null")+"\n"+
//                        name.getText().toString()+"\n"+
//                        username.getText().toString()+"\n"+
//                        genderRetrieve+"\n"+
//                        address.getText().toString()+"\n"+
//                        birthday.getText().toString(),
//                                    Toast.LENGTH_LONG).show();
//                Log.d("image",avatarImage);
                Call<ResponseBody> updateUserInfo = userActivityServices.updateUserInfo(
                        "Bearer "+sharedPreferences.getString("token","null"),
                        name.getText().toString(),
                        username.getText().toString(),
                        avatarImage,genderRetrieve,
                        address.getText().toString(),
                        birthdayDate
                );
                updateUserInfo.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Intent intent = new Intent(UserInfoEditActivity.this,UserInfoActivity.class);
                            finish();startActivity(intent);
                            Toast.makeText(UserInfoEditActivity.this,
                                    "Update Information Successfully",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(UserInfoEditActivity.this,
                                    "Updated Failed\n"+response.message(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(UserInfoEditActivity.this,
                                "Token not correct\n"+t.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    private void pickImage() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(gallery,"Chọn ảnh đại diện"),
                PICK_IMAGE);
    }

    private void getUserInfo() {
        Call<UserInfo> userInfoCall = userInformationServices.getUserInfo(
                "Bearer "+sharedPreferences.getString("token","null")
        );
        userInfoCall.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.isSuccessful()){
                    name.setText(response.body().getFullname());
                    username.setText(response.body().getUsername());
                    birthday.setText(response.body().getBirthday());
                    address.setText(response.body().getAddress());
                    genderSpinner.setSelection(response.body().getGender());
                    Picasso.get().load(url+response.body().getAvatar()).into(avatar);

                }else{
                    Toast.makeText(UserInfoEditActivity.this,
                            "Token not correct\n"+response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {
                Toast.makeText(UserInfoEditActivity.this,
                        "Serve could not response\n"+t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data !=null){
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                String ext = getContentResolver().getType(imageUri);
                avatar.setImageBitmap(bitmap);
                avatarImage = "data:"+ext+";base64,"+getStringImage(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return encodedImage;
    }

    private void getElementId() {
        avatar = (CircleImageView)findViewById(R.id.avatar);
        name = (EditText)findViewById(R.id.name);
        username = (EditText)findViewById(R.id.username);
        address = (EditText)findViewById(R.id.address);
        birthday = (TextView)findViewById(R.id.birthday);
        genderSpinner = (Spinner)findViewById(R.id.datePicker);
        datePicker = (DatePicker)findViewById(R.id.datePicker1);
        btnLayout = (LinearLayout)findViewById(R.id.btnClick);
        btnChoose = (MaterialButton)findViewById(R.id.choose);
        btnCancel = (MaterialButton)findViewById(R.id.cancel);
        back = (ImageButton)findViewById(R.id.back);
        txtava = (TextView)findViewById(R.id.txtava);
        btnUpdate = (MaterialButton)findViewById(R.id.update);
    }
}