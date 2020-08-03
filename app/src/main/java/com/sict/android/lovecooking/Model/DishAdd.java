package com.sict.android.lovecooking.Model;

import com.google.gson.annotations.SerializedName;

public class DishAdd {
    @SerializedName("dish_name")
    private String dishName;
    @SerializedName("cate_id")
    private String cateId;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("description")
    private String des;
    @SerializedName("use")
    private String use;
    @SerializedName("material")
    private String material;
    @SerializedName("steps")
    private String steps;
    @SerializedName("step_imgs1")
    private String stepImgs1;
    @SerializedName("step_imgs2")
    private String stepImgs2;
    @SerializedName("step_imgs3")
    private String stepImgs3;
    @SerializedName("step_imgs4")
    private String stepImgs4;
    @SerializedName("step_imgs5")
    private String stepImgs5;
    @SerializedName("step_imgs6")
    private String stepImgs6;
    @SerializedName("step_imgs7")
    private String stepImgs7;
    @SerializedName("step_imgs8")
    private String stepImgs8;
    @SerializedName("step_imgs9")
    private String stepImgs9;
    @SerializedName("step_imgs10")
    private String stepImgs10;

    public DishAdd() {
    }

    public DishAdd(String dishName, String cateId, String avatar, String des, String use,
                   String material, String steps, String stepImgs1, String stepImgs2, String stepImgs3,
                   String stepImgs4, String stepImgs5, String stepImgs6, String stepImgs7,
                   String stepImgs8, String stepImgs9, String stepImgs10) {
        this.dishName = dishName;
        this.cateId = cateId;
        this.avatar = avatar;
        this.des = des;
        this.use = use;
        this.material = material;
        this.steps = steps;
        this.stepImgs1 = stepImgs1;
        this.stepImgs2 = stepImgs2;
        this.stepImgs3 = stepImgs3;
        this.stepImgs4 = stepImgs4;
        this.stepImgs5 = stepImgs5;
        this.stepImgs6 = stepImgs6;
        this.stepImgs7 = stepImgs7;
        this.stepImgs8 = stepImgs8;
        this.stepImgs9 = stepImgs9;
        this.stepImgs10 = stepImgs10;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getCateId() {
        return cateId;
    }

    public void setCateId(String cateId) {
        this.cateId = cateId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getStepImgs1() {
        return stepImgs1;
    }

    public void setStepImgs1(String stepImgs1) {
        this.stepImgs1 = stepImgs1;
    }

    public String getStepImgs2() {
        return stepImgs2;
    }

    public void setStepImgs2(String stepImgs2) {
        this.stepImgs2 = stepImgs2;
    }

    public String getStepImgs3() {
        return stepImgs3;
    }

    public void setStepImgs3(String stepImgs3) {
        this.stepImgs3 = stepImgs3;
    }

    public String getStepImgs4() {
        return stepImgs4;
    }

    public void setStepImgs4(String stepImgs4) {
        this.stepImgs4 = stepImgs4;
    }

    public String getStepImgs5() {
        return stepImgs5;
    }

    public void setStepImgs5(String stepImgs5) {
        this.stepImgs5 = stepImgs5;
    }

    public String getStepImgs6() {
        return stepImgs6;
    }

    public void setStepImgs6(String stepImgs6) {
        this.stepImgs6 = stepImgs6;
    }

    public String getStepImgs7() {
        return stepImgs7;
    }

    public void setStepImgs7(String stepImgs7) {
        this.stepImgs7 = stepImgs7;
    }

    public String getStepImgs8() {
        return stepImgs8;
    }

    public void setStepImgs8(String stepImgs8) {
        this.stepImgs8 = stepImgs8;
    }

    public String getStepImgs9() {
        return stepImgs9;
    }

    public void setStepImgs9(String stepImgs9) {
        this.stepImgs9 = stepImgs9;
    }

    public String getStepImgs10() {
        return stepImgs10;
    }

    public void setStepImgs10(String stepImgs10) {
        this.stepImgs10 = stepImgs10;
    }

    @Override
    public String toString() {
        return "DishAdd{" +
                "dishName='" + dishName + '\'' +
                ", cateId='" + cateId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", des='" + des + '\'' +
                ", use='" + use + '\'' +
                ", material='" + material + '\'' +
                ", steps='" + steps + '\'' +
                ", stepImgs1='" + stepImgs1 + '\'' +
                ", stepImgs2='" + stepImgs2 + '\'' +
                ", stepImgs3='" + stepImgs3 + '\'' +
                ", stepImgs4='" + stepImgs4 + '\'' +
                ", stepImgs5='" + stepImgs5 + '\'' +
                ", stepImgs6='" + stepImgs6 + '\'' +
                ", stepImgs7='" + stepImgs7 + '\'' +
                ", stepImgs8='" + stepImgs8 + '\'' +
                ", stepImgs9='" + stepImgs9 + '\'' +
                ", stepImgs10='" + stepImgs10 + '\'' +
                '}';
    }
}
