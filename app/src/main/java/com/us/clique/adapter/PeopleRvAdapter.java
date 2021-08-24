package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.databinding.PeopleInterestBinding;
import com.us.clique.people.PeoplePojo;

import java.util.ArrayList;
import java.util.List;

public class PeopleRvAdapter extends RecyclerView.Adapter<PeopleRvAdapter.MyViewHolder> {
    Context context;
    ArrayList<PeoplePojo.SubCat> peopleModels = new ArrayList<>();
    ArrayList<PeoplePojo.SubCat> userList = new ArrayList<>();
    //    List<PeoplePojo.User> catArrayList = new ArrayList<>();
    LayoutInflater layoutInflater;

    public PeopleRvAdapter(Context context, ArrayList<PeoplePojo.SubCat> peopleModels,ArrayList<PeoplePojo.SubCat> userList) {
        this.context = context;
        this.peopleModels = peopleModels;
        this.userList = userList;
//        this.catArrayList = catArrayList;

    }

    @NonNull
    @Override
    public PeopleRvAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        PeopleInterestBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.people_interest, parent, false);
        return new PeopleRvAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleRvAdapter.MyViewHolder holder, int position) {
        PeoplePojo.SubCat reportModel = peopleModels.get(position);
        holder.binding.tvMovie.setText(reportModel.getSubCategoryName());
        if (reportModel.getCommon()) {
            holder.binding.tvMovie.setBackground(context.getResources().getDrawable(R.drawable.people_btn_bg));
            holder.binding.tvMovie.setTextColor(context.getResources().getColor(R.color.white));

        } else {
            holder.binding.tvMovie.setBackground(context.getResources().getDrawable(R.drawable.people_white_bg));
            holder.binding.tvMovie.setTextColor(context.getResources().getColor(R.color.black));
        }

    }
    @Override
    public int getItemCount() {
        return peopleModels.size();
    }

    public void updateList(List<PeoplePojo.SubCat> subCat, ArrayList<PeoplePojo.SubCat> loginUserSubCat) {
        peopleModels = (ArrayList<PeoplePojo.SubCat>) subCat;
        userList = (ArrayList<PeoplePojo.SubCat>) loginUserSubCat;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        PeopleInterestBinding binding;
        public MyViewHolder(@NonNull PeopleInterestBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}

