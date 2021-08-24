package com.us.clique.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.databinding.LayoutCheckboxListBinding;
import com.us.clique.modle.FiltersModle;

import java.util.ArrayList;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.MyviewHolder> {
    Context context;
    ArrayList<FiltersModle> filtersModles = new ArrayList<>();
    LayoutInflater layoutInflater;


    public FilterAdapter(Context context, ArrayList<FiltersModle> filtersModles) {
        this.context = context;
        this.filtersModles = filtersModles;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutCheckboxListBinding binding =
                DataBindingUtil.inflate(layoutInflater, R.layout.layout_checkbox_list, parent, false);
        return new MyviewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder holder, int position) {
        FiltersModle filtersModle = filtersModles.get(position);
        holder.binding.tvExperence.setText(filtersModle.getName());
        holder.binding.ckBox.setTag(position);

        holder.binding.ckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    holder.binding.tvExperence.setTextColor(ContextCompat.getColor(context,R.color.tab_text));
//                    holder.binding.ckBox.setTin(Color.parseColor("#0053CB"));
                    holder.binding.ckBox.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.tab_text, null)));
                }
                if (!isChecked) {
                    holder.binding.tvExperence.setTextColor(ContextCompat.getColor(context,R.color.check_box));
                    holder.binding.ckBox.setButtonTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.check_box, null)));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return filtersModles.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        LayoutCheckboxListBinding binding;
        public MyviewHolder(@NonNull  LayoutCheckboxListBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
        }
    }

