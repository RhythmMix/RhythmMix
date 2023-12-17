package com.example.rhythmix.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.bumptech.glide.Glide;
import com.example.rhythmix.Activities.InsidePlaylistActivity;
import com.example.rhythmix.R;

import java.util.List;

public class ChoosePlaylistAdapter extends RecyclerView.Adapter  {
    List<Playlist> playlists;
    Context callingActivity;
    String playlistId;
    public static final String TAG = "playlistTagAdapter";
    private OnPlaylistClickListener playlistClickListener;
    public interface OnPlaylistClickListener {
        void onPlaylistClick(String playlistId);
    }
    public ChoosePlaylistAdapter(List<Playlist> playlists, Context callingActivity, OnPlaylistClickListener listener) {
        this.playlists = playlists;
        this.callingActivity = callingActivity;
        this.playlistClickListener = listener;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View playListFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_playlist,parent,false);
        return new PlaylistRecyclerViewAdapter.playlistViewHolder(playListFragment);
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
        holder.itemView.setOnClickListener(view -> {
            Intent goToInsidePlaylist = new Intent(callingActivity, InsidePlaylistActivity.class);
            goToInsidePlaylist.putExtra("playlistName", playlistName);
            goToInsidePlaylist.putExtra("playlistBackground", playlistImagePath);
            playlistId = playlists.get(position).getId();
            playlistClickListener.onPlaylistClick(playlistId);
            goToInsidePlaylist.putExtra("playlistID", playlistId);

            Log.i("TAG", "PlaylistId is from adapter: " + playlistId);
            callingActivity.startActivity(goToInsidePlaylist);
        });
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }
    public static class ChooseplaylistViewHolder extends RecyclerView.ViewHolder{

        public ChooseplaylistViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
