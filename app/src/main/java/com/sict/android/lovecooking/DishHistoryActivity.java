package com.sict.android.lovecooking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DishHistoryActivity extends AppCompatActivity {

    private ImageButton arrowBack;
    private TextView dishname,authorName,history;
    private ImageView dishAvatar;
    private CircleImageView authorAvatar;
    private String dishName,avatar, historyPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_history);

        init();
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        getComponentID();
        intentGetDishInfo();
        Picasso.get().load(avatar).into(dishAvatar);
        history.setText(historyPost);
        dishname.setText(dishName);
    }

    public void intentGetDishInfo(){
        Intent intent = getIntent();
        dishName = intent.getStringExtra("dish_name");
        avatar = intent.getStringExtra("dish_ava");
        historyPost = intent.getStringExtra("history");
    }

    public void getComponentID(){

        dishname = (TextView)findViewById(R.id.dish_name);
        authorName = (TextView)findViewById(R.id.author);
        dishAvatar = (ImageView)findViewById(R.id.dish_image);
        authorAvatar = (CircleImageView)findViewById(R.id.author_avatar);
        arrowBack= (ImageButton)findViewById(R.id.arrowBack);
        history = (TextView)findViewById(R.id.post);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}