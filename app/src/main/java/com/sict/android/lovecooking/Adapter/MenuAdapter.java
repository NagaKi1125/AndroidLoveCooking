package com.sict.android.lovecooking.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.MenuInfoActivity;
import com.sict.android.lovecooking.Model.MenuList;
import com.sict.android.lovecooking.R;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.UserActivityServices;
import com.sict.android.lovecooking.ViewHolder.MenuViewHolder;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    private List<MenuList> menuLists;
    private Context context;
    //private String url = "http://192.168.43.129:8000/";
    //private String url = "http://192.168.0.101:8000/";
    //private String url = "http://lovecooking.herokuapp.com";
    private String url;
    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;
    UserActivityServices userActivityServices;

    public MenuAdapter() {
    }

    @SuppressLint("CommitPrefEdits")
    public void setData(List<MenuList> menuLists, Context context) {
        this.menuLists = menuLists;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.url = sharedPreferences.getString("url","http://192.168.0.101:8000/");
        this.userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_menu_item,parent,false);
        return new MenuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuViewHolder holder, int position) {
        holder.menuDate.setText(menuLists.get(position).getMenuDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MenuInfoActivity.class);
                intent.putExtra("breakfast",menuLists.get(position).getBreakfastList());
                intent.putExtra("lunch",menuLists.get(position).getLunchList());
                intent.putExtra("dinner",menuLists.get(position).getDinnerList());
                intent.putExtra("date",menuLists.get(position).getMenuDate());
                intent.putExtra("menu_id",String.valueOf(menuLists.get(position).getId()));
                v.getContext().startActivity(intent);
            }
        });

        holder.btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> deleteMenu = userActivityServices.deleteMenu(
                        "Bearer "+sharedPreferences.getString("token","null"),
                        menuLists.get(position).getId());
                deleteMenu.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()){
                            menuLists.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,menuLists.size());
                            Toast.makeText(v.getContext(),
                                    "Removed",
                                    Toast.LENGTH_LONG).show();

                        }else {
                            Toast.makeText(v.getContext(),
                                    "Failed",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(v.getContext(),
                                "Serve could not response",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuLists.size();
    }
}
