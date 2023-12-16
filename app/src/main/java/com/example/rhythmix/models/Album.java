package com.example.rhythmix.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {

    private long id;
    private String title;
    private String tracklist;

    private String cover;

    public Album(JSONObject albumObject) throws JSONException {
        this.id = albumObject.getInt("id");
        this.title = albumObject.getString("title");
        this.tracklist=albumObject.getString("tracklist");
        this.cover = albumObject.getString("cover");
    }

    public String getTitle() {
        return title;
    }


    public String getCover() {
        return cover;
    }

    public String getTrackList() {
        return tracklist;
    }

    public long getId() {
        return id;
}

//
}