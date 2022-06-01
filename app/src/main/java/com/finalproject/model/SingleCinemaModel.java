package com.finalproject.model;

import java.io.Serializable;

public class SingleCinemaModel extends StatusResponse implements Serializable {
    private CinemaModel.Model data;

    public CinemaModel.Model getData() {
        return data;
    }
}
