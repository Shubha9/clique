package com.us.clique.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.activites.EventsAroundYouDetailsActivity;
import com.us.clique.databinding.LayoutEventsAroundYouListBinding;
import com.us.clique.modle.EventsAroundYouModle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventsAroundYouAdapter extends RecyclerView.Adapter<EventsAroundYouAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<EventsAroundYouModle.Datum> eventsAroundYouModles = new ArrayList<>();
    LayoutInflater layoutInflater;

    public EventsAroundYouAdapter(Context mContext, ArrayList<EventsAroundYouModle.Datum> eventsAroundYouModles) {
        this.mContext = mContext;
        this.eventsAroundYouModles = eventsAroundYouModles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutEventsAroundYouListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_events_around_you_list, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EventsAroundYouModle.Datum eventsAroundYouModle = eventsAroundYouModles.get(position);
        String  dateAndTime,Address;
        dateAndTime = eventsAroundYouModle.getEventTime()+","+eventsAroundYouModle.getEventDate();
//        Address = eventsAroundYouModle.getLocationName()+","+eventsAroundYouModle.getCityName()+","+eventsAroundYouModle.getStateName()+","+eventsAroundYouModle.getCountryName()+","+eventsAroundYouModle.getContinentName();
        holder.binding.tvMoveTitle.setText(eventsAroundYouModle.getTitle());
        holder.binding.tvTime.setText(dateAndTime);
        holder.binding.tvAddress.setText(eventsAroundYouModle.getCityName());
        holder.binding.tvDescription.setText(eventsAroundYouModle.getDescription());
        Picasso.get().load(eventsAroundYouModle.getEventImagePic())
                .into(holder.binding.ivFeature);
        holder.binding.rlEventsMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventsAroundYouDetailsActivity.class);
                intent.putExtra("eventId",eventsAroundYouModle.getSkEventId());
               // intent.putExtra("masterEventId",eventsAroundYouModle.getMasterEventId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventsAroundYouModles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutEventsAroundYouListBinding binding;
        public MyViewHolder(@NonNull LayoutEventsAroundYouListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
