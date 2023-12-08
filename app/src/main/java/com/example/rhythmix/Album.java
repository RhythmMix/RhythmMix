package com.example.rhythmix;

import org.json.JSONException;
import org.json.JSONObject;

public class Album {

    private long id;
    private String title;
    private String trackList;

    private String cover;


    public Album(JSONObject albumObject) throws JSONException {
        this.id = albumObject.getInt("id");
        this.title = albumObject.getString("title");
        this.trackList=albumObject.getString("tracklist");
        this.cover = albumObject.getString("cover");
    }

    public String getTitle() {
        return title;
    }


    public String getCover() {
        return cover;
    }
}
