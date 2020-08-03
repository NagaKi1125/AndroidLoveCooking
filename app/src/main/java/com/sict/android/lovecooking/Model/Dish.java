package com.sict.android.lovecooking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Dish {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("dish_name")
    @Expose
    private String dishName;
    @SerializedName("cate_id")
    @Expose
    private String cateId;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("use")
    @Expose
    private String use;
    @SerializedName("material")
    @Expose
    private String material;
    @SerializedName("steps")
    @Expose
    private String steps;
    @SerializedName("step_imgs")
    @Expose
    private String stepImgs;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("liked_count")
    @Expose
    private int likedCount;
    @SerializedName("checked")
    @Expose
    private Integer checked;
    @SerializedName("history")
    @Expose
    private List<History> history = null;
    @SerializedName("cmt")
    @Expose
    private List<Comment> cmt = null;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Dish() {
    }

    public Dish(int id, String dishName, String cateId, String avatar, String description,
                String use, String material, String steps, String stepImgs, String author,
                int likedCount, Integer checked, List<History> history, List<Comment> cmt,
                String createdAt, String updatedAt) {
        this.id = id;
        this.dishName = dishName;
        this.cateId = cateId;
        this.avatar = avatar;
        this.description = description;
        this.use = use;
        this.material = material;
        this.steps = steps;
        this.stepImgs = stepImgs;
        this.author = author;
        this.likedCount = likedCount;
        this.checked = checked;
        this.history = history;
        this.cmt = cmt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getStepImgs() {
        return stepImgs;
    }

    public void setStepImgs(String stepImgs) {
        this.stepImgs = stepImgs;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLikedCount() {
        return likedCount;
    }

    public void setLikedCount(int likedCount) {
        this.likedCount = likedCount;
    }

    public Integer getChecked() {
        return checked;
    }

    public void setChecked(Integer checked) {
        this.checked = checked;
    }

    public List<History> getHistory() {
        return history;
    }

    public void setHistory(List<History> history) {
        this.history = history;
    }

    public List<Comment> getCmt() {
        return cmt;
    }

    public void setCmt(List<Comment> cmt) {
        this.cmt = cmt;
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
        return "Dish{" +
                "id=" + id +
                ", dishName='" + dishName + '\'' +
                ", cateId='" + cateId + '\'' +
                ", avatar='" + avatar + '\'' +
                ", description='" + description + '\'' +
                ", use='" + use + '\'' +
                ", material='" + material + '\'' +
                ", steps='" + steps + '\'' +
                ", stepImgs='" + stepImgs + '\'' +
                ", author='" + author + '\'' +
                ", likedCount=" + likedCount +
                ", checked=" + checked +
                ", history=" + history +
                ", cmt=" + cmt +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }

    public class Comment {
        @SerializedName("id")
        @Expose
        private String commentID;

        @SerializedName("name")
        @Expose
        private String name;

        @SerializedName("comment")
        @Expose
        private String comment;

        @SerializedName("created_at")
        @Expose
        private String cmt_created_at;

        @SerializedName("updated_at")
        @Expose
        private String cmt_updated_at;

        public Comment() {
        }

        public Comment(String commentID, String name, String comment, String cmt_created_at, String cmt_updated_at) {
            this.commentID = commentID;
            this.name = name;
            this.comment = comment;
            this.cmt_created_at = cmt_created_at;
            this.cmt_updated_at = cmt_updated_at;
        }

        public Comment(String commentID, String name, String comment) {
            this.commentID = commentID;
            this.name = name;
            this.comment = comment;
        }

        public String getCmt_created_at() {
            return cmt_created_at;
        }

        public void setCmt_created_at(String cmt_created_at) {
            this.cmt_created_at = cmt_created_at;
        }

        public String getCmt_updated_at() {
            return cmt_updated_at;
        }

        public void setCmt_updated_at(String cmt_updated_at) {
            this.cmt_updated_at = cmt_updated_at;
        }

        public String getCommentID() {
            return commentID;
        }

        public void setCommentID(String commentID) {
            this.commentID = commentID;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        @Override
        public String toString() {
            return "Comment{" +
                    "commentID='" + commentID + '\'' +
                    ", name='" + name + '\'' +
                    ", comment='" + comment + '\'' +
                    ", cmt_created_at='" + cmt_created_at + '\'' +
                    ", cmt_updated_at='" + cmt_updated_at + '\'' +
                    '}';
        }
    }

    public class History{
        @SerializedName("dh_posts")
        @Expose
        private String dhposts;


        public History() {
        }

        public History(String dhposts) {
            this.dhposts = dhposts;
        }

        public String getDhposts() {
            return dhposts;
        }

        public void setDhposts(String dhposts) {
            this.dhposts = dhposts;
        }

        @Override
        public String toString() {
            return "History{" +
                    "dhposts='" + dhposts + '\'' +
                    '}';
        }
    }
}
