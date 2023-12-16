package com.example.rhythmix.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Artist implements Serializable {
    private long id;
    private String name;
    private String link;
    private String trackList;
    private String picture;


    public Artist(JSONObject artistObject) throws JSONException {
        this.id = artistObject.getInt("id");
        this.name = artistObject.getString("name");
        this.link = artistObject.getString("link");
        this.trackList = artistObject.getString("tracklist");
        this.picture = artistObject.getString("picture");
    }

    public String getName() {
        return name;
    }

    public String getPicture() {
        return picture;
}
}