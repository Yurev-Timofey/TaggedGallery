package com.neuron.taggedgallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Image> images;
    private Context context;

    DataAdapter(Context context, List<Image> images) {
        this.images = images;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.cell_layout, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        Image img = images.get(position);
        Glide
                .with(context)
                .load(img.getImagePath())
                .centerCrop()
                .into(holder.imageView);

//        holder.imageView.setImageURI(Uri.fromFile(new File(img.getImagePath())));
//        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
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
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(image);
        notifyItemInserted(images.size() - 1);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;

        ViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.img);
        }
    }
}