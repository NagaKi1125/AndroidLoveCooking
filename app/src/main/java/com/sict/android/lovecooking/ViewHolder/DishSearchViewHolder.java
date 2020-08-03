package com.sict.android.lovecooking.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.R;

public class DishSearchViewHolder extends RecyclerView.ViewHolder {

    public TextView dishName,author,description;
    public ImageView avatar;

    public DishSearchViewHolder(@NonNull View itemView) {
        super(itemView);
        dishName = (TextView)itemView.findViewById(R.id.dishname);
        author = (TextView)itemView.findViewById(R.id.author);
        description = (TextView)itemView.findViewById(R.id.description);
        avatar = (ImageView)itemView.findViewById(R.id.avatar);
    }
}
