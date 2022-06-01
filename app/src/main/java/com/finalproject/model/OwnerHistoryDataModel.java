package com.finalproject.model;

import java.io.Serializable;
import java.util.List;

public class OwnerHistoryDataModel extends StatusResponse implements Serializable {
    private List<OwnerHistoryModel> data;

    public List<OwnerHistoryModel> getData() {
        return data;
    }
}
