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
import com.amplifyframework.datastore.generated.model.User;
import com.example.rhythmix.Activites.AddToFavoritesActivity;
import com.example.rhythmix.Activites.MainActivity;
import com.example.rhythmix.R;
import com.example.rhythmix.models.Music;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.List;


public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.TrackListViewHolder> {

    private List<Music> musicList;
    private Activity context;
    private static final String TAG = "TrackRecyclerViewAdapter";

    public TrackRecyclerViewAdapter(Activity context, List<Music> musicList) {
        this.context = context;
        this.musicList = musicList;
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
        if (musicList != null && position >= 0 && position < musicList.size()) {
            Music music = musicList.get(position);
            if (music != null && music.getPreview() != null && music.getArtist().getName() != null && music.getArtist().getPicture() != null) {
                String imageUrl = music.getArtist().getPicture();
                ImageView musicImage = holder.itemView.findViewById(R.id.musicImage);
                TextView musicTitle = holder.itemView.findViewById(R.id.musicTitle);
                ImageButton toggleButton = holder.itemView.findViewById(R.id.toggleButton);
                ImageView menuButton = holder.itemView.findViewById(R.id.menu_button_main);
                TextView musicArtistName= holder.itemView.findViewById(R.id.musicArtistName);
                initializePopupMenu(menuButton , music);


                Picasso.get().load(imageUrl).into(musicImage);

                MediaPlayer mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(context, Uri.parse(music.getPreview()));
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                String titleArtist = music.getTitle();
                musicTitle.setText(titleArtist);

                String artistName = music.getArtist().getName();
                musicArtistName.setText(artistName);


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
        return musicList.size();
    }

    public class TrackListViewHolder extends RecyclerView.ViewHolder {
        public TrackListViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }



    //==============================
    // Menu popup  functionality
    //==============================
    public void initializePopupMenu(View menuButton, Music selectedTrack) {

        if (menuButton != null) {
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view, selectedTrack);
                }
            });
        }
    }

    public void showPopupMenu(View view, Music selectedTrack) {
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

    private void onMenuItemClick(int itemId, Music selectedTrack) {
        if (itemId == R.id.menu_text1) {
            Toast.makeText(context, "Add to Playlist Clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_text2) {
            Toast.makeText(context, "Add to Favorite Clicked", Toast.LENGTH_SHORT).show();
            addToFavorites(selectedTrack);
        } else if (itemId == R.id.menu_text3) {
            String trackLink = selectedTrack.getPreview();
            shareTrack(trackLink);
            Toast.makeText(context, "Share Clicked", Toast.LENGTH_SHORT).show();
        }
    }

    private void shareTrack(String trackLink) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Track Link");
        shareIntent.putExtra(Intent.EXTRA_TEXT, trackLink);
        context.startActivity(Intent.createChooser(shareIntent, "Share Track Link"));
    }

    private void addToFavorites(Music selectedTrack) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();
        Intent addToFavoritesIntent = new Intent(context, AddToFavoritesActivity.class);

        if (authUser != null) {
            long trackId = selectedTrack.getId();
            String trackTitle = selectedTrack.getTitle();
            String trackArtist = selectedTrack.getArtist().getName();
            String trackMp3 = selectedTrack.getPreview();
            String albumCover= selectedTrack.getAlbum().getCover();

            buildUserAndAddToFavorites(trackId, trackTitle, trackArtist, trackMp3, albumCover);

            addToFavoritesIntent.putExtra("TRACK_ID", trackId);
            addToFavoritesIntent.putExtra("TRACK_TITLE", trackTitle);
            addToFavoritesIntent.putExtra("TRACK_ARTIST", trackArtist);
            addToFavoritesIntent.putExtra("TRACK_COVER", albumCover);
            addToFavoritesIntent.putExtra("TRACK_MP3", trackMp3);

            context.startActivity(addToFavoritesIntent);
        }
    }


    private void buildUserAndAddToFavorites(long trackId, String trackTitle, String trackArtist, String trackMp3 , String albumCover) {
        AuthUser authUser = Amplify.Auth.getCurrentUser();

        Favorite favorite = Favorite.builder()
                .favoriteId(String.valueOf(trackId))
                .favoriteTitle(trackTitle)
                .favoriteArtist(trackArtist)
                .favoriteMp3(trackMp3)
                .userId(authUser.getUserId())
                .favoriteCover(albumCover)
                .build();


        Amplify.API.mutate(
                ModelMutation.create(favorite),
                successResponse -> Log.i(TAG, "Saved item for playlist with name: " + successResponse.getData()),
                failureResponse -> Log.e(TAG, "Error saving item", failureResponse)
        );
    }

    public void addTrack(Music music) {
        musicList.add(music);
        notifyDataSetChanged();
    }

}