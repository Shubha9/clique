package com.us.clique.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.ActivitySettingsBinding;
import com.us.clique.modle.NotificationView;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.us.clique.utils.NetworkConnection;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivitySettingsBinding binding;
    TextView tvYes,tvNo;
    UserSessionManager session;
    ProgressDialog loading;
    String access_token = "", userId = "";
    OnlineUserStatus onlineUserStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(SettingsActivity.this,
                R.layout.activity_settings);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setSettings(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        OnlineUserStatus onlineUserStatus=new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
        binding.ivOnBackPress.setOnClickListener(this);
        binding.rlNotification.setOnClickListener(this);
        binding.rlHelp.setOnClickListener(this);
        binding.rlAbout.setOnClickListener(this);
        binding.rlLogout.setOnClickListener(this);
        binding.rlDelete.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_onBackPress:
                onBackPressed();
                break;
            case R.id.rl_notification:
                Intent i=new Intent(getApplicationContext(),NotificationActivity.class);
                startActivity(i);
                break;
            case R.id.rl_help:
                Intent intent=new Intent(getApplicationContext(),HelpActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_about:
                Intent intent1=new Intent(getApplicationContext(),AboutActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_logout:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                final View custom_layout = getLayoutInflater().inflate(R.layout.logout_popup, null);
                alertDialogBuilder.setView(custom_layout);
                tvNo= (TextView)custom_layout.findViewById(R.id.tv_no);
                tvYes= (TextView)custom_layout.findViewById(R.id.tv_yes);
                AlertDialog alertDialog = alertDialogBuilder.create();
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        session.logoutUser();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }
                });
                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
            case R.id.rl_delete:
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);
                final View custom_layout1 = getLayoutInflater().inflate(R.layout.delete_popup, null);
                alertDialogBuilder1.setView(custom_layout1);
                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                tvNo= (TextView)custom_layout1.findViewById(R.id.tv_no);
                tvYes= (TextView)custom_layout1.findViewById(R.id.tv_yes);
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NetworkConnection networkConnection=new NetworkConnection(getApplicationContext());
                        if(networkConnection.isInternetAvailable())
                        {
                            callAPIDelete();
                            alertDialog1.dismiss();
                        }else
                        {
//                            setContentView(R.layout.activity_networksignal);
                        }


                    }
                });
                tvNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1.show();
                break;
        }
    }

    private void callAPIDelete() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("update_type", "DeleteAccount");

        loading = ProgressDialog.show(this, "Loading......", "wait....", false, false);
        Call<NotificationView> call = api.deleteaccount(access_token, body);
        call.enqueue(new Callback<NotificationView>() {
            @Override
            public void onResponse(Call<NotificationView> call, Response<NotificationView> response) {
                loading.cancel();
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                Toast.makeText(getApplicationContext(),"Account Deleted",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<NotificationView> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }
}