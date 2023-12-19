package com.example.rhythmix.models;


import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
import java.util.concurrent.CompletableFuture;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Favorite;
import com.example.rhythmix.Adapter.FavoritesAdapter;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class FavoritesHandler implements FavoritesHandlerInterface {
    List<Favorite> favorites;
    private Context context;
    private static final String TAG = "FavoritesHandler";
    FavoritesAdapter favoritesAdapter;


    public FavoritesHandler(List<Favorite> favorites, Context context, FavoritesAdapter favoritesAdapter) {
        this.favorites = favorites;
        this.context = context;
        this.favoritesAdapter = favoritesAdapter;
    }

    public FavoritesHandler(Context context, FavoritesAdapter favoritesAdapter) {
        this.context = context;
        this.favoritesAdapter = favoritesAdapter;
    }

    public void addToFavorites(Track selectedTrack) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        if (authUser != null) {
            String trackId = String.valueOf(selectedTrack.getId());
            String title = selectedTrack.getTitle();
            String artist = selectedTrack.getArtist().getName();
            String mp3 = selectedTrack.getPreview();
            String albumCover = selectedTrack.getAlbum().getCover();

            checkAndAddToFavorites(trackId, title, artist, mp3, albumCover);
        } else {
            showAuthenticationMessage();
        }
    }



    public void checkAndAddToFavorites(String trackId, String title, String artist,
                                       String mp3, String cover) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        if (authUser != null) {
            Amplify.API.query(
                    ModelQuery.list(Favorite.class, Favorite.FAVORITE_ID.eq(trackId)),
                    response -> {
                        if (response.getData() != null && response.getData().getItems() != null) {
                            Iterator<Favorite> iterator = response.getData().getItems().iterator();
                            if (iterator.hasNext()) {
                                showToast("Track is already in favorites");
                            } else {
                                addToFavorites(trackId, title, artist, mp3, cover);
                            }
                        }
                    },
                    error -> {
                        showToast("Error checking for track: " + error.getMessage());
                    }
            );
        }
    }



    public CompletableFuture<Boolean> checkIfInFavorites(Track selectedTrack) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        if (authUser != null) {
            Amplify.API.query(
                    ModelQuery.list(Favorite.class, Favorite.FAVORITE_ID.eq(selectedTrack.getId())),
                    response -> {
                        Iterable<Favorite> favorites = response.getData().getItems();
                        if (favorites.iterator().hasNext()) {
                            future.complete(true);
                        } else {
                            future.complete(false);
                        }
                    },
                    error -> {
                        showToast("Error checking for track: " + error.getMessage());
                        future.complete(false);
                    }
            );
        } else {
            future.complete(false);
        }

        return future;
    }




    private void addToFavorites(String trackId, String title, String artist,
                                String mp3, String cover) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        if (authUser != null) {
            Favorite favorite = Favorite.builder()
                    .favoriteId(trackId)
                    .favoriteTitle(title)
                    .favoriteArtist(artist)
                    .favoriteMp3(mp3)
                    .userId(authUser.getUserId())
                    .favoriteCover(cover)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(favorite),
                    response -> showToast("Track added to favorites"),
                    error -> showToast("Error adding track to favorites: " + error.getMessage())
            );
        }
    }

    private void showAuthenticationMessage() {
        runOnUiThread(() -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Please log in or sign up to add tracks to favorites.")
                    .setPositiveButton("OK", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    @Override
    public void deleteFromFavorites(String trackId) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        if (authUser != null) {
            Amplify.API.query(
                    ModelQuery.list(Favorite.class, Favorite.FAVORITE_ID.eq(trackId)),
                    response -> {
                        if (response.getData() != null && response.getData().getItems() != null) {
                            Iterator<Favorite> iterator = response.getData().getItems().iterator();
                            if (iterator.hasNext()) {
                                Favorite favoriteToDelete = iterator.next();
                                Amplify.API.mutate(
                                        ModelMutation.delete(favoriteToDelete),
                                        deleteResponse -> {
                                            showToast("Track deleted from favorites");
                                            runOnUiThread(() -> {
                                                favorites.remove(favoriteToDelete);
                                                favoritesAdapter.notifyDataSetChanged();
                                            });
                                        },
                                        deleteError -> {
                                            showToast("Error deleting track: " + deleteError.getMessage());
                                        }
                                );
                            } else {
                                showToast("Track not found in favorites");
                            }
                        }
                    },
                    error -> {
                        showToast("Error checking for track: " + error.getMessage());
                    }
            );
        }
    }

    @Override
    public void shareTrack(String trackLink) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Track Link");
        shareIntent.putExtra(Intent.EXTRA_TEXT, trackLink);
        context.startActivity(Intent.createChooser(shareIntent, "Share Track Link"));
    }

    @Override
    public void queryFavorites() {
        Set<String> uniqueIds = new HashSet<>();
        AuthUser authUser = Amplify.Auth.getCurrentUser();

        if (authUser != null) {
            String currentUserId = authUser.getUserId();

            Amplify.API.query(
                    ModelQuery.list(Favorite.class, Favorite.USER_ID.eq(currentUserId)),
                    response -> {
                        runOnUiThread(() -> {
                            for (Favorite favorite : response.getData()) {
                                String favoriteId = favorite.getFavoriteId();
                                if (uniqueIds.add(favoriteId)) {
                                    favorites.add(favorite);
                                    Log.d(TAG, "Added to favorites: " + favorite);
                                } else {
                                    Log.d(TAG, "Duplicate track found, not added: " + favoriteId);
                                }
                            }
                            favoritesAdapter.notifyDataSetChanged();
                        });
                    },
                    error -> Log.e(TAG, "Error querying favorites", error)
            );
        }
    }


    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(context.getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }

}