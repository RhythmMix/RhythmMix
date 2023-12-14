package com.example.rhythmix.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhythmix.Music;
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;

public class AlbumTracksAdapter extends RecyclerView.Adapter<AlbumTracksAdapter.TrackViewHolder> {
    private List<Music> trackList;
    private Activity context;
    private static final String  TAG="AlbumTracksAdapter";

    public AlbumTracksAdapter(List<Music> trackList, Activity context) {
        this.context = context;
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public AlbumTracksAdapter.TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_item, parent, false);
        return new  AlbumTracksAdapter.TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        if (trackList != null && position >= 0 && position < trackList.size()) {
            Music music = trackList.get(position);
            if (music != null && music.getArtist() != null && music.getArtist().getName() != null && music.getArtist().getPicture() != null) {

                String imageUrl = music.getAlbum().getCover();
                ImageView trackImage = holder.trackImage;
                TextView trackTitle = holder.trackTitle;
                ImageButton trackToggleButton = holder.trackToggleButton;

//                holder.trackTitle.setText(music.getTitle());
                // Set other views as needed
                // Load image using Picasso
                Picasso.get().load(imageUrl).into(trackImage);

                // Set up media player and toggle button
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(music.getPreview()));
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    Log.e(TAG, "Error setting data source", e);
                    mediaPlayer.release();
                }

                String titleArtist = music.getTitle() + " \n \n " + music.getArtist().getName();
                trackTitle.setText(titleArtist);

                trackToggleButton.setOnClickListener(v -> {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        trackToggleButton.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        mediaPlayer.start();
                        trackToggleButton.setImageResource(android.R.drawable.ic_media_pause);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return trackList != null ? trackList.size() : 0;
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


