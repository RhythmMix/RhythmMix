package com.example.rhythmix;


import android.content.Context;

import com.example.rhythmix.models.APIConfig;
import com.example.rhythmix.models.Album;
import com.example.rhythmix.models.Data;
import com.example.rhythmix.models.Track;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MusicApiInterface {
    String apiKey = APIConfig.getApiKey(this);
    String apiHost = APIConfig.getApiHost(this);

    @Headers({"X-RapidAPI-Key: "+ APIConfig.getApiKey() , "X-RapidAPI-Host: " +  APIConfig.getApiHost()})
    @GET("search")
    Call<Data> getData(@Query("q") String query);

    @Headers({"X-RapidAPI-Key: "+ APIConfig.getApiKey() , "X-RapidAPI-Host: " +  APIConfig.getApiHost()})
    @GET("album/{albumId}")
    Call<Album> getAlbum(@Path("albumId") long albumId);

    @Headers({"X-RapidAPI-Key: "+ APIConfig.getApiKey() , "X-RapidAPI-Host: " +  APIConfig.getApiHost()})
    @GET("track/{trackIds}")
    Call<Track> getTracks(@Path("trackIds") long trackId);

    @Headers({"X-RapidAPI-Key: "+ APIConfig.getApiKey() , "X-RapidAPI-Host: " +  APIConfig.getApiHost()})
    @GET("search")
    Call<Data> getAllData(@Query("q") String query);

    @Headers({"X-RapidAPI-Key: "+ APIConfig.getApiKey() , "X-RapidAPI-Host: " +  APIConfig.getApiHost()})
    @GET("album/{albumIds}/tracks")
    Call<Data> getAlbumTracks(@Path("albumIds") long trackId);


}