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
    public TextView UserName,UserComment;
    public EditText editCmtText;
    public ImageButton saveEdit;
    public LinearLayout cmtEdit;
    public RelativeLayout cmtShow;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);
        cmtShow = (RelativeLayout)itemView.findViewById(R.id.cmt_show);

        userAvatar = (CircleImageView)itemView.findViewById(R.id.userAvatar);
        UserName = (TextView)itemView.findViewById(R.id.userName);
        UserComment = (TextView)itemView.findViewById(R.id.userCmt);

        cmtEdit = (LinearLayout)itemView.findViewById(R.id.cmt_edit);

        editCmtText = (EditText)itemView.findViewById(R.id.cmt_edit_text);
        saveEdit = (ImageButton)itemView.findViewById(R.id.save_edit);
    }
}
