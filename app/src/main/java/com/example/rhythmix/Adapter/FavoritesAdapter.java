package com.example.rhythmix.Adapter;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Favorite;
import com.example.rhythmix.R;
import com.example.rhythmix.models.FavoritesHandler;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter {
    List<Favorite> favorites;
    Context callingActivity;
    FavoritesHandler favoritesHandler;
    private int currentlyPlayingPosition = -1;
    private MediaPlayer currentMediaPlayer;
    private RecyclerView recyclerView;

    public FavoritesAdapter(List<Favorite> favorites, Context callingActivity,RecyclerView recyclerView) {
        this.favorites = favorites;
        this.callingActivity = callingActivity;
        this.favoritesHandler = new FavoritesHandler(favorites, callingActivity, this);
        this.recyclerView=recyclerView;
    }

    public FavoritesAdapter(List<Favorite> favorites, Context callingActivity) {
        this.favorites = favorites;
        this.callingActivity = callingActivity;
        this.favoritesHandler = new FavoritesHandler(favorites, callingActivity, this);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View playListFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_each_item, parent, false);
        return new PlaylistRecyclerViewAdapter.playlistViewHolder(playListFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Favorite favorite = favorites.get(position);

        TextView title = holder.itemView.findViewById(R.id.title);

        TextView description = holder.itemView.findViewById(R.id.description);
        ImageButton previewBtn = holder.itemView.findViewById(R.id.preview_button);
        RoundedImageView albumCoverView = holder.itemView.findViewById(R.id.image);
        ImageView deleteFavorite = holder.itemView.findViewById(R.id.menu_button);

        String trackId = favorite.getFavoriteId();
        String trackTitle = favorite.getFavoriteTitle();
        String trackPreview = favorite.getFavoriteMp3();
        String artistName = favorite.getFavoriteArtist();
        String albumCover = favorite.getFavoriteCover();

        deleteFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favoritesHandler.deleteFromFavorites(trackId);//////////////////////////////////////////////////////////////////////////
            }
        });

        title.setText(trackTitle);
        description.setText(artistName);


        Picasso.get().load(albumCover).into(albumCoverView);


        // MediaPlayer initialization
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(callingActivity, Uri.parse(trackPreview));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        previewBtn.setOnClickListener(v -> {
            int clickedPosition = holder.getAdapterPosition();

            if (currentMediaPlayer != null && currentMediaPlayer.isPlaying()) {
                if (currentlyPlayingPosition == clickedPosition) {
                    currentMediaPlayer.pause();
                    updateToggleIconForItem(R.drawable.round_play_circle_24, clickedPosition);
                    currentlyPlayingPosition = -1;
                } else {
                    currentMediaPlayer.pause();
                    updateToggleIconForItem(R.drawable.round_play_circle_24, currentlyPlayingPosition);

                    mediaPlayer.start();
                    updateToggleIconForItem(android.R.drawable.ic_media_pause, clickedPosition);
                    currentlyPlayingPosition = clickedPosition;
                }
            } else {
                // No song is currently playing or paused, start the clicked song
                mediaPlayer.start();
                updateToggleIconForItem(android.R.drawable.ic_media_pause, clickedPosition);
                currentlyPlayingPosition = clickedPosition; // Update currentlyPlayingPosition
            }

            // Update the currentMediaPlayer to the new one
            currentMediaPlayer = mediaPlayer;
        });
    }

    private void updateToggleIconForItem(int iconResId, int itemPosition) {
        View itemView = recyclerView.findViewHolderForAdapterPosition(itemPosition).itemView;
        ImageButton toggleButton = itemView.findViewById(R.id.preview_button);
        toggleButton.setImageResource(iconResId);
    }


    @Override
    public int getItemCount() {
        return favorites.size();
    }
}