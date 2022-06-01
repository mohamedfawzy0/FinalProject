package com.finalproject.model;

import java.io.Serializable;

public class DetailsDataModel extends StatusResponse implements Serializable {
    private PostModel data;

    public PostModel getData() {
        return data;
    }
}
