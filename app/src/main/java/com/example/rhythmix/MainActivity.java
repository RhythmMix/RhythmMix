package com.example.rhythmix;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.inputmethod.EditorInfo;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.rhythmix.Adapter.DataListAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class MainActivity extends AppCompatActivity {
//    private static final String TAG = "MainActivity";
//    private static final String BASE_URL = "https://deezerdevs-deezer.p.rapidapi.com/";
//    private static Retrofit retrofit;
//    DataListAdapter adapter;
//    private EditText searchEditText;
//    private AutoCompleteTextView autoCompleteTextView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        searchEditText = findViewById(R.id.editTextSearch);
//        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
//        setupAutoComplete();
//
//        retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        searchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
//
//        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
//                if(actionId == EditorInfo.IME_ACTION_DONE){
//                    performSearch();
//                }
//                return false;
//            }
//        });
//    }
//
//    private void setupAutoComplete() {
//        // Create ArrayAdapter
//        ArrayAdapter<String> autoCompleteAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
//        autoCompleteTextView.setAdapter(autoCompleteAdapter);
//
//        // Set a threshold for the number of characters before suggestions are shown
//        autoCompleteTextView.setThreshold(1);
//
//        // Set listener for item selection
//        autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
//            String selectedSuggestion = (String) parent.getItemAtPosition(position);
//            autoCompleteTextView.setText(selectedSuggestion);
//            performSearch(); // Trigger search on suggestion selection
//        });
//    }
//
//        private void performSearch() {
//        String searchQuery = searchEditText.getText().toString();
//
//        if (!TextUtils.isEmpty(searchQuery)) {
//            MusicApiInterface musicApiInterface = retrofit.create(MusicApiInterface.class);
//            Call<Data> retrofitData = musicApiInterface.getData(searchQuery);
//            Log.d(TAG, "Request URL: " + retrofitData.request().url());
//            List<String> suggestions = new ArrayList<>(); // Fetch suggestions from the API or another source
//            ArrayAdapter<String> autoCompleteAdapter = (ArrayAdapter<String>) autoCompleteTextView.getAdapter();
//            autoCompleteAdapter.clear();
//            autoCompleteAdapter.addAll(suggestions);
//            autoCompleteAdapter.notifyDataSetChanged();
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
//                                Log.d(TAG, "OnSuccess:" + response.body());
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
//
//
//}
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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
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
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> autoCompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            performSearch();
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Not used in this example
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                fetchSuggestions(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used in this example
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
                    if (musicList !=null) {
                        for (Music music : musicList) {
                            String musicDetails = music.getTitle() + "\n" + music.getArtist().getName();
                            suggestions.add(musicDetails);
                        }
                    }else {
                        Log.e(TAG, "musicList is null");

                    }

                    if (autoCompleteAdapter == null) {
                        autoCompleteAdapter = new ArrayAdapter<>(MainActivity.this, R.layout.dropdown_item, R.id.dropdown_text, suggestions);
                        autoCompleteTextView.setAdapter(autoCompleteAdapter);
                    } else {
                        // Clear and update the adapter with new suggestions
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

        } else {
            Toast.makeText(MainActivity.this, "Please enter a search query", Toast.LENGTH_SHORT).show();
        }
    }

    private void onSuggestionSelected(String selectedSuggestion) {

        Intent intent = new Intent(this, DetailedActivity.class);
        intent.putExtra("selectedSuggestion", selectedSuggestion);
        startActivity(intent);
    }




}
