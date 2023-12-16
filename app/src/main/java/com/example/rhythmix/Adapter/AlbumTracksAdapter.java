package com.example.rhythmix.Adapter;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.Music;
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class AlbumTracksAdapter extends RecyclerView.Adapter<AlbumTracksAdapter.TrackViewHolder> {
    private final List<Music> trackList2;
    private final MediaPlayer mediaPlayer;
    private int playingPosition = -1; // Keep track of the currently playing position


    public AlbumTracksAdapter(List<Music> trackList) {
        trackList2 = trackList;
        this.mediaPlayer = new MediaPlayer();

        for (Music track : trackList) {
            String trackTitle = track.getTitle();
            String albumTitle = (track.getAlbum() != null) ? track.getAlbum().getTitle() : "Unknown Album";
            Log.d("AlbumTracksAdapter", "Trackfromme: " + trackTitle + ", Albumfromme: " + albumTitle);
        }
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new TrackViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Music music = trackList2.get(position);
        Picasso.get().load(getDefaultCoverUrl()).into(holder.trackImage);


        holder.trackTitle.setText(music.getTitle());


        holder.trackToggleButton.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();

            if (adapterPosition == playingPosition) {
                // If the clicked item is the currently playing item, stop the music
                stopMediaPlayer();
                playingPosition = -1;
            } else {
                // If a different item is clicked, stop the currently playing music (if any)
                stopMediaPlayer();

                // Play the new music
                playMediaPlayer(music.getPreview());
                playingPosition = adapterPosition;
            }

            notifyDataSetChanged(); // Update the UI
        });


        // Update the UI based on the current playing position
        holder.trackToggleButton.setImageResource(holder.getAdapterPosition() == playingPosition
                ? android.R.drawable.ic_media_pause : android.R.drawable.ic_media_play);
    }


    // Provide a default cover URL
    private String getDefaultCoverUrl() {
        // Set a default URL for the cover image or return null if not available
        return "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSxT0wmzxwDPQjiJHNNQvDxWA_1uHhPpR0uVIPeG5d81RsLk_F1";
    }


    @Override
    public int getItemCount() {
        return trackList2 != null ? trackList2.size() : 0;
    }

    private void playMediaPlayer(String previewUrl) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(previewUrl);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            Log.e("AlbumTracksAdapter", "Error playing media", e);
        }
    }

    private void stopMediaPlayer() {
        mediaPlayer.stop();
        mediaPlayer.reset();
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        public ImageView trackImage;
        public TextView trackTitle;
        public ImageButton trackToggleButton;

        public TrackViewHolder(View view) {
            super(view);
            trackImage = view.findViewById(R.id.trackImage);
            trackTitle = view.findViewById(R.id.trackTitle);
            trackToggleButton = view.findViewById(R.id.trackToggleButton);
        }
    }
}


