package com.sict.android.lovecooking.Services;

import com.sict.android.lovecooking.Model.Category;
import com.sict.android.lovecooking.Model.Comment;
import com.sict.android.lovecooking.Model.CommentEdit;
import com.sict.android.lovecooking.Model.Dish;
import com.sict.android.lovecooking.Model.DishAdd;
import com.sict.android.lovecooking.Model.UserLikedList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserActivityServices {

    //logout
    @GET("logout")
    Call<ResponseBody> logout (@Header("Authorization") String token);

    //userliked
    //http://lovecooking.herokuapp.com/api/dishes/{id}/love
    @PUT("dishes/{id}/love")
    Call<UserLikedList> loveThis(@Header("Authorization") String token, @Path("id") int id);

    //userunliked
    //http://lovecooking.herokuapp.com/api/dishes/{id}/loveless
    @PUT("dishes/{id}/loveless")
    Call<UserLikedList> notLoveThis(@Header("Authorization") String token,@Path("id") int id);

    //user new menu
    //https://lovecooking.herokuapp.com/api/menu-new
    @POST("menu-new")
    @FormUrlEncoded
    Call<ResponseBody> MakeNewMenu(@Header("Authorization") String token,
                                   @Field("menu_date")String date);

    //user add dish to menu
    //https://lovecooking.herokuapp.com/api/menu-add/{id}
    @POST("menu-add/{id}")
    @FormUrlEncoded
    Call<ResponseBody> menuAddDish(@Header("Authorization") String token,
                                   @Path("id")int id,
                                   @Field("breakfast") String breakfast,
                                   @Field("lunch") String lunch,
                                   @Field("dinner") String dinner);

    //user remove dish from menu
    //https://lovecooking.herokuapp.com/api/menu-remove/{id}
    @FormUrlEncoded
    @POST("menu-remove/{id}")
    Call<ResponseBody> menuRemoveDish(@Header("Authorization") String token,
                                  @Path("id")int id,
                                  @Field("breakfast") String breakfast,
                                  @Field("lunch") String lunch,
                                  @Field("dinner") String dinner);

    //user delete menu
    //https://lovecooking.herokuapp.com/api/menu-delete/{id}
    @DELETE("menu-delete/{id}")
    Call<ResponseBody> deleteMenu(@Header("Authorization") String token,
                                  @Path("id")int id);

    //user post dish
    @POST("dishes/upload")
    Call<Dish> postNewDish(@Header("Authorization") String token,
                           @Body DishAdd dishAdd);
    //user post edit
    @POST("dishes-edit/{id}")
    Call<Dish> dishEdit(@Header("Authorization") String token,
                        @Path("id") String id,
                        @Body DishAdd dishAdd);


    //user delete dish
    @DELETE("dishes-delete/{id}")
    Call<ResponseBody> deleteDish(@Header("Authorization") String token,
                                  @Path("id")String dishId);

    //user comment
    @FormUrlEncoded
    @POST("dishes/{id}/comment")
    Call<Comment> postComment(@Header("Authorization") String token,
                              @Path("id") int dishId,
                              @Field("comment") String comment);

    @FormUrlEncoded
    @POST("dishes/comment/{cmtid}/edit")
    Call<CommentEdit> editComment(@Header("Authorization") String token,
                                  @Path("cmtid") int cmtId,
                                  @Field("comment") String comment);

    @DELETE("dishes/comment/{cmtid}/delete")
    Call<CommentEdit> deleteComment(@Header("Authorization") String token,
                                     @Path("cmtid") int cmtId);

    // category add
    @POST("cate-add")
    @FormUrlEncoded
    Call<Category> addNewCategory(@Header("Authorization") String token,
                                  @Field("category") String cate);

    //follow and unfollow
    @POST("unfollow/{fid}")
    Call<ResponseBody> unfollow(@Header("Authorization") String token,
                                @Path("fid") String fid);

    //user information update (without password)
    @POST("user-update")
    @FormUrlEncoded
    Call<ResponseBody> updateUserInfo(@Header("Authorization") String token,
                                      @Field("name") String name,
                                      @Field("username") String username,
                                      @Field("avatar") String avatar,
                                      @Field("gender") int gender,
                                      @Field("address")String address,
                                      @Field("birthday")String birthday);

    //user information update (change password)
    @POST("user-change-password")
    @FormUrlEncoded
    Call<ResponseBody> changePassword(@Header("Authorization") String token,
                                      @Field("password") String pass,
                                      @Field("oldPass")String oldPass);

}
