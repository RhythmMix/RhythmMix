package com.example.rhythmix.Adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
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

import com.example.rhythmix.AlbumTracksActivity;
import com.example.rhythmix.Music;
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;

public class AlbumTracksAdapter extends RecyclerView.Adapter<AlbumTracksAdapter.TrackViewHolder> {
    private List<Music> trackList;
    private Activity context;

    private static final String TAG = "AlbumTracks_HOLDER";

    public AlbumTracksAdapter(List<Music> trackList, Activity context) {
        this.context = context;
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public AlbumTracksAdapter.TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_track, parent, false);
        return new AlbumTracksAdapter.TrackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        if (trackList != null && position >= 0 && position < trackList.size()) {
            Music music = trackList.get(position);
            if (music != null && music.getArtist() != null && music.getArtist().getName() != null && music.getArtist().getPicture() != null) {
                String imageUrl = music.getAlbum().getCover();


//                ImageView albumCover = holder.itemView.findViewById(R.id.musicImage);
//                TextView albumTitle = holder.itemView.findViewById(R.id.musicTitle);
//                ImageButton toggleButton = holder.itemView.findViewById(R.id.toggleButton);
//
//                Picasso.get().load(imageUrl).into(albumCover);
//
//                MediaPlayer mediaPlayer = new MediaPlayer();
//                try {
//                    mediaPlayer.setDataSource(context, Uri.parse(music.getPreview()));
//                    mediaPlayer.prepareAsync();
//                } catch (IOException e) {
//                    Log.e(TAG, "Error setting data source", e);
//                    mediaPlayer.release();
//                }
//
//                String titleArtist = music.getTitle() + "\n\n" + music.getArtist().getName();
//                albumTitle.setText(titleArtist);

//                toggleButton.setOnClickListener(v -> {
//                    if (mediaPlayer.isPlaying()) {
//                        mediaPlayer.pause();
//                        toggleButton.setImageResource(android.R.drawable.ic_media_play);
//                    } else {
//                        mediaPlayer.start();
//                        toggleButton.setImageResource(android.R.drawable.ic_media_pause);
//                    }
//                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return trackList != null ? trackList.size() : 0;
    }

    @Override
    public void onViewRecycled(@NonNull TrackViewHolder holder) {
        MediaPlayer mediaPlayer = holder.mediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        super.onViewRecycled(holder);
    }

    public static class TrackViewHolder extends RecyclerView.ViewHolder {
        ImageView trackImage;
        TextView trackTitle;
        ImageButton trackToggleButton;
        MediaPlayer mediaPlayer;

        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            trackImage = itemView.findViewById(R.id.musicImage);
            trackTitle = itemView.findViewById(R.id.musicTitle);
            trackToggleButton = itemView.findViewById(R.id.toggleButton);
        }
    }
}

