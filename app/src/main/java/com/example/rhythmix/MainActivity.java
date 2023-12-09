package com.example.rhythmix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhythmix.Adapter.DataListAdapter;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String BASE_URL = "https://deezerdevs-deezer.p.rapidapi.com/";
    private static Retrofit retrofit;
    DataListAdapter adapter;
    private EditText searchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.editTextSearch);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });

        List<Long> albumIds = Arrays.asList(302127L, 302128L, 302129L);
        displayMultipleAlbums(albumIds);
    }

    private void performSearch() {
        String searchQuery = searchEditText.getText().toString();

        if (!TextUtils.isEmpty(searchQuery)) {
            MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
            Call<Data> retrofitData = musicApiInterface.getData(searchQuery);
            Log.d(TAG, "Request URL: " + retrofitData.request().url());

            retrofitData.enqueue(new Callback<Data>() {
                @Override
                public void onResponse(Call<Data> call, Response<Data> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<Music> musicList = response.body().getData();

                        if (!musicList.isEmpty()) {
                            RecyclerView musicListRecyclerView = findViewById(R.id.recyclerView);

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                            musicListRecyclerView.setLayoutManager(layoutManager);

                            adapter = new DataListAdapter(MainActivity.this, musicList);
                            musicListRecyclerView.setAdapter(adapter);

                            Log.d(TAG, "OnSuccess:" + response.body());

                        } else {
                            Log.e(TAG, "Empty music list");
                        }
                    } else {
                        Log.e(TAG, "Unsuccessful response");
                    }
                }

                @Override
                public void onFailure(Call<Data> call, Throwable t) {
                    Log.e(TAG, "OnFailure: " + t.getMessage());
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Please enter a search query", Toast.LENGTH_SHORT).show();
        }
    }

        private void displayMultipleAlbums(List<Long> albumIds) {
            ListView listView = findViewById(R.id.listView);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
            listView.setAdapter(adapter);

            for (Long albumId : albumIds) {
                displayAlbumDetails(albumId, adapter);
            }


        }
    private List<Long> generateRandomAlbumIds(int numberOfAlbums) {
        List<Long> randomAlbumIds = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < numberOfAlbums; i++) {
            // Generate a random long value as an album ID
            long randomAlbumId = Math.abs(random.nextLong());
            randomAlbumIds.add(randomAlbumId);
        }

        return randomAlbumIds;
    }
        private void displayAlbumDetails(long albumId, ArrayAdapter<String> adapter) {
            MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
            Call<Album> retrofitData = musicApiInterface.getAlbum(albumId);
            Log.d(TAG, "Request URL: " + retrofitData.request().url());

            retrofitData.enqueue(new Callback<Album>() {
                @Override
                public void onResponse(Call<Album> call, Response<Album> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Album album = response.body();

                        if (album != null) {
                            // Display album details here
                            String imageUrl = album.getCover();
                            if (imageUrl != null) {
                                // Add album information to the adapter for the ListView
                                adapter.add("Album Title: " + album.getTitle() + "\n"
                                        + "Album Tracklist: " + album.getTrackList() + "\n\n");
                            } else {
                                Log.e(TAG, "Album cover URL is null");
                            }
                        } else {
                            Log.e(TAG, "Album or album.getAlbum() is null");
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





}
