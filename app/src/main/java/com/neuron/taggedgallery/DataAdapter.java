package com.neuron.taggedgallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Image> images;

    DataAdapter(Context context, List<Image> images) {
        this.images = images;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cell_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        Image img = images.get(position);

        holder.imageView.setImageURI(Uri.fromFile(new File(img.getImagePath())));
        Uri.fromFile(new File(img.getImagePath()));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setImages(List<Image> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    public void addImage(Image image) {
        if (images == null){
            images = new ArrayList<>();
        }
        images.add(image);
        notifyItemInserted(images.size()-1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.img);
        }
    }
}