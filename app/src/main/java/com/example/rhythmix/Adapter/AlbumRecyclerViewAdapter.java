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

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumListViewHolder> {
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
            if (album != null && album.getCover() != null && album.getTitle() != null) {
                String imageUrl = album.getCover();
                Log.d(TAG, "Album Image in HRV" + imageUrl);

                ImageView albumCover = holder.itemView.findViewById(R.id.HRImageViewRecyclerView);
//                TextView albumTitle = holder.itemView.findViewById(R.id.HRAlbumTitle);

                Picasso.get().load(imageUrl).into(albumCover);
                String albumTitle1 = album.getTitle();
//                albumTitle.setText(albumTitle1);

//                holder.albumTitle.setText(albumTitle1);
            }

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
//            albumTitle = itemView.findViewById(R.id.HRAlbumTitle);
        }

        public void bind(Album album) {
            String imageUrl = album.getCover();
            Picasso.get().load(imageUrl).into(albumCover);

//            String albumTitle1 = album.getTitle();
//            albumTitle.setText(albumTitle1);
        }

    }

    public void addAlbum(Album album) {
        albumList.add(album);
        notifyDataSetChanged();
    }
}
