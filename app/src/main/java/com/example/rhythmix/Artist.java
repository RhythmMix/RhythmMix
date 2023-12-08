package com.example.rhythmix;

import org.json.JSONException;
import org.json.JSONObject;

public class Artist {
    private long id;
    private String name;
    private String link;
    private String trackList;
    private String pictureSmall;


    public Artist(JSONObject artistObject) throws JSONException {
        this.id = artistObject.getInt("id");
        this.name = artistObject.getString("name");
        this.link = artistObject.getString("link");
        this.trackList = artistObject.getString("tracklist");
        this.pictureSmall = artistObject.getString("picture_small");
    }

    public String getName() {
        return name;
    }

    public String getPictureSmall() {
        return pictureSmall;
    }
}
