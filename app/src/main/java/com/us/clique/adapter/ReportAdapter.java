package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.bottomNavigation.fragments.ExperiencesFragment;
import com.us.clique.bottomNavigation.fragments.module.ReportModel;
import com.us.clique.databinding.LayoutReportListBinding;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    Context context;
    ArrayList<ReportModel> reportModels = new ArrayList<>();
    LayoutInflater layoutInflater;
    ExperiencesFragment experiencesFragment;
    ArrayList<String> selectedItems = new ArrayList<>();
    public ReportAdapter(Context context, ArrayList<ReportModel> reportModels,ExperiencesFragment experiencesFragment) {
        this.context = context;
        this.reportModels = reportModels;
        this.experiencesFragment = experiencesFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutReportListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_report_list, parent, false);
        return new MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ReportModel reportModel = reportModels.get(position);
        holder.binding.tvBuletText.setText(reportModel.getName());
       /* holder.binding.ckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!holder.binding.ckBox.isChecked()){
                    selectedItems.clear();
                }else {
                   selectedItems.add(reportModel.getName());
                    experiencesFragment.selectText(selectedItems);
                }

            }
        });*/
        holder.binding.ckBox.setChecked(false);
        holder.binding.ckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){

                    selectedItems.add(reportModel.getName());
                }
                else
                {
                    selectedItems.remove(reportModel.getName());
                }
                experiencesFragment.selectText(selectedItems);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reportModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutReportListBinding binding;
        public MyViewHolder(@NonNull LayoutReportListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }
}
