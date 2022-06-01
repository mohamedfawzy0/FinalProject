package com.finalproject.model;

import java.io.Serializable;
import java.util.List;

public class PostDataModel extends StatusResponse implements Serializable {
    private List<PostModel> data;

    public List<PostModel> getData() {
        return data;
    }
}
