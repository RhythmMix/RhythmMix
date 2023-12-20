package com.example.rhythmix.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Music;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.amplifyframework.datastore.generated.model.PlaylistMusic;
import com.bumptech.glide.Glide;
import com.example.rhythmix.Activities.ChoosePlaylistActivity;
import com.example.rhythmix.R;
import com.example.rhythmix.models.Track;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class ChoosePlaylistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Playlist> playlists;
    private Context callingActivity;
    private OnPlaylistClickListener playlistClickListener;
    private static final String TAG = "playlistTagAdapter";
    String getPlaylistId;
    Intent getTrackIntent;
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
        View playListFragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_choose_playlist, parent, false);
        return new ChooseplaylistViewHolder(playListFragment);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView playlistFragmentText = holder.itemView.findViewById(R.id.playlistTitle);
        String playlistName = playlists.get(position).getPlaylistName();

        ImageView playlistFragmentImage = holder.itemView.findViewById(R.id.playlistImageButton); // Change to ImageView
        String playlistImagePath = playlists.get(position).getPlaylistBackground();
        playlistFragmentText.setText(playlistName);
        Log.d("TaskDetailActivity", "Image URL: " + playlistImagePath);
        String imagePath = "https://rhythmmix90bba48f17b9485194f4a1c4ae1c9bc1200138-dev.s3.us-east-2.amazonaws.com/public/" + playlistImagePath;
        Log.d("imagePath", "Image path: " + imagePath);
        Glide.with(holder.itemView.getContext()).load(imagePath)
                .error(R.drawable.rhythemix)
                .into(playlistFragmentImage);

        holder.itemView.setOnClickListener(view -> {
            getTrackIntent = ((ChoosePlaylistActivity) callingActivity).getIntent();
            Track selectedTrack = (Track) getTrackIntent.getSerializableExtra("SELECTED_TRACK");
            getPlaylistId = playlists.get(position).getId();
            addToPlaylistAndAmplify(selectedTrack);

            showSnackbar(holder.itemView, "Song was added successfully.");
        });
    }
    private void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }


        public void addToPlaylistAndAmplify(Track selectedTrack) {
        String trackTitle = getTrackIntent.getStringExtra("TRACK_TITLE");
        String trackArtist = getTrackIntent.getStringExtra("TRACK_ARTIST");
        String trackMp3 = getTrackIntent.getStringExtra("TRACK_MP3");
        String trackCover = getTrackIntent.getStringExtra("TrackCover");

        Set<String> uniqueIds = new HashSet<>();
        AuthUser authUser = Amplify.Auth.getCurrentUser();

        if (authUser != null) {
            String currentUserId = authUser.getUserId();

            Music music = Music.builder()
                    .musicTitle(trackTitle)
                    .musicArtist(trackArtist)
                    .musicCover(trackCover)
                    .musicMp3(trackMp3)
                    .build();
            PlaylistMusic playlistMusic = PlaylistMusic.builder()
                    .playlist(Playlist.justId(getPlaylistId))
                    .track(music)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(playlistMusic),
                    successResponse -> {
                        Log.i(TAG, "Saved PlaylistMusic item: " + successResponse.getData());
                        saveMusicItem(music);
                    },
                    failureResponse -> Log.e(TAG, "Error saving PlaylistMusic item", failureResponse)
            );
            ((ChoosePlaylistActivity) callingActivity).runOnUiThread(() -> notifyDataSetChanged());
        }
    }

    private boolean isTrackAlreadyInPlaylist(List<PlaylistMusic> playlistMusicList, String musicId) {
        for (PlaylistMusic playlistMusic : playlistMusicList) {
            // Assuming musicId is a unique identifier for Music
            if (playlistMusic.getTrack().getId().equals(musicId)) {
                return true; // Track already exists in the playlist
            }
        }
        return false; // Track is not in the playlist
    }

    private void saveMusicItem(Music music) {
        Amplify.API.mutate(
                ModelMutation.create(music),
                successResponse -> Log.i(TAG, "Saved Music item: " + successResponse.getData()),
                failureResponse -> Log.e(TAG, "Error saving Music item", failureResponse)
        );
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public static class ChooseplaylistViewHolder extends RecyclerView.ViewHolder {
        public ChooseplaylistViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}