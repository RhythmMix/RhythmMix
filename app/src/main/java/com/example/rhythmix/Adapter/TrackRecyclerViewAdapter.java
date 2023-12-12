package com.example.rhythmix.Adapter;

import android.app.Activity;
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

import com.example.rhythmix.Album;
import com.example.rhythmix.Music;
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.TrackListViewHolder> {

    private List<Music> musicList;
    private Activity context;
    private static final String TAG = "Vertical_HOLDER";


    public TrackRecyclerViewAdapter(Activity context, List<Music> musicList) {
        this.context = context;
        this.musicList = musicList;

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


                Picasso.get().load(imageUrl).into(musicImage);

                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(music.getPreview()));
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // Displaying title & Artist name
                String titleArtist = music.getTitle() + " \n \n " + music.getArtist().getName();
                musicTitle.setText(titleArtist);


                toggleButton.setOnClickListener(v -> {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        toggleButton.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        mediaPlayer.start();
                        toggleButton.setImageResource(android.R.drawable.ic_media_pause);
                    }
                });
            }
        }

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

