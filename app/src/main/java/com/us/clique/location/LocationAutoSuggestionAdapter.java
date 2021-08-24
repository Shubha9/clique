package com.us.clique.location;

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
import com.us.clique.bottomNavigation.fragments.CreateExperienceFragment;
import com.us.clique.bottomNavigation.interfaces.SelectLocationName;
import com.us.clique.databinding.LayoutLocationListBinding;

import java.util.ArrayList;
import java.util.List;

public class LocationAutoSuggestionAdapter  extends RecyclerView.Adapter<LocationAutoSuggestionAdapter.MuViewViewHolde> implements Filterable {
    Context mContext;
    ArrayList<LocationAutoSuggestionModle.Datum> locationAutoSuggestionModles = new ArrayList<>();
    LayoutInflater layoutInflater;
    private List<LocationAutoSuggestionModle.Datum> filterlist=new ArrayList<>();
    CreateExperienceFragment experienceFragment;
    SelectLocationName selectLocationName;

    public LocationAutoSuggestionAdapter(Context mContext, ArrayList<LocationAutoSuggestionModle.Datum> locationAutoSuggestionModles, SelectLocationName selectLocationName) {
        this.mContext = mContext;
        this.locationAutoSuggestionModles = locationAutoSuggestionModles;
        filterlist =locationAutoSuggestionModles ;
        this.selectLocationName = selectLocationName;
    }

    @NonNull
    @Override
    public LocationAutoSuggestionAdapter.MuViewViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutLocationListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_location_list, parent, false);
        return new LocationAutoSuggestionAdapter.MuViewViewHolde(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationAutoSuggestionAdapter.MuViewViewHolde holder, int position) {
        LocationAutoSuggestionModle.Datum autoSuggestionModle = locationAutoSuggestionModles.get(position);
        String locationID=autoSuggestionModle.getSkLocationId();
        String address  = autoSuggestionModle.getLocationName()+","+autoSuggestionModle.getCityName()+","+autoSuggestionModle.getStateName()+","+autoSuggestionModle.getCountryName();
        holder.binding.tvLocation.setText(address);
//            holder.binding.tvCity.setText(autoSuggestionModle.getCityName()+",");
//            holder.binding.tvState.setText(autoSuggestionModle.getStateName()+",");
//            holder.binding.tvCountry.setText(autoSuggestionModle.getCountryName());
            holder.binding.tvLocation.setOnClickListener(new View.OnClickListener() {
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

    public Filter filter=new Filter() {
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
            try {
                locationAutoSuggestionModles.clear();
                locationAutoSuggestionModles.addAll((List)results.values);
                notifyDataSetChanged();
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }


        }
    };

    public class MuViewViewHolde extends RecyclerView.ViewHolder {
        LayoutLocationListBinding binding;
        public MuViewViewHolde(@NonNull LayoutLocationListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
