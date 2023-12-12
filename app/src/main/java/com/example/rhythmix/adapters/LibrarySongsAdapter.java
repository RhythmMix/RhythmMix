package com.example.rhythmix.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.R;

import java.util.ArrayList;
import java.util.Arrays;

public class LibrarySongsAdapter extends RecyclerView.Adapter<LibrarySongsAdapter.ViewHolder>{
    final static String TAG="libraryAdapter";
    private ArrayList<String> originalSongList;
    private ArrayList<String> songList;
    private ArrayList<String> originalSongPaths;
    public ArrayList<String> songPaths;
    private ArrayList<String> originalArtistList;
    private ArrayList<String> artistList;
    private LayoutInflater inflater;
    private boolean isPlaying = false;
    public boolean[] isPlayingArray;
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemClickListener playPauseButtonClickListener;


    public LibrarySongsAdapter(Context context, ArrayList<String> songList, ArrayList<String> songPaths, ArrayList<String> artistList) {
        this.originalSongList = new ArrayList<>(songList);
        this.songList = songList;
        this.originalSongPaths = new ArrayList<>(songPaths);
        this.songPaths = songPaths;
        this.originalArtistList = new ArrayList<>(artistList);
        this.artistList = artistList;
        this.inflater = LayoutInflater.from(context);
        this.isPlayingArray = new boolean[songList.size()];
        Arrays.fill(isPlayingArray, false);
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
        holder.playPauseButton.setTag(position);

        holder.itemView.setOnClickListener(v -> {
            int clickedPosition = (int) v.getTag();
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(null, holder.itemView, clickedPosition, clickedPosition);
            }
        });

        holder.playPauseButton.setOnClickListener(v -> {
            int clickedPosition = (int) v.getTag();
            togglePlayPause(clickedPosition);
            if (playPauseButtonClickListener != null) {
                playPauseButtonClickListener.onItemClick(null,holder.itemView, clickedPosition, clickedPosition);
            }
        });

        int playPauseIcon = isPlayingArray[position] ? R.drawable.ic_pause_24 : R.drawable.round_play_circle_24;
        holder.playPauseButton.setImageResource(playPauseIcon);
    }


    private void togglePlayPause(int position) {
        isPlaying = !isPlaying;
        Log.d(TAG, "Toggled play/pause for position " + position + ". Status: " + isPlayingArray[position]);
        notifyItemChanged(position);
    }

    public void setPlaying(boolean isPlaying, int position) {
        this.isPlayingArray[position] = isPlaying;
        notifyItemChanged(position);
    }



    @Override
    public int getItemCount() {
        return songList.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
    public void setOnPlayPauseButtonClickListener(AdapterView.OnItemClickListener listener) {
        this.playPauseButtonClickListener = listener;
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
        ImageButton playPauseButton;
        public ViewHolder(View itemView) {
            super(itemView);
            textSong = itemView.findViewById(R.id.songName);
            number = itemView.findViewById(R.id.number);
            playPauseButton = itemView.findViewById(R.id.preview_button);
        }
    }
}

