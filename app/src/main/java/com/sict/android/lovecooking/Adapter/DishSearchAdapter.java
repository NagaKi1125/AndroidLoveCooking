package com.sict.android.lovecooking.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.DishInfoActivity;
import com.sict.android.lovecooking.Model.Dish;
import com.sict.android.lovecooking.Model.SearchFilter;
import com.sict.android.lovecooking.R;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.ApplicationInfoServices;
import com.sict.android.lovecooking.Services.UserActivityServices;
import com.sict.android.lovecooking.ViewHolder.DishSearchViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class DishSearchAdapter extends RecyclerView.Adapter<DishSearchViewHolder> implements  Filterable{

    private List<SearchFilter> searchFilter;
    private List<SearchFilter> searchFilterFull;
    private Context context;
    //private String url = "http://192.168.43.129:8000/";
    //private String url = "http://192.168.0.101:8000/";
    //private String url = "http://lovecooking.herokuapp.com";
    private String url;
    ApplicationInfoServices applicationInfoServices;
    UserActivityServices userActivityServices;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public DishSearchAdapter() {
    }

    @SuppressLint("CommitPrefEdits")
    public void setData(List<SearchFilter> searchFilter, Context context) {
        this.context = context;
        this.searchFilter = searchFilter;
        this.searchFilterFull = new ArrayList<>(searchFilter);
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
    public DishSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item,parent,false);
        return new DishSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishSearchViewHolder holder, int position) {
        holder.dishName.setText(searchFilter.get(position).getName());
        holder.author.setText(searchFilter.get(position).getAuthor());
        holder.description.setText(searchFilter.get(position).getDescription());
        Picasso.get().load(url+searchFilter.get(position).getAvatar()).into(holder.avatar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDishInfo(String.valueOf(searchFilter.get(position).getId()),v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchFilter.size();
    }

    @Override
    public Filter getFilter() {
        return autoFilter;
    }

    public Filter autoFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SearchFilter> searchFiltersList =  new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                searchFiltersList.addAll(searchFilterFull);
            }else{
                String filterPatern = constraint.toString().toLowerCase().trim();
                for(SearchFilter item: searchFilterFull){
                    if(item.getName().contains(filterPatern) || item.getDescription().contains(filterPatern)
                    || item.getUse().contains(filterPatern)){
                        searchFiltersList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = searchFiltersList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchFilter.clear();
            searchFilter.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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
                    String authorId = response.body().getAuthorId();
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
                            authorId,author,liked_count,historyPost,created_at,updated_at,v);

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
                              String use, String material, String steps, String step_imgs,String authorId,
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
        intent.putExtra("author_id",authorId);
        intent.putExtra("author",author);
        intent.putExtra("like_count",liked_count);
        intent.putExtra("hispost", historyPost);
        v.getContext().startActivity(intent);
    }
}
