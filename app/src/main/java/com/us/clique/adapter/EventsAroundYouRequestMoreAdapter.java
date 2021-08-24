package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.bottomNavigation.interfaces.EventsAroundYouDetailsInterface;
import com.us.clique.bottomNavigation.modle.RequestsResponceModle;
import com.us.clique.databinding.LayoutRequestListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class EventsAroundYouRequestMoreAdapter  extends RecyclerView.Adapter<EventsAroundYouRequestMoreAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<RequestsResponceModle.Datum> requestModles = new ArrayList<>();
    private List<RequestsResponceModle.Datum> filterlist=null;
    //EventsDetailsActivity eventsDetailsActivity;
    EventsAroundYouDetailsInterface eventsAroundYouDetailsInterface;

    public EventsAroundYouRequestMoreAdapter(Context mContext, ArrayList<RequestsResponceModle.Datum> requestModles,EventsAroundYouDetailsInterface eventsAroundYouDetailsInterface) {
        this.mContext = mContext;
        this.requestModles = requestModles;
        this.eventsAroundYouDetailsInterface = eventsAroundYouDetailsInterface;
        filterlist = new ArrayList<>(requestModles);

    }

    @NonNull
    @Override
    public EventsAroundYouRequestMoreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutRequestListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_request_list, parent, false);
        return new EventsAroundYouRequestMoreAdapter.MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsAroundYouRequestMoreAdapter.MyViewHolder holder, int position) {
        RequestsResponceModle.Datum  requestModle = requestModles.get(position);
        holder.binding.tvName.setText(requestModle.getRequestedUserName());
        Picasso.get().load(requestModle.getRequestedUserPic())
                .into(holder.binding.ivRequestProfile);
        holder.binding.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsAroundYouDetailsInterface.acceptRequest(position);
                holder.binding.tvAccept.setBackground(ContextCompat.getDrawable(mContext,R.drawable.button_background));
                holder.binding.tvReject.setBackground(ContextCompat.getDrawable(mContext,R.drawable.learnfun_btn_bg));
                holder.binding.tvReject.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                holder.binding.tvAccept.setTextColor(ContextCompat.getColor(mContext, R.color.tab_text));

            }
        });

        holder.binding.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventsAroundYouDetailsInterface.rejectRequest(position);
                holder.binding.tvReject.setBackground(ContextCompat.getDrawable(mContext,R.drawable.button_background));
                holder.binding.tvAccept.setBackground(ContextCompat.getDrawable(mContext,R.drawable.learnfun_btn_bg));
                holder.binding.tvAccept.setTextColor(ContextCompat.getColor(mContext, R.color.tab_text));
                holder.binding.tvReject.setTextColor(ContextCompat.getColor(mContext, R.color.white));
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

