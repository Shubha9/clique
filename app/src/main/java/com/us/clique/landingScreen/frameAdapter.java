package com.us.clique.landingScreen;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.activites.LoginActivity;

import java.util.ArrayList;

class frameAdapter extends RecyclerView.Adapter<frameAdapter.ViewHolder>{
    private ArrayList<frameitems> listdata;
    String status1,str1;
    String str2,str3,str4;
    RecyclerView recyclerView;
    FrameActivity parent;
    Context context;
    public frameAdapter(Context context, ArrayList<frameitems> listdata, FrameActivity parent) {
       this.context=context;
        this.listdata=listdata;
        this.parent=parent;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.frame_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    holder.setframeData(listdata.get(position));
        if (position==0){
            holder.skip.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i=new Intent(parent, LoginActivity.class);
                            parent.startActivity(i);
                        }
                    }
            );
        }
if(position==1){
    holder.title.setTextColor(Color.parseColor("#FFFFFF"));
    holder.description.setTextColor(Color.parseColor("#FFFFFF"));
    holder.skip.setTextColor(Color.parseColor("#FFFFFF"));
    holder.skip.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(parent, LoginActivity.class);
                    parent.startActivity(i);
                }
            }
    );


}
if (position==2){
holder.skip.setOnClickListener(
        new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(parent, LoginActivity.class);
               parent.startActivity(i);
            }
        }
);
    }
    }

    @Override
    public  int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title,skip;
        public TextView description;
        public ImageView  img;
public Button btn;

        public ViewHolder(View itemView) {
            super(itemView);
            btn=(Button)itemView.findViewById(R.id.btn);
            title=(TextView)itemView.findViewById(R.id.txt1);
            description=(TextView)itemView.findViewById(R.id.frametxt1);
            img=(ImageView)itemView.findViewById(R.id.img1);
            skip=(TextView)itemView.findViewById(R.id.skip);


        }
        void setframeData(frameitems item){
            title.setText(item.getTitle());
           description.setText(item.getDescription());
            img.setImageResource(item.getImg(R.drawable.frame1));
            skip.setText(item.getSkip());
        }

    }

}

