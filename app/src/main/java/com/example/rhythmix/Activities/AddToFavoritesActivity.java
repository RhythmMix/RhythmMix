package com.example.rhythmix.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Favorite;

import com.example.rhythmix.Adapter.FavoritesAdapter;
import com.example.rhythmix.R;
import com.example.rhythmix.models.FavoritesHandler;

import java.util.ArrayList;
import java.util.List;


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

        RecyclerView favoritesRecyclerView = findViewById(R.id.favoritesRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        favoritesRecyclerView.setLayoutManager(layoutManager);

        FavoritesAdapter favoritesAdapter = new FavoritesAdapter(favorites, this,favoritesRecyclerView);
        favoritesRecyclerView.setAdapter(favoritesAdapter);

        FavoritesHandler favoritesHandler = new FavoritesHandler(favorites, this, favoritesAdapter);
        initialization();
        favoritesHandler.queryFavorites();

        ImageView back = findViewById(R.id.backButtonFavorite);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddToFavoritesActivity.this, PlaylistsActivity.class);
                startActivity(intent);
            }
        });

//        Log.i(TAG,"Favorites" + favorites);
//        TextView emptyFavoritePage =findViewById(R.id.emptyFavoritePage);
//            if (favorites.isEmpty()) {
//                emptyFavoritePage.setVisibility(View.VISIBLE);
//            } else {
//                emptyFavoritePage.setVisibility(View.GONE);
//            }
    }


    private void initialization() {

        String trackTitle = getIntent().getStringExtra("TRACK_TITLE");
        String trackArtist = getIntent().getStringExtra("TRACK_ARTIST");
        String trackMp3 = getIntent().getStringExtra("TRACK_MP3");
        String trackCover = getIntent().getStringExtra("TRACK_COVER");

    }

}