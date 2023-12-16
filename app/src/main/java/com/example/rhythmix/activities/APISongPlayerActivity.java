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
import android.widget.Toast;

import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class APISongPlayerActivity extends AppCompatActivity {

    final static String TAG="songPlayerActivity";
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private boolean isShuffleActive;
    private SeekBar seekBar;
    TextView songNameTextView;
    TextView artistNameTextView;
    private String songPath;
    private  List<String> songPaths;
    private int currentPosition;
    private int lastPlayedPosition = 0;
    private TextView txtStart, txtStop;
    private Handler handler;
    private Runnable runnable;
    private ImageView shuffleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apisong_player);

        isShuffleActive = false;

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
        Button nextButton = findViewById(R.id.nextButton);
        Button prevButton = findViewById(R.id.prevButton);
        seekBar = findViewById(R.id.ApiSeekBar);
        handler = new Handler();
        shuffleButton = findViewById(R.id.shuffleButton);

        Intent intent = getIntent();
        songPaths = intent.getStringArrayListExtra("PREVIEW_URLS");
  ;

        playButton.setOnClickListener(view -> {
            if (isPlaying) {
                pausePlayback();
            } else {
                resumePlayback();
            }
        });

        currentPosition = getIntent().getIntExtra("CURRENT_POSITION", 0);
        nextButton.setOnClickListener(view -> playSequentialNextSong());
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

        // Set up shuffle listener
        shuffleButton.setOnClickListener(view -> toggleShuffle());
        updateShuffleButtonColor();
    }

    //============================
    // MediaPlayer initialization
    //============================
    public void mediaInitialization() {
        mediaPlayer = new MediaPlayer();

        Intent intent = getIntent();
        String songPath = intent.getStringExtra("SONG_PATH");

        Log.i(TAG, "Song Path in media: " + songPath);

        try {mediaPlayer.setOnCompletionListener(mp -> {
            playNextSong();
        });
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
                isPlaying = true;
                updateSeekBar();
                updatePlayButton();
            });
            mediaPlayer.setDataSource(APISongPlayerActivity.this, Uri.parse(songPath));
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

        if (songPaths != null && !songPaths.isEmpty()) {
            currentPosition = songPaths.indexOf(selectedSongPath);
            playSong(selectedSongPath);
        }
    }


    private void playSong(String path) {
        // Implement the logic to stop the current playback and start the playback of the new song
        stopPlayback();
        songPath = path;

        Intent intent = getIntent();
        ArrayList<String> titlesList = intent.getStringArrayListExtra("TITLES_LIST");
        ArrayList<String> artistsList = intent.getStringArrayListExtra("ARTISTS_LIST");

        if (currentPosition >= 0 && currentPosition < titlesList.size()) {
            String songTitle = titlesList.get(currentPosition);
            String songArtist = artistsList.get(currentPosition);

            songNameTextView.setText(songTitle);
            artistNameTextView.setText(songArtist);
        }

        try {
            mediaPlayer.setDataSource(APISongPlayerActivity.this, Uri.parse(songPath));
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
            Log.i(TAG,"cureent inside next"+ currentPosition);
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
        Log.d(TAG, "toggleShuffle: Shuffle button clicked");
        isShuffleActive = !isShuffleActive;

        if (!isShuffleActive) {
            currentPosition = lastPlayedPosition;
        }

        updateShuffleButtonColor();
    }

    private void playRandomSong() {
        Log.d(TAG, "playRandomSong: Playing random song");
        if (songPaths != null && songPaths.size() > 1) {
            ArrayList<String> availableSongs = new ArrayList<>(songPaths);
            availableSongs.remove(songPath);

            int randomIndex = (int) (Math.random() * availableSongs.size());

            lastPlayedPosition = currentPosition;
            playSong(availableSongs.get(randomIndex));
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


    private void updateShuffleButtonColor() {
        int shuffleButtonColor = isShuffleActive ? R.color.shuffleColor : R.color.white;
        shuffleButton.setColorFilter(getResources().getColor(shuffleButtonColor));
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