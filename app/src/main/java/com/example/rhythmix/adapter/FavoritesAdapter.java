package com.example.rhythmix.adapter;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Favorite;
import com.amplifyframework.datastore.generated.model.FavoriteMusic;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.bumptech.glide.Glide;
import com.example.rhythmix.Activites.AddToFavoritesActivity;
import com.example.rhythmix.Activites.InsidePlaylistActivity;
import com.example.rhythmix.R;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.List;


public class FavoritesAdapter extends RecyclerView.Adapter {
    List<Favorite> favorites;
    Context callingActivity;
    public FavoritesAdapter(List<Favorite> favorites,Context callingActivity) {

        this.favorites = favorites;
        this.callingActivity = callingActivity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View playListFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_each_item,parent,false);
        return new playlistViewHolder(playListFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Favorite favorite = favorites.get(position);

        TextView title = holder.itemView.findViewById(R.id.title);
        TextView description = holder.itemView.findViewById(R.id.description);
        ImageButton previewBtn = holder.itemView.findViewById(R.id.preview_button);
        RoundedImageView albumCoverView= holder.itemView.findViewById(R.id.image);
        ImageView deleteFavorite= holder.itemView.findViewById(R.id.menu_button);

        String trackId= favorite.getFavoriteId();
        String trackTitle = favorite.getFavoriteTitle();
        String trackPreview= favorite.getFavoriteMp3();
        String artistName= favorite.getFavoriteArtist();
        String albumCover= favorite.getFavoriteCover();

        deleteFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromFavorites(trackId);
            }
        });

        title.setText(trackTitle);
        description.setText(artistName);


        Picasso.get().load(albumCover).into(albumCoverView);


        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(callingActivity, Uri.parse(trackPreview));
            mediaPlayer.setOnPreparedListener(mp -> {
                if (mediaPlayer.isPlaying()) {
                    previewBtn.setImageResource(android.R.drawable.ic_media_pause);
                } else {
                    previewBtn.setImageResource(android.R.drawable.ic_media_play);
                }

                previewBtn.setOnClickListener(v -> {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        previewBtn.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        mediaPlayer.start();
                        previewBtn.setImageResource(android.R.drawable.ic_media_pause);
                    }
                });
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        holder.itemView.setOnClickListener(view -> {
                  Intent goToFav = new Intent(callingActivity, InsidePlaylistActivity.class);
                  goToFav.putExtra("TRACK_TITLE", trackTitle);
                  goToFav.putExtra("TRACK_ARTIST", artistName);
                  goToFav.putExtra("TRACK_COVER", albumCover);
                  goToFav.putExtra("TRACK_MP3", trackPreview);

                  callingActivity.startActivity(goToFav);
              });
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }
   public static class playlistViewHolder extends RecyclerView.ViewHolder{

        public playlistViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }


    ////=============== delete from Favorites ================== ////

    private void deleteFromFavorites(String trackId) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        if (authUser != null) {
            Amplify.API.query(
                    ModelQuery.list(Favorite.class, Favorite.FAVORITE_ID.eq(trackId)),
                    response -> {
                        if (response.getData() != null && response.getData().getItems() != null) {
                            Iterator<Favorite> iterator = response.getData().getItems().iterator();
                            if (iterator.hasNext()) {
                                // Track exists, delete it
                                Favorite favoriteToDelete = iterator.next();
                                Amplify.API.mutate(
                                        ModelMutation.delete(favoriteToDelete),
                                        deleteResponse -> {
                                            showToast("Track deleted from favorites");
                                            runOnUiThread(() -> {
                                                favorites.remove(favoriteToDelete);
                                                notifyDataSetChanged();
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

    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(callingActivity.getApplicationContext(), message, Toast.LENGTH_SHORT).show());
    }

}
