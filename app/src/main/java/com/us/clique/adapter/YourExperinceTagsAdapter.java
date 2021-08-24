package com.us.clique.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.bottomNavigation.modle.YourExperienceResponseModle;
import com.us.clique.databinding.TagsListLayoutBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YourExperinceTagsAdapter extends RecyclerView.Adapter<YourExperinceTagsAdapter.MyViewHolder> {
    Context mContext;
    LayoutInflater layoutInflater;
    ArrayList<String> tagslist = new ArrayList<>();
    List<YourExperienceResponseModle.EventDetail> experinceList = new ArrayList<>();
    int pos;
    public YourExperinceTagsAdapter(Context mContext, ArrayList<String> tagsList, List<YourExperienceResponseModle.EventDetail> eventDetailsList, int position) {
        this.mContext = mContext;
        this.tagslist = tagsList;
        this.experinceList = eventDetailsList;
        this.pos = position;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        TagsListLayoutBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.tags_list_layout, parent, false);
        return new MyViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (experinceList.get(pos).getCategoryId().equals("1")){
            holder.binding.tvTagName.setTextColor(ContextCompat.getColor(mContext,R.color.fun));
            holder.binding.tvTagName.setBackgroundResource(R.drawable.fun_btn_bg);
        }else if (experinceList.get(pos).getCategoryId().equals("2")){
            holder.binding.tvTagName.setTextColor(ContextCompat.getColor(mContext,R.color.tab_text));
            holder.binding.tvTagName.setBackgroundResource(R.drawable.out_fun_btn_bg);
        }else if (experinceList.get(pos).getCategoryId().equals("3")){
            holder.binding.tvTagName.setTextColor(ContextCompat.getColor(mContext,R.color.sportfun_bg));
            holder.binding.tvTagName.setBackgroundResource(R.drawable.sportfun_btn_bg);
        }else if (experinceList.get(pos).getCategoryId().equals("4")){
            holder.binding.tvTagName.setTextColor(ContextCompat.getColor(mContext,R.color.learnfun_bg));
            holder.binding.tvTagName.setBackgroundResource(R.drawable.learnfun_btn_bg);
        }else if (experinceList.get(pos).getCategoryId().equals("5")){
            holder.binding.tvTagName.setTextColor(ContextCompat.getColor(mContext,R.color.freelanchfun_bg));
            holder.binding.tvTagName.setBackgroundResource(R.drawable.free_lanchfun_btn_bg);
        }else if (experinceList.get(pos).getCategoryId().equals("6")){
            holder.binding.tvTagName.setTextColor(ContextCompat.getColor(mContext,R.color.freejoinfun_bg));
            holder.binding.tvTagName.setBackgroundResource(R.drawable.joinfun_btn_bg);
        }
        String[] allIdsArray = TextUtils.split(tagslist.get(position).toString(), ",");
        ArrayList<String> idsList = new ArrayList<String>(Arrays.asList(allIdsArray));
        for(String element : idsList){
            holder.binding.tvTagName.setText(element);
        }

       // holder.binding.tvTagName.setText(tagslist.get(position));

    }

    @Override
    public int getItemCount() {
        return tagslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TagsListLayoutBinding binding;
        public MyViewHolder(@NonNull TagsListLayoutBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
