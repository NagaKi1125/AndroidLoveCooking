package com.sict.android.lovecooking;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.sict.android.lovecooking.Adapter.StepImgEditUpdateAdapter;
import com.sict.android.lovecooking.Model.Category;
import com.sict.android.lovecooking.Model.Dish;
import com.sict.android.lovecooking.Model.DishAdd;
import com.sict.android.lovecooking.Model.StepImage;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.ApplicationInfoServices;
import com.sict.android.lovecooking.Services.UserActivityServices;
import com.sict.android.lovecooking.ViewHolder.StepImgViewHolder;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishInfoEditActivity extends AppCompatActivity {
    private EditText dish_name,description,material,cateInput;
    private MaterialButton btnNewSteps, btnUpload, btnAddNewCate,btnCancel;
    private RecyclerView step_imgs;
    private TextView category;
    private ArrayList<String> categoriesName;
    private String[] categoriesGet;
    private List<StepImage> stepImages;
    private ImageButton back;
    private ImageView dishAva;
    private LinearLayout lnNewCate;
    private String cateName="", cate_id="",steps ="";
    private boolean [] cateChecked;
    private String dish_id;
    //private String url = "http://192.168.43.129:8000/";
    private String url = "http://192.168.0.101:8000/";
    //private String url = "http://lovecooking.herokuapp.com";

    private static final int PICK_IMAGE =0;
    String avatarImage = "null";
    String[] stepImage;

    StepImgEditUpdateAdapter stepImgEditUpdateAdapter;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ApplicationInfoServices applicationInfoServices;
    UserActivityServices userActivityServices;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_info_edit);

        sharedPreferences = getSharedPreferences("UserInfo",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        applicationInfoServices = RetrofitClient.getRetrofit().create(ApplicationInfoServices.class);
        userActivityServices = RetrofitClient.getRetrofit().create(UserActivityServices.class);
        dish_id = getIntent().getStringExtra("dish_id");

        categoriesName = new ArrayList<>();
        stepImage = new String[]{"null", "null", "null", "null", "null", "null", "null", "null", "null", "null"};


        getElementId();
        getCategories();

        //add and remove item
        step_imgs.setHasFixedSize(true);
        step_imgs.setLayoutManager(new LinearLayoutManager(this));

        stepImages = new ArrayList<>();
        stepImgEditUpdateAdapter = new StepImgEditUpdateAdapter();

        retrieveDishInfo();

        btnNewSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stepImages.size() >=10){
                    Toast.makeText(DishInfoEditActivity.this, "Tối đa 10 bước", Toast.LENGTH_SHORT).show();
                }else{
                    int position = stepImages.size();
                    stepImages.add(new StepImage("", ""));
                    stepImgEditUpdateAdapter.notifyItemInserted(position);
                    step_imgs.scrollToPosition(position);
                }
            }
        });
        //add and remove item --end

        material.setHint("Nhập Nguyên liệu \n" +
                "2 muỗng muối\n" +
                "3 muỗng mắm\n" +
                "400gr thịt bò");

        lnNewCate.setVisibility(View.GONE);

        dishAva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoriesSelection();
            }
        });

        btnAddNewCate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String category = cateInput.getText().toString();
                addNewCate(category);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEditRecipe();
            }
        });
    }

    private void uploadEditRecipe() {

        for(int i =0;i<stepImgEditUpdateAdapter.getItemCount();i++){
            steps = steps + stepImgEditUpdateAdapter.getStep(i)+"_";
        }

        DishAdd dishAdd = new DishAdd(
                dish_name.getText().toString(), cate_id, avatarImage,
                description.getText().toString(), "Món ăn ngon và bổ dưỡng cho gia đình",
                material.getText().toString(),steps,
                stepImage[0],stepImage[1],stepImage[2],stepImage[3],stepImage[4],
                stepImage[5],stepImage[6],stepImage[7],stepImage[8],stepImage[9]
        );
        Call<Dish> editRecipe = userActivityServices.dishEdit(
                "Bearer "+sharedPreferences.getString("token","null"),
                dish_id,dishAdd
        );
        editRecipe.enqueue(new Callback<Dish>() {
            @Override
            public void onResponse(Call<Dish> call, Response<Dish> response) {
                if (response.isSuccessful()){
                    Toast.makeText(DishInfoEditActivity.this,
                            "Dish edit successfully",
                            Toast.LENGTH_LONG).show();
                    int id = response.body().getId();
                    String dish_name = response.body().getDishName();
                    String cate_id = response.body().getCateId();
                    String avatar =  "http://lovecooking.herokuapp.com/"+response.body().getAvatar();
                    String updated_at = response.body().getUpdatedAt();
                    String created_at = response.body().getCreatedAt();
                    String description = response.body().getDescription();
                    String use = response.body().getUse();
                    String material =response.body().getMaterial();
                    String steps = response.body().getSteps();
                    String step_imgs = response.body().getStepImgs();
                    String author = response.body().getAuthor();
                    int liked_count = response.body().getLikedCount();
                    List<Dish.History> dhpost = response.body().getHistory();
                    List<Dish.Comment> comment = response.body().getCmt();
                    String post = "";String cmt= "";

                    for(Dish.History hispost: dhpost){
                        post+=hispost.getDhposts()+"_";
                    }

                    for(Dish.Comment comments : comment){
                        cmt+=comments.getCommentID()+"#"+comments.getName()+"#"+comments.getComment()+"#"
                                +comments.getCmt_updated_at()+"_";
                    }
                    String dishcmt = cmt;
                    String historyPost = post;
                    sendDishInfo(id,dish_name,cate_id,avatar,description,use,material,steps,step_imgs,
                            author,liked_count,historyPost,dishcmt,created_at,updated_at);
                }else{
                    Toast.makeText(DishInfoEditActivity.this,
                            "Dish edit Failed\n"+response.message()+"\n"+
                                    response.errorBody(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Dish> call, Throwable t) {
                Toast.makeText(DishInfoEditActivity.this,
                        "Serve could not response\n"+t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void categoriesSelection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn 1 hoặc nhiều danh mục cho món ăn")
                .setMultiChoiceItems(categoriesGet, cateChecked, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            categoriesName.add(categoriesGet[which]);
                            cateChecked[which] = true;
                        }else{
                            categoriesName.remove(categoriesGet[which]);
                            cateChecked[which] = false;
                        }
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(DishInfoEditActivity.this,
                                categoriesName.toString(),
                                Toast.LENGTH_LONG).show();
                        cateName="";cate_id="";
                        for (int i =0 ;i<categoriesName.size();i++){
                            String [] split = categoriesName.get(i).split("_");
                            cateName = cateName + split[1]+" - ";
                            cate_id = cate_id + split[0]+"_";
                        }
                        category.setText(cateName);
                    }
                }).setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setNeutralButton("Nhập mới", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lnNewCate.setVisibility(View.VISIBLE);
                category.setVisibility(View.GONE);
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void addNewCate(String cate){
        Call<Category>  cateAdd = userActivityServices.addNewCategory(
                "Bearer "+sharedPreferences.getString("token","null"),
                cate);
        cateAdd.enqueue(new Callback<Category>() {
            @Override
            public void onResponse(Call<Category> call, Response<Category> response) {
                if (response.isSuccessful()){

                    getCategories();
                    Toast.makeText(DishInfoEditActivity.this,
                            "Success",
                            Toast.LENGTH_LONG).show();
                    cateInput.setText("");
                    lnNewCate.setVisibility(View.GONE);
                    category.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(DishInfoEditActivity.this,
                            "Failed\n"+response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Category> call, Throwable t) {
                Toast.makeText(DishInfoEditActivity.this,
                        "Failed\nServe could not response"+t.toString(),
                        Toast.LENGTH_LONG).show();
                cateInput.setText("");
            }
        });
    }
    public void getCategories(){
        Call<List<Category>> listCateCall = applicationInfoServices.getCategory();
        listCateCall.enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
                if(response.isSuccessful()){
                    Toast.makeText(DishInfoEditActivity.this,
                            "Category retrieve success",
                            Toast.LENGTH_LONG).show();
                    categoriesGet = new String[response.body().size()];
                    cateChecked = new boolean[response.body().size()];
                    for(int i =0;i<response.body().size();i++){
                        String cate = response.body().get(i).getId()
                                + "_" + response.body().get(i).getCategory();
                        categoriesGet[i] = cate;
                        cateChecked[i] = false;
                    }

                }else{
                    Toast.makeText(DishInfoEditActivity.this,
                            "Could not receive response",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Category>> call, Throwable t) {
                Toast.makeText(DishInfoEditActivity.this,
                        "Serve not response\n"+t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void retrieveDishInfo() {
        Call<Dish> getDishInfo = applicationInfoServices.getDishInfo(dish_id);
        getDishInfo.enqueue(new Callback<Dish>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<Dish> call, Response<Dish> response) {
                if(response.isSuccessful()){
                    Picasso.get().load(url+response.body().getAvatar()).into(dishAva);
                    dish_name.setText(response.body().getDishName());
                    description.setText(response.body().getDescription());
                    material.setText(response.body().getMaterial());
                    String[] steps = response.body().getSteps().split("_");
                    String[] stepImgs = response.body().getStepImgs().split("_");
                    avatarImage = response.body().getAvatar();
                    for(int i = 0;i<steps.length;i++){
                        stepImages.add(new StepImage(steps[i],stepImgs[i]));
                        stepImage[i] = stepImgs[i];
                    }
                    stepImgEditUpdateAdapter.setData(stepImages,DishInfoEditActivity.this);
                    step_imgs.setAdapter(stepImgEditUpdateAdapter);
                    cateChecked = new boolean[categoriesGet.length];
                    String[] cate = response.body().getCateId().split("_");
                    for(String s:cate){
                        String[] dishCate = s.split("#");
                        cateName= cateName + dishCate[1]+" - ";
                        cate_id = cate_id + dishCate[0]+"_";
                    }
                    category.setText(cateName);
                }else{
                    Toast.makeText(DishInfoEditActivity.this,
                            "Retrieve Failed\n"+response.message(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Dish> call, Throwable t) {
                Toast.makeText(DishInfoEditActivity.this,
                        "Serve could not response\n"+t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data !=null){
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                String ext = getContentResolver().getType(imageUri);
                dishAva.setImageBitmap(bitmap);
                avatarImage = "data:"+ext+";base64,"+getStringImage(bitmap);
                Log.d("avatar",avatarImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(resultCode == RESULT_OK && data !=null){
            StepImgViewHolder imgViewHolder =
                    (StepImgViewHolder)step_imgs.findViewHolderForAdapterPosition(requestCode-1);
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                String ext = getContentResolver().getType(uri);
                ImageView imageView = imgViewHolder.imgSteps;
                imageView.setImageBitmap(bitmap);
                stepImage[requestCode-1] = "data:"+ext+";base64,"+getStringImage(bitmap);
                Log.d("step" + (requestCode-1),stepImage[requestCode-1]);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
        return encodedImage;
    }

    private void sendDishInfo(int id, String dish_name, String cate_id, String avatar, String description,
                              String use, String material, String steps, String step_imgs,
                              String author, int liked_count, String historyPost, String dishcmt,
                              String created_at, String updated_at) {

        Intent intent = new Intent(DishInfoEditActivity.this, DishInfoActivity.class);
        intent.putExtra("Did",id);
        intent.putExtra("dish_name",dish_name);
        intent.putExtra("cate_id",cate_id);
        intent.putExtra("description",description);
        intent.putExtra("avatar",avatar);
        intent.putExtra("use",use);
        intent.putExtra("material",material);
        intent.putExtra("steps",steps);
        intent.putExtra("step_imgs",step_imgs);
        intent.putExtra("created_at",created_at);
        intent.putExtra("updated_at",updated_at);
        intent.putExtra("author",author);
        intent.putExtra("like_count",liked_count);
        intent.putExtra("hispost", historyPost);
        intent.putExtra("cmt",dishcmt);
        finish();startActivity(intent);
    }
    
    private void getElementId() {
        dish_name = (EditText) findViewById(R.id.dish_name);
        description = (EditText) findViewById(R.id.des);
        material =(EditText) findViewById(R.id.material);
        step_imgs = (RecyclerView) findViewById(R.id.step_and_img);
        btnNewSteps = (MaterialButton)findViewById(R.id.addNewStep);
        category = (TextView) findViewById(R.id.category);
        back = (ImageButton) findViewById(R.id.back);
        dishAva = (ImageView)findViewById(R.id.dish_ava);
        btnUpload = (MaterialButton)findViewById(R.id.addDish);
        cateInput = (EditText)findViewById(R.id.cateAdd);
        btnAddNewCate = (MaterialButton)findViewById(R.id.btnCateSave);
        lnNewCate = (LinearLayout)findViewById(R.id.newCategory);
        btnCancel = (MaterialButton)findViewById(R.id.cancel);
    }
    private void pickImage() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(gallery,"Chọn ảnh đại diện"),
                PICK_IMAGE);
    }
   
    
    public void back(){
        Intent intent = new Intent(DishInfoEditActivity.this, MainActivity.class);
        finish();startActivity(intent);
    }
}