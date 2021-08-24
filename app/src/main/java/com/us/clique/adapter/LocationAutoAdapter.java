package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.bottomNavigation.interfaces.SelectLocationName;
import com.us.clique.databinding.LayoutLocationListBinding;
import com.us.clique.location.LocationAutoSuggestionModle;

import java.util.ArrayList;
import java.util.List;

public class LocationAutoAdapter  extends RecyclerView.Adapter<LocationAutoAdapter.MyViewHolder> implements Filterable {
    Context mContext;
    ArrayList<LocationAutoSuggestionModle.Datum> locationAutoSuggestionModles = new ArrayList<>();
    LayoutInflater layoutInflater;
    private List<LocationAutoSuggestionModle.Datum> filterlist=null;
   // EditeExperienceActivity editeExperienceActivity;

    SelectLocationName selectLocationName;
    public LocationAutoAdapter(Context mContext, ArrayList<LocationAutoSuggestionModle.Datum> locationAutoSuggestionModles, SelectLocationName selectLocationName ) {
        this.mContext = mContext;
        this.locationAutoSuggestionModles = locationAutoSuggestionModles;
        filterlist = new ArrayList<>(locationAutoSuggestionModles);
        this.selectLocationName = selectLocationName;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutLocationListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_location_list, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LocationAutoSuggestionModle.Datum autoSuggestionModle = locationAutoSuggestionModles.get(position);
        String locationID=autoSuggestionModle.getSkLocationId();
        String address  = autoSuggestionModle.getLocationName()+","+autoSuggestionModle.getCityName()+","+autoSuggestionModle.getStateName()+","+autoSuggestionModle.getCountryName();
        holder.binding.tvLocation.setText(address);
//            holder.binding.tvCity.setText(autoSuggestionModle.getCityName()+",");
//            holder.binding.tvState.setText(autoSuggestionModle.getStateName()+",");
//            holder.binding.tvCountry.setText(autoSuggestionModle.getCountryName());
        holder.binding.llLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectLocationName.selectLocation(address,position);
                selectLocationName.selectId(locationID,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return locationAutoSuggestionModles.size();
    }


    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<LocationAutoSuggestionModle.Datum> filteredlist=new ArrayList<>();
            if(constraint==null||constraint.length()==0)
            {
                filteredlist.addAll(filterlist);
            }else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for (LocationAutoSuggestionModle.Datum item:filterlist)
                {
                    if(item.getCityName().toLowerCase().contains(filterpattern)||item.getLocationName().toLowerCase().contains(filterpattern)||item.getStateName().toLowerCase().contains(filterpattern))
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
            locationAutoSuggestionModles.clear();
            try {
                locationAutoSuggestionModles.addAll((List)results.values);
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }

            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutLocationListBinding binding;
        public MyViewHolder(@NonNull LayoutLocationListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
