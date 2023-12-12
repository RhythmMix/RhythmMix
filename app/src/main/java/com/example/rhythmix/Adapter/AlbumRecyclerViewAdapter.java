package com.example.rhythmix.Adapter;


import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.Album;
import com.example.rhythmix.Music;
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumListViewHolder>{
    private List<Album> albumList;
    private Activity context;
    private static final String TAG = "Horizontal_HOLDER";



    public AlbumRecyclerViewAdapter(Activity context, List<Album> albumList) {
        this.context = context;
        this.albumList = albumList;

    }


    @NonNull
    @Override
    public AlbumListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_each_item, parent, false);
        return new AlbumListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumListViewHolder holder, int position) {
        if (albumList != null && position >= 0 && position < albumList.size()) {
            Album album = albumList.get(position);
            String imageUrl= album.getCover();
            Log.d(TAG,"Album Image in HRV" + imageUrl);





//                String imageUrl= music.getAlbum().getCover();
//                Log.d(TAG,"iMAGE" + imageUrl);
////            String imageUrl = "";
//
//                // Use Picasso to load the image into the ImageView
//                if(music.getAlbum().getCover()!=null){
//                    imageUrl= music.getAlbum().getCover();
//                }
//
//                Log.d(TAG, "IMAGE URL" + imageUrl);
//
//                ImageView musicImage = holder.itemView.findViewById(R.id.musicImage);
//                TextView musicTitle = holder.itemView.findViewById(R.id.musicTitle);
//                ImageButton toggleButton = holder.itemView.findViewById(R.id.toggleButton);
//
//                Picasso.get().load(imageUrl).into(musicImage);
//
//                MediaPlayer mediaPlayer = new MediaPlayer();
//                try {
//                    mediaPlayer.setDataSource(context, Uri.parse(music.getPreview()));
//                    mediaPlayer.prepareAsync(); // Use prepareAsync to avoid blocking UI thread
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//
//                // Display title & Artist name
//                String titleArtist = music.getTitle() + " \n \n " + music.getArtist().getName();
//                musicTitle.setText(titleArtist);
//
//
//                toggleButton.setOnClickListener(v -> {
//                    if (mediaPlayer.isPlaying()) {
//                        mediaPlayer.pause();
//                        toggleButton.setImageResource(android.R.drawable.ic_media_play);
//                    } else {
//                        mediaPlayer.start();
//                        toggleButton.setImageResource(android.R.drawable.ic_media_pause);
//                    }
//                });
//            }
//        }


            holder.bind(album);

        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class AlbumListViewHolder extends RecyclerView.ViewHolder {
        private ImageView albumCover;
        private TextView albumTitle;
        public AlbumListViewHolder(@NonNull View itemView) {
            super(itemView);
            albumCover = itemView.findViewById(R.id.HRImageViewRecyclerView);
            albumTitle = itemView.findViewById(R.id.HRAlbumTitle);
        }
        public void bind(Album album) {

            String imageUrl = album.getCover();
            Picasso.get().load(imageUrl).into(albumCover);

            String titleArtist = album.getTitle();
            albumTitle.setText(titleArtist);


        }

    }
}
