package com.example.rhythmix.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.R;
import com.example.rhythmix.adapters.LibrarySongsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 100;
    private static final String LOG_TAG = "LibraryActivity";
    private static final String LOG_TAG_MEDIA_PLAYER = "MediaPlayer";
    private LibrarySongsAdapter librarySongsAdapter;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private ArrayList<String> songPaths;
    private ArrayList<String> songList;
    private ArrayList<String> artistNames;
    private String selectedSongInLibraryPath;
    private int currentPosition;
    private boolean isItemClickListenerActive = false;
    private boolean isPlayAllClicked = false;
    private boolean isPlayAllStopped   = false;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        RecyclerView recyclerView = findViewById(R.id.libraryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            handleLibraryPermissions();
        } else {
            displaySongs();
        }

        setupNavigationBar();
        setupBottomNavigationView();
        searchBar();
        initializePopupMenu();
        setListeners();

    }


    //==============================
    // Songs/Playlist Navbar
    //==============================
    private void setupNavigationBar() {
        RadioGroup navigationBar = findViewById(R.id.navigationBar);
        navigationBar.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.songsButton) {
                RecyclerView libraryRecyclerView = findViewById(R.id.libraryRecyclerView);
                libraryRecyclerView.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.playlistsButton) {
                startActivity(new Intent(LibraryActivity.this, PlaylistsActivity.class));
            }
        });
        navigationBar.check(R.id.songsButton);
    }
    //================================================================================================================================

    //==============================
    // Main Navbar
    //==============================
    private void setupBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.getMenu().findItem(R.id.Home).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Library).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Search).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Profile).setChecked(false);
            if (item.getItemId() == R.id.Home) {
                startActivity(new Intent(LibraryActivity.this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Search) {
                startActivity(new Intent(LibraryActivity.this, SearchActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Library) {
                return true;
            } else if (item.getItemId() == R.id.Profile) {
                startActivity(new Intent(LibraryActivity.this, ProfileActivity.class));
                ;
                return true;
            } else return false;
        });
        bottomNavigationView.getMenu().findItem(R.id.Library).setChecked(true);
    }
    //================================================================================================================================


    //==============================
    // Permissions
    //==============================
    private void handleLibraryPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            displaySongs();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displaySongs();
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //================================================================================================================================

    //==============================
    // Search Bar
    //==============================
    private void searchBar() {
        TextInputEditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                librarySongsAdapter.filter(charSequence.toString());
                updateNoSongsFoundVisibility();
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
    //==========================================================================================

    //==============================
    // Set Listeners
    //==============================
    private void setListeners() {

        // Go to player Activity click item
        librarySongsAdapter = new LibrarySongsAdapter(this, songList, songPaths, artistNames, this);
        RecyclerView recyclerView = findViewById(R.id.libraryRecyclerView);
        recyclerView.setAdapter(librarySongsAdapter); // using the adapter as our data source for encapsulation and separation of responsibilities

        librarySongsAdapter.setOnItemClickListener((parent, view, position, id) -> {
            isItemClickListenerActive = true;
            isPlayAllClicked = false;

            String selectedSongPath = librarySongsAdapter.songPaths.get(position);

            Intent intent = new Intent(LibraryActivity.this, SongPlayerActivity.class);
            intent.putExtra("SONG_PATHS", librarySongsAdapter.songPaths);
            intent.putExtra("SONG_PATH", selectedSongPath);
            intent.putExtra("CURRENT_POSITION", position);
            startActivity(intent);
            stopPlayback();
            isItemClickListenerActive = false;
        });

        // Play/Pause Button
        librarySongsAdapter.setOnPlayPauseButtonClickListener((parent, view, position, id) -> {
            if (!isItemClickListenerActive) {
                if (currentPosition != -1 && currentPosition != position) {
                    librarySongsAdapter.setPlaying(false, currentPosition);
                }

                String clickedSongPath = librarySongsAdapter.songPaths.get(position);
                if (!clickedSongPath.equals(selectedSongInLibraryPath) || !isPlaying) {
                    selectedSongInLibraryPath = clickedSongPath;
                    currentPosition = position;
                    initializeMediaPlayer();
                    playSelectedSong();
                    librarySongsAdapter.setPlaying(true, currentPosition);
                } else {
                    if (isPlaying) {
                        pausePlayback();
                    } else {
                        resumePlayback();
                    }
                }
            }
        });

//         Play All layout
        LinearLayout playAllLayout = findViewById(R.id.controlsLayout);
        playAllLayout.setOnClickListener(v -> {
            if (!isPlayAllClicked) {
                isPlayAllClicked = true;
                try {
                    playAllSongs();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        // number of songs beside the playAll
        TextView numberOfSongsText = findViewById(R.id.numberOfSongsText);
        numberOfSongsText.setText("(" + songPaths.size() + ")");
    }

    //==========================================================================================


    //==============================
    // DisplaySongs
    //==============================
    private void displaySongs() {
        songList = new ArrayList<>();
        songPaths = new ArrayList<>();
        artistNames = new ArrayList<>();

        // Fetch the songs from local storage
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        try {
            // Querying the external media content provider
            Cursor cursor = getContentResolver().query(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    null,
                    null,
                    null
            );

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String songTitle = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    String songArtist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                    String songDuration = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    String songPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));

                    String formattedDuration = formatDuration(songDuration);

                    String displayText = songTitle + "\nArtist: " + songArtist + " " + formattedDuration;

                    songList.add(displayText);
                    songPaths.add(songPath);
                    artistNames.add(songArtist);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //==========================================================================================


    //==============================
    // MediaPlayer Initialization
    //==============================
    private void initializeMediaPlayer() {
        // play the audio of fetched songs

        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                isPlaying = true;
            });
            if (selectedSongInLibraryPath != null) {
                ContentResolver resolver = getContentResolver();
                Uri contentUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
                String selection = MediaStore.Audio.Media.DATA + "=?";
                String[] selectionArgs = {selectedSongInLibraryPath};
                Cursor cursor = resolver.query(contentUri, null, selection, selectionArgs, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int idColumnIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);

                    if (idColumnIndex != -1) {
                        long id = cursor.getLong(idColumnIndex);
                        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
                        cursor.close();

                        mediaPlayer.setDataSource(this, uri);
                        mediaPlayer.prepareAsync();
                    } else cursor.close();
                } else {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //==============================================================================================================

    //==============================
    // Play Song Controls
    //==============================
    private void playSelectedSong() {
        if (selectedSongInLibraryPath != null && !selectedSongInLibraryPath.isEmpty()) {
            currentPosition = songPaths.indexOf(selectedSongInLibraryPath);
            playSong(selectedSongInLibraryPath);
        }
    }

    private void playSong(String path) {
        // Implement the logic to stop the current playback and start the playback of the new song
        stopPlayback();
        selectedSongInLibraryPath = path;
        initializeMediaPlayer();
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            isPlaying = false;
        }

    }

    private void pausePlayback() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPlaying = false;
            updatePlayButton();
        }
    }

    private void resumePlayback() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            isPlaying = true;
            updatePlayButton();
        }
    }

    //======================================================================================

    //==============================
    // PlayAll functionality
    //==============================
    private void playAllSongs() throws IOException {
        if (librarySongsAdapter != null && librarySongsAdapter.getItemCount() > 0) {
            ArrayList<String> songPaths = librarySongsAdapter.songPaths;

            if (currentPosition != -1) {
                librarySongsAdapter.setPlaying(false, currentPosition);
            }
            if (isPlayAllStopped) {
                currentPosition = 0;
                isPlayAllStopped = false;
            }
            playSongAtIndex(songPaths, 0);
        }
    }

    public void playSongAtIndex(ArrayList<String> songPaths, int index) throws IOException {
        if (index < songPaths.size() && isPlayAllClicked) {
            librarySongsAdapter.setPlaying(true, index);
            String songPath = songPaths.get(index);

            playSong(songPath);

//            long duration = getDurationOfSong(songPath);
            long duration = getFullDurationOfSong(songPath);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (isPlayAllClicked) {
                    librarySongsAdapter.setPlaying(false, index);

                    Log.d(LOG_TAG, "playSongAtIndex: Finished playing song at index " + index);

                    try {
                        currentPosition = index + 1;
                        playSongAtIndex(songPaths, index + 1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }, duration);
        } else {
            if (isPlayAllClicked) {
                isPlayAllClicked = false;
                stopPlayback();
                isPlayAllStopped = true;
            }
        }
    }

    public void onPlayPauseClick(int position) {
        if (isPlayAllClicked) {
            // The user clicked on the same song
            if (currentPosition == position) {
                stopPlayback();
                isPlayAllClicked = false;
            } else {
                // The user clicked on a different song, stop "Play All" and play the selected song
                isPlayAllClicked = false; // Stop "Play All"
                stopPlayback();
                selectedSongInLibraryPath = songPaths.get(position);
                initializeMediaPlayer();
                playSelectedSong();
            }
        }
        updatePlayAllButtonState(position);
    }


    private long getFullDurationOfSong(String songPath) throws IOException {
        try {
            if (mediaPlayer == null || mediaPlayer.isPlaying()) {
                return 0;
            }
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(songPath);

            String durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(durationString);

            retriever.release();
            return duration;
        } catch (IllegalStateException e) {
            return 0;
        }
    }

    private long getDurationOfSong(String songPath) {
        return 4000;
    }

    public boolean isPlayAllClicked() {
        return isPlayAllClicked;
    }
    //======================================================================================

    //==============================
    // Add To Playlist functionality
    //==============================

    public void initializePopupMenu() {
        ImageView menuButton = findViewById(R.id.menu_button);

        if (menuButton != null) {
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view);
                }
            });
        }
    }

    public void showPopupMenu(View view) {
        View popupView = getLayoutInflater().inflate(R.layout.playlist_dropdown, null);

        // Create the PopupWindow
        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                true
        );

        // Set the background to allow touch outside to dismiss
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Find your custom menu items
        TextView menuText1 = popupView.findViewById(R.id.menu_text1);
        ImageView menuIcon = popupView.findViewById(R.id.menu_icon);

        ImageView favoriteIcon = popupView.findViewById(R.id.menu_favorite);
        TextView menuText2 = popupView.findViewById(R.id.menu_text2);

        // Set onClickListener for your custom menu items
        menuText1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle custom menu item click
                popupWindow.dismiss();
                onMenuItemClick(R.id.menu_add_to_playlist);
            }
        });

        menuText2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle custom menu item click
                popupWindow.dismiss();
                onMenuItemClick(R.id.menu_text2);
            }
        });

        if (!popupWindow.isShowing()) {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, location[0], location[1] - popupWindow.getHeight());
        }
        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle custom menu item click for favoriteIcon
                popupWindow.dismiss();
                onMenuItemClick(R.id.menu_favorite);
            }
        });


        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle custom menu item click
                popupWindow.dismiss();
                onMenuItemClick(R.id.menu_add_to_playlist);
            }
        });

        // Show the PopupWindow
        if (!popupWindow.isShowing()) {
            popupWindow.showAsDropDown(view);
        }
    }

    private void onMenuItemClick(int itemId) {
        // Handle item click as needed
        if (itemId == R.id.menu_add_to_playlist) {
            Toast.makeText(LibraryActivity.this, "Add to Playlist Clicked", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.menu_text2) {
            Toast.makeText(LibraryActivity.this, "Add to Favorite Clicked", Toast.LENGTH_SHORT).show();
            // Add your logic for "Add to Favorite" action
        } else if (itemId == R.id.menu_favorite) {
            Toast.makeText(LibraryActivity.this, "Favorite Clicked", Toast.LENGTH_SHORT).show();
            // Add your logic for "Favorite" action
        }
    }

    //======================================================================================

    //==============================
    // UI Updates
    //==============================

    private void updatePlayButton() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            isPlaying = true;
        }
    }
    private void updatePlayAllButtonState(int position) {
        boolean isPlaying = mediaPlayer != null && mediaPlayer.isPlaying();
        librarySongsAdapter.setPlaying(isPlaying, position);
    }


    private void updateNoSongsFoundVisibility() {
        TextView noSongsFoundTextView = findViewById(R.id.noSongsFoundTextView);
        if (librarySongsAdapter.getItemCount() == 0) {
            noSongsFoundTextView.setVisibility(View.VISIBLE);
        } else {
            noSongsFoundTextView.setVisibility(View.GONE);
        }
    }

    //========================================================================================================================

    private String formatDuration(String duration) {
        long millis = Long.parseLong(duration);
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }


    //==============================
    // Media Player Lifecycle
    //==============================
    @Override
    protected void onPause() {
        super.onPause();
        pausePlayback();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPlayback();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumePlayback();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}