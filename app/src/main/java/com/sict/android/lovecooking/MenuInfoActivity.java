package com.sict.android.lovecooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sict.android.lovecooking.Adapter.BreakfastAdapter;
import com.sict.android.lovecooking.Adapter.DinnerAdapter;
import com.sict.android.lovecooking.Adapter.LunchAdapter;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.UserActivityServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuInfoActivity extends AppCompatActivity {

    private RecyclerView breakfast,lunch,dinner;
    private List<String> breakfastList,lunchList,dinnerList;
    private String brlist,lulist,dinlist,menu_date,menu_id,token;
    private String[] breakfast_array,lunch_array,dinner_array;
    private LinearLayout br_null,lu_null,di_null;
    BreakfastAdapter breakfastAdapter;
    LunchAdapter lunchAdapter;
    DinnerAdapter dinnerAdapter;
    private TextView menuDate;
    private ImageButton cancel,save,edit;
    private Boolean deleteOrNot;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor  editor;
    UserActivityServices userActivityServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_info);
        getElementsID();

        br_null.setVisibility(View.GONE);
        lu_null.setVisibility(View.GONE);
        di_null.setVisibility(View.GONE);

        userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);
        sharedPreferences = this.getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        deleteOrNot = false;

        token = sharedPreferences.getString("token","null");


        cancel.setVisibility(View.GONE);
        save.setVisibility(View.GONE);

        getIntentInfo();
        convertStringToList();

        menuDate.setText(menu_date);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrNot = true;

                cancel.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                edit.setVisibility(View.GONE);

                setBreakfastRecyclerView(deleteOrNot);
                setLunchRecyclerView(deleteOrNot);
                setDinnerRecyclerView(deleteOrNot);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrNot = false;

                cancel.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);

                setBreakfastRecyclerView(deleteOrNot);
                setLunchRecyclerView(deleteOrNot);
                setDinnerRecyclerView(deleteOrNot);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteOrNot = false;
                String breakfast = sharedPreferences.getString("breakfast","null");
                String lunch = sharedPreferences.getString("lunch","null");
                String dinner = sharedPreferences.getString("dinner","null");

                Call<ResponseBody> removeMenu = userActivityServices.menuRemoveDish(
                        "Bearer "+sharedPreferences.getString("token","null"),
                        Integer.parseInt(menu_id),breakfast,lunch,dinner);
                removeMenu.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(MenuInfoActivity.this,
                                    "Dish Menu remove successfully",
                                    Toast.LENGTH_SHORT).show();
                            cancel.setVisibility(View.GONE);
                            save.setVisibility(View.GONE);
                            edit.setVisibility(View.VISIBLE);
                        }else{
                            Toast.makeText(MenuInfoActivity.this,
                                    "Dish Menu remove Failed\n"+response.message(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MenuInfoActivity.this,
                                "Serve could not response",
                                Toast.LENGTH_SHORT).show();
                    }
                });
//                Toast.makeText(MenuInfoActivity.this,
//                                breakfast+"\n"+menu_id+"\n"+token,
//                Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void convertStringToList() {
        // breakfast
        String breakfastShared = "";
        breakfastList = new ArrayList<>();
        if(brlist.equals("")){
            breakfastList.add("0#null#null");
            breakfastShared="0";
            breakfast.setVisibility(View.GONE);
            br_null.setVisibility(View.VISIBLE);
        }else{
            breakfast_array = brlist.split("_");
            breakfastList.addAll(Arrays.asList(breakfast_array));
            for (int i = 0; i < breakfastList.size(); i++) {
                String[] dishInfo = breakfastList.get(i).split("#");
                breakfastShared += dishInfo[0] + "_";
            }
            setBreakfastRecyclerView(deleteOrNot);
        }
        editor.putString("breakfast",breakfastShared);
        editor.apply();


        //lunch
        String lunchShared = "";
        lunchList = new ArrayList<>();
        if(lulist.equals("")){
            lunchList.add("0#null#null");
            lunchShared="0";
            lunch.setVisibility(View.GONE);
            lu_null.setVisibility(View.VISIBLE);
        }else{
            lunch_array = lulist.split("_");
            lunchList.addAll(Arrays.asList(lunch_array));
            for (int i = 0; i < lunchList.size(); i++) {
                String[] dishInfo = lunchList.get(i).split("#");
                lunchShared += dishInfo[0] + "_";
            }
            setLunchRecyclerView(deleteOrNot);
        }

        editor.putString("lunch",lunchShared);
        editor.apply();

        //dinner
        String dinnerShared = "";
        dinnerList = new ArrayList<>();
        if(dinlist.equals("")){
         dinnerList.add("0#null#null");
         dinnerShared="0";
         dinner.setVisibility(View.GONE);
         di_null.setVisibility(View.VISIBLE);
        }else{
            dinner_array = dinlist.split("_");
            dinnerList.addAll(Arrays.asList(dinner_array));
            for (int i = 0; i < dinnerList.size(); i++) {
                String[] dishInfo = dinnerList.get(i).split("#");
                dinnerShared += dishInfo[0] + "_";
            }
            setDinnerRecyclerView(deleteOrNot);
        }
        editor.putString("dinner",dinnerShared);
        editor.apply();
    }
    private void getElementsID() {
        breakfast = (RecyclerView)findViewById(R.id.breakfast_recyclerView);
        lunch = (RecyclerView)findViewById(R.id.lunch_recyclerView);
        dinner = (RecyclerView)findViewById(R.id.dinner_recyclerView);
        menuDate = (TextView)findViewById(R.id.date);
        cancel = (ImageButton)findViewById(R.id.cancel);
        save = (ImageButton)findViewById(R.id.save);
        edit = (ImageButton)findViewById(R.id.edit);
        br_null = (LinearLayout)findViewById(R.id.br_null);
        lu_null = (LinearLayout)findViewById(R.id.lu_null);
        di_null = (LinearLayout)findViewById(R.id.di_null);

    }

    private void getIntentInfo() {
        brlist = getIntent().getStringExtra("breakfast");
        lulist = getIntent().getStringExtra("lunch");
        dinlist = getIntent().getStringExtra("dinner");
        menu_date = "Thực đơn ngày: "+getIntent().getStringExtra("date");
        menu_id = getIntent().getStringExtra("menu_id");
    }

    private void setBreakfastRecyclerView(Boolean deleteOrNot) {
        breakfast.setHasFixedSize(true);
        breakfast.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false));
        breakfastAdapter = new BreakfastAdapter();
        breakfastAdapter.setData(breakfastList,MenuInfoActivity.this,menu_id,deleteOrNot);
        breakfast.setAdapter(breakfastAdapter);
    }

    private void setLunchRecyclerView(Boolean deleteOrNot) {
        lunch.setHasFixedSize(true);
        lunch.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false));
        lunchAdapter = new LunchAdapter();
        lunchAdapter.setData(lunchList, MenuInfoActivity.this, menu_id, deleteOrNot);
        lunch.setAdapter(lunchAdapter);

    }

    private void setDinnerRecyclerView(Boolean deleteOrNot) {
        dinner.setHasFixedSize(true);
        dinner.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,false));
        dinnerAdapter = new DinnerAdapter();
        dinnerAdapter.setData(dinnerList, MenuInfoActivity.this, menu_id, deleteOrNot);
        dinner.setAdapter(dinnerAdapter);
    }
}