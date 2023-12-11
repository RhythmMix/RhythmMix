package com.example.rhythmix;

import org.json.JSONObject;

public class Track {

    private Long id;
    private String title;
    private String link;
    private Integer duration;
    private Integer rank;
    private String preview;
    private Artist artist;
    private Album album;
    private String type;

    public Track(Long id, String title, String link, Integer duration, Integer rank, String preview, Artist artist, Album album, String type) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.duration = duration;
        this.rank = rank;
        this.preview = preview;
        this.artist = artist;
        this.album = album;
        this.type = type;
    }

    public Track(JSONObject trackObject) {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }


    public Integer getDuration() {
        return duration;
    }


    public String getPreview() {
        return preview;
    }

    public Artist getArtist() {
        return artist;
    }


    public String getLink() {
        return link;
    }


    public String getType() {
        return type;

    }

    public Album getAlbum() {
        return album;
    }














}