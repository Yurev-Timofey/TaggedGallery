package com.neuron.taggedgallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.neuron.taggedgallery.Image;
import com.neuron.taggedgallery.R;

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
        holder.imageView.setImageResource(img.getImage());
//        holder.nameView.setText(img.getName());
//        holder.companyView.setText(img.getCompany());
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView imageView;
        ViewHolder(View view){
            super(view);
            imageView = view.findViewById(R.id.img);
        }
    }
}