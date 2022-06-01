package com.finalproject.model;

import java.io.Serializable;
import java.util.List;

public class HomeDataModel extends StatusResponse implements Serializable {
    private List<PostModel>moves;
    private List<PostModel>shows;
    private List<ComingSoonModel>soon;


    public List<PostModel> getMoves() {
        return moves;
    }

    public List<PostModel> getShows() {
        return shows;
    }

    public List<ComingSoonModel> getSoon() {
        return soon;
    }
}
