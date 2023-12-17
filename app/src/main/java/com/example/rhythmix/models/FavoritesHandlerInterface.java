package com.example.rhythmix.models;

public interface FavoritesHandlerInterface {
    void addToFavorites(Music selectedTrack);
    void deleteFromFavorites(String trackId);
    void shareTrack(String trackLink);
    void queryFavorites();


}
