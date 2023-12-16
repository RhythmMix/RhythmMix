package com.example.rhythmix.adapter;

import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Favorite;
import com.amplifyframework.datastore.generated.model.Playlist;
import com.amplifyframework.datastore.generated.model.Music;
import com.amplifyframework.datastore.generated.model.User;
import com.example.rhythmix.Activites.AddToFavoritesActivity;

import com.example.rhythmix.Activites.ChoosePlaylistActivity;
import com.example.rhythmix.Activites.SearchActivity;
import com.example.rhythmix.R;
import com.example.rhythmix.models.Track;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;


public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.TrackListViewHolder> {

    private List<Track> trackList;
    private Activity context;
    private static final String TAG = "TrackRecyclerViewAdapter";

    public TrackRecyclerViewAdapter(Activity context, List<Track> trackList) {
        this.context = context;
        this.trackList = trackList;
    }

    @NonNull
    @Override
    public TrackRecyclerViewAdapter.TrackListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_item, parent, false);
        TrackListViewHolder viewHolder = new TrackListViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackListViewHolder holder, int position) {
        if (trackList != null && position >= 0 && position < trackList.size()) {
            Track track = trackList.get(position);
            if (track != null && track.getPreview() != null && track.getArtist().getName() != null && track.getArtist().getPicture() != null) {
                String imageUrl = track.getArtist().getPicture();
                ImageView musicImage = holder.itemView.findViewById(R.id.musicImage);
                TextView musicTitle = holder.itemView.findViewById(R.id.musicTitle);
                ImageButton toggleButton = holder.itemView.findViewById(R.id.toggleButton);
                ImageView menuButton = holder.itemView.findViewById(R.id.menu_button_main);
                initializePopupMenu(menuButton , track);


                Picasso.get().load(imageUrl).into(musicImage);

                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(track.getPreview()));
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

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
    }
    @Override
    public int getItemCount() {
        return trackList.size();
    }

    public class TrackListViewHolder extends RecyclerView.ViewHolder {
        public TrackListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

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
//            Toast.makeText(context, "Add to Playlist Clicked", Toast.LENGTH_SHORT).show();
            addToPlaylist(selectedTrack);
        } else if (itemId == R.id.menu_text2) {
            Toast.makeText(context, "Add to Favorite Clicked", Toast.LENGTH_SHORT).show();
            addToFavorites(selectedTrack);
        } else if (itemId == R.id.menu_text3) {
            Toast.makeText(context, "Share Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    //////>>>>>>>>>>>>>>>>>>>>>>>>> Farah AddToPLayList<<<<<<<<<<<<<<<<<<<<<<<<<<<<

    private void addToPlaylist(Track selectedTrack) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        Intent addToPlaylistIntent = new Intent(context, ChoosePlaylistActivity.class);
        if (authUser != null) {

            long trackId = selectedTrack.getId();
            String trackTitle = selectedTrack.getTitle();
            String trackArtist = selectedTrack.getArtist().getName();
            String trackMp3 = selectedTrack.getPreview();
            String userEmail = authUser.getUsername();
            String cover =selectedTrack.getAlbum().getCover();
            addToPlaylistIntent.putExtra("TRACK_ID", trackId);
            addToPlaylistIntent.putExtra("TRACK_TITLE", trackTitle);
            addToPlaylistIntent.putExtra("TRACK_ARTIST", trackArtist);
            addToPlaylistIntent.putExtra("TRACK_MP3", trackMp3);
            addToPlaylistIntent.putExtra("TrackCover",cover);
            addToPlaylistIntent.putExtra("SELECTED_TRACK", selectedTrack);
            context.startActivity(addToPlaylistIntent);
        }
    }
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>Balqees AddToFavorites<<<<<<<<<<<<<<<<<<<<<<<<<<<


    private void addToFavorites(Track selectedTrack) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        Intent addToFavoritesIntent = new Intent(context, AddToFavoritesActivity.class);

        if (authUser != null) {
            // Extract relevant information from the selected track
            long trackId = selectedTrack.getId();
            String trackTitle = selectedTrack.getTitle();
            String trackArtist = selectedTrack.getArtist().getName();
            String trackMp3 = selectedTrack.getPreview();
            String userEmail = authUser.getUsername(); // Assuming the user's email is used as the username
            String cover =selectedTrack.getAlbum().getCover();


            buildUserAndAddToFavorites(authUser, userEmail, trackId, trackTitle, trackArtist, trackMp3, cover);
            addToFavoritesIntent.putExtra("TRACK_ID", selectedTrack.getId());
            addToFavoritesIntent.putExtra("TRACK_TITLE", selectedTrack.getTitle());
            addToFavoritesIntent.putExtra("TRACK_ARTIST", selectedTrack.getArtist().getName());
            addToFavoritesIntent.putExtra("TRACK_MP3", selectedTrack.getPreview());
            addToFavoritesIntent.putExtra("TrackCover",selectedTrack.getAlbum().getCover());

            context.startActivity(addToFavoritesIntent);
        }
    }
    private void buildUserAndAddToFavorites(AuthUser authUser, String userEmail, long trackId, String trackTitle, String trackArtist, String trackMp3, String cover) {
        User user = User.builder()
                .email(userEmail)
                .id(authUser.getUserId())
                .username(authUser.getUsername())
                .userImageS3Key("")
                .build();

        Favorite favorite = Favorite.builder()
                .favoriteId(String.valueOf(trackId))
                .favoriteTitle(trackTitle)
                .favoriteArtist(trackMp3)
                .favoriteMp3(trackArtist)
                .userId(authUser.getUserId())
                .favoriteCover(cover)
                .build();

        Amplify.API.mutate(
                ModelMutation.create(favorite),
                successResponse -> Log.i(TAG, "Saved item for playlist with name: " + successResponse.getData()),
                failureResponse -> Log.e(TAG, "Error saving item", failureResponse)
        );
    }





    public void addTrack(Track track) {
        trackList.add(track);
        notifyDataSetChanged();
    }

}