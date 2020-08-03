package com.sict.android.lovecooking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Menu {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("menu_date")
    @Expose
    private String menuDate;
    @SerializedName("breakfast_list")
    @Expose
    private String breakfastList;
    @SerializedName("lunch_list")
    @Expose
    private String lunchList;
    @SerializedName("dinner_list")
    @Expose
    private String dinnerList;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Menu() {
    }

    public Menu(Integer id, Integer userId, String menuDate, String breakfastList, String lunchList,
                String dinnerList, String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.menuDate = menuDate;
        this.breakfastList = breakfastList;
        this.lunchList = lunchList;
        this.dinnerList = dinnerList;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(String menuDate) {
        this.menuDate = menuDate;
    }

    public String getBreakfastList() {
        return breakfastList;
    }

    public void setBreakfastList(String breakfastList) {
        this.breakfastList = breakfastList;
    }

    public String getLunchList() {
        return lunchList;
    }

    public void setLunchList(String lunchList) {
        this.lunchList = lunchList;
    }

    public String getDinnerList() {
        return dinnerList;
    }

    public void setDinnerList(String dinnerList) {
        this.dinnerList = dinnerList;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", userId=" + userId +
                ", menuDate='" + menuDate + '\'' +
                ", breakfastList='" + breakfastList + '\'' +
                ", lunchList='" + lunchList + '\'' +
                ", dinnerList='" + dinnerList + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
