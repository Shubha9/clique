package com.us.clique.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.BottomNavigationActivity;
import com.us.clique.bottomNavigation.modle.RequestsResponceModle;
import com.us.clique.databinding.LayoutEventsDetailsPeopleBinding;
import com.squareup.picasso.Picasso;
import com.us.clique.signIn.SignInActivity;

import java.util.ArrayList;

public class EventsDetailAdapter extends RecyclerView.Adapter<EventsDetailAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<RequestsResponceModle.Datum> eventsDetailModles = new ArrayList<>();
    LayoutInflater layoutInflater;
    UserSessionManager session;

    public EventsDetailAdapter(Context mContext, ArrayList<RequestsResponceModle.Datum> eventsDetailModles, UserSessionManager session) {
        this.mContext = mContext;
        this.eventsDetailModles = eventsDetailModles;
        this.session =session;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutEventsDetailsPeopleBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_events_details_people, parent, false);
        return new MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RequestsResponceModle.Datum eventsDetailModle = eventsDetailModles.get(position);
        //holder.binding.ivEventDetailProfile.setImageResource(eventsDetailModle.getImages());
        Picasso.get().load(eventsDetailModle.getRequestedUserPic())
                .into(holder.binding.ivEventDetailProfile);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             session.setUserProfileCLicked(true);
             session.setUserProfile(eventsDetailModle.getEventUserId());
                Intent i = new Intent(mContext, BottomNavigationActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventsDetailModles.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutEventsDetailsPeopleBinding binding;
        public MyViewHolder(@NonNull LayoutEventsDetailsPeopleBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
