package com.finalproject.model;

import java.io.Serializable;
import java.util.List;

public class OwnerHistoryModel implements Serializable {
    private String id;
    private String day_id;
    private String hour;
    private String created_at;
    private String updated_at;
    private String chairs_count;
    private String chairs_reserved;
    private String chairs_available;
    private boolean isSelected;

    public String getId() {
        return id;
    }

    public String getDay_id() {
        return day_id;
    }

    public String getHour() {
        return hour;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getChairs_count() {
        return chairs_count;
    }

    public String getChairs_reserved() {
        return chairs_reserved;
    }

    public String getChairs_available() {
        return chairs_available;
    }
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
