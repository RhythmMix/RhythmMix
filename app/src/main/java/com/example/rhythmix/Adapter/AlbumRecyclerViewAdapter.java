package com.example.rhythmix.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.Album;
import com.example.rhythmix.R;

import java.util.List;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumListViewHolder>{
    private List<Album> albumList;


    public AlbumRecyclerViewAdapter(List<Album> albumList) {

        this.albumList = albumList;

    }


    @NonNull
    @Override
    public AlbumListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_item, parent, false);

        return new AlbumListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumListViewHolder holder, int position) {
        if (albumList != null && position >= 0 && position < albumList.size()) {
            Album album = albumList.get(position);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class AlbumListViewHolder extends RecyclerView.ViewHolder {
        public AlbumListViewHolder(@NonNull View itemView) {
            super(itemView);
        }

    }
}
