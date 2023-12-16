package com.example.rhythmix;


import com.example.rhythmix.models.Album;
import com.example.rhythmix.models.Data;
import com.example.rhythmix.models.Track;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MusicApiInterface {
    @Headers({"X-RapidAPI-Key: bae0efd049mshd3823f0e2a43ab1p192424jsn46ab3ffb4f66", "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"})
    @GET("search")
    Call<Data> getData(@Query("q") String query);

    @Headers({"X-RapidAPI-Key: bae0efd049mshd3823f0e2a43ab1p192424jsn46ab3ffb4f66", "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"})
    @GET("album/{albumId}")
    Call<Album> getAlbum(@Path("albumId") long albumId);

    @Headers({"X-RapidAPI-Key: bae0efd049mshd3823f0e2a43ab1p192424jsn46ab3ffb4f66", "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"})
    @GET("track/{trackIds}")
    Call<Track> getTracks(@Path("trackIds") long trackId);

    @Headers({"X-RapidAPI-Key: bae0efd049mshd3823f0e2a43ab1p192424jsn46ab3ffb4f66", "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"})
    @GET("search")
    Call<Data> getAllData(@Query("q") String query);

    @Headers({"X-RapidAPI-Key: bae0efd049mshd3823f0e2a43ab1p192424jsn46ab3ffb4f66", "X-RapidAPI-Host: deezerdevs-deezer.p.rapidapi.com"})
    @GET("album/{albumIds}/tracks")
    Call<Data> getAlbumTracks(@Path("albumIds") long trackId);

//    https://api.deezer.com/album/265655342/tracks

}

