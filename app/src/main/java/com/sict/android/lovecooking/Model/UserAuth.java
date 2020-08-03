package com.sict.android.lovecooking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAuth {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("level")
    @Expose
    private int level;
    @SerializedName("dish_liked")
    @Expose
    private String dishLiked;
    @SerializedName("follow")
    @Expose
    private String follow;

    public UserAuth(int id, String name, String username, int level, String dishLiked, String follow) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.level = level;
        this.dishLiked = dishLiked;
        this.follow = follow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getDishLiked() {
        return dishLiked;
    }

    public void setDishLiked(String dishLiked) {
        this.dishLiked = dishLiked;
    }

    public String getFollow() {
        return follow;
    }

    public void setFollow(String follow) {
        this.follow = follow;
    }

    @Override
    public String toString() {
        return "UserAuth{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", level=" + level +
                ", dishLiked='" + dishLiked + '\'' +
                ", follow='" + follow + '\'' +
                '}';
    }
}
