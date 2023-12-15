package com.example.rhythmix.Activites;

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
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rhythmix.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SongPlayerActivity extends AppCompatActivity {

    final static String TAG="songPlayerActivity";
    private static final int REQUEST_PERMISSION = 100;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private SeekBar seekBar;
    private Handler handler;
    private Runnable runnable;
    private TextView txtStart, txtStop;
    TextView songNameTextView;
    TextView artistNameTextView;
    private ArrayList<String> songPaths;
    private String songPath;
    private int currentPosition;
    private boolean isShuffleActive;
    private ImageView shuffleButton;
    private int lastPlayedPosition = 0;
    private boolean isFavorite = false;
    private ImageView heartButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_player);

        songPaths = getIntent().getStringArrayListExtra("SONG_PATHS");
        songPath = getIntent().getStringExtra("SONG_PATH");
        isShuffleActive = false;

        // Permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            handlePermissions();
        } else {
            initializeMediaPlayer(); // For versions lower than Marshmallow
        }

        // UI elements
        initializeUI();

        // MediaPlayer setup
        initializeMediaPlayer();

        // Play the selected song
        playSelectedSong();

        // Back button functionality
        backButton();

    }

    //==============================
    // Permissions
    //==============================
    private void handlePermissions() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
        } else {
            initializeMediaPlayer();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeMediaPlayer();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    //==============================
    // backButton
    //==============================
    private void backButton(){
        ImageButton backButton=findViewById(R.id.backButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(SongPlayerActivity.this, LibraryActivity.class);
            stopPlayback();
            startActivity(intent);
        });
    }

    //==============================
    // UI Initialization
    //==============================
    private void initializeUI() {
        // UI elements initialization
        songNameTextView = findViewById(R.id.playerActivitySongName);
        artistNameTextView=findViewById(R.id.artistName);
        ImageView songImageView = findViewById(R.id.playerActivitySongImage);
        txtStart = findViewById(R.id.playerActivityTxtStart);
        txtStop = findViewById(R.id.playerActivityTxtStop);
        Button playButton = findViewById(R.id.playButton);
        Button nextButton = findViewById(R.id.nextButton);
        Button prevButton = findViewById(R.id.prevButton);
        seekBar = findViewById(R.id.seekBar);
        shuffleButton = findViewById(R.id.shuffleButton);
        heartButton = findViewById(R.id.heartButton);

        // Other UI setup
        currentPosition = getIntent().getIntExtra("CURRENT_POSITION", 0);

        playButton.setOnClickListener(view -> {
            if (isPlaying) {
                pausePlayback();
            } else {
                resumePlayback();
            }
        });

        nextButton.setOnClickListener(view -> playNextSong());

        prevButton.setOnClickListener(view -> playPreviousSong());

        // Set up seek bar listener
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    txtStart.setText(formatDuration(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Not needed for your functionality but for override
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Not needed for your functionality but for override
            }
        });

        // Set up favorite listener
        heartButton.setOnClickListener(view -> onHeartButtonClick());

        // Set up shuffle listener
        shuffleButton.setOnClickListener(view -> toggleShuffle());
        updateShuffleButtonColor();
    }

    private void updateShuffleButtonColor() {
        int shuffleButtonColor = isShuffleActive ? R.color.shuffleColor : R.color.white;
        shuffleButton.setColorFilter(getResources().getColor(shuffleButtonColor));
    }


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

        handler = new Handler();

        try {
            mediaPlayer.setOnCompletionListener(mp -> {
                playNextSong();
            });
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                isPlaying = true;
                updateSeekBar();
                updatePlayButton();
            });
            if (songPath != null) {
                ContentResolver resolver = getContentResolver();
                Uri contentUri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL);
                String selection = MediaStore.Audio.Media.DATA + "=?";
                String[] selectionArgs = {songPath};
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

    private void playSelectedSong() {
        String selectedSongPath = getIntent().getStringExtra("SONG_PATH");

        Log.i(TAG, "Selected Song Path (SongPlayerActivity): " + selectedSongPath);

        if (songPaths != null && !songPaths.isEmpty()) {
            currentPosition = songPaths.indexOf(selectedSongPath);
            playSong(selectedSongPath);
        }
    }


    private void playSong(String path) {
        // Implement the logic to stop the current playback and start the playback of the new song
        stopPlayback();
        songPath = path;
        initializeMediaPlayer();
        String songName = getSongNameFromPath(path);
        songNameTextView.setText(songName);
        String songArtist =getArtistNameFromPath(path);
        artistNameTextView.setText(songArtist);
    }
    private String getSongNameFromPath(String path) {
        String fileName = new File(path).getName();
        int dashIndex = fileName.indexOf(" - ");
        if (dashIndex != -1) {
            return fileName.substring(0, dashIndex);
        } else {
            return fileName;
        }
    }
    private String getArtistNameFromPath(String path) {
        String fileName = new File(path).getName();
        int dashIndex = fileName.indexOf(" - ");
        int dotIndex = fileName.lastIndexOf(".");
        if (dashIndex != -1 && dotIndex != -1) {
            return fileName.substring(dashIndex + 3, dotIndex);
        } else {
            return "";
        }
    }

    //==============================
    // Playback Control
    //==============================
    private void playNextSong() {
        if (isShuffleActive) {
            playRandomSong();
        } else {
            playSequentialNextSong();
        }
    }
    private void playSequentialNextSong() {
        if (currentPosition < songPaths.size() - 1) {
            currentPosition++;
        } else {
            // This is the last song, stop playback
            stopPlayback();
            return;
        }
        String nextSongPath = songPaths.get(currentPosition);
        playSong(nextSongPath);
        isPlaying = true;
        updatePlayButton();
    }

    private void playPreviousSong() {
        if (currentPosition > 0) {
            currentPosition--;
        } else {
            // This is the first song, stop playback
            stopPlayback();
            return;
        }
        String prevSongPath = songPaths.get(currentPosition);
        playSong(prevSongPath);
        isPlaying = true;
        updatePlayButton();
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.reset();
            isPlaying = false;
            updatePlayButton();
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
            updateSeekBar(); // To refresh the progress
        }
    }

    //==============================
    // Shuffle Methods
    //==============================
    private void toggleShuffle() {
        isShuffleActive = !isShuffleActive;

        if (!isShuffleActive) {
            currentPosition = lastPlayedPosition;
        }

        updateShuffleButtonColor();
    }

    private void playRandomSong() {
        if (songPaths != null && songPaths.size() > 1) {
            ArrayList<String> availableSongs = new ArrayList<>(songPaths);
            availableSongs.remove(songPath);

            int randomIndex = (int) (Math.random() * availableSongs.size());

            lastPlayedPosition = currentPosition;
            playSong(availableSongs.get(randomIndex));
        }
    }

    //==============================
    // Favorite Methods
    //==============================

    private void onHeartButtonClick() {
        isFavorite = !isFavorite;
        updateHeartButtonColor();

        String message = isFavorite ? "Added to favorites" : "Removed from favorites";
        showToast(message);
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void updateHeartButtonColor() {
        int heartButtonColor = isFavorite ? R.color.red : R.color.white;
        heartButton.setColorFilter(getResources().getColor(heartButtonColor));
    }


    //==============================
    // UI Updates
    //==============================
    private void updatePlayButton() {
        Button playButton = findViewById(R.id.playButton);
        playButton.setBackgroundResource(isPlaying ? R.drawable.ic_pause_24 : R.drawable.round_play_circle_24);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            isPlaying = true;
        }
    }

    private void updateSeekBar() {
        if (mediaPlayer != null) {
            int duration = mediaPlayer.getDuration();

            if (duration <= 0) {
                duration = getDefaultDuration();
            }

            seekBar.setMax(duration);
            txtStop.setText(formatDuration(duration));

            runnable = new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        int currentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(currentPosition);
                        txtStart.setText(formatDuration(currentPosition));
                        handler.postDelayed(this, 1000);
                    }
                }
            };

            handler.postDelayed(runnable, 1000);
        }
    }

    //==============================
    // Utility Methods
    //==============================
    private String formatDuration(int duration) {
        if (duration < 0) {
            duration = 0;
        }
        int seconds = (duration / 1000) % 60;
        int minutes = (duration / 1000) / 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private int getDefaultDuration() {
        return 214000;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            handler.removeCallbacks(runnable);
 }
}
}