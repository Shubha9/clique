package com.us.clique.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.us.clique.R;
import com.us.clique.utils.ConnectionStateMonitor;


public class BaseActivity extends AppCompatActivity {
    public static String TAG = BaseActivity.class.getName();
    private Dialog mDialog;
//    NetworkChangeListner networkChangeListner = new NetworkChangeListner();

    public void showProgressDialog(Context context) {
        if (mDialog == null) {
            mDialog = new Dialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.progress_dialog_spinner_layout, null);
            mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
        }
        mDialog.show();
    }

    public void showSomething(Context context) {
        View dialogView = getLayoutInflater().inflate(R.layout.layout_something_went_wrong, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog dialog = builder.create();
        dialog.setView(dialogView);
        Button btn_done = (Button) dialogView.findViewById(R.id.btn_done);
        dialog.setCanceledOnTouchOutside(false);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
        dialog.show();
    }
    public void dismissProgressDialog() {

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }


    public void noInternet(Context context) {

        View dialogView = getLayoutInflater().inflate(R.layout.fragment_internet_connection, null);


        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.DialogTheme);
        androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.setView(dialogView);
        Button btn_done = (Button) dialogView.findViewById(R.id.bt_refresh);
        ImageView BackPress = (ImageView) dialogView.findViewById(R.id.iv_onBackPress);
        dialog.setCanceledOnTouchOutside(false);
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                ((Activity) context).finish();
                context.startActivity(((Activity) context).getIntent());
            }
        });
        BackPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
//                ((Activity) context).finish();
//                context.startActivity(((Activity) context).getIntent());
            }
        });
        dialog.show();
    }





//        View dialogView = getLayoutInflater().inflate(R.layout.fragment_internet_connection, null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DialogTheme);
//        AlertDialog dialog = builder.create();
//        dialog.setView(dialogView);
//        Button btn_done = (Button) dialogView.findViewById(R.id.bt_refresh);
//        ImageView BackPress = (ImageView) dialogView.findViewById(R.id.iv_onBackPress);
//        dialog.setCanceledOnTouchOutside(false);
//        btn_done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//                ((Activity) context).finish();
//                context.startActivity(((Activity) context).getIntent());
//            }
//        });
//        BackPress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
////                ((Activity) context).finish();
////                context.startActivity(((Activity) context).getIntent());
//            }
//        });
//        dialog.show();
//    }


    public void checkconnection(Context context){

        ConnectionStateMonitor connectionStateMonitor = new ConnectionStateMonitor(context);
        connectionStateMonitor.observe(this, new Observer<Boolean>() {


            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean){

                }else{
                    // network lost
                    noInternet(context);
                }
            }
        });
    }






}


