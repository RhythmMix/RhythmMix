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

import com.example.rhythmix.Data;
import com.example.rhythmix.MainActivity;
import com.example.rhythmix.Music;
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.DataListViewHolder> {
    private Activity context;
    private List<Music> dataList;
    private static final String TAG = "HOLDER";

    public DataListAdapter(Activity context, List<Music> dataList) {
        this.context = context;
        this.dataList = dataList;
    }


    @NonNull
    @Override
    public DataListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View musicFragment= LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item, parent, false);
        return new DataListViewHolder(musicFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull DataListViewHolder holder, int position) {
        if (dataList != null && position >= 0 && position < dataList.size()) {
            Music music = dataList.get(position);

            // Use Picasso to load the image into the ImageView
            String imageUrl = music.getAlbum().getCover();
            Log.d(TAG, "IMAGE URL" + imageUrl);
            // Initialize the ImageView, TextView, and ImageButtons using the holder
            ImageView musicImage = holder.itemView.findViewById(R.id.musicImage);
            TextView musicTitle = holder.itemView.findViewById(R.id.musicTitle);
            ImageButton playBtn = holder.itemView.findViewById(R.id.playButton);
            ImageButton pauseBtn = holder.itemView.findViewById(R.id.pauseButton);

            // Load album cover image with Picasso
            Picasso.get().load(imageUrl).into(musicImage);

            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(context, Uri.parse(music.getPreview()));
                mediaPlayer.prepareAsync(); // Use prepareAsync to avoid blocking UI thread
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Set other data, assuming you have appropriate methods in your Music or Data class
            musicTitle.setText(music.getTitle());

            // Set click listeners or perform any other operations as needed
//            holder.itemView.setOnClickListener(view -> {
//                Intent detailIntent = new Intent(context, MainActivity.class); // when we click on the image of the music
//                // Pass any relevant data to the detail activity
//                context.startActivity(detailIntent);
//            });

            playBtn.setOnClickListener(v -> {
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            });

            pauseBtn.setOnClickListener(v -> {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }

    public class DataListViewHolder extends RecyclerView.ViewHolder {
        public DataListViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}
