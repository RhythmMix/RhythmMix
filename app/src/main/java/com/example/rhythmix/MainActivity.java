package com.example.rhythmix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhythmix.Adapter.DataListAdapter;

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
        searchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event){
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    performSearch();
                }
                return false;
            }
        });
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


}
