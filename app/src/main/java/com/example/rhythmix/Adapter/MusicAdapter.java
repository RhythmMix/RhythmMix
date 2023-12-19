package com.example.rhythmix.Adapter;
import android.app.AlertDialog;
import android.content.Context;
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
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {

    private final List<Music> musicList;
    private final Context callingActivity;

    public MusicAdapter(List<Music> musicList, Context context) {
        this.musicList = musicList;
        this.callingActivity = context;
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

        Picasso.get().load(music.getMusicCover()).into(albumCoverView);
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(callingActivity, Uri.parse(music.getMusicMp3()));
            mediaPlayer.setOnPreparedListener(mp -> {
                if (mediaPlayer.isPlaying()) {
                    previewBtn.setImageResource(android.R.drawable.ic_media_pause);
                } else {
                    previewBtn.setImageResource(android.R.drawable.ic_media_play);
                }
                previewBtn.setOnClickListener(v -> {
                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        previewBtn.setImageResource(android.R.drawable.ic_media_play);
                    } else {
                        mediaPlayer.start();
                        previewBtn.setImageResource(android.R.drawable.ic_media_pause);
                    }
                });
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        holder.deleteMusicButton.setOnClickListener(v -> deleteMusicItem(position));
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
                                musicList.remove(position);
                                notifyItemRemoved(position);
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