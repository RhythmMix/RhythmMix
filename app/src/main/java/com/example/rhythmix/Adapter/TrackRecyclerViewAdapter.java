package com.example.rhythmix.Adapter;

import android.app.Activity;
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
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.activities.APISongPlayerActivity;
import com.example.rhythmix.activities.MainActivity;
import com.example.rhythmix.models.Music;
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.TrackListViewHolder> {

    private List<Music> musicList;
    private Activity context;
    private MediaPlayer currentMediaPlayer;
    private RecyclerView recyclerView;
    private int currentlyPlayingPosition = -1;
    private MainActivity mainActivity;

    private static final String TAG = "Vertical_HOLDER";

    public TrackRecyclerViewAdapter(Activity context, List<Music> musicList, RecyclerView recyclerView, MainActivity mainActivity) {
        this.context = context;
        this.musicList = musicList;
        this.recyclerView = recyclerView;
    }


    @NonNull
    @Override
    public TrackRecyclerViewAdapter.TrackListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item, parent, false);
        return new TrackRecyclerViewAdapter.TrackListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackListViewHolder holder, int position) {
        if (musicList != null && position >= 0 && position < musicList.size()) {
            Music music = musicList.get(position);
            if (music != null && music.getPreview() != null && music.getArtist().getName() != null && music.getArtist().getPicture() != null) {
                String imageUrl = music.getArtist().getPicture();
                ImageView musicImage = holder.itemView.findViewById(R.id.musicImage);
                TextView musicTitle = holder.itemView.findViewById(R.id.musicTitle);
                ImageButton toggleButton = holder.itemView.findViewById(R.id.toggleButton);
                TextView artistName = holder.itemView.findViewById(R.id.musicArtistName);

                Picasso.get().load(imageUrl).into(musicImage);

                // Displaying title & Artist name & path
                String songTitle = music.getTitle();
                musicTitle.setText(songTitle);

                String songArtist = music.getArtist().getName();
                artistName.setText(songArtist);

                String songPath = music.getPreview();



                // Set click listener for the entire item view
                holder.itemView.setOnClickListener(v -> {
                    int clickedPosition = holder.getAdapterPosition();

                    // Pass data to SongPlayerActivity
                    Music clickedMusic = musicList.get(clickedPosition);
                    if (clickedMusic != null) {

                        Intent intent = new Intent(context, APISongPlayerActivity.class);
                        intent.putExtra("SONG_TITLE", clickedMusic.getTitle());
                        intent.putExtra("SONG_ARTIST", clickedMusic.getArtist().getName());
                        intent.putExtra("SONG_PATH", clickedMusic.getPreview());

                        context.startActivity(intent);
                    }
                });

                holder.itemView.setTag(position);


                // MediaPlayer initialization
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(songPath));
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                toggleButton.setOnClickListener(v -> {
                    int clickedPosition = holder.getAdapterPosition();

                    if (currentMediaPlayer != null && currentMediaPlayer.isPlaying()) {
                        if (currentlyPlayingPosition == clickedPosition) {
                            currentMediaPlayer.pause();
                            updateToggleIconForItem(android.R.drawable.ic_media_play, clickedPosition);
                            currentlyPlayingPosition = -1;
                        } else {
                            currentMediaPlayer.pause();
                            updateToggleIconForItem(android.R.drawable.ic_media_play, currentlyPlayingPosition);

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
        }
    }

    private void updateToggleIconForItem(int iconResId, int itemPosition) {
        View itemView = recyclerView.findViewHolderForAdapterPosition(itemPosition).itemView;
        ImageButton toggleButton = itemView.findViewById(R.id.toggleButton);
        toggleButton.setImageResource(iconResId);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }


    public class TrackListViewHolder extends RecyclerView.ViewHolder {
        public TrackListViewHolder(@NonNull View itemView) {
            super(itemView);
        }


    }

    public void addTrack(Music music) {
        musicList.add(music);
        notifyDataSetChanged();
    }

}

