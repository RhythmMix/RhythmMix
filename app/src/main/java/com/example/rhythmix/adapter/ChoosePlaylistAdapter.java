package com.example.rhythmix.adapter;

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

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Music;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.amplifyframework.datastore.generated.model.PlaylistMusic;
import com.bumptech.glide.Glide;
import com.example.rhythmix.Activites.InsidePlaylistActivity;
import com.example.rhythmix.R;

import java.util.List;

public class ChoosePlaylistAdapter extends RecyclerView.Adapter  {
    List<Playlist> playlists;
    Context callingActivity;
    Intent getTrackIntent;
    String playlistId;
    public static final String TAG = "playlistTagAdapter";
    private OnPlaylistClickListener playlistClickListener;


//    @Override
//    public void onPlaylistClick(String playlistId) {
//
//    }

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
        return new playlistRecyclerViewAdapter.playlistViewHolder(playListFragment);


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

            // Get the playlist ID and pass it to onPlaylistClick
            playlistId = playlists.get(position).getId();
            playlistClickListener.onPlaylistClick(playlistId);

            Log.i("TAG", "PlaylistId is from adapter: " + playlistId);
            callingActivity.startActivity(goToInsidePlaylist);
        });
    }


//    public void addToPlaylistAndAmplify(Intent selectedTrack) {
//        String trackTitle = getTrackIntent.getStringExtra("TRACK_TITLE");
//        String trackArtist = getTrackIntent.getStringExtra("TRACK_ARTIST");
//        String trackMp3 = getTrackIntent.getStringExtra("TRACK_MP3");
//        String trackCover = getTrackIntent.getStringExtra("TrackCover");
//
//        Music music = Music.builder()
//                .musicTitle(trackTitle)
//                .musicArtist(trackArtist)
//                .musicCover(trackCover)
//                .musicMp3(trackMp3)
//                .build();
//        Log.i("TAG","before creating playlistmusic table: "+playlistId);
//        if (playlistId != null) {
//            PlaylistMusic playlistMusic = PlaylistMusic.builder()
//                    .playlist(Playlist.justId(playlistId))
//                    .track(music)
//                    .build();
//            Amplify.API.mutate(
//                    ModelMutation.create(playlistMusic),
//                    successResponse -> {
//                        Log.i(TAG, "addToPlaylistAndAmplify: Saved PlaylistMusic item: " + successResponse.getData());
//                        saveMusicItem(music);
//                    },
//                    failureResponse -> Log.e(TAG, "addToPlaylistAndAmplify: Error saving PlaylistMusic item", failureResponse)
//            );
//
//        } else {
//            Log.e(TAG, "addToPlaylistAndAmplify: No playlist ID available");
//        }
//    }



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
