package com.finalproject.model;

import java.io.Serializable;
import java.util.List;

public class ShowDataModel extends StatusResponse implements Serializable {

    private List<PostModel> data;

    public List<PostModel> getData() {
        return data;
    }
}
