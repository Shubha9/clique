package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.activites.YourExperienceActivity;
import com.us.clique.bottomNavigation.modle.YourExperienceResponseModle;
import com.us.clique.databinding.LayoutDateListBinding;
import com.us.clique.modle.DateModle;

import java.util.ArrayList;
import java.util.List;

public class YourExperienceAdapter extends RecyclerView.Adapter<YourExperienceAdapter.MyviewHolder> {
    Context mContext;
    ArrayList<DateModle> dateModles = new ArrayList<>();
    LayoutInflater layoutInflater;

   // ArrayList<YourExperienceModle> yourExperienceModles = new ArrayList<>();
    List<YourExperienceResponseModle.EventDetail> eventDetailsList = new ArrayList<>();
    List<YourExperienceResponseModle.EventDescription> yourExperienceResponseModles = new ArrayList<>();
    YourExperienceActivity yourExperienceActivity;
    public YourExperienceAdapter(Context mContext, List<YourExperienceResponseModle.EventDescription> yourExperienceResponseModles,YourExperienceActivity yourExperienceActivity) {
        this.mContext = mContext;
        this.yourExperienceResponseModles = yourExperienceResponseModles;
        this.yourExperienceActivity =yourExperienceActivity;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutDateListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_date_list, parent, false);
        return new MyviewHolder(binding);      }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        //DateModle dateModle = dateModles.get(position);
        YourExperienceResponseModle.EventDescription yourExperienceResponseModle= yourExperienceResponseModles.get(position);
        eventDetailsList = yourExperienceResponseModle.getEventDetails();
        holder.binding.tvDate.setText(yourExperienceResponseModle.getEventDateFormat());
        holder.binding.rvTodayList.setLayoutManager(new LinearLayoutManager(mContext));
        holder.binding.rvTodayList.setAdapter(new YourExperinceChildAdapter(mContext, eventDetailsList,yourExperienceActivity));
    }

    @Override
    public int getItemCount() {
        return yourExperienceResponseModles.size();
    }


    public class MyviewHolder extends RecyclerView.ViewHolder {
        LayoutDateListBinding binding;
        public MyviewHolder(@NonNull LayoutDateListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
           // eventDetailsList.clear();
//            yourExperienceModles.add(new YourExperienceModle("Movies @ NYC Free tickets","08:30 PM, Fri 22 Aug","NYC, Centrl Theater","3-5","","movies"));
//            yourExperienceModles.add(new YourExperienceModle("Movies @ NYC Free tickets","08:30 PM, Fri 22 Aug","NYC, Centrl Theater","3-5","","sports"));


        }
    }
}
