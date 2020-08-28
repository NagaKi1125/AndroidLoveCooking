package com.sict.android.lovecooking.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.R;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.UserActivityServices;
import com.sict.android.lovecooking.Services.UserInformationServices;
import com.sict.android.lovecooking.ViewHolder.FollowListViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowListAdapter extends RecyclerView.Adapter<FollowListViewHolder> {

    Context context;
    List<String> followLists;
    //private String url = "http://192.168.43.129:8000/";
    //private String url = "http://192.168.0.101:8000/";
    //private String url = "http://lovecooking.herokuapp.com";
    private String url;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    UserInformationServices userInformationServices;
    UserActivityServices userActivityServices;

    public FollowListAdapter() {
    }

    @SuppressLint("CommitPrefEdits")
    public void setData(Context context, List<String> followLists) {
        this.context = context;
        this.followLists = followLists;
        this.sharedPreferences = context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.url = sharedPreferences.getString("url","http://192.168.0.101:8000/");
        this.userInformationServices = RetrofitClient.getRetrofit().create(UserInformationServices.class);
        this.userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FollowListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.user_follow_item,parent,false);
        return new FollowListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowListViewHolder holder, int position) {
        String[] followList = followLists.get(position).split("#");
        if(followList[2].equals("null")) holder.usAvatar.setImageResource(R.drawable.ic_account_circle_black_24dp);
        else Picasso.get().load(url+followList[2]).into(holder.usAvatar);
        holder.usName.setText(followList[1]);

        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> unfollow = userActivityServices.unfollow(
                        "Bearer "+sharedPreferences.getString("token","null"),
                        followList[0]
                );
                unfollow.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            followLists.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,followLists.size());
                        }else{
                            Toast.makeText(context,
                                    "Unfollow Failed \n"+response.message(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context,
                                "Serve could not response \n"+t.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return followLists.size();
    }
}
