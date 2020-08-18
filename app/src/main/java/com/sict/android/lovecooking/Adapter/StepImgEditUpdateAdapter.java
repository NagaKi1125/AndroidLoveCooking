package com.sict.android.lovecooking.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.DishInfoEditActivity;
import com.sict.android.lovecooking.Model.StepImage;
import com.sict.android.lovecooking.R;
import com.sict.android.lovecooking.ViewHolder.StepImgViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class StepImgEditUpdateAdapter extends RecyclerView.Adapter<StepImgViewHolder> {

    private List<StepImage> stepImages;
    private List<String> stepByStep;
    private Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    //private String url = "http://192.168.43.129:8000/";
    private String url = "http://192.168.0.101:8000/";
    //private String url = "http://lovecooking.herokuapp.com";
    public StepImgEditUpdateAdapter() {
    }

    public void setData(List<StepImage> stepImages, Context context) {
        this.stepImages = stepImages;
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        this.editor =sharedPreferences.edit();
        this.stepByStep = new ArrayList<>();
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
        holder.steps.setText(stepImages.get(position).getStep());
        if(stepImages.get(position).getImg().equals("null") ){
            // do nothing -- not showing image because it not have any image data
        }else if(stepImages.get(position).getImg().equals("") ){
            // do nothing -- not showing image because it not have any image data
        }else{
            byte[] decodeImageString = Base64.decode(stepImages.get(position).getImg(),Base64.DEFAULT);
            Bitmap decodeByteImg = BitmapFactory.decodeByteArray(decodeImageString,0,decodeImageString.length);
            holder.imgSteps.setImageBitmap(decodeByteImg);
        }
        //Picasso.get().load(url+stepImages.get(position).getImg()).into(holder.imgSteps);
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
                ((DishInfoEditActivity)context).startActivityForResult(
                        Intent.createChooser(gallery,"Chọn ảnh đại diện"),
                        position+1);
            }
        });

        holder.steps.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = s.toString();
                stepByStep.add(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                stepByStep.set(position,text);
            }
        });

    }

    public String getStep(int position){
        return stepByStep.get(position);
    }

    @Override
    public int getItemCount() {
        return stepImages.size();
    }
}
