package com.example.rhythmix.Activites;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUser;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.Favorite;
import com.amplifyframework.datastore.generated.model.User;
import com.example.rhythmix.adapter.AlbumRecyclerViewAdapter;
import com.example.rhythmix.adapter.ImageSliderAdapter;
import com.example.rhythmix.adapter.TrackRecyclerViewAdapter;
import com.example.rhythmix.models.Album;
import com.example.rhythmix.models.Music;
import com.example.rhythmix.MusicApiInterface;
import com.example.rhythmix.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://deezerdevs-deezer.p.rapidapi.com/";
    private static Retrofit retrofit;
    private RecyclerView horizontalView;
    private List<Album> albumList = new ArrayList<>();
    private List<Music> musicList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private AlbumRecyclerViewAdapter albumHorizontalRecyclerViewAdapter;
    private TrackRecyclerViewAdapter trackRecyclerViewAdapter;
    private RecyclerView albumRecyclerView;
    private RecyclerView trackRecyclerView;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            bottomNavigationView.getMenu().findItem(R.id.Home).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Library).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Search).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Profile).setChecked(false);

            if (item.getItemId() == R.id.Home) {
                return true;
            } else if (item.getItemId() == R.id.Search) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Library) {
                startActivity(new Intent(MainActivity.this, LibraryActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                return true;
            } else return false;
        });
        bottomNavigationView.getMenu().findItem(R.id.Home).setChecked(true);
        goToLogIn();
//        initializePopupMenu();

        //=======================================================================================================================================

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        init();
        displayMultipleAlbums(fetchingAlbumsId());
        displayMultipleTracks(fetchingTracksId());
    }

    public void goToLogIn() {
        Button logIn = findViewById(R.id.login);
        logIn.setOnClickListener(view -> {
            Intent goToAllSongs = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToAllSongs);
        });
    }


    private void displayMultipleAlbums(List<Long> albumIds) {
        albumRecyclerView = findViewById(R.id.horizontalRecyclerView);
        List<Album> albumList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
        albumRecyclerView.setLayoutManager(layoutManager);
        AlbumRecyclerViewAdapter recyclerViewAdapter = new AlbumRecyclerViewAdapter(this, albumList);
        albumRecyclerView.setAdapter(recyclerViewAdapter);

        for (Long albumId : albumIds) {
            displayAlbumDetails(albumId, recyclerViewAdapter);
        }
    }

    private void displayAlbumDetails(long albumId, AlbumRecyclerViewAdapter recyclerViewAdapter) {
        MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
        Call<Album> retrofitData = musicApiInterface.getAlbum(albumId);
        Log.d(TAG, "retrofitData of albums: " + retrofitData);
        Log.d(TAG, "Request URL of albums: " + retrofitData.request().url());

        retrofitData.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Album album = response.body();
                    Log.d(TAG, "Album details: " + album);
                    Log.d(TAG, "imageUrl=" + album.getCover());
                    Log.d(TAG, "Album Id=" + album.getId());

                    if (album != null && album.getCover() != null && album.getTitle() != null) {
                        recyclerViewAdapter.addAlbum(album);
                        Log.d(TAG, "album after is being added" + album);
                    } else {
                        Log.e(TAG, "Album is null");
                    }
                } else {
                    Log.e(TAG, "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                Log.e(TAG, "OnFailure: " + t.getMessage());
            }
        });
    }


    private List<Long> fetchingAlbumsId() {
        List<Long> ids = new ArrayList<>();
        Collections.addAll(ids,
                595243L, 7040437L, 11674708L, 278981762L, 464273655L, 418542097L, 104188L, 265655342L, 510479581L, 100856872L, 15478674L, 315512547L, 12231484L, 117053822L, 306544897L, 129186032L, 8113734L, 280436792L, 6475501L
        );
        Collections.shuffle(ids);
        List<Long> shuffled = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            shuffled.add(ids.get(i));
        }

        return shuffled;
    }

    //////////////////////////////////////////////////////////////////////

    private List<Long> fetchingTracksId() {

        List<Long> ids = new ArrayList<>();
        Collections.addAll(ids,
                1842063587L, 2372912335L, 1586852522L, 2582294482L, 435491442L, 11747937L, 1409072752L, 117797212L,
                447098092L, 1584508822L, 2129775057L, 1058814092L, 727429062L, 1582561882L);
        Collections.shuffle(ids);
        List<Long> shuffled = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            shuffled.add(ids.get(i));
        }

        return shuffled;
    }

    private void displayMultipleTracks(List<Long> tracksId) {
        trackRecyclerView = findViewById(R.id.verticalRecyclerView);
        List<Music> musicList = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        trackRecyclerView.setLayoutManager(layoutManager);
        TrackRecyclerViewAdapter trackRecyclerViewAdapter = new TrackRecyclerViewAdapter(this, musicList);
        trackRecyclerView.setAdapter(trackRecyclerViewAdapter);

        for (Long trackId : tracksId) {
            displayTrackDetails(trackId, trackRecyclerViewAdapter);
        }

    }

    private void displayTrackDetails(long trackId, TrackRecyclerViewAdapter trackRecyclerViewAdapter) {
        MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
        Call<Music> retrofitData = musicApiInterface.getTracks(trackId);
        Log.d(TAG, "retrofitData of tracks: " + retrofitData);
        Log.d(TAG, "Request URL of tracks: " + retrofitData.request().url());

        retrofitData.enqueue(new Callback<Music>() {
            @Override
            public void onResponse(Call<Music> call, Response<Music> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Music music = response.body();
                    List<Music> musicList = new ArrayList<>();
                    musicList.add(music);
                    Log.d(TAG, "Music list" + musicList);

                    if (music != null) {
                        if (music.getArtist() != null && music.getTitle() != null && music.getPreview() != null) {
                            trackRecyclerViewAdapter.addTrack(music);

//                            trackRecyclerViewAdapter.setOn

                        } else {
                            Log.e(TAG, "Album is null");
                        }
                    } else {
                        Log.e(TAG, "Unsuccessful response");
                    }
                }
            }

            @Override
            public void onFailure(Call<Music> call, Throwable t) {
                Log.e(TAG, "OnFailure: " + t.getMessage());
            }
        });
    }

    //==============================
    // Slider
    //==============================
    public void init() {

        List<Integer> imageResources = Arrays.asList(R.drawable.imageslider1, R.drawable.imageslider2, R.drawable.imageslider3, R.drawable.imageslider4);
        ViewPager2 imageSlider = findViewById(R.id.imageSlider);
        ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(imageResources);
        imageSlider.setAdapter(imageSliderAdapter);

        // Auto-scroll the ViewPager2
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                int currentPage = imageSlider.getCurrentItem();
                int totalPages = imageSliderAdapter.getItemCount();
                if (currentPage == totalPages - 1) {
                    currentPage = 0;
                } else {
                    currentPage++;
                }
                imageSlider.setCurrentItem(currentPage, true);
            }
        };

        // Delay in milliseconds between slides
        int delay = 7000;

        // Start the auto-scroll loop
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(update);
            }
        }, 0, delay);
    }




}
