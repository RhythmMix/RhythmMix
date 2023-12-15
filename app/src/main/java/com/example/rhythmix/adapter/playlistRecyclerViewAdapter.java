package com.example.rhythmix.adapter;

import android.content.res.AssetFileDescriptor;
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

import com.amplifyframework.datastore.generated.model.Playlist;
import com.bumptech.glide.Glide;
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class playlistRecyclerViewAdapter extends RecyclerView.Adapter {
    List<Playlist> playlists;

    public playlistRecyclerViewAdapter(List<Playlist> playlists) {
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View playListFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_playlist,parent,false);
        return new playlistViewHolder(playListFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView playlistFragmentText = holder.itemView.findViewById(R.id.playlistTitle);
        String playlistName = playlists.get(position).getPlaylistName();

        ImageButton playlistFragmentImage = holder.itemView.findViewById(R.id.playlistImageButton);
        String playlistImagePath = playlists.get(position).getPlaylistBackground();

        playlistFragmentText.setText(playlistName);

        Log.d("TaskDetailActivity", "Image URL: " + playlistImagePath);
        String imagePath = "https://rhythmmix90bba48f17b9485194f4a1c4ae1c9bc1200138-dev.s3.us-east-2.amazonaws.com/public/"+playlistImagePath;
        Log.d("imagePath", "Image path: " + imagePath);
        Glide.with(holder.itemView.getContext()).load(imagePath)
                .error(R.drawable.rhythemix)
                .into(playlistFragmentImage);
    }
    @Override
    public int getItemCount() {
        return playlists.size();
    }
    public static class playlistViewHolder extends RecyclerView.ViewHolder{

        public playlistViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
