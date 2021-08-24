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
import com.us.clique.databinding.LayoutRequestListBinding;
import com.us.clique.modle.ConnectAndRejectModle;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RequestMessagesAdapter extends RecyclerView.Adapter<RequestMessagesAdapter.MyViewHolder> implements Filterable {
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<ConnectAndRejectModle.Datum> requestModles = new ArrayList<>();
    RequestsFragment requestsFragment;
    private List<ConnectAndRejectModle.Datum> filterlist=null;

    public RequestMessagesAdapter(Context mContext, ArrayList<ConnectAndRejectModle.Datum> requestModles,RequestsFragment requestsFragment) {
        this.mContext = mContext;
        this.requestModles = requestModles;
        this.requestsFragment = requestsFragment;
        filterlist = new ArrayList<>(requestModles);

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutRequestListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_request_list, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ConnectAndRejectModle.Datum  requestModle = requestModles.get(position);
        holder.binding.tvName.setText(requestModle.getFollowerUserName());
        holder.binding.tvAccept.setText("Connect");
        Picasso.get().load(requestModle.getFollowerUserPic())
                .into(holder.binding.ivRequestProfile);
        holder.binding.tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestsFragment.connectRequest(position);
            }
        });

        holder.binding.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestsFragment.ConnectionRejected(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(requestModles.size() > 3){
            return 3;
        }
        else
        {
            return requestModles.size();
        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
    private Filter filter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ConnectAndRejectModle.Datum> filteredlist=new ArrayList<>();
            if(constraint==null||constraint.length()==0)
            {
                filteredlist.addAll(filterlist);
            }else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for (ConnectAndRejectModle.Datum item:filterlist)
                {
                    if(item.getFollowerUserName().toLowerCase().contains(filterpattern))
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
