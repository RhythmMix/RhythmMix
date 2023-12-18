package com.example.rhythmix.models;

public interface FavoritesHandlerInterface {
    void addToFavorites(Track selectedTrack);
    void deleteFromFavorites(String trackId);
    void shareTrack(String trackLink);
    void queryFavorites();

}