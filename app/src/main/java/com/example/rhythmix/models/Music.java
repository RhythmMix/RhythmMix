package com.example.rhythmix.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Music {
    private long id;
    private String title;
    private String link;
    private int duration;
    private int rank;
    private boolean explicitLyrics;
    private boolean explicitContentLyrics;
    private boolean explicitContentCover;
    private String preview;
    private Artist artist;     //Nested Class
    private Album album;      //Nested Class


    public Music(JSONObject musicObject) throws JSONException {
        id = musicObject.getInt("id");
        title = musicObject.getString("title");
        link = musicObject.getString("link");
        duration = musicObject.getInt("duration");
        rank = musicObject.getInt("rank");
        explicitLyrics = musicObject.getBoolean("explicit_lyrics");
        explicitContentLyrics = musicObject.getBoolean("explicit_content_lyrics");
        explicitContentCover = musicObject.getBoolean("explicit_content_cover");
        preview = musicObject.getString("preview");

        // Extracting and assigning values to the associated Artist and Album objects
        JSONObject artistObject = musicObject.getJSONObject("artist");
        artist = new Artist(artistObject);

        JSONObject albumObject = musicObject.getJSONObject("album");
        album = new Album(albumObject);
    }


    public String getTitle() {
        return title;
    }

    public String getPreview() {
        return preview;
    }

    public Artist getArtist() {
        return artist;
    }

    public Album getAlbum() {
        return album;
    }


    public long getId() {
        return id;
}

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}