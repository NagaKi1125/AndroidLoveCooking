package com.sict.android.lovecooking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuList {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("menu_date")
    @Expose
    public String menuDate;
    @SerializedName("breakfast_list")
    @Expose
    public String breakfastList;
    @SerializedName("lunch_list")
    @Expose
    public String lunchList;
    @SerializedName("dinner_list")
    @Expose
    public String dinnerList;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;

    public MenuList(Integer id, Integer userId, String menuDate, String breakfastList,
                    String lunchList, String dinnerList, String createdAt, String updatedAt) {
        this.id = id;
        this.userId = userId;
        this.menuDate = menuDate;
        this.breakfastList = breakfastList;
        this.lunchList = lunchList;
        this.dinnerList = dinnerList;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public MenuList() {
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
        return "MenuList{" +
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
