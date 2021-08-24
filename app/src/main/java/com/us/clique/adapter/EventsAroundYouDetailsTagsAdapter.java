package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.databinding.TagsListLayoutBinding;

import java.util.ArrayList;

public class EventsAroundYouDetailsTagsAdapter extends RecyclerView.Adapter<EventsAroundYouDetailsTagsAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> tagslist = new ArrayList<>();
    LayoutInflater layoutInflater;


    public EventsAroundYouDetailsTagsAdapter(Context context, ArrayList<String> tagslist) {
        this.context = context;
        this.tagslist = tagslist;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TagsListLayoutBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.tags_list_layout, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvTagName.setTextColor(ContextCompat.getColor(context,R.color.learnfun_bg));
        holder.binding.tvTagName.setBackgroundResource(R.drawable.learnfun_btn_bg);
        holder.binding.tvTagName.setText(tagslist.get(position));

    }

    @Override
    public int getItemCount() {
        return tagslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TagsListLayoutBinding binding;
        public MyViewHolder(@NonNull TagsListLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
