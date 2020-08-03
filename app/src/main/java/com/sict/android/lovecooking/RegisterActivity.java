package com.sict.android.lovecooking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.sict.android.lovecooking.Model.User;
import com.sict.android.lovecooking.Model.UserCreate;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.ApplicationActivityServices;

import org.w3c.dom.Text;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText fullname,username,add,pass,re_pass,mail;
    private MaterialButton btnRegister,btnLogin;
    private RadioGroup radioGroup;
    private RadioButton male,female;
    private TextView birth;
    private int gender=0;
    private String birthday = "";
    ApplicationActivityServices
            applicationActivityServices = RetrofitClient.getRetrofit().create(ApplicationActivityServices.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getElementsId();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToLogin();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                View view = LayoutInflater.from(RegisterActivity.this)
                        .inflate(R.layout.alert_dialog_date_picker_view,null,false);
                DatePicker datePicker = view.findViewById(R.id.datePicker1);
                builder.setTitle("Chọn ngày sinh của bạn");
                builder.setView(view);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        birthday = datePicker.getYear() +"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth();
                        birth.setText(datePicker.getDayOfMonth()+"-"+(datePicker.getMonth()+1)
                                +"-"+datePicker.getYear());
                    }
                }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog newMenu = builder.create();
                newMenu.show();
            }
        });
    }

    private void register() {
        String fullName = fullname.getText().toString();
        String userName = username.getText().toString();
        String address = add.getText().toString();
        
        String password = pass.getText().toString();
        String re_password = re_pass.getText().toString();
        String email = mail.getText().toString();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                checkedId = group.getCheckedRadioButtonId();
                if(checkedId == R.id.male) gender = 0;
                else if(checkedId == R.id.female) gender = 1;
                else gender =-1;
            }
        });

        if(re_password.equals(password)){
            UserCreate userCreate = new UserCreate(fullName,userName,email,password,gender,birthday,address);
            Call<User> userCall = applicationActivityServices.getRegister(userCreate);
            userCall.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    Toast.makeText(RegisterActivity.this,
                            "All Input true\nRegister successfully. " +
                                    "Please return to Login page and login again",
                            Toast.LENGTH_LONG).show();
                    returnToLogin();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this,
                            "Serve could not response",
                            Toast.LENGTH_LONG).show();
                }
            });
        }else{
            Toast.makeText(getApplicationContext(),
                    "Password and re-password must be the same",Toast.LENGTH_LONG).show();
        }
    }

    private void getElementsId() {
        fullname = (EditText)findViewById(R.id.fname);
        username = (EditText)findViewById(R.id.uname);
        add = (EditText)findViewById(R.id.address);
        birth = (TextView) findViewById(R.id.birth);
        pass = (EditText)findViewById(R.id.password);
        re_pass = (EditText)findViewById(R.id.re_password);
        mail = (EditText)findViewById(R.id.email);
        radioGroup = (RadioGroup)findViewById(R.id.gender);
        male= (RadioButton)findViewById(R.id.male);
        female = (RadioButton)findViewById(R.id.female);
        btnLogin = (MaterialButton)findViewById(R.id.login);
        btnRegister = (MaterialButton)findViewById(R.id.btnregister);
    }

    private void returnToLogin() {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        finish();startActivity(intent);
    }
}