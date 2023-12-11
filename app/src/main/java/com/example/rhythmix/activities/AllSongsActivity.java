package com.example.rhythmix.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.R;
import com.example.rhythmix.adapters.AllSongsAdapter;

import java.util.ArrayList;

public class AllSongsActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 100;
    final static String TAG="allSongsActivity";
    AllSongsAdapter allSongsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
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

        allSongsAdapter = new AllSongsAdapter(this, songList, songPaths,artistNames);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(allSongsAdapter);

        allSongsAdapter.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSongPath = songPaths.get(position);
            String selectedSongTitle = songList.get(position).split("\n")[0];

            String[] titleAndArtist = selectedSongPath.split(" - ");
            String songArtist = titleAndArtist.length > 1 ? titleAndArtist[1] : "";

            songArtist = songArtist.replace(".mp3", "");


            Intent intent = new Intent(AllSongsActivity.this, SongPlayerActivity.class);
            intent.putExtra("SONG_PATHS", songPaths);
            intent.putExtra("SONG_PATH", selectedSongPath);
            intent.putExtra("SONG_NAME", selectedSongTitle);
            intent.putExtra("ARTIST_NAME", songArtist);
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
                Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
