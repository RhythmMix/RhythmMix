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


    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }
}
