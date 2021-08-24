package com.us.clique.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.activites.EditeExperienceActivity;
import com.us.clique.activites.YourExperienceDetailsActivity;
import com.us.clique.bottomNavigation.interfaces.SaveAndCopyLinkInterface;
import com.us.clique.bottomNavigation.interfaces.ShareInterface;
import com.us.clique.bottomNavigation.modle.YourExperienceResponseModle;
import com.us.clique.databinding.LayoutDateList2Binding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class YourExperinceChildAdapter extends RecyclerView.Adapter<YourExperinceChildAdapter.MyViewHolder> {
    Context mContext;
    //ArrayList<YourExperienceModle> yourExperienceModles = new ArrayList<>();
    LayoutInflater layoutInflater;
    List<YourExperienceResponseModle.EventDetail> eventDetailsList = new ArrayList<>();
    YourExperinceTagsAdapter yourExperinceTagsAdapter;
    ArrayList<String> tagsList = new ArrayList<>();
    YourExperienceAdapter yourExperienceAdapter;
    ShareInterface shareInterface;
   // YourExperienceActivity activity;
    SaveAndCopyLinkInterface saveAndCopyLinkInterface;
    String evId;
    boolean isVisible = false;
    public YourExperinceChildAdapter(Context mContext, List<YourExperienceResponseModle.EventDetail> eventDetailsList, SaveAndCopyLinkInterface saveAndCopyLinkInterface ) {
        this.mContext = mContext;
        this.eventDetailsList = eventDetailsList;
        this.saveAndCopyLinkInterface = saveAndCopyLinkInterface;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutDateList2Binding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_date_list2, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        YourExperienceResponseModle.EventDetail eventDetail = eventDetailsList.get(position);
        if (eventDetail.getCategoryId().equals("1")) {
            holder.binding.ivIcon.setImageResource(R.drawable.ic_movie_icon);
        } else if (eventDetail.getCategoryId().equals("2")) {
            holder.binding.ivIcon.setImageResource(R.drawable.outdooricon);
        } else if (eventDetail.getCategoryId().equals("3")) {
            holder.binding.ivIcon.setImageResource(R.drawable.sportsicon);
        } else if (eventDetail.getCategoryId().equals("4")) {
            holder.binding.ivIcon.setImageResource(R.drawable.learnicon);
        } else if (eventDetail.getCategoryId().equals("5")) {
            holder.binding.ivIcon.setImageResource(R.drawable.freelanchicon);
        } else if (eventDetail.getCategoryId().equals("6")) {
            holder.binding.ivIcon.setImageResource(R.drawable.joinfreeicon);
        }

        holder.setIsRecyclable(false);
        String address,peopel;
        address = eventDetail.getLocationName()+","+eventDetail.getCityName()+","+eventDetail.getStateName()+","+eventDetail.getCountryName()+","+eventDetail.getContinentName();
        peopel = eventDetail.getMinimumMember()+"-"+eventDetail.getMaximumMember();
        evId = eventDetail.getSkEventId();
        holder.binding.tvMovie.setText(eventDetail.getTitle());
        holder.binding.tvTime.setText(eventDetail.getEventTime());
        holder.binding.tvAddress.setText(address);
        holder.binding.tvPeople.setText(peopel);
        holder.binding.tvDescription.setText(eventDetail.getDescription());
        if(holder.binding.tvDescription.length()>0){
            holder.binding.tvMore.setVisibility(View.VISIBLE);
        }else{
            holder.binding.tvMore.setVisibility(View.GONE);
        }
        holder.binding.tvRequestNo.setText(eventDetail.getRequestCount());
        holder.binding.tvParticipants.setText(eventDetail.getParticipantsCount());
        Picasso.get().load(eventDetail.getEventImagePic())
                .placeholder(R.drawable.art)
                .into(holder.binding.ivBgImage);
//        Picasso.get().load(eventDetail.getEventImagePic())
//                .into(holder.binding.ivBgImage);
        tagsList = (ArrayList<String>) eventDetail.getTagList();
        yourExperinceTagsAdapter = new YourExperinceTagsAdapter(mContext, tagsList, eventDetailsList,position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.binding.rvTags.setLayoutManager(linearLayoutManager);
        holder.binding.rvTags.setAdapter(yourExperinceTagsAdapter);


        holder.binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mContext.startActivity(new Intent(mContext,EditeExperienceActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                Intent i = new Intent(mContext, EditeExperienceActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("experinceId", eventDetail.getSkEventId());
                mContext.startActivity(i);
            }
        });

        holder.binding.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, YourExperienceDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                i.putExtra("experinceId",eventDetail.getSkEventId());
                mContext.startActivity(i);
            }
        });


        holder.binding.ivDots.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isVisible) {
                    holder.binding.rlSavePopup.setVisibility(View.VISIBLE);
                    isVisible = true;
                } else {
                    holder.binding.rlSavePopup.setVisibility(View.GONE);
                    isVisible = false;
                }
            }
        });
        holder.binding.rlSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.rlSavePopup.setVisibility(View.GONE);
                saveAndCopyLinkInterface.showtooltip(eventDetail.getSkEventId());
            }
        });
        holder.binding.rlCopylink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.binding.rlSavePopup.setVisibility(View.GONE);
                saveAndCopyLinkInterface.showtooltip2(eventDetail.getSkEventId(),eventDetail.getEventUrl());
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventDetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutDateList2Binding binding;
        public MyViewHolder(@NonNull LayoutDateList2Binding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
