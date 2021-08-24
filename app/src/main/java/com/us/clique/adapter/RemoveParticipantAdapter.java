package com.us.clique.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.databinding.LayoutRemovePeopleBinding;

import java.util.ArrayList;

public class RemoveParticipantAdapter extends RecyclerView.Adapter<RemoveParticipantAdapter.MyViewHolder> {
    Context context;
    TextView tvYes,tvNo;
    LayoutInflater layoutInflater;

ArrayList<RemoveParticipant> removeParticipants=new ArrayList<RemoveParticipant>();
    public RemoveParticipantAdapter(Context context,ArrayList<RemoveParticipant> removeParticipants) {
        this.context = context;
     this.removeParticipants=removeParticipants;

    }

    @NonNull
    @Override
    public RemoveParticipantAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        LayoutRemovePeopleBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.layout_remove_people, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(removeParticipants.get(position).getName());
        holder.binding.ivRemoveProfile.setImageResource(removeParticipants.get(position).getImg());
        holder.binding.tvReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                final View custom_layout =layoutInflater.inflate(R.layout.layout_remove_participants, null);
                alertDialogBuilder.setView(custom_layout);
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                tvNo= (TextView)custom_layout.findViewById(R.id.tv_no);
                tvYes= (TextView)custom_layout.findViewById(R.id.tv_yes);
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
//                        NetworkConnection networkConnection=new NetworkConnection(context.getApplicationContext());
//                        if(networkConnection.isInternetAvailable())
//                        {
//                            alertDialog.dismiss();
//                        }else
//                        {
//                            setContentView(R.layout.activity_networksignal);
//                        }


                    }
                });
                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return removeParticipants.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        LayoutRemovePeopleBinding binding;
        public MyViewHolder(@NonNull LayoutRemovePeopleBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
        }
    }



}
