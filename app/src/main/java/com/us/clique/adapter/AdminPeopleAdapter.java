package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.databinding.LayoutAdminPeopleBinding;

import java.util.ArrayList;

public class AdminPeopleAdapter extends RecyclerView.Adapter<AdminPeopleAdapter.ViewHolder> {
    ArrayList<AdminPeople> adminPeople=new ArrayList<>();
    LayoutInflater layoutInflater;
    Context mcontext;
    Boolean isVisible=false;
    public AdminPeopleAdapter(Context mcontext,ArrayList<AdminPeople> adminPeople ) {
        this.mcontext=mcontext;
        this.adminPeople=adminPeople;
    }
    @NonNull
    @Override
    public AdminPeopleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());

        }
        LayoutAdminPeopleBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_admin_people, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPeopleAdapter.ViewHolder holder, int position) {
        AdminPeople adminPeople1=adminPeople.get(position);
        holder.binding.ivRemoveProfile.setImageResource(adminPeople.get(position).getImg());

            holder.binding.ivRemoveProfile.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    if (!isVisible) {
                        holder.binding.ivRemoveProfile.setBackgroundResource(R.drawable.ic_ellipse_299);
                        isVisible=true;
                    } else {
                        holder.binding.ivRemoveProfile.setBackgroundResource(R.color.white);
                        isVisible=false;
                    }
                }


            });

    }

    @Override
    public int getItemCount() {
        return  adminPeople.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LayoutAdminPeopleBinding binding;
        public ViewHolder(@NonNull LayoutAdminPeopleBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }

}
