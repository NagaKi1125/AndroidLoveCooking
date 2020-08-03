package com.sict.android.lovecooking.Fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.sict.android.lovecooking.Adapter.DishAdapter;
import com.sict.android.lovecooking.Adapter.DishSearchAdapter;
import com.sict.android.lovecooking.DishAddActivity;
import com.sict.android.lovecooking.MainActivity;
import com.sict.android.lovecooking.Model.Dish;
import com.sict.android.lovecooking.Model.SearchFilter;
import com.sict.android.lovecooking.Model.UserInfo;
import com.sict.android.lovecooking.R;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.ApplicationActivityServices;
import com.sict.android.lovecooking.Services.ApplicationInfoServices;
import com.sict.android.lovecooking.UserInfoActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DishFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DishFragment extends Fragment{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //
    RecyclerView dishRecyclerView;
    private DishAdapter dishAdapter;
    private ImageView btnNew,btnAdd;
    ApplicationInfoServices applicationInfoServices;
    ApplicationActivityServices applicationActivityServices;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    //


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DishFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DishFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DishFragment newInstance(String param1, String param2) {
        DishFragment fragment = new DishFragment();
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

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_dish,container,false);
        applicationInfoServices =
                RetrofitClient.getRetrofit().create(ApplicationInfoServices.class);
        applicationActivityServices = RetrofitClient.getRetrofit().create(ApplicationActivityServices.class);

        dishRecyclerView =(RecyclerView)view.findViewById(R.id.mainDish);
        dishRecyclerView.setHasFixedSize(true);
        dishRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2,
                RecyclerView.VERTICAL,false));
        dishAdapter = new DishAdapter();



        getAllDishes();

        btnNew = (ImageView)view.findViewById(R.id.addNewDish);
        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DishAddActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getAllDishes() {

        final Call<List<Dish>>dishCall = applicationInfoServices.getAllDish();
        dishCall.enqueue(new Callback<List<Dish>>() {
            @Override
            public void onResponse(Call<List<Dish>> call, Response<List<Dish>> response) {
                if(response.isSuccessful()){
                    dishAdapter.setData(response.body(),getActivity().getApplication(),response.body().size());
                    dishRecyclerView.setAdapter(dishAdapter);
                }else{
                    Toast.makeText(getContext(),
                            "Could not get any response",
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



}