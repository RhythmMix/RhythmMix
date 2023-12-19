package com.example.rhythmix.Adapter;
import android.app.AlertDialog;
import android.content.Context;
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
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Music;
import com.example.rhythmix.Activities.InsidePlaylistActivity;
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private final List<Music> musicList;
    private final Context callingActivity;
    private MediaPlayer currentMediaPlayer;

    private RecyclerView recyclerView;
    private int currentlyPlayingPosition = -1;

    public MusicAdapter(List<Music> musicList, Context context,RecyclerView recyclerView) {
        this.musicList = musicList;
        this.callingActivity = context;
        this.recyclerView=recyclerView;
    }
    @NonNull
    @Override
    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_music, parent, false);
        return new MusicViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
        Music music = musicList.get(position);
        holder.titleTextView.setText(music.getMusicTitle());
        holder.artistName.setText(music.getMusicArtist());
        ImageView albumCoverView = holder.musicCover;
        ImageButton previewBtn = holder.mp3Datat;
        String trackPreview = music.getMusicMp3();

        Picasso.get().load(music.getMusicCover()).into(albumCoverView);

        // MediaPlayer initialization
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(callingActivity, Uri.parse(trackPreview));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        previewBtn.setOnClickListener(v -> {
            int clickedPosition = holder.getAdapterPosition();

            if (currentMediaPlayer != null && currentMediaPlayer.isPlaying()) {
                if (currentlyPlayingPosition == clickedPosition) {
                    currentMediaPlayer.pause();
                    updateToggleIconForItem(R.drawable.round_play_circle_24, clickedPosition);
                    currentlyPlayingPosition = -1;
                } else {
                    currentMediaPlayer.pause();
                    updateToggleIconForItem(R.drawable.round_play_circle_24, currentlyPlayingPosition);

                    mediaPlayer.start();
                    updateToggleIconForItem(android.R.drawable.ic_media_pause, clickedPosition);
                    currentlyPlayingPosition = clickedPosition;
                }
            } else {
                // No song is currently playing or paused, start the clicked song
                mediaPlayer.start();
                updateToggleIconForItem(android.R.drawable.ic_media_pause, clickedPosition);
                currentlyPlayingPosition = clickedPosition; // Update currentlyPlayingPosition
            }

            // Update the currentMediaPlayer to the new one
            currentMediaPlayer = mediaPlayer;
        });
        holder.deleteMusicButton.setOnClickListener(v -> deleteMusicItem(position));
    }

    private void updateToggleIconForItem(int iconResId, int itemPosition) {
        View itemView = recyclerView.findViewHolderForAdapterPosition(itemPosition).itemView;
        ImageButton toggleButton = itemView.findViewById(R.id.preview_button);
        toggleButton.setImageResource(iconResId);
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }
    public static class MusicViewHolder extends RecyclerView.ViewHolder {
        public final TextView titleTextView;
        public final ImageView musicCover;
        public final TextView artistName;
        public final ImageButton mp3Datat;
        public final ImageButton deleteMusicButton;
        public MusicViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.musicTitle);
            musicCover = itemView.findViewById(R.id.musicCover);
            artistName = itemView.findViewById(R.id.artistName);
            mp3Datat = itemView.findViewById(R.id.preview_button);
            deleteMusicButton = itemView.findViewById(R.id.deleteMusicButton);
        }
    }
    private void deleteMusicItem(int position) {
        Music musicToDelete = musicList.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(callingActivity);
        builder.setMessage("Are you sure you want to delete this track from this playlist?")
                .setPositiveButton("OK", (dialog, which) -> {
                    Amplify.API.mutate(
                            ModelMutation.delete(musicToDelete),
                            response -> {
                                // Remove the item from the list
                                musicList.remove(position);
                                // Notify the adapter about the removal
                                notifyItemRemoved(position);
                                // Notify the adapter about the data set change
                                notifyDataSetChanged();
                            },
                            error -> {
                                Log.e("MusicAdapter", "Error deleting music item", error);
                            }
                    );
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}


