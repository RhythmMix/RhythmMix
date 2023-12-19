package com.example.rhythmix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Favorite;

import com.example.rhythmix.Adapter.FavoritesAdapter;
import com.example.rhythmix.MusicApiInterface;
import com.example.rhythmix.R;
import com.example.rhythmix.models.APIConfig;
import com.example.rhythmix.models.Data;
import com.example.rhythmix.models.FavoritesHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;


public class AddToFavoritesActivity extends AppCompatActivity {
    private List<Favorite> favorites = new ArrayList<>();
    private FavoritesAdapter favoritesAdapter;
    private MediaPlayer mediaPlayer;
    private static final String TAG = "AddToFavoritesActivity";
    private Context context;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_favorites);


        TextView noFavoritesMessage = findViewById(R.id.noFavoritesMessage);

        if (favorites.isEmpty()) {
            noFavoritesMessage.setVisibility(View.VISIBLE);
        } else {
            noFavoritesMessage.setVisibility(View.GONE);
        }


        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        favoritesRecyclerView.setLayoutManager(layoutManager);

        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(favorites, this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        FavoritesHandler favoritesHandler = new FavoritesHandler(favorites, this, favoritesAdapter);
        initialization();
        favoritesHandler.queryFavorites();

    }


    private void initialization() {

        String trackTitle = getIntent().getStringExtra("TRACK_TITLE");
        String trackArtist = getIntent().getStringExtra("TRACK_ARTIST");
        String trackMp3 = getIntent().getStringExtra("TRACK_MP3");
        String trackCover = getIntent().getStringExtra("TRACK_COVER");

    }

}