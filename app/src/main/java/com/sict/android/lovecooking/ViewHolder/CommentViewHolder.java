package com.sict.android.lovecooking.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentViewHolder extends RecyclerView.ViewHolder {
    public CircleImageView userAvatar;
    public TextView UserName,UserComment,dateTime;
    public EditText editCmtText;
    public ImageButton saveEdit;
    public LinearLayout cmtEdit;
    public RelativeLayout cmtShow;


    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        cmtShow = itemView.findViewById(R.id.cmt_show);

        userAvatar = itemView.findViewById(R.id.userAvatar);
        UserName = itemView.findViewById(R.id.userName);
        UserComment = itemView.findViewById(R.id.userCmt);

        cmtEdit = itemView.findViewById(R.id.cmt_edit);

        editCmtText = itemView.findViewById(R.id.cmt_edit_text);
        saveEdit = itemView.findViewById(R.id.save_edit);

        dateTime = itemView.findViewById(R.id.cmtTime);
    }
}
