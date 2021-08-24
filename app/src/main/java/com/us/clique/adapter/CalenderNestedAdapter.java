package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.bottomNavigation.modle.CalenderResponceModle;
import com.us.clique.databinding.LayoutCalenderList2Binding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CalenderNestedAdapter extends RecyclerView.Adapter<CalenderNestedAdapter.MyViewHolder>/* implements Filterable*/ {
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<CalenderResponceModle.EventDetail> nestedModles = new ArrayList<>();
    private List<CalenderResponceModle.EventDetail> filterlist=null;
    CharSequence searchString;
    public CalenderNestedAdapter(Context mContext, ArrayList<CalenderResponceModle.EventDetail> nestedModles,CharSequence searchString) {
        this.mContext = mContext;
        this.nestedModles = nestedModles;
        this.searchString = searchString.toString().toLowerCase();
        filterlist = new ArrayList<>(nestedModles);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutCalenderList2Binding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_calender_list2, parent, false);
        return new MyViewHolder(binding);        }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CalenderResponceModle.EventDetail nestedModle = nestedModles.get(position);

        if(nestedModle.getTitle().toLowerCase().contains(searchString)||searchString=="") {
            Picasso.get().load(nestedModle.getProfileImage())
                    .into(holder.binding.ivProfile);
            holder.binding.tvAdmin.setText(nestedModle.getUserName());
            holder.binding.tvMovieName.setText(nestedModle.getTitle());
            holder.binding.tvMovieLocation.setText(nestedModle.getCityName());
            if (nestedModle.getCategoryId().equals("1")){
                holder.binding.ivType.setImageResource(R.drawable.ic_movie_icon);
            }else if (nestedModle.getCategoryId().equals("2")){
                holder.binding.ivType.setImageResource(R.drawable.outdooricon);
            }else if (nestedModle.getCategoryId().equals("3")){
                holder.binding.ivType.setImageResource(R.drawable.sportsicon);
            }
            else if (nestedModle.getCategoryId().equals("4")){
                holder.binding.ivType.setImageResource(R.drawable.outdooricon);
            }
            else if (nestedModle.getCategoryId().equals("5")){
                holder.binding.ivType.setImageResource(R.drawable.ic_movie_icon);
            }
            else if (nestedModle.getCategoryId().equals("6")){
                holder.binding.ivType.setImageResource(R.drawable.freelanchicon);
            }

        }else {
            holder.itemView.setVisibility(View.GONE);
        }
      /*  holder.binding.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(mContext, CalenderExperienceActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(i);

            }
        });*/


    }

    @Override
    public int getItemCount() {
        return nestedModles.size();
    }

  /*  @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<CalenderResponceModle.EventDetail> filteredlist=new ArrayList<>();
            if(constraint==null||constraint.length()==0)
            {
                filteredlist.addAll(filterlist);
            }else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for (CalenderResponceModle.EventDetail item:filterlist)
                {
                    if(item.getTitle().toLowerCase().contains(filterpattern))
                    {
                        filteredlist.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredlist;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            nestedModles.clear();
            nestedModles.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };*/



    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutCalenderList2Binding binding;
        public MyViewHolder(@NonNull LayoutCalenderList2Binding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
