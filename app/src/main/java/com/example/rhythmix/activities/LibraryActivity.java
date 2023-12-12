package com.example.rhythmix.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.R;
import com.example.rhythmix.adapters.LibrarySongsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class LibraryActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 100;
    final static String TAG="allSongsActivity";
    LibrarySongsAdapter librarySongsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);

        RecyclerView recyclerView = findViewById(R.id.libraryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
            } else {
                displaySongs();
            }
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


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            bottomNavigationView.getMenu().findItem(R.id.Home).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Library).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Search).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Profile).setChecked(false);
            if (item.getItemId() == R.id.Home) {
                startActivity(new Intent(LibraryActivity.this, MainActivity.class));
                item.setChecked(true);
                return true;
            } else if (item.getItemId() == R.id.Search) {
                startActivity(new Intent(LibraryActivity.this, SearchActivity.class));
                item.setChecked(true);
                return true;
            } else if (item.getItemId() == R.id.Library) {
                item.setChecked(true);
                return true;
            } else if (item.getItemId() == R.id.Profile) {
                startActivity(new Intent(LibraryActivity.this, ProfileActivity.class));
                item.setChecked(true);
                return true;
            } else return false;
        });
    }


    private void displaySongs() {
        ArrayList<String> songList = new ArrayList<>();
        ArrayList<String> songPaths = new ArrayList<>();
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

        librarySongsAdapter = new LibrarySongsAdapter(this, songList, songPaths);
        RecyclerView recyclerView = findViewById(R.id.libraryRecyclerView);
        recyclerView.setAdapter(librarySongsAdapter);

        librarySongsAdapter.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSongPath = songPaths.get(position);
            String selectedSongTitle = songList.get(position).split("\n")[0];
            String artistName = artistNames.get(position);

            Intent intent = new Intent(LibraryActivity.this, SongPlayerActivity.class);
            intent.putExtra("SONG_PATHS", songPaths);
            intent.putExtra("SONG_PATH", selectedSongPath);
            intent.putExtra("CURRENT_POSITION", position);
            startActivity(intent);
        });
    }

    private String formatDuration(String duration) {
        long millis = Long.parseLong(duration);
        long minutes = (millis / 1000) / 60;
        long seconds = (millis / 1000) % 60;
        return String.format("%02d:%02d", minutes, seconds);
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
}
