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
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DishAdapter extends RecyclerView.Adapter<DishViewHolder> {
    private List<Dish> dishList;
    //private String url = "http://192.168.43.129:8000/";
    private String url = "http://192.168.0.101:8000/";
    //private String url = "http://lovecooking.herokuapp.com";
    private int[] clickCount;
    private int[] likedCount;

    ApplicationInfoServices applicationInfoServices;
    UserActivityServices userActivityServices;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public DishAdapter() {
    }

    @SuppressLint("CommitPrefEdits")
    public void setData(List<Dish> dishList, Context context,int listSize) {
        this.dishList = dishList;
        this.sharedPreferences = context.getSharedPreferences("UserInfo",MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.clickCount = new int[listSize];
        this.likedCount = new int[listSize];
        this.applicationInfoServices =
                RetrofitClient.getRetrofit().create(ApplicationInfoServices.class);
        this.userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_item,parent,false);
        return new DishViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishViewHolder holder,int position) {
        int id = dishList.get(position).getId();
        String dish_name = dishList.get(position).getDishName();
        String cate_id = dishList.get(position).getCateId();
        String avatar =  url+dishList.get(position).getAvatar();
        String updated_at = dishList.get(position).getUpdatedAt();
        String created_at = dishList.get(position).getCreatedAt();
        String description = dishList.get(position).getDescription();
        String use = dishList.get(position).getUse();
        String material =dishList.get(position).getMaterial();
        String steps = dishList.get(position).getSteps();
        String step_imgs = dishList.get(position).getStepImgs();
        String author = dishList.get(position).getAuthor();
        likedCount[position] = dishList.get(position).getLikedCount();
        List<Dish.History> dhpost = dishList.get(position).getHistory();
        List<Dish.Comment> comment = dishList.get(position).getCmt();
        String post = "";String cmt= "";

        for(Dish.History hispost: dhpost){
            post+=hispost.getDhposts()+"_";
        }

        for(Dish.Comment comments : comment){
            cmt+=comments.getCommentID()+"#"+comments.getName()+"#"+comments.getComment()+"#"
                    +comments.getCmt_updated_at()+"_";
        }

        //show to recycler view
        Picasso.get().load(avatar).into(holder.dish_image);
        holder.dish_name.setText(dish_name);
        holder.author.setText(dishList.get(position).getAuthor()+"");
        String liked = likedCount[position] + " lượt thích";
        holder.liked.setText(liked);

        //event for liked button

        if(!sharedPreferences.getString("dish_liked","_").contains(String.valueOf(id))){
            clickCount[position]=0;
            holder.love.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }else{
            clickCount[position]=1;
            holder.love.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        holder.love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCount[position]++;
                if(clickCount[position] ==2) {
                    Call<UserLikedList> unLove = userActivityServices.notLoveThis(
                            "Bearer "+sharedPreferences.getString("token","null"),id);
                    unLove.enqueue(new Callback<UserLikedList>() {
                        @Override
                        public void onResponse(Call<UserLikedList> call, Response<UserLikedList> response) {
                            if(response.isSuccessful()){
                                holder.love.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                likedCount[position]-=1;
                                String liked = likedCount[position] + " lượt thích";
                                holder.liked.setText(liked);
                                clickCount[position] = 0;
                                //dishLiked = response.body().getDishIdList();
                                editor.putString("dish_liked",response.body().getDishIdList());
                                editor.apply();
                            }else{
                                Toast.makeText(v.getContext(),"Could not get any response",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLikedList> call, Throwable t) {
                            Toast.makeText(v.getContext(),
                                    "Serve could not response",Toast.LENGTH_LONG).show();
                        }
                    });

                } else{
                    Call<UserLikedList> loveCall = userActivityServices.loveThis(
                            "Bearer "+sharedPreferences.getString("token","null"),id);
                    loveCall.enqueue(new Callback<UserLikedList>() {
                        @Override
                        public void onResponse(Call<UserLikedList> call, Response<UserLikedList> response) {
                            if(response.isSuccessful()){
                                holder.love.setImageResource(R.drawable.ic_favorite_black_24dp);
                                likedCount[position]+=1;
                                String liked = likedCount[position] + " lượt thích";
                                holder.liked.setText(liked);
                                //dishLiked = response.body().getDishIdList();
                                editor.putString("dish_liked",response.body().getDishIdList());
                                editor.apply();
                            }else{
                                Toast.makeText(v.getContext(),"Could not get any response",Toast.LENGTH_LONG).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<UserLikedList> call, Throwable t) {
                            Toast.makeText(v.getContext(),
                                    "Serve could not response",Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        //ready information for intent to dish info activity
        String dishcmt = cmt;
        String historyPost = post;
        int likeC = likedCount[position];
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),DishInfoActivity.class);
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
                intent.putExtra("like_count",likedCount[position]);
                intent.putExtra("hispost", historyPost);
                intent.putExtra("cmt",dishcmt);

                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }
}
