package com.example.rhythmix.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class APISongPlayerActivity extends AppCompatActivity {

    final static String TAG="songPlayerActivity";
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private SeekBar seekBar;
    TextView songNameTextView;
    TextView artistNameTextView;
    private String songPath;
    private  List<String> songPaths;
    private int currentPosition;
    private TextView txtStart, txtStop;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apisong_player);

        backButton();
        initializeUI();
        mediaInitialization();
        playSelectedSong();
    }


    //==============================
    // backButton
    //==============================
    private void backButton(){
        ImageButton backButton=findViewById(R.id.ApiBackButton);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(APISongPlayerActivity.this, MainActivity.class);
            stopPlayback();
            startActivity(intent);
        });
    }

    //==============================
    // UI Initialization
    //==============================
    private void initializeUI() {
        // UI elements initialization
        songNameTextView = findViewById(R.id.ApiPlayerActivitySongName);
        artistNameTextView=findViewById(R.id.ApiArtistName);
        txtStart = findViewById(R.id.ApiPlayerActivityTxtStart);
        txtStop = findViewById(R.id.ApiPlayerActivityTxtStop);
        Button playButton = findViewById(R.id.ApiPlayButton);
        seekBar = findViewById(R.id.ApiSeekBar);
        handler = new Handler();

        Intent intent = getIntent();
        songPaths = intent.getStringArrayListExtra("SONG_PATH");
        Log.i(TAG,"track paths in previous" + songPaths);

        playButton.setOnClickListener(view -> {
            if (isPlaying) {
                pausePlayback();
            } else {
                resumePlayback();
            }
        });

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

    }

    //============================
    // MediaPlayer initialization
    //============================
    public void mediaInitialization() {
        mediaPlayer = new MediaPlayer();

        Intent intent = getIntent();
        String songPath = intent.getStringExtra("SONG_PATH");

        Log.i(TAG, "Song Path in media: " + songPath);

        try {
            mediaPlayer.setDataSource(APISongPlayerActivity.this, Uri.parse(songPath));
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    resumePlayback();
                }
            });
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //==============================
    // Playback Control
    //==============================
    private void playSelectedSong() {

        Intent intent = getIntent();
        String selectedSongPath = intent.getStringExtra("SONG_PATH");

        Log.i(TAG, "Selected Song Path: " + selectedSongPath);
        playSong(selectedSongPath);
    }


    private void playSong(String path) {
        // Implement the logic to stop the current playback and start the playback of the new song
        stopPlayback();
        songPath = path;
        mediaInitialization();

        Intent intent = getIntent();
        String songTitle = intent.getStringExtra("SONG_TITLE");
        songNameTextView.setText(songTitle);
        String songArtist = intent.getStringExtra("SONG_ARTIST");
        artistNameTextView.setText(songArtist);
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
    // UI Updates
    //==============================
    private void updatePlayButton() {
        Button playButton = findViewById(R.id.ApiPlayButton);
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
        return 30 * 1000;
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