package com.example.rhythmix.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rhythmix.R;

import java.util.ArrayList;

public class AllSongsActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 100;
    final static String TAG="allSongsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_songs);

        ListView listView = findViewById(R.id.listViewSong);

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
                }
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        customAdapter adapter = new customAdapter(songList,songPaths);
        ListView listView = findViewById(R.id.listViewSong);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSongPath = songPaths.get(position);
            String selectedSongTitle = songList.get(position).split("\n")[0];

            Intent intent = new Intent(AllSongsActivity.this, SongPlayerActivity.class);
            intent.putExtra("SONG_PATHS", songPaths);
            intent.putExtra("SONG_PATH", selectedSongPath);
            intent.putExtra("SONG_NAME", selectedSongTitle);
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

    class customAdapter extends BaseAdapter {

        private ArrayList<String> songList;
        private ArrayList<String> songPaths;

        public customAdapter(ArrayList<String> songList, ArrayList<String> songPaths) {
            this.songList = songList;
            this.songPaths = songPaths;
        }

        @Override
        public int getCount() {
            return songList.size();
        }

        @Override
        public Object getItem(int i) {
            return songList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView textSong = myView.findViewById(R.id.songName);
            textSong.setSelected(true);
            textSong.setText(songList.get(i));
            myView.setTag(songPaths.get(i));
            return myView;
        }
    }

}
