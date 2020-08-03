package com.sict.android.lovecooking.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchFilter {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("use")
    @Expose
    private String use;
    @SerializedName("avatar")
    @Expose
    private String avatar;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("create")
    @Expose
    private String create;

    public SearchFilter() {
    }

    public SearchFilter(Integer id, String name, String description, String use, String avatar, String author, String create) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.use = use;
        this.avatar = avatar;
        this.author = author;
        this.create = create;
    }

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCreate() {
        return create;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    @Override
    public String toString() {
        return "SearchFilter{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", use='" + use + '\'' +
                ", avatar='" + avatar + '\'' +
                ", author='" + author + '\'' +
                ", create='" + create + '\'' +
                '}';
    }
}
