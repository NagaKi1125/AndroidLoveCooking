package com.sict.android.lovecooking.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.sict.android.lovecooking.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class FollowListViewHolder extends RecyclerView.ViewHolder {

    public TextView usName;
    public CircleImageView usAvatar;
    public MaterialButton btnFollow;

    public FollowListViewHolder(@NonNull View itemView) {
        super(itemView);
        usName = (TextView) itemView.findViewById(R.id.usName);
        usAvatar = (CircleImageView)itemView.findViewById(R.id.usAvatar);
        btnFollow = (MaterialButton)itemView.findViewById(R.id.btnFollow);

    }
}
