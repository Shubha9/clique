package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.bottomNavigation.modle.ChooseYourInterestModle;
import com.us.clique.databinding.LayoutMoviesListBinding;
import com.us.clique.interfaces.SelectMoviesName;

import java.util.ArrayList;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<ChooseYourInterestModle.Data.Category> moviesListModles = new ArrayList<>();
    LayoutInflater layoutInflater;
    String type;
    SelectMoviesName mCallBack;

    public MoviesListAdapter(Context mContext, ArrayList<ChooseYourInterestModle.Data.Category> moviesListModles,String type, SelectMoviesName mCallBack ) {
        this.mContext = mContext;
        this.moviesListModles = moviesListModles;
        this.type = type;
        this.mCallBack = mCallBack;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutMoviesListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_movies_list, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ChooseYourInterestModle.Data.Category moviesListModle = moviesListModles.get(position);
        holder.binding.tvItem.setText(moviesListModle.getCategoryName());
    }

    @Override
    public int getItemCount() {
        return moviesListModles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutMoviesListBinding binding;
        public MyViewHolder(@NonNull LayoutMoviesListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            binding.llMovies.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (type.equalsIgnoreCase("Select Movie Name")) {
                mCallBack.setSelectMoviesName(moviesListModles.get(position).getCategoryName(),moviesListModles.get(position).getCategoryId());

            }
        }
    }
}
