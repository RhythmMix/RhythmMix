package com.example.rhythmix.adapter;

import android.annotation.SuppressLint;
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

import com.example.rhythmix.Activites.AddToFavoritesActivity;
import com.example.rhythmix.R;
import com.example.rhythmix.models.Track;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.DataListViewHolder> {
    private Activity context;
    private List<Track> dataList;
    private static final String TAG = "HOLDER";

    public DataListAdapter(Activity context, List<Track> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public DataListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View musicFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item, parent, false);
        return new DataListViewHolder(musicFragment);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull DataListViewHolder holder, int position) {
        if (dataList != null && position >= 0 && position < dataList.size()) {
            Track track = dataList.get(position);
            String imageUrl= track.getAlbum().getCover();
            Log.d(TAG,"iMAGE" + imageUrl);
//            String imageUrl = "";

            // Use Picasso to load the image into the ImageView
            if(track.getAlbum().getCover()!=null){
                imageUrl= track.getAlbum().getCover();
            }
            Log.d(TAG, "IMAGE URL" + imageUrl);
            ImageView musicImage = holder.itemView.findViewById(R.id.musicImage);
            TextView musicTitle = holder.itemView.findViewById(R.id.musicTitle);
            ImageButton toggleButton = holder.itemView.findViewById(R.id.toggleButton);
            Picasso.get().load(imageUrl).into(musicImage);

            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(context, Uri.parse(track.getPreview()));
                mediaPlayer.prepareAsync(); // Use prepareAsync to avoid blocking UI thread
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Display title & Artist name
            String titleArtist = track.getTitle() + " \n \n " + track.getArtist().getName();
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

    private void addToFavorites(Track selectedTrack) {
        Intent addToFavoritesIntent = new Intent(context, AddToFavoritesActivity.class);

        addToFavoritesIntent.putExtra("TRACK_ID", selectedTrack.getId());
        addToFavoritesIntent.putExtra("TRACK_TITLE", selectedTrack.getTitle());
        addToFavoritesIntent.putExtra("TRACK_ARTIST", selectedTrack.getArtist().getName());
        addToFavoritesIntent.putExtra("TRACK_MP3", selectedTrack.getPreview());

        context.startActivity(addToFavoritesIntent);
    }


    @Override
    public int getItemCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }

    public void update(List<Track> newData) {
        this.dataList = newData;
        notifyDataSetChanged();
    }

    // Method to add a single item to the adapter
    public void add(Track track) {
        dataList.add(track);
        notifyItemInserted(dataList.size() - 1);
    }


    public class DataListViewHolder extends RecyclerView.ViewHolder {
        public DataListViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}
