package com.sict.android.lovecooking.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.DishAddActivity;
import com.sict.android.lovecooking.Model.StepImage;
import com.sict.android.lovecooking.R;
import com.sict.android.lovecooking.ViewHolder.StepImgViewHolder;

import java.util.List;

public class StepImgAdapter extends RecyclerView.Adapter<StepImgViewHolder> {

    private List<StepImage> stepImages;
    private Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private String url;
    private Uri stepUri;
    public StepImgAdapter() {
    }

    @SuppressLint("CommitPrefEdits")
    public void setData(List<StepImage> stepImages, Context context) {
        this.stepImages = stepImages;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        this.editor =sharedPreferences.edit();
        this.url = sharedPreferences.getString("url","http://192.168.0.101:8000/");
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepImgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_img_item,parent,false);

        return new StepImgViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepImgViewHolder holder, int position) {
        holder.btnStepView.setText(String.valueOf(position+1));
        holder.steps.setHint("Cho nước vào, đưa ngón tay vào đến khi nước cao bằng hai đốt tay thì đủ");
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepImages.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,stepImages.size());
            }
        });

        holder.imgSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                ((DishAddActivity)context).startActivityForResult(
                        Intent.createChooser(gallery,"Chọn ảnh đại diện"),
                        position+1);
            }
        });


    }



    @Override
    public int getItemCount() {
        return stepImages.size();
    }
}
