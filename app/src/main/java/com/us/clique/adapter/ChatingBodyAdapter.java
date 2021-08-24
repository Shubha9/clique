package com.us.clique.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.activites.ChatingActivity;
import com.us.clique.bottomNavigation.modle.ChatingBodyResponceModle;
import com.us.clique.modle.MessagesModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class ChatingBodyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    //ArrayList<ChatingMessagesModle> chatingBodyModles = new ArrayList<>();
    ArrayList<MessagesModel> chatsModles = new ArrayList<>();

    LayoutInflater layoutInflater;
    private static int TYPE_SENDER = 1;
    private static int TYPE_RECIEVER = 2;
    String userId;
    ChatingActivity chatingActivity;
    ChatingBodyResponceModle chatingBodyModle;
    public ChatingBodyAdapter(Context context, ArrayList<MessagesModel> chatsModles,String userid) {
        this.context = context;
        this.chatsModles = chatsModles;
        this.userId=userid;

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)  {

        View view;
        if (viewType == TYPE_SENDER) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_chating_list_sender, parent, false);
            return new SenderViewHolder(view);

        } else {
            view = LayoutInflater.from(context).inflate(R.layout.layout_chating_list_reciver, parent, false);
            return new RecieverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        //ChatIndexModle.Datum datum = chatsModles.get(position);
        if (getItemViewType(position) == TYPE_SENDER) {
            ((SenderViewHolder) holder).setSenderDetails(chatsModles.get(position));

        } else {
            ((RecieverViewHolder) holder).setReciverDetails(chatsModles.get(position));

        }
    }


    @Override
    public int getItemCount() {
        return chatsModles.size();
    }

    @Override
    public int getItemViewType(int position) {
        // String senderID = "51";
        String senderID = chatsModles.get(position).getUserid();
        if (userId.equals(chatsModles.get(position).getUserid())){
            return TYPE_SENDER;
        } else {
            return TYPE_RECIEVER;
        }
    }


    class SenderViewHolder extends RecyclerView.ViewHolder {

        private TextView sMessage,tv_recivetime;
        ImageView senderImage;

        SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sMessage = itemView.findViewById(R.id.tv_recive);
            senderImage = itemView.findViewById(R.id.iv_rReciver);
//            tv_recivetime=itemView.findViewById(R.id.tv_sendtime);
//            senderImage = itemView.findViewById(R.id.iv_sImage);
        }

        public void setSenderDetails(MessagesModel messagesModel) {
            sMessage.setText(messagesModel.getMessage());

        }
    }

    class RecieverViewHolder extends RecyclerView.ViewHolder {

        private TextView rMessage,tv_rectim;
        ImageView rImage;

        RecieverViewHolder(@NonNull View itemView) {
            super(itemView);
            rMessage = itemView.findViewById(R.id.tv_recive);
            rImage = itemView.findViewById(R.id.iv_rReciver);
            tv_rectim=itemView.findViewById(R.id.tv_recivetime);
        }

        public void setReciverDetails(MessagesModel messagesModel) {
            rMessage.setText(messagesModel.getMessage());
            try {
                tv_rectim.setText(getDateCurrentTimeZone(messagesModel.getTimestamp()));
            }catch (NullPointerException e)
            {
                e.printStackTrace();
            }

//            tv_rectime.setText(datum.getChatTime());
        }
    }
    public String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currenTimeZone = (Date) calendar.getTime();
            return sdf.format(currenTimeZone);
        }catch (Exception e) {
        }
        return "";
    }
}
