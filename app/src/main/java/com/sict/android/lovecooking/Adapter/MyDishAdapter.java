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

import com.sict.android.lovecooking.DishInfoActivity;
import com.sict.android.lovecooking.Model.Dish;
import com.sict.android.lovecooking.Model.UserLikedList;
import com.sict.android.lovecooking.R;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.ApplicationInfoServices;
import com.sict.android.lovecooking.Services.UserActivityServices;
import com.sict.android.lovecooking.ViewHolder.DishViewHolder;
import com.sict.android.lovecooking.ViewHolder.MyDishViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MyDishAdapter extends RecyclerView.Adapter<MyDishViewHolder> {
    Context context;
    private List<Dish> dishList;
    //private String url = "http://192.168.43.129:8000/";
    //private String url = "http://192.168.0.101:8000/";
    //private String url = "http://lovecooking.herokuapp.com";
    private String url;
    ApplicationInfoServices applicationInfoServices;
    UserActivityServices userActivityServices;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public MyDishAdapter() {
    }

    @SuppressLint("CommitPrefEdits")
    public void setData(List<Dish> dishList, Context context) {
        this.dishList = dishList;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("UserInfo",MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.url = sharedPreferences.getString("url","http://192.168.0.101:8000/");
        this.applicationInfoServices =
                RetrofitClient.getRetrofit().create(ApplicationInfoServices.class);
        this.userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyDishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_dish_item,parent,false);
        return new MyDishViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyDishViewHolder holder, int position) {
        int id = dishList.get(position).getId();
        int checked = dishList.get(position).getChecked();
        holder.dishName.setText(dishList.get(position).getDishName());
        Picasso.get().load(url+dishList.get(position).getAvatar()).into(holder.dishAva);
        if(checked == 0){
            holder.status.setText("Chờ duyệt");
            holder.status.setBackgroundResource(R.color.yellow);
        }else{
            holder.status.setText("Đã duyệt");
            holder.status.setBackgroundResource(R.color.green);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDishInfo(String.valueOf(id),v);
            }
        });
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> deleteDish = userActivityServices.deleteDish(
                        "Bearer "+sharedPreferences.getString("token","null"),
                        String.valueOf(id)
                );
                deleteDish.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            dishList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position,dishList.size());
                        }else{
                            Toast.makeText(context,
                                    "Could not receive any response",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(context,
                                "Serve could not response",
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

    private void getDishInfo(String id, View v) {
        Call<Dish> dishInfoCall = applicationInfoServices.getDishInfo(id);
        dishInfoCall.enqueue(new Callback<Dish>() {
            @Override
            public void onResponse(Call<Dish> call, Response<Dish> response) {
                if(response.isSuccessful()){
                    Toast.makeText(v.getContext(),"Successfull",Toast.LENGTH_LONG).show();
                    int id = response.body().getId();
                    String dish_name = response.body().getDishName();
                    String cate_id = response.body().getCateId();
                    String avatar =  "http://lovecooking.herokuapp.com/"+response.body().getAvatar();
                    String updated_at = response.body().getUpdatedAt();
                    String created_at = response.body().getCreatedAt();
                    String description = response.body().getDescription();
                    String use = response.body().getUse();
                    String material =response.body().getMaterial();
                    String steps = response.body().getSteps();
                    String step_imgs = response.body().getStepImgs();
                    String author = response.body().getAuthor();
                    int liked_count = response.body().getLikedCount();
                    List<Dish.History> dhpost = response.body().getHistory();
                    String post = "";

                    for(Dish.History hispost: dhpost){
                        post+=hispost.getDhposts()+"_";
                    }


                    String historyPost = post;
                    // send info
                    sendDishInfo(id,dish_name,cate_id,avatar,description,use,material,steps,step_imgs,
                            author,liked_count,historyPost,created_at,updated_at,v);

                }else {
                    Toast.makeText(v.getContext(),
                            "Could not receive any response",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Dish> call, Throwable t) {
                Toast.makeText(v.getContext(),
                        "Serve could not response",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    private void sendDishInfo(int id, String dish_name, String cate_id, String avatar, String description,
                              String use, String material, String steps, String step_imgs,
                              String author, int liked_count, String historyPost,
                              String created_at, String updated_at, View v) {

        Intent intent = new Intent(v.getContext(), DishInfoActivity.class);
        intent.putExtra("Did",id);
        intent.putExtra("dish_name",dish_name);
        intent.putExtra("cate_id",cate_id);
        intent.putExtra("description",description);
        intent.putExtra("avatar",avatar);
        intent.putExtra("use",use);
        intent.putExtra("material",material);
        intent.putExtra("steps",steps);
        intent.putExtra("step_imgs",step_imgs);
        intent.putExtra("created_at",created_at);
        intent.putExtra("updated_at",updated_at);
        intent.putExtra("author",author);
        intent.putExtra("like_count",liked_count);
        intent.putExtra("hispost", historyPost);
        v.getContext().startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }
}
