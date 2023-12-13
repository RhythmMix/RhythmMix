package com.example.rhythmix.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rhythmix.Album;
import com.example.rhythmix.AlbumTracksActivity;
import com.example.rhythmix.R;
import com.squareup.picasso.Picasso;
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

                ImageButton albumCoverButton = holder.itemView.findViewById(R.id.HRImageButtonRecyclerView);
                albumCoverButton.setOnClickListener(view -> {
                    Intent albumTracksActivity = new Intent(view.getContext(), AlbumTracksActivity.class);
                    albumTracksActivity.putExtra("Album Cover", album.getCover());
                    albumTracksActivity.putExtra("Album tracks", album.getTrackList());
                    albumTracksActivity.putExtra("Album title", album.getTitle());
                    albumTracksActivity.putExtra("Album ID", album.getId());
                    view.getContext().startActivity(albumTracksActivity);
                });

                Picasso.get().load(imageUrl).into(albumCoverButton);
                String albumTitle1 = album.getTitle();
            }

            holder.bind(album);
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public class AlbumListViewHolder extends RecyclerView.ViewHolder {
        private ImageButton albumCoverButton;
        private TextView albumTitle;

        public AlbumListViewHolder(@NonNull View itemView) {
            super(itemView);
            albumCoverButton = itemView.findViewById(R.id.HRImageButtonRecyclerView);
//            albumTitle = itemView.findViewById(R.id.albumTitle);
        }

        public void bind(Album album) {
            String imageUrl = album.getCover();
        }
    }

    public void addAlbum(Album album) {
        albumList.add(album);
        notifyDataSetChanged();
    }


}
