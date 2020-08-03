package com.sict.android.lovecooking.Model;

public class DishHistory {
    public int id;
    public int dish_id;
    public String dh_posts;
    public String dh_images;
    public String created_at;
    public String updated_at;

    public DishHistory() {
    }

    public DishHistory(int id, int dish_id, String dh_posts, String dh_images, String created_at, String updated_at) {
        this.id = id;
        this.dish_id = dish_id;
        this.dh_posts = dh_posts;
        this.dh_images = dh_images;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDish_id() {
        return dish_id;
    }

    public void setDish_id(int dish_id) {
        this.dish_id = dish_id;
    }

    public String getDh_posts() {
        return dh_posts;
    }

    public void setDh_posts(String dh_posts) {
        this.dh_posts = dh_posts;
    }

    public String getDh_images() {
        return dh_images;
    }

    public void setDh_images(String dh_images) {
        this.dh_images = dh_images;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    @Override
    public String toString() {
        return "DishHistory{" +
                "id='" + id + '\'' +
                ", dish_id=" + dish_id +
                ", dh_posts='" + dh_posts + '\'' +
                ", dh_images='" + dh_images + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                '}';
    }
}
