package com.example.rhythmix.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rhythmix.R;

import java.util.List;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {
    private List<Integer> imageResources;

    public ImageSliderAdapter(List<Integer> imageResources) {
        this.imageResources = imageResources;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_slider, parent, false);
        return new ImageSliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        int imageResource = imageResources.get(position);
        holder.imageView.setImageResource(imageResource);
    }

    @Override
    public int getItemCount() {
        return imageResources.size();
    }

    public static class ImageSliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageSliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.sliderImage);
 }
}
}