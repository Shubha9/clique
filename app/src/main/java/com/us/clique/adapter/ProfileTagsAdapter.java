package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.bottomNavigation.modle.ProfileModle;
import com.us.clique.databinding.LayoutTagsListBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileTagsAdapter extends RecyclerView.Adapter<ProfileTagsAdapter.MyViewHolder> {
    Context context;
    LayoutInflater layoutInflater;
    List<ProfileModle.SubCat> catArrayList = new ArrayList<>();

    public ProfileTagsAdapter(Context context, List<ProfileModle.SubCat> catArrayList) {
        this.context = context;
        this.catArrayList = catArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutTagsListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_tags_list, parent, false);
        return new MyViewHolder(binding);    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProfileModle.SubCat subCat = catArrayList.get(position);
        holder.binding.tvEditeProfile.setText(subCat.getSubCategoryName());
    }

    @Override
    public int getItemCount() {
        return catArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutTagsListBinding binding;
        public MyViewHolder(@NonNull LayoutTagsListBinding itemView) {
            super(itemView.getRoot());

            this.binding = itemView;
        }
    }
}
