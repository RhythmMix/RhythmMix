package com.example.rhythmix.adapter;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

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
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Favorite;
import com.amplifyframework.datastore.generated.model.User;
import com.example.rhythmix.Activites.AddToFavoritesActivity;
import com.example.rhythmix.Activites.MainActivity;
import com.example.rhythmix.R;
import com.example.rhythmix.models.FavoritesHandler;
import com.example.rhythmix.models.Music;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;


public class TrackRecyclerViewAdapter extends RecyclerView.Adapter<TrackRecyclerViewAdapter.TrackListViewHolder> {

    private List<Music> musicList;
    private List<Favorite> musicListFavorite;
    private Activity context;
    private static final String TAG = "TrackRecyclerViewAdapter";

    FavoritesHandler favoritesHandler;
    FavoritesAdapter favoritesAdapter;

    public TrackRecyclerViewAdapter(Activity context, List<Music> musicList) {

        this.context = context;
        this.musicList = musicList;
        this.favoritesHandler = new FavoritesHandler(musicListFavorite,context, favoritesAdapter);

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

    public void addTrack(Music music) {
        musicList.add(music);
        notifyDataSetChanged();
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
//            Toast.makeText(context, "Add to Playlist Clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_text2) {
//            Toast.makeText(context, "Add to Favorite Clicked", Toast.LENGTH_SHORT).show();
            favoritesHandler.addToFavorites(selectedTrack);
        } else if (itemId == R.id.menu_text3) {
            String trackLink = selectedTrack.getPreview();
            favoritesHandler.shareTrack(trackLink);
        }
    }


    ///// =================== Share Track Functionality ==================== ///////////


//    private void shareTrack(String trackLink) {
//        Intent shareIntent = new Intent(Intent.ACTION_SEND);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Sharing Track Link");
//        shareIntent.putExtra(Intent.EXTRA_TEXT, trackLink);
//        context.startActivity(Intent.createChooser(shareIntent, "Share Track Link"));
//    }



    ///// =================== Add To Favorites Functionality ==================== ///////////


//    private void addToFavorites(Music selectedTrack) {
//        String trackId = String.valueOf(selectedTrack.getId());
//        String title = selectedTrack.getTitle();
//        String artist = selectedTrack.getArtist().getName();
//        String mp3 = selectedTrack.getPreview();
//        String albumCover = selectedTrack.getAlbum().getCover();
//
//        checkAndAddToFavorites(trackId, title, artist, mp3, albumCover);
//    }
//
//    private void checkAndAddToFavorites(String trackId, String title, String artist,
//                                        String mp3, String cover) {
//        AuthUser authUser = Amplify.Auth.getCurrentUser();
//        if (authUser != null) {
//            Amplify.API.query(
//                    ModelQuery.list(Favorite.class, Favorite.FAVORITE_ID.eq(trackId)),
//                    response -> {
//                        if (response.getData() != null && response.getData().getItems() != null) {
//                            Iterator<Favorite> iterator = response.getData().getItems().iterator();
//                            if (iterator.hasNext()) {
//                                // Track already exists
//                                showToast("Track is already in favorites");
//                            } else {
//                                // Track doesn't exist, add it
//                                addToFavorites(trackId, title, artist, mp3, cover);
//                            }
//                        }
//                    },
//
//                        error -> {
//                        showToast("Error checking for track: " + error.getMessage());
//                    }
//            );
//        }
//    }
//
//    private void addToFavorites(String trackId, String title, String artist,
//                                String mp3, String cover) {
//        AuthUser authUser = Amplify.Auth.getCurrentUser();
//        if (authUser != null) {
//            Favorite favorite = Favorite.builder()
//                    .favoriteId(trackId)
//                    .favoriteTitle(title)
//                    .favoriteArtist(artist)
//                    .favoriteMp3(mp3)
//                    .userId(authUser.getUserId())
//                    .favoriteCover(cover)
//                    .build();
//
//            Amplify.API.mutate(
//                    ModelMutation.create(favorite),
//                    response -> showToast("Track added to favorites"),
//                    error -> showToast("Error adding track to favorites: " + error.getMessage())
//            );
//        }
//    }
//
//    private void showToast(String message) {
//        runOnUiThread(() -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show());
//    }
}
