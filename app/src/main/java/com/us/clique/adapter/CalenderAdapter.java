package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.bottomNavigation.modle.CalenderResponceModle;
import com.us.clique.databinding.LayoutCalenderListBinding;

import java.util.ArrayList;

public class CalenderAdapter extends RecyclerView.Adapter<CalenderAdapter.MyViewHolder> {
    Context mContext;
    ArrayList<CalenderResponceModle.Datum> calenderModles = new ArrayList<>();
    LayoutInflater layoutInflater;
    ArrayList<CalenderResponceModle.EventDetail> nestedModles = new ArrayList<>();
    CharSequence searchString ="";

    private int[] images= {R.drawable.event_profile_1,R.drawable.event_profile_2,R.drawable.event_profile_3};


    public CalenderAdapter(Context mcontext, ArrayList<CalenderResponceModle.Datum> calenderModles) {
        mContext = mcontext;
        this.calenderModles = calenderModles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutCalenderListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_calender_list, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CalenderResponceModle.Datum calenderModle =calenderModles.get(position);
            holder.binding.tvDate.setText(calenderModle.getEventDateFormat());
        nestedModles = (ArrayList) calenderModle.getEventDetails();
        holder.binding.rvNested.setLayoutManager(new LinearLayoutManager(mContext));
        holder.binding.rvNested.setAdapter(new CalenderNestedAdapter(mContext, nestedModles,searchString));
    }

    @Override
    public int getItemCount() {
        return calenderModles.size();
    }

    public void searchString(CharSequence s) {
        searchString = s;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutCalenderListBinding binding;
        public MyViewHolder(@NonNull LayoutCalenderListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
//            nestedModles.clear();
//            nestedModles.add(new CalenderNestedModle(R.drawable.event_profile_1,"Superman Movie","3PM, NYC Central Theater","Admin","movies"));
//            nestedModles.add(new CalenderNestedModle(R.drawable.event_profile_2,"Superman Movie","3PM, NYC Central Theater","Admin","sports"));
//            nestedModles.add(new CalenderNestedModle(R.drawable.event_profile_2,"Mounesh","3PM, NYC Central Theater","Admin","free lanch"));

//            binding.rvNested.setLayoutManager(new LinearLayoutManager(mContext));
//            binding.rvNested.setAdapter(new CalenderNestedAdapter(mContext, nestedModles,searchString));
        }
    }
}
