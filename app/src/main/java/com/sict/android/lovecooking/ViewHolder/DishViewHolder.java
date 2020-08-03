package com.sict.android.lovecooking.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DishViewHolder extends RecyclerView.ViewHolder {
    public TextView author,date_create,dish_name,liked;
    public CircleImageView dish_image;
    public ImageButton love;
    public DishViewHolder(@NonNull View itemView) {
        super(itemView);
        author = (TextView)itemView.findViewById(R.id.author);
        dish_name = (TextView) itemView.findViewById(R.id.dish_name);
        dish_image = (CircleImageView) itemView.findViewById(R.id.dish_image);
        love = (ImageButton)itemView.findViewById(R.id.love);
        liked = (TextView)itemView.findViewById(R.id.liked);
    }
}
