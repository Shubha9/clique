package com.us.clique.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.us.clique.activites.ChatingActivity;
import com.us.clique.bottomNavigation.modle.ChatIndexModle;
import com.us.clique.databinding.LayoutChatListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.MyViewHolder> implements Filterable  {
    Context mContext;
    ArrayList<ChatIndexModle.Datum> chatsModles = new ArrayList<>();
    LayoutInflater layoutInflater;
    String notification;
    private List<ChatIndexModle.Datum> filterlist=null;
    public ChatsAdapter(Context mContext, ArrayList<ChatIndexModle.Datum> chatsModles) {
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
        ChatIndexModle.Datum chatsModle = chatsModles.get(position);
        if (chatsModle.getOnlineStatus().equals("0")){
            holder.binding.ivOffOnline.setVisibility(View.VISIBLE);
            holder.binding.ivOnline.setVisibility(View.GONE);
           // holder.binding.ivOnline.setImageResource(R.drawable.ic_baseline_circle_read);
        }else if (chatsModle.getOnlineStatus().equals("1")){
           // holder.binding.ivOnline.setImageResource(R.drawable.ic_baseline_circle);
            holder.binding.ivOffOnline.setVisibility(View.GONE);
            holder.binding.ivOnline.setVisibility(View.VISIBLE);        }
        notification = chatsModle.getUnreadMsgCount();
        if (notification.equals("")){
            holder.binding.tvNotification.setVisibility(View.GONE);
        }else {
            holder.binding.tvNotification.setVisibility(View.VISIBLE);
            holder.binding.tvNotification.setText(chatsModle.getUnreadMsgCount());
            holder.binding.tvName.setTextColor(ContextCompat.getColor(mContext,R.color.month_color));
            holder.binding.tvMessage.setTextColor(ContextCompat.getColor(mContext,R.color.month_color));
            holder.binding.tvDate.setTextColor(ContextCompat.getColor(mContext,R.color.month_color));
        }
        holder.binding.tvName.setText(chatsModle.getRecieverName());
        holder.binding.tvMessage.setText(chatsModle.getLatestMessage());
        holder.binding.tvDate.setText(chatsModle.getLatestchatDate());
        Picasso.get().load(chatsModle.getProfilePathPic())
                .into(holder.binding.ivProfile);
        holder.binding.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext, ChatingActivity.class);
                i.putExtra("name", chatsModle.getRecieverName());
                i.putExtra("image",chatsModle.getProfilePathPic());
                i.putExtra("recieverId",chatsModle.getRecieverId());
                i.putExtra("chatid",chatsModle.getUniqueRequestId());
                mContext.startActivity(i);
            }
        });

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
            List<ChatIndexModle.Datum> filteredlist=new ArrayList<>();
            if(constraint==null||constraint.length()==0)
            {
                filteredlist.addAll(filterlist);
            }else
            {
                String filterpattern=constraint.toString().toLowerCase().trim();
                for (ChatIndexModle.Datum item:filterlist)
                {
                    if(item.getRecieverName().toLowerCase().contains(filterpattern))
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
