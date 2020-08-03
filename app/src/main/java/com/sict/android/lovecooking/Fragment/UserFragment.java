package com.sict.android.lovecooking.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sict.android.lovecooking.Adapter.LikedListAdapter;
import com.sict.android.lovecooking.Adapter.MenuAdapter;
import com.sict.android.lovecooking.Adapter.MyDishAdapter;
import com.sict.android.lovecooking.Model.Dish;
import com.sict.android.lovecooking.Model.FollowLikedList;
import com.sict.android.lovecooking.Model.MenuList;
import com.sict.android.lovecooking.Model.User;
import com.sict.android.lovecooking.R;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.UserInformationServices;
import com.sict.android.lovecooking.UserInfoActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView likedDishRecyclerView,menuRecyclerView,myDishrecyclerView;
    private LikedListAdapter likedListAdapter;
    private MenuAdapter menuAdapter;
    private MyDishAdapter myDishAdapter;
    private TextView menu,likedDish;
    private ImageButton btnMenuRef,btnLikedRef,btnMyDishRef;

    private String token;

    UserInformationServices userInformationServices;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        menu = (TextView)view.findViewById(R.id.menu);
        likedDish = (TextView)view.findViewById(R.id.likedDish);
        btnLikedRef = (ImageButton)view.findViewById(R.id.btn_liked_refresh);
        btnMenuRef = (ImageButton)view.findViewById(R.id.btn_menu_refresh);
        btnMyDishRef = (ImageButton)view.findViewById(R.id.btn_md_refresh);

        sharedPreferences = view.getContext().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        userInformationServices = RetrofitClient.getRetrofit().create(UserInformationServices.class);

        token = sharedPreferences.getString("token","null");

        //menu
        menuRecyclerView = (RecyclerView)view.findViewById(R.id.menusRecyclerView);
        menuRecyclerView.setHasFixedSize(true);
        menuRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        menuAdapter = new MenuAdapter();
        getMenuList();
        btnMenuRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMenuList();
            }
        });


        //liked dish
        likedDishRecyclerView = (RecyclerView)view.findViewById(R.id.likedDishList);
        likedDishRecyclerView.setHasFixedSize(true);
        likedDishRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        likedListAdapter = new LikedListAdapter();
        getLikedList();
        btnLikedRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLikedList();
            }
        });

        likedDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserInfoActivity.class);
                startActivity(intent);
            }
        });

        //mydish
        myDishrecyclerView = (RecyclerView)view.findViewById(R.id.myDishList);
        myDishrecyclerView.setHasFixedSize(true);
        myDishrecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        myDishAdapter = new MyDishAdapter();
        getMyDishInfo();
        btnMyDishRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMyDishInfo();
            }
        });


        return view;
    }

    private void getMyDishInfo() {
        Call<List<Dish>> getDishInfo = userInformationServices.getUserDishes("Bearer "+token);
        getDishInfo.enqueue(new Callback<List<Dish>>() {
            @Override
            public void onResponse(Call<List<Dish>> call, Response<List<Dish>> response) {
                if(response.isSuccessful()){
                    myDishAdapter.setData(response.body(),getContext());
                    myDishrecyclerView.setAdapter(myDishAdapter);
                }else{
                    Toast.makeText(getContext(),
                            "Could not receive any response",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Dish>> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Serve could not response",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getLikedList() {
        Call<FollowLikedList> followLikedListCall =userInformationServices.getFollowLikedList("Bearer "+token);
        followLikedListCall.enqueue(new Callback<FollowLikedList>() {
            @Override
            public void onResponse(Call<FollowLikedList> call, Response<FollowLikedList> response) {
                if(response.isSuccessful()){
                    String[] likedList = response.body().getDishLikedList().split("_");
                    List<String> dishLikedList = new ArrayList<>();
                    dishLikedList.addAll(Arrays.asList(likedList));
                    likedListAdapter.setData(dishLikedList,getContext());
                    likedDishRecyclerView.setAdapter(likedListAdapter);
                }else {
                    Toast.makeText(getContext(),
                            "Could not receive any response",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<FollowLikedList> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Serve could not response",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getMenuList() {
        Call<List<MenuList>> menuListCall = userInformationServices.getMenuList("Bearer "+token);
        menuListCall.enqueue(new Callback<List<MenuList>>() {
            @Override
            public void onResponse(Call<List<MenuList>> call, Response<List<MenuList>> response) {
                if(response.isSuccessful()){
                    menuAdapter.setData(response.body(),getContext());
                    menuRecyclerView.setAdapter(menuAdapter);
                }else{
                    Toast.makeText(getContext(),
                            "Token not correct could not get any response",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<MenuList>> call, Throwable t) {
                Toast.makeText(getContext(),
                        "Serve not response\n" +
                                "Please check your internet connection and try again",Toast.LENGTH_LONG).show();
            }
        });
    }
}