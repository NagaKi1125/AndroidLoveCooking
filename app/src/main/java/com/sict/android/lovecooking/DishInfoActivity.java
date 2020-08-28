package com.sict.android.lovecooking;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.github.marlonlom.utilities.timeago.TimeAgoMessages;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.sict.android.lovecooking.Adapter.CommentAdapter;
import com.sict.android.lovecooking.Model.Comment;
import com.sict.android.lovecooking.Model.Dish;
import com.sict.android.lovecooking.Model.UserLikedList;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.ApplicationInfoServices;
import com.sict.android.lovecooking.Services.UserActivityServices;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishInfoActivity extends AppCompatActivity {

    private LinearLayout material,stepAndImg,category;
    private MaterialButton follow;
    private FrameLayout overlayView;
    private ImageButton love,arrowBack,sendComment, btnEdit,btnDelete,btnMenuAdd,overlayBackButton;
    private TextView dishname,authorName,date,liked,history,overlayTxt,mate;
    private ImageView dishAvatar;
    private CircleImageView authorAvatar;
    private PhotoView overlayImage;
    private EditText textCmt;
    private int btnLoveClick=0,btnFollowClick=0;
    //private String url = "http://192.168.43.129:8000/";
    //private String url = "http://192.168.0.101:8000/";
    //private String url = "http://lovecooking.herokuapp.com";
    private String url;

    RecyclerView comment;
    CommentAdapter commentAdapter;

    UserActivityServices userActivityServices ;
    ApplicationInfoServices applicationInfoServices;

    SharedPreferences sharedPreferences ;
    SharedPreferences.Editor editor;


    //dish Info, id, name, cateid, avatar, des, use,material, steps,stepimgs, author,like,created,updated
    private String id;
    private String dishName;
    private String cateID;
    private String avatar;
    private String des;
    private String use;
    private String materialList;
    private String steps;
    private String stepImgs;
    private String author;
    private int like;
    private String created;
    private String updated;
    private String historyPost;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_info);
        getComponentID();

        sharedPreferences = this.getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        url = sharedPreferences.getString("url","http://192.168.0.101:8000/");
        userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);
        applicationInfoServices = RetrofitClient.getRetrofit().create(ApplicationInfoServices.class);
        intentGetDishInfo();
        overlayView.setVisibility(View.GONE);
        comment.setHasFixedSize(true);
        comment.setLayoutManager(new LinearLayoutManager(this));

        if(!author.equals(sharedPreferences.getString("full_name","Admin - Love Cooking"))){
            btnDelete.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
        }

        init();

        love.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                btnLoveClick++;
                if(btnLoveClick==2) {
                    Call<UserLikedList> unlove = userActivityServices.notLoveThis(
                            "Bearer "+sharedPreferences.getString("token","null"),Integer.parseInt(id));
                    unlove.enqueue(new Callback<UserLikedList>() {
                        @Override
                        public void onResponse(Call<UserLikedList> call, Response<UserLikedList> response) {
                            if(response.isSuccessful()){
                                love.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                btnLoveClick=0;
                                like-=1;
                                liked.setText(like + " lượt thích");
                                //dishLiked = removeDishUnlikedId(dishLiked,id);
                                editor.putString("dish_liked",response.body().getDishIdList());
                                editor.apply();
                            }else{
                                Toast.makeText(v.getContext(),"Could not get any response",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLikedList> call, Throwable t) {
                            Toast.makeText(v.getContext(),
                                    "Serve could not response",Toast.LENGTH_LONG).show();                        }
                    });

                } else{
                    Call<UserLikedList> addLove = userActivityServices.loveThis(
                            "Bearer "+sharedPreferences.getString("token","null"),Integer.parseInt(id));
                    addLove.enqueue(new Callback<UserLikedList>() {
                        @Override
                        public void onResponse(Call<UserLikedList> call, Response<UserLikedList> response) {
                            if(response.isSuccessful()){
                                love.setImageResource(R.drawable.ic_favorite_black_24dp);
                                like+=1;
                                liked.setText(like + " lượt thích");
                                //dishLiked = addDishLikedId(dishLiked,id);
                                editor.putString("dish_liked",response.body().getDishIdList());
                                editor.apply();
                            }else{
                                Toast.makeText(v.getContext(),"Could not get any response",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<UserLikedList> call, Throwable t) {
                            Toast.makeText(v.getContext(),
                                    "Serve could not response",Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }
        });

        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishInfoActivity.this,DishHistoryActivity.class);
                intent.putExtra("history",historyPost);
                intent.putExtra("dish_name",dishName);
                intent.putExtra("dish_ava",avatar);
                finish();startActivity(intent);
            }
        });

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textComment = textCmt.getText().toString();

                Call<Comment> postComment = userActivityServices.postComment(
                        "Bearer "+sharedPreferences.getString("token","null"),
                        Integer.parseInt(id),
                        textComment);
                postComment.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if(response.isSuccessful()){
                            textCmt.setText("");
                            getDishComment(id);
                        }else{
                            Toast.makeText(DishInfoActivity.this,
                                    "Token or id not correct",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Toast.makeText(DishInfoActivity.this,
                                "Serve could not response",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        //delete dish
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseBody> deleteDish = userActivityServices.deleteDish(
                        "Bearer "+sharedPreferences.getString("token","null"),id
                );
                deleteDish.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Intent intent = new Intent(DishInfoActivity.this,MainActivity.class);
                            finish();startActivity(intent);
                            Toast.makeText(DishInfoActivity.this,
                                    "Dish delete success\n"+response.message(),
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(DishInfoActivity.this,
                                    "Dish delete Failed\n"+response.message(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(DishInfoActivity.this,
                                "Serve could not response\n"+t.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DishInfoActivity.this,DishInfoEditActivity.class);
                intent.putExtra("dish_id",id);
                finish();startActivity(intent);
            }
        });


        btnMenuAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menuAdd = new PopupMenu(DishInfoActivity.this,btnMenuAdd, Gravity.CENTER);
                menuAdd.inflate(R.menu.dish_menu);
                menuAdd.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.alreadyMenu:
                                Intent intent = new Intent(DishInfoActivity.this,MenuAddActivity.class);
                                intent.putExtra("dish_id",id);
                                intent.putExtra("dish_avatar",avatar);
                                intent.putExtra("dish_name",dishName);
                                finish();startActivity(intent);
                                return true;
                            case R.id.newMenu:
                                makeNewMenu();
                                return true;
                            default: return false;
                        }
                    }
                });
                menuAdd.show();
            }
        });

        overlayBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayView.setVisibility(View.GONE);
            }
        });
        overlayTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayView.setVisibility(View.GONE);
            }
        });
    }

    private void makeNewMenu() {
        AlertDialog.Builder newMenuBuilder = new AlertDialog.Builder(DishInfoActivity.this);
        View view = LayoutInflater.from(this).inflate(R.layout.alert_dialog_date_picker_view,null,false);
        DatePicker datePicker = view.findViewById(R.id.datePicker1);
        newMenuBuilder.setTitle("Chọn ngày để tạo thực đơn");
        newMenuBuilder.setView(view);
        newMenuBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String date = datePicker.getYear() +"-"+(datePicker.getMonth()+1)+"-"+datePicker.getDayOfMonth();
                Call<ResponseBody> addNewMenu = userActivityServices.MakeNewMenu(
                        "Bearer "+sharedPreferences.getString("token","null"),date);
                addNewMenu.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(DishInfoActivity.this,
                                    "Make new menu Success\n"+response.message(),
                                    Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(DishInfoActivity.this,
                                    "Make new menu Failed\n"+response.message(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(DishInfoActivity.this,
                                "Make new menu Failed\n"+t.toString(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        })
        .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog newMenu = newMenuBuilder.create();
        newMenu.show();
    }


    private void back() {
        Intent intent = new Intent(DishInfoActivity.this, MainActivity.class);
        finish();startActivity(intent);
    }

    private void init() {
        //split material list into array String[]
        String[] mateList = materialList.split("\n");

        //split steps and img list into array String[]
        String[] stepsList = steps.split("_");
        String[] stepimgsList = stepImgs.split("_");

        //String[] datecreate = created.split("T");
        String[] date_created = created.split("_");
        String d = getDate(Long.parseLong(date_created[1]),"E, dd MMM yyyy#HH:mm:ss");
        String[] dSplit = d.split("#");
        //split cate
        String[] cate = cateID.split("_");

        //cmt
        getDishComment(id);
        //cmt

        //material
        mate.setText("Nguyên Liệu ("+mateList.length+" loại nguyên liệu):");
        for (int i=0;i<mateList.length;i++) {
            TextView textView = new TextView(this);
            String mate = "- "+mateList[i];
            textView.setText(mate);
            material.addView(textView);
        }

        //steps and image
        for(int i=0;i<stepsList.length;i++){
            TextView textView = new TextView(this);
            int b =i+1;
            textView.setText("  "+stepsList[i]);
            TextView txt = new TextView(this);
            txt.setText("Bước : "+b);
            txt.setTypeface(Typeface.DEFAULT_BOLD);
            stepAndImg.addView(txt);
            stepAndImg.addView(textView);

            ImageView imageView = new ImageButton(this);
            if(!stepimgsList[i].equals("null")){
                String path =url+stepimgsList[i];
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        overlayView.setVisibility(View.VISIBLE);
                        Picasso.get().load(path).into(overlayImage);
                    }
                });
                stepAndImg.addView(imageView);
                Picasso.get().load(path).into(imageView);
            }
        }

        for(int i=0;i<cate.length;i++){
            TextView textView = new TextView(this);
            String[] ct = cate[i].split("#");
            String text = (i+1)+". "+ct[1];
            textView.setText(text);
            category.addView(textView);
        }
        Picasso.get().load(avatar).into(dishAvatar);
        dishAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overlayView.setVisibility(View.VISIBLE);
                Picasso.get().load(avatar).into(overlayImage);
            }
        });
        dishname.setText(dishName);
        authorName.setText(author);
        date.setText(date_created[0]+", "+dSplit[0]+"\nVào lúc "+dSplit[1]);
        liked.setText(like + " lượt thích");
    
        //like button
        if(!sharedPreferences.getString("dish_liked","_").contains(id)){
            btnLoveClick=0;
            love.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }else{
            btnLoveClick=1;
            love.setImageResource(R.drawable.ic_favorite_black_24dp);
        }

        //follow button
        String userId = String.valueOf(sharedPreferences.getInt("id", 0));
        if(sharedPreferences.getString("follow","_").contains(userId)){
            btnFollowClick=0;
            follow.setText("Theo dõi");
        }else{
            btnFollowClick=1;
            follow.setText("Hủy theo dõi");
        }
    }

    private void getDishComment(String dishID) {
        Call<List<Comment>> getDishesComment = applicationInfoServices.getDishComment(dishID);
        getDishesComment.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.isSuccessful()){
                    commentAdapter = new CommentAdapter();
                    commentAdapter.setData(response.body(),DishInfoActivity.this,id);
                    comment.setAdapter(commentAdapter);
                }else {
                    Toast.makeText(DishInfoActivity.this,
                            "Could not get comments",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(DishInfoActivity.this,
                        "Serve could not response",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public String getDate(long milliseconds,String dateFormat){
        SimpleDateFormat format = new SimpleDateFormat(dateFormat,Locale.forLanguageTag("vi"));
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return format.format(calendar.getTime());
    }


    public void intentGetDishInfo(){
        Intent intent = getIntent();
        id = String.valueOf(intent.getIntExtra("Did",0));
        dishName = intent.getStringExtra("dish_name");
        avatar = intent.getStringExtra("avatar");
        cateID = intent.getStringExtra("cate_id");
        des = intent.getStringExtra("description");
        use =intent.getStringExtra("use");
        materialList =intent.getStringExtra("material");
        steps = intent.getStringExtra("steps");
        stepImgs = intent.getStringExtra("step_imgs");
        author =intent.getStringExtra("author");
        like = intent.getIntExtra("like_count",0);
        created= intent.getStringExtra("created_at");
        updated = intent.getStringExtra("updated_at");
        historyPost = intent.getStringExtra("hispost");
    }

    public void getComponentID(){
        material = findViewById(R.id.material);
        stepAndImg = findViewById(R.id.step_and_img);
        category = findViewById(R.id.category);
        love = findViewById(R.id.love);
        dishname = findViewById(R.id.dish_name);
        date = findViewById(R.id.date);
        mate = findViewById(R.id.mate);
        liked = findViewById(R.id.liked);
        dishAvatar = findViewById(R.id.dish_image);
        authorAvatar = findViewById(R.id.author_avatar);
        authorName = findViewById(R.id.author);
        arrowBack= findViewById(R.id.arrowBack);
        comment = findViewById(R.id.cmt);
        history = findViewById(R.id.history);
        sendComment = findViewById(R.id.send);
        textCmt = findViewById(R.id.textcmt);
        btnEdit = findViewById(R.id.editDish);
        btnDelete = findViewById(R.id.deleteDish);
        btnMenuAdd = findViewById(R.id.addDishMenu);
        overlayView = findViewById(R.id.overlayView);
        overlayImage = findViewById(R.id.overlayImage);
        overlayBackButton = findViewById(R.id.overlayBackButton);
        overlayTxt = findViewById(R.id.overlayTxt);
        follow = (MaterialButton)findViewById(R.id.follow);
    }
}