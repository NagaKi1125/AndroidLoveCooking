package com.sict.android.lovecooking.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuViewHolder extends RecyclerView.ViewHolder {

    public TextView menuDate;
    public CircleImageView btndel;

    public MenuViewHolder(@NonNull View itemView) {
        super(itemView);
        menuDate = (TextView)itemView.findViewById(R.id.menu_date);
        btndel = (CircleImageView)itemView.findViewById(R.id.del);
    }
}
