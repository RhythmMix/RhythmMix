package com.example.rhythmix.Adapter;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

import android.annotation.SuppressLint;
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
import com.example.rhythmix.Activities.AddToFavoritesActivity;
import com.example.rhythmix.Activities.ChoosePlaylistActivity;
import com.example.rhythmix.R;
import com.example.rhythmix.models.FavoritesHandler;
import com.example.rhythmix.models.Track;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;

public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.DataListViewHolder> {
    private Activity context;
    private List<Track> dataList;
    private List<Favorite> musicListFavorite;
    private static final String TAG = "HOLDER";
    RecyclerView recyclerView;
    private int currentlyPlayingPosition = -1;
    private MediaPlayer currentMediaPlayer;
    FavoritesHandler favoritesHandler;
    FavoritesAdapter favoritesAdapter;



    public DataListAdapter(Activity context, List<Track> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.favoritesHandler = new FavoritesHandler(musicListFavorite,context, favoritesAdapter);

    }

    public DataListAdapter(Activity context, List<Track> dataList,RecyclerView recyclerView) {
        this.context = context;
        this.dataList = dataList;
        this.recyclerView=recyclerView;
        this.favoritesHandler = new FavoritesHandler(musicListFavorite,context, favoritesAdapter);
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
            ImageView menuBtn= holder.itemView.findViewById(R.id.menu_button_main);
            TextView musicArtistName= holder.itemView.findViewById(R.id.musicArtistName);
            initializePopupMenu(menuBtn , track);

            Picasso.get().load(imageUrl).into(musicImage);



            MediaPlayer mediaPlayer = new MediaPlayer();
            try {
                mediaPlayer.setDataSource(context, Uri.parse(track.getPreview()));
                mediaPlayer.prepareAsync(); // Use prepareAsync to avoid blocking UI thread
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Display title & Artist name
            String title = track.getTitle();
            String ArtistName= track.getArtist().getName();
            musicTitle.setText(title);
            musicArtistName.setText(ArtistName);

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

    private void updateToggleIconForItem(int iconResId, int itemPosition) {
        View itemView = recyclerView.findViewHolderForAdapterPosition(itemPosition).itemView;
        ImageButton toggleButton = itemView.findViewById(R.id.toggleButton);
        toggleButton.setImageResource(iconResId);
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
                builder.setMessage("Please log in or sign up to add tracks to playlists.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            });
        }

    }
}