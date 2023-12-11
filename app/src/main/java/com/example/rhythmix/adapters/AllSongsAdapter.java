package com.example.rhythmix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rhythmix.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class AllSongsAdapter  extends RecyclerView.Adapter<AllSongsAdapter.ViewHolder>{

    private ArrayList<String> songList;
    private ArrayList<String> songPaths;
    private ArrayList<String> artistNames;
    private LayoutInflater inflater;
    private AdapterView.OnItemClickListener onItemClickListener;


    public AllSongsAdapter(Context context, ArrayList<String> songList, ArrayList<String> songPaths,ArrayList<String> artistNames) {
        this.songList = songList;
        this.songPaths = songPaths;
        this.artistNames = artistNames;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.song_item_allsongs, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textSong.setSelected(true);
        holder.textSong.setHorizontallyScrolling(true);
        holder.textSong.setSingleLine(true);
        holder.textSong.setText(songList.get(position));
        holder.number.setText(String.valueOf(position + 1));
        holder.itemView.setTag(songPaths.get(position));

        Glide.with(holder.itemView.getContext())
                .load(R.drawable.ic_music_style)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(null, holder.itemView, position, position);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textSong;
        TextView number;
        RoundedImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            textSong = itemView.findViewById(R.id.songName);
            number = itemView.findViewById(R.id.number);
            image = itemView.findViewById(R.id.image);
        }
    }
}

