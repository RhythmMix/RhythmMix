package com.example.rhythmix;

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


    public static List<Music> parseMusicJson(String jsonString) {
        List<Music> musicList = new ArrayList<>();
        try {
            // Parsing the JSON string into a JSONObject
            JSONObject json = new JSONObject(jsonString);

            // Extracting the "dataArray" from the JSON object
            JSONArray dataArray = json.getJSONArray("data");

            // Iterating through each element in the "data" array
            for (int i = 0; i < dataArray.length(); i++) {
                // Getting a JSON object representing a music track
                JSONObject musicObject = dataArray.getJSONObject(i);

                // Creating a new Music object
                Music music = new Music();

                // Extracting and assigning values to the fields of the Music object
                music.id = musicObject.getInt("id");
                music.title = musicObject.getString("title");
                music.link = musicObject.getString("link");
                music.duration = musicObject.getInt("duration");
                music.rank = musicObject.getInt("rank");
                music.explicitLyrics = musicObject.getBoolean("explicit_lyrics");
                music.explicitContentLyrics = musicObject.getBoolean("explicit_content_lyrics");
                music.explicitContentCover = musicObject.getBoolean("explicit_content_cover");
                music.preview = musicObject.getString("preview");

                // Extracting and assigning values to the associated Artist and Album objects
                JSONObject artistObject = musicObject.getJSONObject("artist");
                music.artist = new Artist(artistObject);

                JSONObject albumObject = musicObject.getJSONObject("album");
                music.album = new Album(albumObject);

                // Adding the Music object to the list
                musicList.add(music);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return musicList;
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


}