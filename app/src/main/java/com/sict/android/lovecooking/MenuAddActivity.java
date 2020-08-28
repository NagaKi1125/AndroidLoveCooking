package com.sict.android.lovecooking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.sict.android.lovecooking.Model.Menu;
import com.sict.android.lovecooking.Model.MenuList;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.UserActivityServices;
import com.sict.android.lovecooking.Services.UserInformationServices;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAddActivity extends AppCompatActivity {

    private CircleImageView dishAvatar;
    private TextView dishName,newMenuDate;
    private Spinner menuSpinner,dateTimeSpinner;
    private MaterialButton btnAddToMenu,btnAddMenu,btnChoose,btnCancel;
    private LinearLayout btnLayout;
    private DatePicker datePicker;
    UserInformationServices userInformationServices;
    UserActivityServices userActivityServices;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //private String url = "http://192.168.43.129:8000/";
    //private String url = "http://192.168.0.101:8000/";
    //private String url = "http://lovecooking.herokuapp.com";
    private String url;

    private String dish_id,dish_avatar,dish_name;
    //new date for menu
    private String dateUpload, breakfast,lunch,dinner;
    private int menu_id,when=0;
    ArrayAdapter menuAdapterSpinner;
    ArrayList<Menu> menuLists = new ArrayList<>();
    ArrayList<String> menuDate = new ArrayList<>();

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_add);
        getComponentId();
        getDishInfo();
        //prepare
        userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);
        userInformationServices = RetrofitClient.getRetrofit().create(UserInformationServices.class);
        sharedPreferences = MenuAddActivity.this.getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        url = sharedPreferences.getString("url","http://192.168.0.101:8000/");

        // retrieve dish data
        Picasso.get().load(dish_avatar).into(dishAvatar);
        dishName.setText(dish_name);

        btnAddMenu.setVisibility(View.GONE);
        datePicker.setVisibility(View.GONE);
        btnLayout.setVisibility(View.GONE);

        //date time spinner
        ArrayAdapter<CharSequence> dateAdapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.dateTime,android.R.layout.simple_spinner_item);
        dateAdapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateTimeSpinner.setAdapter(dateAdapterSpinner);
        dateTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String time = parent.getItemAtPosition(position).toString();
                when = position;
                Toast.makeText(MenuAddActivity.this,
                        time,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //menu spinner
        getMenuSpinnerData();
        menuSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(position!= 0){
                   menu_id = menuLists.get(position).getId();
                   breakfast = menuLists.get(position).getBreakfastList();
                   lunch = menuLists.get(position).getLunchList();
                   dinner = menuLists.get(position).getDinnerList();
                   Toast.makeText(MenuAddActivity.this,
                           " "+menu_id+"\n"+
                           breakfast+"\n"+lunch+"\n"+dinner,
                           Toast.LENGTH_LONG).show();
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //end-prepare

        //click in date picker
        newMenuDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);
                btnLayout.setVisibility(View.VISIBLE);
            }
        });
        //btn in btnlayout
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateUpload = datePicker.getYear()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth();
                String date = datePicker.getDayOfMonth()+"-"+(datePicker.getMonth()+1)+"-"+datePicker.getYear();
                Toast.makeText(MenuAddActivity.this,"Date Picker: "+date,Toast.LENGTH_LONG).show();
                try {
                    if(ifDateAfterToday(dateUpload)) {
                        newMenuDate.setText("Đã chọn ngày: "+ date);
                        datePicker.setVisibility(View.GONE);
                        btnLayout.setVisibility(View.GONE);
                        btnAddMenu.setVisibility(View.VISIBLE);
                    }else{
                        Toast.makeText(MenuAddActivity.this,
                                "Ngày được chọn: "+date+" phải sau ngày hôm nay" +
                                        "\nHãy chọn lại",Toast.LENGTH_LONG).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.GONE);
                btnLayout.setVisibility(View.GONE);
            }
        });
        btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> addNewMenu = userActivityServices.MakeNewMenu(
                        "Bearer "+sharedPreferences.getString("token","null"),
                        dateUpload
                );
                addNewMenu.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(MenuAddActivity.this,
                                    "Successfully",
                                    Toast.LENGTH_LONG).show();
                            getMenuSpinnerData();
                        }else{
                            Toast.makeText(MenuAddActivity.this,
                                    "Add menu Failed\n"+response.message(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MenuAddActivity.this,
                                "Serve could not response\n"+t.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnAddToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(when == 1){
                    breakfast = breakfast+dish_id+"_";
                    addDishToMenu(menu_id,breakfast,lunch,dinner);
                } else if (when == 2){
                    lunch = lunch+dish_id+"_";
                    addDishToMenu(menu_id,breakfast,lunch,dinner);
                } else if (when == 3){
                    dinner = dinner+dish_id+"_";
                    addDishToMenu(menu_id,breakfast,lunch,dinner);
                } else{
                    Toast.makeText(MenuAddActivity.this,
                            "Hãy chọn thời gian thêm vào",
                            Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void addDishToMenu(int menu_id, String breakfast, String lunch, String dinner) {
        Call<ResponseBody> addDishToMenu = userActivityServices.menuAddDish(
                "Bearer "+sharedPreferences.getString("token","null"),
                menu_id,breakfast,lunch,dinner
        );
        addDishToMenu.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MenuAddActivity.this,
                            "Successfully",
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MenuAddActivity.this,
                            "Dish Added failed\n"+response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(MenuAddActivity.this,
                        "Serve could not response\n"+t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void getMenuSpinnerData() {
        Call<List<Menu>> getMenuList = userInformationServices.getMenu(
                "Bearer "+ sharedPreferences.getString("token","null"));
        getMenuList.enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                if(response.isSuccessful()){
                    Toast.makeText(MenuAddActivity.this,
                            "Menu List retrieve success",
                            Toast.LENGTH_LONG).show();
                    menuDate.clear();
                    menuDate.add(0,"Chọn thực đơn thêm vào");
                    menuLists.clear();
                    menuLists.add(0,new Menu());
                    for(int i=0;i<response.body().size();i++){
                        try {
                            if(ifDateAfterToday(response.body().get(i).getMenuDate())){
                                menuDate.add(response.body().get(i).getMenuDate());
                                menuLists.add(response.body().get(i));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    menuAdapterSpinner = new ArrayAdapter(MenuAddActivity.this,
                            android.R.layout.simple_spinner_item,menuDate);
                    menuAdapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    menuSpinner.setAdapter(menuAdapterSpinner);


                }else{
                    Toast.makeText(MenuAddActivity.this,
                            "Menu: Something went wrong\n"+response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {
                Toast.makeText(MenuAddActivity.this,
                        "Serve could not response\n"+t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean ifDateAfterToday(String y_m_d) throws ParseException {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat ymdFormart = new SimpleDateFormat("yyyy-MM-dd");
        Date d = ymdFormart.parse(y_m_d);
        Date today = new Date();
        return !today.after(d);
    }

    private void getDishInfo() {
        dish_id = getIntent().getStringExtra("dish_id");
        dish_avatar = getIntent().getStringExtra("dish_avatar");
        dish_name = getIntent().getStringExtra("dish_name");
    }

    private void getComponentId() {
        dishAvatar = (CircleImageView)findViewById(R.id.dish_avatar);
        dishName = (TextView)findViewById(R.id.dish_name);
        newMenuDate = (TextView)findViewById(R.id.newDate);
        menuSpinner = (Spinner)findViewById(R.id.menuPicker);
        dateTimeSpinner = (Spinner)findViewById(R.id.dateTimePicker);
        btnAddToMenu = (MaterialButton)findViewById(R.id.addToMenu);
        btnAddMenu = (MaterialButton)findViewById(R.id.add);
        btnChoose = (MaterialButton)findViewById(R.id.choose);
        btnCancel = (MaterialButton)findViewById(R.id.cancel);
        datePicker = (DatePicker)findViewById(R.id.datePicker1);
        btnLayout = (LinearLayout)findViewById(R.id.btnClick);
    }
}