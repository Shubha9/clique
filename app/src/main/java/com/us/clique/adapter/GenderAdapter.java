package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.bottomNavigation.interfaces.MovieNameInterface;
import com.us.clique.databinding.LayoutGenderListBinding;
import com.us.clique.modle.MoviesListModle;

import java.util.ArrayList;

public class GenderAdapter extends RecyclerView.Adapter<GenderAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<MoviesListModle> moviesListModles = new ArrayList<>();
    LayoutInflater layoutInflater;
    String type;
    MovieNameInterface mCallBack;

    public GenderAdapter(Context mContext, ArrayList<MoviesListModle> moviesListModles, String type, MovieNameInterface mCallBack) {
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
        LayoutGenderListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_gender_list, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MoviesListModle moviesListModle = moviesListModles.get(position);
        holder.binding.tvItem.setText(moviesListModle.getMoviesName());
    }

    @Override
    public int getItemCount() {
        return moviesListModles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LayoutGenderListBinding binding;
        public MyViewHolder(@NonNull LayoutGenderListBinding itemView) {
            super(itemView.getRoot());
            this.binding=itemView;
            binding.llMovies.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (type.equalsIgnoreCase("Select Gender")) {
                mCallBack.setSelectMoviesName(moviesListModles.get(position).getMoviesName());
            }
        }
    }
}
