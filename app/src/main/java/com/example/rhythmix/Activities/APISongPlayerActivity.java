package com.example.rhythmix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.amplifyframework.datastore.generated.model.Favorite;
import com.example.rhythmix.Adapter.FavoritesAdapter;
import com.example.rhythmix.R;
import com.example.rhythmix.models.FavoritesHandler;
import com.example.rhythmix.models.Track;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class APISongPlayerActivity extends AppCompatActivity {

    final static String TAG = "songPlayerActivity";
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private boolean isShuffleActive;
    private SeekBar seekBar;
    TextView songNameTextView;
    TextView artistNameTextView;
    private String songPath;
    private List<String> songPaths;
    private int currentPosition;
    private int lastPlayedPosition = 0;
    private TextView txtStart, txtStop;
    private Handler handler;
    private Runnable runnable;
    private ImageView shuffleButton;
    private ImageView heartButton;
    private boolean isFavorite = false;
    private List<Favorite> favorites =new ArrayList<>();
    private FavoritesAdapter favoritesAdapter = new FavoritesAdapter(favorites,this);
    private FavoritesHandler favoritesHandlerAddToFav = new FavoritesHandler(this, favoritesAdapter);
    private FavoritesHandler favoritesHandlerRemoveFromFav = new FavoritesHandler(favorites,this, favoritesAdapter);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apisong_player);

        isShuffleActive = false;

        backButton();
        initializeUI();
        mediaInitialization();
        playSelectedSong();

        Intent intent = getIntent();
        Track selectedTrack = (Track) intent.getSerializableExtra("SONG_TRACK_OBJECT");

        CompletableFuture<Boolean> future = favoritesHandlerAddToFav.checkIfInFavorites(selectedTrack);

        future.thenAccept(isFav -> {
            isFavorite = isFav;

            if (isFavorite) {
                heartButton.setColorFilter(getResources().getColor(R.color.red));
            } else {
                heartButton.setColorFilter(getResources().getColor(R.color.white));
            }
        });
    }


    //==============================
    // backButton
    //==============================
    private void backButton() {
        ImageButton backButton = findViewById(R.id.ApiBackButton);
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
        artistNameTextView = findViewById(R.id.ApiArtistName);
        txtStart = findViewById(R.id.ApiPlayerActivityTxtStart);
        txtStop = findViewById(R.id.ApiPlayerActivityTxtStop);
        Button playButton = findViewById(R.id.ApiPlayButton);
        Button nextButton = findViewById(R.id.nextButton);
        Button prevButton = findViewById(R.id.prevButton);
        seekBar = findViewById(R.id.ApiSeekBar);
        handler = new Handler();
        shuffleButton = findViewById(R.id.shuffleButton);
        heartButton = findViewById(R.id.heartButton);

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

        // Set up shuffle listener
        shuffleButton.setOnClickListener(view -> toggleShuffle());
        updateShuffleButtonColor();

        // Set up favorite listener
        heartButton.setOnClickListener(view -> onHeartButtonClick());
    }

    //============================
    // MediaPlayer initialization
    //============================
    public void mediaInitialization() {
        mediaPlayer = new MediaPlayer();
        Log.i(TAG, "song paths" + songPaths);
        Intent intent = getIntent();
        String songPath = intent.getStringExtra("SONG_PATH");

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
//        String selectedSongTitle = intent.getStringExtra("SONG_TITLE");
//        String selectedSongArtist = intent.getStringExtra("SONG_ARTIST");
//        String selectedSongID = intent.getStringExtra("SONG_ID");


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

            Log.i(TAG, "Current position in playSong: " + currentPosition);
            String songTitle = titlesList.get(currentPosition);
            String songArtist = artistsList.get(currentPosition);
            Log.i(TAG, "Current position in playSong after setters: " + currentPosition);
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
        } else {
            stopPlayback();
            return;
        }

        if (isShuffleActive) {
            playRandomSong();
        } else {
            String nextSongPath = songPaths.get(currentPosition);
            playSong(nextSongPath);
            isPlaying = true;
            updatePlayButton();
        }
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
        if (!isShuffleActive) {
            lastPlayedPosition = currentPosition;
        }
        isShuffleActive = !isShuffleActive;
        updateShuffleButtonColor();
    }


    private void playRandomSong() {
        if (songPaths != null && songPaths.size() > 1) {
            ArrayList<String> availableSongs = new ArrayList<>(songPaths);
            availableSongs.remove(songPath);

            int originalPosition = currentPosition;

            if (!isShuffleActive) {
                if (currentPosition < songPaths.size() - 1) {
                    currentPosition++;
                } else {
                    stopPlayback();
                    return;
                }
            } else {
                lastPlayedPosition = originalPosition;
            }

            int randomIndex = (int) (Math.random() * availableSongs.size());

            currentPosition = randomIndex;
            Log.i(TAG, "Current position in random: " + currentPosition);

            playSong(availableSongs.get(randomIndex));
        }
    }

    //==============================
    // Favorite Methods
    //==============================

    private void onHeartButtonClick() {
        Intent intent = getIntent();
        Track selectedTrack = (Track) intent.getSerializableExtra("SONG_TRACK_OBJECT");
        Log.d(TAG, "selected track in fav button: " + selectedTrack);

        if (selectedTrack != null) {
            isFavorite = !isFavorite;
            updateHeartButtonColor();



            if (isFavorite) {
                favoritesHandlerAddToFav.addToFavorites(selectedTrack);
            } else {
                favoritesHandlerRemoveFromFav.deleteFromFavorites(String.valueOf(selectedTrack.getId()));
            }
        } else {
            showToast("Failed to convert the selected song object");
        }
    }

    private void updateHeartButtonColor() {
        int heartButtonColor = isFavorite ? R.color.red : R.color.white;
        heartButton.setColorFilter(getResources().getColor(heartButtonColor));
    }




    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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