package com.sict.android.lovecooking.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sict.android.lovecooking.Model.Comment;
import com.sict.android.lovecooking.Model.CommentEdit;
import com.sict.android.lovecooking.R;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.ApplicationInfoServices;
import com.sict.android.lovecooking.Services.UserActivityServices;
import com.sict.android.lovecooking.ViewHolder.CommentViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class CommentAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private List<Comment> cmtList;
    private Context context;
    private String id;
    //private String url = "http://192.168.43.129:8000/";
    private String url = "http://192.168.0.101:8000/";
    //private String url = "http://lovecooking.herokuapp.com";
    ApplicationInfoServices applicationInfoServices;
    UserActivityServices userActivityServices;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public CommentAdapter() {
    }

    public void setData(List<Comment> cmtList, Context context,String id) {
        this.cmtList = cmtList;
        this.context = context;
        this.id = id;
        this.sharedPreferences = context.getSharedPreferences("UserInfo",MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
        this.applicationInfoServices =
                RetrofitClient.getRetrofit().create(ApplicationInfoServices.class);
        this.userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item,parent,false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.UserName.setText(cmtList.get(position).getName());
        holder.UserComment.setText(cmtList.get(position).getComment());
        holder.cmtEdit.setVisibility(View.GONE);
        Picasso.get().load(url+cmtList.get(position).getAvatar()).into(holder.userAvatar);
        if(cmtList.get(position).getUserId() == sharedPreferences.getInt("id",1)){
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    PopupMenu popupMenu = new PopupMenu(context,holder.itemView, Gravity.CENTER);
                    popupMenu.inflate(R.menu.comment_context_menu);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.cmt_edit:
                                    holder.cmtShow.setVisibility(View.GONE);
                                    holder.cmtEdit.setVisibility(View.VISIBLE);
                                    holder.editCmtText.setText(holder.UserComment.getText().toString());
                                    holder.saveEdit.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Call<CommentEdit> cmtEditCall = userActivityServices.editComment(
                                                    "Bearer "+sharedPreferences.getString("token","null"),
                                                    cmtList.get(position).getId(),
                                                    holder.editCmtText.getText().toString());
                                            cmtEditCall.enqueue(new Callback<CommentEdit>() {
                                                @Override
                                                public void onResponse(Call<CommentEdit> call, Response<CommentEdit> response) {
                                                    if(response.isSuccessful()){
                                                        holder.UserComment.setText(response.body().getComment());
                                                        holder.cmtEdit.setVisibility(View.GONE);
                                                        holder.cmtShow.setVisibility(View.VISIBLE);
                                                        Toast.makeText(context,
                                                                "Edit "+response.body().getComment(),
                                                                Toast.LENGTH_LONG).show();
                                                    }else{
                                                        Toast.makeText(context,
                                                                "Token not correct",
                                                                Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Call<CommentEdit> call, Throwable t) {
                                                    Toast.makeText(context,
                                                            "Serve could not response",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    });
                                    return true;
                                case R.id.cmt_del:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                                    builder.setTitle("Bạn có chắc muốn xóa bình luận này");
                                    builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteComment(cmtList.get(position).getId(),position);
                                        }
                                    });
                                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                    return true;
                            }
                            return true;
                        }
                        //end menu click
                    });
                    popupMenu.show();
                    return true;
                } // end menu
            }); //end long click
        }// end if



    }

    private void deleteComment(Integer cmtId,int position) {
        Call<CommentEdit> cmtDeleteCall = userActivityServices.deleteComment(
                "Bearer "+sharedPreferences.getString("token","null"), cmtId
        );
        cmtDeleteCall.enqueue(new Callback<CommentEdit>() {
            @Override
            public void onResponse(Call<CommentEdit> call, Response<CommentEdit> response) {
                if(response.isSuccessful()){
                    cmtList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,cmtList.size());
                }else{
                    Toast.makeText(context,
                            "Token not correct",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<CommentEdit> call, Throwable t) {
                Toast.makeText(context,
                        "Serve could not response",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cmtList.size();
    }


}
