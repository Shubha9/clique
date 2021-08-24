package com.us.clique.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.Glide.GlideApp;
import com.us.clique.R;
import com.us.clique.databinding.PeopleLayoutBinding;
import com.us.clique.people.PeoplePojo;

import java.util.ArrayList;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.MyViewHolder> {
    Context context;
    ArrayList<PeoplePojo.User> peopleModels = new ArrayList<>();
    LayoutInflater layoutInflater;
    Context context1;

    public PeopleAdapter(Context context, ArrayList<PeoplePojo.User> peopleModels, Context context1) {
        this.context = context;
        this.context1 =context1;
        this.peopleModels = peopleModels;
        for(int i= 0;i<peopleModels.size();i++){
            peopleModels.get(i).setImage_no(0);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
     PeopleLayoutBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.people_layout, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PeoplePojo.User reportModel = peopleModels.get(position);
       GlideApp.with(context1).load(reportModel.getProfileImage()).into(holder.binding.ivPeopleImg);
        holder.binding.ivPeopleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportModel.getImage_no() < 3)
                    reportModel.setImage_no(reportModel.getImage_no() + 1);
                else
                    reportModel.setImage_no(0);

                switch (reportModel.getImage_no()) {
                    case 0:
                        GlideApp.with(context1).load(reportModel.getProfileImage()).into(holder.binding.ivPeopleImg);
                        break;
                    case 1:
                        GlideApp.with(context1).load(reportModel.getProfileImage2()).into(holder.binding.ivPeopleImg);
                        break;
                    case 2:
                        GlideApp.with(context1).load(reportModel.getProfileImage3()).into(holder.binding.ivPeopleImg);
                        break;
                    case 3:
                        GlideApp.with(context1).load(reportModel.getProfileImage4()).into(holder.binding.ivPeopleImg);
                        break;
                }

            }
        });
    }
    @Override
    public int getItemCount() {
        return peopleModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        PeopleLayoutBinding binding;
        public MyViewHolder(@NonNull PeopleLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
