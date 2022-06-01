package com.finalproject.model;

import java.io.Serializable;
import java.util.List;

public class PostModel implements Serializable {
    private String id;
    private String title;
    private String rate;
    private String image;
    private String video;
    private String details;
    private String category_id;
    private CategoryModel category;
    private String minutes;
    private String count_hours;
    private List<HeroModel> heroes;
    private String type;
    private String added;
    private String created_at;
    private String updated_at;


    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getVideo() {
        return video;
    }

    public String getCategory_id() {
        return category_id;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public String getMinutes() {
        return minutes;
    }

    public String getTitle() {
        return title;
    }

    public String getRate() {
        return rate;
    }

    public String getDetails() {
        return details;
    }

    public String getCount_hours() {
        return count_hours;
    }

    public List<HeroModel> getHeroes() {
        return heroes;
    }

    public String getType() {
        return type;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getAdded() {
        return added;
    }

    public void setAdded(String added) {
        this.added = added;
    }

}
