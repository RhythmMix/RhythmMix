package com.example.rhythmix.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.R;

import java.util.ArrayList;

public class LibrarySongsAdapter extends RecyclerView.Adapter<LibrarySongsAdapter.ViewHolder>{
    final static String TAG="libraryAdapter";
    private ArrayList<String> originalSongList;
    private ArrayList<String> songList;
    public ArrayList<String> songPaths;
    private ArrayList<String> originalArtistList;
    private ArrayList<String> artistList;
    private LayoutInflater inflater;
    private AdapterView.OnItemClickListener onItemClickListener;
    private ArrayList<String> originalSongPaths;


    public LibrarySongsAdapter(Context context, ArrayList<String> songList, ArrayList<String> songPaths,ArrayList<String> artistList) {
        this.originalSongList = new ArrayList<>(songList);
        this.songList = songList;
        this.songPaths = songPaths;
        this.originalArtistList = new ArrayList<>(artistList);
        this.artistList = artistList;
        this.inflater = LayoutInflater.from(context);
        this.originalSongPaths = new ArrayList<>(songPaths);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.song_item_library, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textSong.setSelected(true);
        holder.textSong.setHorizontallyScrolling(true);
        holder.textSong.setSingleLine(true);
        holder.textSong.setText(songList.get(position));
        holder.number.setText(String.valueOf(position + 1));
        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(v -> {
            int clickedPosition = (int) v.getTag();
            String selectedSongPath = songPaths.get(clickedPosition);

            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(null, holder.itemView, clickedPosition, clickedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void filter(String query) {
        songList.clear();
        artistList.clear();
        songPaths.clear();
        if (query.isEmpty()) {
            songList.addAll(originalSongList);
            artistList.addAll(originalArtistList);
            songPaths.addAll(originalSongPaths);
        } else {
            query = query.toLowerCase();
            for (int i = 0; i < originalSongList.size(); i++) {
                String song = originalSongList.get(i);
                String artist = originalArtistList.get(i);
                if (song.toLowerCase().contains(query) || artist.toLowerCase().contains(query)) {
                    songList.add(song);
                    artistList.add(artist);
                    songPaths.add(originalSongPaths.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textSong;
        TextView number;
        public ViewHolder(View itemView) {
            super(itemView);
            textSong = itemView.findViewById(R.id.songName);
            number = itemView.findViewById(R.id.number);
        }
    }
}

