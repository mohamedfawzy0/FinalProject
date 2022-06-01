package com.finalproject.model;

import java.io.Serializable;
import java.util.List;

public class HistoryDataModel extends StatusResponse implements Serializable {

    private List<HistoryModel> data;

    public List<HistoryModel> getData() {
        return data;
    }
}
