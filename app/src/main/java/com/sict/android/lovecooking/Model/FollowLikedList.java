package com.sict.android.lovecooking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowLikedList {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("follow_list_names")
    @Expose
    private String followListNames;
    @SerializedName("dish_liked_list")
    @Expose
    private String dishLikedList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFollowListNames() {
        return followListNames;
    }

    public void setFollowListNames(String followListNames) {
        this.followListNames = followListNames;
    }

    public String getDishLikedList() {
        return dishLikedList;
    }

    public void setDishLikedList(String dishLikedList) {
        this.dishLikedList = dishLikedList;
    }
}
