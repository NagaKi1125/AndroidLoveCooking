package com.sict.android.lovecooking.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListDishViewHolder extends RecyclerView.ViewHolder {

    public TextView dishName;
    public ImageView dishAva;
    public CircleImageView remove;

    public ListDishViewHolder(@NonNull View itemView) {
        super(itemView);

        remove = (CircleImageView)itemView.findViewById(R.id.menu_remove);
        dishName = (TextView)itemView.findViewById(R.id.dish_name);
        dishAva = (ImageView) itemView.findViewById(R.id.dish_ava);
    }
}
