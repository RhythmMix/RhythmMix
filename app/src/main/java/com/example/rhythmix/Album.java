package com.example.rhythmix;

import org.json.JSONException;
import org.json.JSONObject;

public class Album {

    private long id;
    private String title;
    private String tracklist;
    private String cover; // Updated to handle null or missing cover field

    static String numericPart="";

    public Album(JSONObject albumObject) throws JSONException {
        this.id = albumObject.getLong("id");
        this.title = albumObject.optString("title", "Unknown Album"); // Use a default value if title is missing
        this.tracklist = albumObject.optString("tracklist", ""); // Use an empty string if tracklist is missing
        this.cover = albumObject.optString("cover", null); // Use null if cover is missing
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

}
