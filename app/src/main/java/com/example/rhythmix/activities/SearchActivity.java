package com.example.rhythmix.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.rhythmix.Adapter.DataListAdapter;
import com.example.rhythmix.MusicApiInterface;
import com.example.rhythmix.R;
import com.example.rhythmix.models.Data;
import com.example.rhythmix.models.Music;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private static final String TAG = "SearchBarResultsActivity";
    private static final String BASE_URL = "https://deezerdevs-deezer.p.rapidapi.com/";
    private static Retrofit retrofit;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> autoCompleteAdapter;
    private DataListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            bottomNavigationView.getMenu().findItem(R.id.Home).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Library).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Search).setChecked(false);
            bottomNavigationView.getMenu().findItem(R.id.Profile).setChecked(false);

            if (item.getItemId() == R.id.Home) {
                startActivity(new Intent(SearchActivity.this, MainActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Search) {
                return true;
            } else if (item.getItemId() == R.id.Library) {
                startActivity(new Intent(SearchActivity.this, LibraryActivity.class));
                return true;
            } else if (item.getItemId() == R.id.Profile) {
                startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
                return true;
            } else return false;
        });
        bottomNavigationView.getMenu().findItem(R.id.Search).setChecked(true);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        setupAutoComplete();


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private void setupAutoComplete() {
        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setAdapter(autoCompleteAdapter);
        autoCompleteTextView.setThreshold(1);

        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSuggestion = (String) parent.getItemAtPosition(position);
            autoCompleteTextView.setText(selectedSuggestion);
            autoCompleteTextView.setSelection(selectedSuggestion.length());
            performSearch();
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) { // Not Used
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                fetchSuggestions(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) { // Not Used
            }
        });

        autoCompleteTextView.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void fetchSuggestions(String query) {
        MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
        Call<Data> retrofitData = musicApiInterface.getData(query);

        retrofitData.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Music> musicList = response.body().getData();

                    List<String> suggestions = new ArrayList<>();
                    if (musicList != null) {
                        for (Music music : musicList) {
                            String musicDetails = music.getTitle() + "\n" + music.getArtist().getName();
                            suggestions.add(musicDetails);
                        }
                    } else {
                        Log.e(TAG, "musicList is null");

                    }

                    if (autoCompleteAdapter == null) {
                        autoCompleteAdapter = new ArrayAdapter<>(SearchActivity.this, R.layout.dropdown_item, R.id.dropdown_text, suggestions);
                        autoCompleteTextView.setAdapter(autoCompleteAdapter);
                    } else {
                        autoCompleteAdapter.clear();
                        autoCompleteAdapter.addAll(suggestions);
                        autoCompleteAdapter.notifyDataSetChanged();
                    }


                    Log.d(TAG, "OnSuccess:" + response.body());
                } else {
                    Log.e(TAG, "Unsuccessful response");
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.e(TAG, "OnFailure: " + t.getMessage());
            }
        });
    }

    private void performSearch() {
        String searchQuery = autoCompleteTextView.getText().toString();

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

                            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchActivity.this);
                            musicListRecyclerView.setLayoutManager(layoutManager);

                            adapter = new DataListAdapter(SearchActivity.this, musicList, musicListRecyclerView);
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
            Toast.makeText(SearchActivity.this, "Please enter a search query", Toast.LENGTH_SHORT).show();
        }

    }

    private void onSuggestionSelected(String selectedSuggestion) {

        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("selectedSuggestion", selectedSuggestion);
        startActivity(intent);
    }
}