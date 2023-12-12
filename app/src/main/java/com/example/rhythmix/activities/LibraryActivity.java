package com.example.rhythmix.activities;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
    final static String LOG_TAG ="LibraryActivity";
    private static final String LOG_TAG_MEDIA_PLAYER = "MediaPlayer";
    LibrarySongsAdapter librarySongsAdapter;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private ArrayList<String> songPaths;
    String selectedSongInLibraryPath;
    private int currentPosition;
    boolean isItemClickListenerActive=false;


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

        // Songs/Playlist Navbar
        RadioGroup navigationBar = findViewById(R.id.navigationBar);
        navigationBar.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.songsButton) {
                RecyclerView libraryRecyclerView = findViewById(R.id.libraryRecyclerView);
                libraryRecyclerView.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.playlistsButton) {
                startActivity(new Intent(LibraryActivity.this, PlaylistsActivity.class));
            }
        });
        // Set the default selection to "Songs"
        navigationBar.check(R.id.songsButton);


        // Main Navbar
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
                startActivity(new Intent(LibraryActivity.this, ProfileActivity.class));;
                return true;
            } else return false;
        });
        bottomNavigationView.getMenu().findItem(R.id.Library).setChecked(true);


    }

    //==============================
    // Permissions
    //==============================
    private void handleLibraryPermissions(){
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
    // DisplaySongs
    //==============================
    private void displaySongs() {
        ArrayList<String> songList = new ArrayList<>();
        songPaths = new ArrayList<>();
        ArrayList<String> artistNames = new ArrayList<>();
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

                    String displayText =  songTitle + "\nArtist: " + songArtist +" " + formattedDuration;

                    songList.add(displayText);
                    songPaths.add(songPath);
                    artistNames.add(songArtist);
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        librarySongsAdapter = new LibrarySongsAdapter(this, songList, songPaths,artistNames);
        RecyclerView recyclerView = findViewById(R.id.libraryRecyclerView);
        recyclerView.setAdapter(librarySongsAdapter);

        librarySongsAdapter.setOnItemClickListener((parent, view, position, id) -> {
            isItemClickListenerActive = true;
            String selectedSongPath = librarySongsAdapter.songPaths.get(position);

            Intent intent = new Intent(LibraryActivity.this, SongPlayerActivity.class);
            intent.putExtra("SONG_PATHS", librarySongsAdapter.songPaths);
            intent.putExtra("SONG_PATH", selectedSongPath);
            intent.putExtra("CURRENT_POSITION", position);
            startActivity(intent);
            stopPlayback();
            isItemClickListenerActive = false;
        });


        // Search Bar
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

        // Play/Pause Button
        librarySongsAdapter.setOnPlayPauseButtonClickListener((parent, view, position, id) -> {
            if (!isItemClickListenerActive) {
                if (currentPosition != -1 && currentPosition != position) {
                    librarySongsAdapter.setPlaying(false, currentPosition);
                    Log.d(LOG_TAG, "Play/Pause button clicked for position: " + position + ". Current isPlaying status: " + librarySongsAdapter.isPlayingArray[position]);
                }

                String clickedSongPath = librarySongsAdapter.songPaths.get(position);
                if (!clickedSongPath.equals(selectedSongInLibraryPath) || !isPlaying) {
                    selectedSongInLibraryPath = clickedSongPath;
                    currentPosition = position;
                    initializeMediaPlayer();
                    playSelectedSong();
                    librarySongsAdapter.setPlaying(true, currentPosition);
                    Log.d(LOG_TAG, "Starting playback for new position: " + currentPosition);
                } else {
                    stopPlayback();
                    librarySongsAdapter.setPlaying(false, currentPosition);
                    Log.d(LOG_TAG, "Stopping playback for current position: " + currentPosition);
                    currentPosition = -1;
                }
            }
        });
    }

    //==========================================================================================

    //==============================
    // MediaPlayer Initialization
    //==============================
    private void initializeMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setOnCompletionListener(mp -> {
//                playNextSong();
            });
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

    //======================================================================================

    //==============================
    // UI Updates
    //==============================
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }
    private void releaseMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}