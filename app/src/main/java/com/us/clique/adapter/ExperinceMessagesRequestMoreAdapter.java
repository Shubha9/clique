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
import com.us.clique.bottomNavigation.fragments.RequestsFragment;
import com.us.clique.bottomNavigation.modle.RequestsResponceModle;
import com.us.clique.databinding.LayoutRequestListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ExperinceMessagesRequestMoreAdapter extends RecyclerView.Adapter<ExperinceMessagesRequestMoreAdapter.MyViewHolder> implements Filterable {
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<RequestsResponceModle.Datum> requestModles = new ArrayList<>();
    private List<RequestsResponceModle.Datum> filterlist=null;
    RequestsFragment requestsFragment;

    public ExperinceMessagesRequestMoreAdapter(Context mContext, ArrayList<RequestsResponceModle.Datum> requestModles,RequestsFragment requestsFragment) {
        this.mContext = mContext;
        this.requestsFragment = requestsFragment;
        this.requestModles =requestModles;
        filterlist = new ArrayList<>(requestModles);

    }
    @NonNull
    @Override
    public ExperinceMessagesRequestMoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutRequestListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_request_list, parent, false);
        return new ExperinceMessagesRequestMoreAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperinceMessagesRequestMoreAdapter.MyViewHolder holder, int position) {
        RequestsResponceModle.Datum  requestModle = requestModles.get(position);
        holder.binding.tvName.setText(requestModle.getRequestedUserName());
        Picasso.get().load(requestModle.getRequestedUserPic())
                .into(holder.binding.ivRequestProfile);
        holder.binding.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestsFragment.acceptRequest(position);
            }
        });

        holder.binding.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestsFragment.rejected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestModles.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RequestsResponceModle.Datum> filteredlist=new ArrayList<>();
            if(constraint==null||constraint.length()==0)
            {
                filteredlist.addAll(filterlist);
            }else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for (RequestsResponceModle.Datum item:filterlist)
                {
                    if(item.getRequestedUserName().toLowerCase().contains(filterpattern))
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
            requestModles.clear();
            requestModles.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutRequestListBinding binding;
        public MyViewHolder(@NonNull LayoutRequestListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
