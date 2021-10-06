package com.example.billbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    ArrayList<String> items;
    public MainAdapter(ArrayList<String> items) {
        this.items = items;
    }
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder( MainAdapter.ViewHolder holder, int position) {
        holder.i.setText(items.get(position));
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView i;
        public ViewHolder( View itemView) {
            super(itemView);
            i = itemView.findViewById(R.id.i);
        }
    }
}
