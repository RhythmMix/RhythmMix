package com.example.rhythmix.Adapter;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Favorite;
import com.example.rhythmix.Activities.APISongPlayerActivity;
import com.example.rhythmix.Activities.ChoosePlaylistActivity;
import com.example.rhythmix.Activities.MainActivity;
import com.example.rhythmix.R;
import com.example.rhythmix.models.FavoritesHandler;
import com.example.rhythmix.models.Track;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.TrackListViewHolder> {

    private List<Track> musicList;
    private List<String> previewUrls;
    private List<String> titleList;
    private List<String> artistsList;
    private List<Favorite> musicListFavorite;
    private Activity context;
    private MediaPlayer currentMediaPlayer;

    private RecyclerView recyclerView;
    private int currentlyPlayingPosition = -1;
    private static final String TAG = "TrackRecyclerViewAdapter";
    private MainActivity mainActivity;
    FavoritesHandler favoritesHandler;
    FavoritesAdapter favoritesAdapter;


    public TrackRecyclerViewAdapter(Activity context, List<Track> musicList,List<String> previewUrls, RecyclerView recyclerView,List<String> titleList,List<String> artistsList) {
        this.context = context;
        this.musicList = musicList;
        this.recyclerView = recyclerView;
        this.previewUrls=previewUrls;
        this.titleList=titleList;
        this.artistsList=artistsList;
        this.favoritesHandler = new FavoritesHandler(musicListFavorite,context, favoritesAdapter);
    }
    public TrackRecyclerViewAdapter(Activity context, List<Track> musicList) {
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
            Track music = musicList.get(position);
            if (music != null && music.getPreview() != null && music.getArtist().getName() != null && music.getArtist().getPicture() != null) {
                String imageUrl = music.getArtist().getPicture();
                ImageView musicImage = holder.itemView.findViewById(R.id.musicImage);
                TextView musicTitle = holder.itemView.findViewById(R.id.musicTitle);
                ImageButton toggleButton = holder.itemView.findViewById(R.id.toggleButton);
                TextView artistName = holder.itemView.findViewById(R.id.musicArtistName);
                ImageView menuButton = holder.itemView.findViewById(R.id.menu_button_main);


                initializePopupMenu(menuButton , music);
                Picasso.get().load(imageUrl).into(musicImage);

                // Displaying title & Artist name & path
                String songTitle = music.getTitle();
                musicTitle.setText(songTitle);

                String songArtist = music.getArtist().getName();
                artistName.setText(songArtist);

                String songPath = music.getPreview();

                // Set click listener for the entire item view
                holder.itemView.setOnClickListener(v -> {
                    int clickedPosition = holder.getAdapterPosition();

                    // Pass data to SongPlayerActivity
                    Track clickedMusic = musicList.get(clickedPosition);
                    if (clickedMusic != null) {

                        Intent intent = new Intent(context, APISongPlayerActivity.class);
                        intent.putExtra("SONG_TITLE", clickedMusic.getTitle());
                        intent.putExtra("SONG_ARTIST", clickedMusic.getArtist().getName());
                        intent.putExtra("SONG_PATH", clickedMusic.getPreview());

                        Log.i(TAG,"previewUrlsSSS"+previewUrls);

                        intent.putStringArrayListExtra("PREVIEW_URLS", new ArrayList<>(previewUrls));
                        intent.putStringArrayListExtra("TITLES_LIST", new ArrayList<>(titleList));
                        intent.putStringArrayListExtra("ARTISTS_LIST", new ArrayList<>(artistsList));
                        intent.putExtra("CURRENT_POSITION", clickedPosition);

                        if (currentlyPlayingPosition != -1) {
                            currentMediaPlayer.pause();
                            // Update the toggle icon for the item at the currently playing position
                            updateToggleIconForItem(android.R.drawable.ic_media_play, currentlyPlayingPosition);
                        }


                        context.startActivity(intent);
                    }
                });

                holder.itemView.setTag(position);


                // MediaPlayer initialization
                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(songPath));
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                toggleButton.setOnClickListener(v -> {
                    int clickedPosition = holder.getAdapterPosition();

                    if (currentMediaPlayer != null && currentMediaPlayer.isPlaying()) {
                        if (currentlyPlayingPosition == clickedPosition) {
                            currentMediaPlayer.pause();
                            updateToggleIconForItem(android.R.drawable.ic_media_play, clickedPosition);
                            currentlyPlayingPosition = -1;
                        } else {
                            currentMediaPlayer.pause();
                            updateToggleIconForItem(android.R.drawable.ic_media_play, currentlyPlayingPosition);

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
            }
        }
    }

    private void updateToggleIconForItem(int iconResId, int itemPosition) {
        View itemView = recyclerView.findViewHolderForAdapterPosition(itemPosition).itemView;
        ImageButton toggleButton = itemView.findViewById(R.id.toggleButton);
        toggleButton.setImageResource(iconResId);
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

    public void addTrack(Track music) {
        musicList.add(music);
        notifyDataSetChanged();
    }

    //==============================
    // Menu popup  functionality
    //==============================
    public void initializePopupMenu(View menuButton, Track selectedTrack) {

        if (menuButton != null) {
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view, selectedTrack);
                }
            });
        }
    }

    public void showPopupMenu(View view, Track selectedTrack) {
        View popupView = LayoutInflater.from(context).inflate(R.layout.playlist_dropdown_home_page, null);
        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView menuText1 = popupView.findViewById(R.id.menu_text1);
        TextView menuText2 = popupView.findViewById(R.id.menu_text2);
        TextView menuText3 = popupView.findViewById(R.id.menu_text3);

        menuText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                onMenuItemClick(R.id.menu_text1, selectedTrack);
            }
        });

        menuText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                onMenuItemClick(R.id.menu_text2, selectedTrack);
            }
        });

        menuText3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                onMenuItemClick(R.id.menu_text3, selectedTrack);
            }
        });

        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(view);
        }
    }

    private void onMenuItemClick(int itemId, Track selectedTrack) {
        if (itemId == R.id.menu_text1) {
            addToPlaylist(selectedTrack);
        } else if (itemId == R.id.menu_text2) {
            favoritesHandler.addToFavorites(selectedTrack);
        } else if (itemId == R.id.menu_text3) {
            String trackLink = selectedTrack.getPreview();
            favoritesHandler.shareTrack(trackLink);
        }
    }

    private void addToPlaylist(Track selectedTrack) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        Intent addToPlaylistIntent = new Intent(context, ChoosePlaylistActivity.class);
        if (authUser != null) {
            long trackId = selectedTrack.getId();
            String trackTitle = selectedTrack.getTitle();
            String trackArtist = selectedTrack.getArtist().getName();
            String trackMp3 = selectedTrack.getPreview();
            String userEmail = authUser.getUsername();
            String cover = selectedTrack.getAlbum().getCover();
            addToPlaylistIntent.putExtra("TRACK_ID", trackId);
            addToPlaylistIntent.putExtra("TRACK_TITLE", trackTitle);
            addToPlaylistIntent.putExtra("TRACK_ARTIST", trackArtist);
            addToPlaylistIntent.putExtra("TRACK_MP3", trackMp3);
            addToPlaylistIntent.putExtra("TrackCover", cover);
            addToPlaylistIntent.putExtra("SELECTED_TRACK", selectedTrack);
            context.startActivity(addToPlaylistIntent);
        } else {
            runOnUiThread(() -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Please log in or sign up to add tracks to favorites.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            });
        }

    }
}
