package com.us.clique.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.activites.EventsDetailsActivity;
import com.us.clique.bottomNavigation.fragments.ExperiencesFragment;
import com.us.clique.bottomNavigation.interfaces.BlockInterface;
import com.us.clique.bottomNavigation.interfaces.CopyLinkInterface;
import com.us.clique.bottomNavigation.interfaces.NotIntrested;
import com.us.clique.bottomNavigation.interfaces.ReportInterface;
import com.us.clique.bottomNavigation.interfaces.SaveInterface;
import com.us.clique.bottomNavigation.interfaces.ShareInterface;
import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.squareup.picasso.Picasso;
import com.us.clique.databinding.LayoutExperiencesListBinding;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.us.clique.bottomNavigation.fragments.BlockFragment.TAG;


public class ExperiencesAdapter extends RecyclerView.Adapter<ExperiencesAdapter.MyViewHolder> {

    private Context mContext;
    LayoutInflater layoutInflater;
    //    ArrayList<MoveModle> moveModles = new ArrayList<>();
    ArrayList<ExperinceModle.Datum> experinceList = new ArrayList<>();
    ArrayList<String> tagsList = new ArrayList<>();
    ReportInterface mCallBack;
    NotIntrested notIntrested;
    BlockInterface blockInterface;
    ShareInterface shareInterface;
    SaveInterface saveInterface;
    CopyLinkInterface copyLinkInterface;
    ExperinceTagsAdapter experinceTagsAdapter;
    ExperiencesFragment experiencesFragment;
    public ExperiencesAdapter(Context mContext, ArrayList<ExperinceModle.Datum> experinceList, ReportInterface mCallBack, NotIntrested notIntrested, BlockInterface blockInterface,
                              ShareInterface shareInterface,ExperiencesFragment experiencesFragment,SaveInterface saveInterface,CopyLinkInterface copyLinkInterface) {
        this.mContext = mContext;
        this.experinceList = experinceList;
        this.mCallBack = mCallBack;
        this.notIntrested = notIntrested;
        this.blockInterface = blockInterface;
        this.shareInterface = shareInterface;
        this.experiencesFragment = experiencesFragment;
        this.saveInterface = saveInterface;
        this.copyLinkInterface = copyLinkInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutExperiencesListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_experiences_list, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ExperinceModle.Datum moveModle = experinceList.get(position);
        if (moveModle.getCategoryId().equals("1")) {
            holder.binding.rlMain.setBackgroundResource(R.drawable.experience_bg);
            holder.binding.ivIcon.setImageResource(R.drawable.ic_movie_icon);
        } else if (moveModle.getCategoryId().equals("2")) {
            holder.binding.rlMain.setBackgroundResource(R.drawable.out_door_bg);
            holder.binding.ivIcon.setImageResource(R.drawable.outdooricon);
        } else if (moveModle.getCategoryId().equals("3")) {
            holder.binding.rlMain.setBackgroundResource(R.drawable.sports_bg);
            holder.binding.ivIcon.setImageResource(R.drawable.sportsicon);
        } else if (moveModle.getCategoryId().equals("4")) {
            holder.binding.rlMain.setBackgroundResource(R.drawable.learn_bg);
            holder.binding.ivIcon.setImageResource(R.drawable.learnicon);
        } else if (moveModle.getCategoryId().equals("5")) {
            holder.binding.rlMain.setBackgroundResource(R.drawable.freelunch_bg);
            holder.binding.ivIcon.setImageResource(R.drawable.freelanchicon);
        } else if (moveModle.getCategoryId().equals("6")) {
            holder.binding.rlMain.setBackgroundResource(R.drawable.joinfree_bg);
            holder.binding.ivIcon.setImageResource(R.drawable.joinfreeicon);
        }
        String members,dateTime;
        members = moveModle.getMinimumMember() + "-" + moveModle.getMaximumMember();
        dateTime = moveModle.getEventTimeFormat()+","+ moveModle.getEventDateFormat();
        holder.binding.tvMovie.setText(moveModle.getTitle());
        holder.binding.tvPeople.setText(members);
        holder.binding.tvTime.setText(dateTime);
        holder.binding.tvAddress.setText(moveModle.getCityName());
        if (moveModle.getAge() != null){
            holder.binding.tvAge.setText(Integer.toString(moveModle.getAge()));
        }
        holder.binding.tvName.setText(moveModle.getUserName());
        holder.binding.tvDescription.setText(moveModle.getDescription());
        if(holder.binding.tvDescription.length()>0){
            holder.binding.tvMore.setVisibility(View.VISIBLE);
        }else{
            holder.binding.tvMore.setVisibility(View.GONE);
        }
        Picasso.get().load(moveModle.getEventImagePic())
                .placeholder(R.drawable.art)
                .into(holder.binding.ivBgImage);
        Picasso.get().load(moveModle.getProfileImage())
                .into(holder.binding.cvProfile);
        tagsList = (ArrayList<String>) moveModle.getTagList();

        experinceTagsAdapter = new ExperinceTagsAdapter(mContext, tagsList, experinceList,position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        holder.binding.rvTags.setLayoutManager(linearLayoutManager);
        holder.binding.rvTags.setAdapter(experinceTagsAdapter);
        holder.binding.rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,EventsDetailsActivity.class);
                i.putExtra("experinceId",moveModle.getSkEventId());
                mContext.startActivity(i);
            }
        });


        holder.binding.ivDots.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                String eventId,userName;
                eventId = moveModle.getSkEventId();
                userName  = moveModle.getUserName()+","+moveModle.getAge();
              experiencesFragment.eventId(eventId,userName);
             PopupMenu popupMenu = new PopupMenu(mContext, v, Gravity.END);
                popupMenu.inflate(R.menu.experince_menu);

                //   setMenuBackground();

                // Force icons to show
                Object menuHelper;
                Class[] argTypes;
                try {
                    Field fMenuHelper = PopupMenu.class.getDeclaredField("mPopup");
                    fMenuHelper.setAccessible(true);
                    menuHelper = fMenuHelper.get(popupMenu);
                    argTypes = new Class[]{boolean.class};
                    menuHelper.getClass().getDeclaredMethod("setForceShowIcon", argTypes).invoke(menuHelper, true);
                } catch (Exception e) {
                    Log.w(TAG, "error forcing menu icons to show", e);
                    popupMenu.show();
                    return;
                }
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.report:
                                mCallBack.callReport(v);
                                return true;

                            case R.id.not_interest:
                                notIntrested.notIntrested();
                                return true;

                            case R.id.save:
//                                binding.setVisibility(View.GONE);
//                                activity.showtooltip();
                                 saveInterface.save();
                                return true;
                            case R.id.share:
                                shareInterface.ShareInterFace();
                                return true;
                            case R.id.copy_link:
                                copyLinkInterface.copyLink(moveModle.getEventUrl());
                                return true;
                            case R.id.block:
                                blockInterface.BottomSheetFragment();
                                return true;
                        }
                        return false;
                    }
                });

             /*   if (holder.binding.expandableLayoutReport.isExpanded()){
                    holder.binding.expandableLayoutReport.collapse();
                    holder.binding.rlExpand.setVisibility(View.GONE);

                }else {
                   holder.binding.expandableLayoutReport.expand();
                   holder.binding.rlExpand.setVisibility(View.VISIBLE);
                }*/
            }
        });

      /*  holder.binding.rlReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  mCallBack.callReport(v);
                holder.binding.expandableLayoutReport.collapse();
            }
        });
        holder.binding.rlNotIntrest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notIntrested.notIntrested();
                holder.binding.expandableLayoutReport.collapse();
            }
        });
        holder.binding.rlBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blockInterface.BottomSheetFragment();
                holder.binding.expandableLayoutReport.collapse();
            }
        });
        holder.binding.rlShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareInterface.ShareInterFace();
                holder.binding.expandableLayoutReport.collapse();
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return experinceList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutExperiencesListBinding binding;

        public MyViewHolder(@NonNull LayoutExperiencesListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
