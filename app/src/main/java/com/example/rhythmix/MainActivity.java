package com.example.rhythmix;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rhythmix.Adapter.DataListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        ListView searchEditText = findViewById(R.id.listView);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Button searchButton = findViewById(R.id.searchButton);
//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                performSearch();
//            }
//        });
        fetchData();
//        fetchingAlbumsId();

//        List<Long> albumIds = Arrays.asList(302127L, 302128L, 302129L, 302129L, 302129L, 302129L, 302129L);
//        displayMultipleAlbums(fetchingAlbumsId());
//        displayMultipleTracks(fetchingTracksId());

    }

    private void fetchData() {
        MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
        Call<Data> retrofitData = musicApiInterface.getAllData("");
        Log.d(TAG, "Request URL: " + retrofitData.request().url());
        retrofitData.enqueue(new Callback<Data>() {

            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {

                    List<Music> musicList = response.body().data;
                    Log.d(TAG, "MUSIC:"+ musicList);
                    if (musicList != null && !musicList.isEmpty()) {
                        for (Music music : musicList) {
                            if (music.getAlbum() != null) {
                                getAlbums(music.getAlbum());
                            }
                            if (music.getArtist() != null) {
                                getArtists(music.getArtist());
                            }

                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.e(TAG, "OnFailure: " + t.getMessage());
            }
        });
    }


    private void getAlbums(Album album) {
        Log.d(TAG, "Albums retrieved" + album.getId());
    }

    private void getArtists(Artist artist) {
        Log.d(TAG, "Artists retrieved" + artist.getName());

    }


    /////////////////////////////////////////////////////////////////////////


//    private void performSearch() {
//        String searchQuery = searchEditText.getText().toString();
//
//        if (!TextUtils.isEmpty(searchQuery)) {
//            MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
//            Call<Data> retrofitData = musicApiInterface.getData(searchQuery);
//            Log.d(TAG, "Request URL: " + retrofitData.request().url());
//
//            retrofitData.enqueue(new Callback<Data>() {
//                @Override
//                public void onResponse(Call<Data> call, Response<Data> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        List<Music> musicList = response.body().getData();
//
//                        if (!musicList.isEmpty()) {
//                            RecyclerView musicListRecyclerView = findViewById(R.id.recyclerView);
//
//                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//                            musicListRecyclerView.setLayoutManager(layoutManager);
//
//                            adapter = new DataListAdapter(MainActivity.this, musicList);
//                            musicListRecyclerView.setAdapter(adapter);
//
//                            Log.d(TAG, "OnSuccess:" + response.body());
//
//                        } else {
//                            Log.e(TAG, "Empty music list");
//                        }
//                    } else {
//                        Log.e(TAG, "Unsuccessful response");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Data> call, Throwable t) {
//                    Log.e(TAG, "OnFailure: " + t.getMessage());
//                }
//            });
//        } else {
//            Toast.makeText(MainActivity.this, "Please enter a search query", Toast.LENGTH_SHORT).show();
//        }
//    }

    private void displayMultipleAlbums(List<Long> albumIds) {
        ListView listView = findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        for (Long albumId : albumIds) {
            displayAlbumDetails(albumId, adapter);
        }


    }

    private void displayAlbumDetails(long albumId, ArrayAdapter<String> adapter) {
        MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
        Call<Album> retrofitData = musicApiInterface.getAlbum(albumId);
        Log.d(TAG, "retrofitData: " + retrofitData);
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


    private List<Long> fetchingAlbumsId() {
        List<Long> ids = new ArrayList<>();
        // Add the provided IDs to the list
        Collections.addAll(ids,
                7090505L, 916424L, 103248L, 6461440L, 595243L, 1109731L, 119606L, 916426L, 103248L,
                6461432L, 595243L, 916427L, 103248L, 1109737L, 119606L, 1109739L, 119606L, 854914322L,
                127270232L, 1109730L, 119606L, 1865118037L, 346060987L, 72160314L, 7090505L, 2459657805L,
                489410085L, 548348732L, 72000342L, 916414L, 103248L, 436510892L, 52755402L, 1176211L,
                125748L, 2454196745L, 487933275L, 1109729L, 119606L, 3729755L, 350198L, 2372912335L,
                465874705L, 1109727L, 119606L, 1176202L, 125748L, 125748L, 501928001L
        );
        // Shuffle the list
        Collections.shuffle(ids);
        // Print 5 shuffled IDs
        System.out.println("Shuffled IDs:");
        List<Long> shuffeled = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            System.out.println(ids.get(i));
            shuffeled.add(ids.get(i));
        }

        return shuffeled;
    }

    //////////////////////////////////////////////////////////////////////

    private List<Long> fetchingTracksId() {


        List<Long> ids = new ArrayList<>();
        // Add the provided IDs to the list
        Collections.addAll(ids,
                7090505L, 916424L, 103248L, 6461432L, 916427L, 1109737L, 1109739L, 854914322L);
        // Shuffle the list
        Collections.shuffle(ids);
        // Print 5 shuffled IDs
        System.out.println("Shuffled IDs:");
        List<Long> shuffeled = new ArrayList<>();
        for (int i = 0; i < ids.size(); i++) {
            System.out.println(ids.get(i));
            shuffeled.add(ids.get(i));
        }

        return shuffeled;
    }

    private void displayMultipleTracks(List<Long> tracksId) {
        ListView listView = findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);


        for (Long trackId : tracksId) {

            displayTrackDetails(trackId, adapter);
        }
    }

    private void displayTrackDetails(long trackId, ArrayAdapter<String> adapter) {
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
                        if (music.getArtist() != null) {
                            adapter.add("song Title: " + music.getTitle() + "\n"
                                    + "song preview: " + music.getPreview() + "\n\n"
                                    + "artist name:" + music.getArtist().getName());
                        } else {
                            adapter.add("song Title: " + music.getTitle() + "\n"
                                    + "song preview: " + music.getPreview() + "\n\n");
                        }
                    }


//                        RecyclerView musicListRecyclerView = findViewById(R.id.recyclerView);
//
//                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
//                        musicListRecyclerView.setLayoutManager(layoutManager);
//
//
//                        adapter = new DataListAdapter(MainActivity.this, musicList);
//                        musicListRecyclerView.setAdapter(adapter);
//

//                        Log.d(TAG, "OnSuccess:" + response.body());

//                    } else {
//                        Log.e(TAG, "Empty music list");
//                    }
                } else {
                    Log.e(TAG, "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<Music> call, Throwable t) {
                Log.e(TAG, "OnFailure: " + t.getMessage());
            }
        });
    }


}

