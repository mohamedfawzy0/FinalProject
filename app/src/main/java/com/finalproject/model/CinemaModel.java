package com.finalproject.model;

import android.graphics.ColorSpace;

import java.io.Serializable;

public class CinemaModel implements Serializable {
    private String id;
    private String cinema_id;
    private String post_id;
    private String created_at;
    private String updated_at;
    private Model cinema;

    public String getId() {
        return id;
    }

    public String getCinema_id() {
        return cinema_id;
    }

    public String getPost_id() {
        return post_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public Model getModel() {
        return cinema;
    }

    public static class Model implements Serializable{
        private String id;
        private String title;
        private String location;
        private String user_id;
        private String chairs_count;
        private String price;
        private String created_at;
        private String updated_at;

        public String getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getLocation() {
            return location;
        }

        public String getUser_id() {
            return user_id;
        }

        public String getChairs_count() {
            return chairs_count;
        }

        public String getPrice() {
            return price;
        }

        public String getCreated_at() {
            return created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }
    }
}
