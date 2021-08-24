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
import com.us.clique.bottomNavigation.modle.RequestsResponceModle;
import com.us.clique.databinding.LayoutChatListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RequestedMoreAdapter extends RecyclerView.Adapter<RequestedMoreAdapter.MyViewHolder> implements Filterable {
    Context mContext;
    ArrayList<RequestsResponceModle.Datum> chatsModles = new ArrayList<>();
    LayoutInflater layoutInflater;
    String notification;
    private List<RequestsResponceModle.Datum> filterlist=null;

    public RequestedMoreAdapter(Context mContext, ArrayList<RequestsResponceModle.Datum> chatsModles) {
        this.mContext = mContext;
        this.chatsModles = chatsModles;
        filterlist = new ArrayList<>(chatsModles);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutChatListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_chat_list, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        RequestsResponceModle.Datum chatsModle = chatsModles.get(position);
        if (chatsModle.getRequestingStatus().equals("1")){
            holder.binding.ivOnline.setVisibility(View.VISIBLE);
            holder.binding.ivOnline.setImageResource(R.drawable.ic_baseline_circle_read);
        }else {
            holder.binding.ivOnline.setImageResource(R.drawable.ic_baseline_circle);
            //   holder.binding.ivOnline.setVisibility(View.GONE);
        }
       /*  notification = chatsModle.getNotification();
        if (notification.equals("")){
            holder.binding.tvNotification.setVisibility(View.GONE);
        }else {
            holder.binding.tvNotification.setVisibility(View.VISIBLE);
            holder.binding.tvNotification.setText(chatsModle.getNotification());
            holder.binding.tvName.setTextColor(ContextCompat.getColor(mContext,R.color.month_color));
            holder.binding.tvMessage.setTextColor(ContextCompat.getColor(mContext,R.color.month_color));
            holder.binding.tvDate.setTextColor(ContextCompat.getColor(mContext,R.color.month_color));
        }*/
        holder.binding.tvName.setText(chatsModle.getRequestedUserName());
        holder.binding.tvMessage.setText(chatsModle.getEventTitle());
        holder.binding.tvDate.setText(chatsModle.getCreatedDate());
        Picasso.get().load(chatsModle.getRequestedUserPic())
                .into(holder.binding.ivProfile);
    }

    @Override
    public int getItemCount() {
        return chatsModles.size();
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
            chatsModles.clear();
            chatsModles.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutChatListBinding binding;
        public MyViewHolder(@NonNull LayoutChatListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
