package com.us.clique.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.us.clique.databinding.ActivityNotificationBinding;
import com.us.clique.modle.Notification;
import com.us.clique.modle.NotificationView;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;


import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityNotificationBinding binding;
    BottomNavigationViewModel bottomNavigationViewModel;
    int t_pause = 0, t_exp = 0, t_eve = 0, t_mess = 0, t_req = 0, t_email = 0;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ProgressDialog loading;
    UserSessionManager session;
    String access_token = "", userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(NotificationActivity.this,
                R.layout.activity_notification);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        OnlineUserStatus onlineUserStatus=new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
//        NetworkConnection networkConnection=new NetworkConnection(getApplicationContext());
//        if(networkConnection.isInternetAvailable())
//        {
            callApiNotification();
//        }else
//        {
////            setContentView(R.layout.activity_networksignal);
//        }
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setNotification(bottomNavigationViewModel);
        binding.ivOnBackPress.setOnClickListener(this);
        binding.togglePauseall.setOnClickListener(this);
        binding.toggleExperience.setOnClickListener(this);
        binding.toggleEvents.setOnClickListener(this);
        binding.toggleMessages.setOnClickListener(this);
        binding.toggleRequests.setOnClickListener(this);
        binding.toggleEmail.setOnClickListener(this);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_onBackPress:
                onBackPressed();
                break;
            case R.id.toggle_pauseall:
                if(checked())
                {
                    callAPI();
                }
                break;
            case R.id.toggle_experience:
                if(checked())
                {
                    callAPI();
                }
                break;
            case R.id.toggle_events:
                if(checked())
                {
                    callAPI();
                }
                break;
            case R.id.toggle_messages:
                if(checked())
                {
                    callAPI();
                }
                break;
            case R.id.toggle_requests:
                if(checked())
                {
                    callAPI();
                }
                break;
            case R.id.toggle_email:
                if(checked())
                {
                    callAPI();
                }
                break;
        }
    }



    private boolean checked() {
        if (binding.togglePauseall.isChecked()) {
            t_pause = 1;
        } else {
            t_pause = 0;
        }
        if (binding.toggleExperience.isChecked()) {
            t_exp = 1;
        } else {
            t_exp = 0;
        }
        if (binding.toggleEvents.isChecked()) {
            t_eve = 1;
        } else {
            t_eve = 0;
        }
        if (binding.toggleMessages.isChecked()) {
            t_mess = 1;
        } else {
            t_mess = 0;
        }
        if (binding.toggleRequests.isChecked()) {
            t_req = 1;
        } else {
            t_req = 0;
        }
        if (binding.toggleEmail.isChecked()) {
            t_email = 1;
        } else {
            t_email = 0;
        }

        return true;
    }
    private void callAPI() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("pause_all", t_pause);
        body.addProperty("experience",t_exp);
        body.addProperty("event_notify",t_eve);
        body.addProperty("message",t_mess);
        body.addProperty("request", t_req);
        body.addProperty("email_sms", t_email);

        loading = ProgressDialog.show(this, "Loading......", "wait....", false, false);
        Call<ExperinceModle> call = api.Settingdetails(access_token, "application/json", body);
        call.enqueue(new Callback<ExperinceModle>() {
            @Override
            public void onResponse(Call<ExperinceModle> call, Response<ExperinceModle> response) {
                loading.cancel();
                Toast.makeText(getApplicationContext(),"Saved",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ExperinceModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }
    private void callApiNotification() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading......", "wait....", false, false);
        Call<NotificationView> call = api.Notificationdetails(access_token);
        call.enqueue(new Callback<NotificationView>() {
            @Override
            public void onResponse(Call<NotificationView> call, Response<NotificationView> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    NotificationView notificationView=response.body();
                    List<Notification> data=notificationView.getData();
                    if(data.get(0).getPauseAll().equals("0"))
                    {
                        binding.togglePauseall.setChecked(false);
                    }else
                    {
                        binding.togglePauseall.setChecked(true);
                    }

                    if(data.get(0).getExperience().equals("0"))
                    {
                        binding.toggleExperience.setChecked(false);
                    }else
                    {
                        binding.toggleExperience.setChecked(true);
                    }
                    if(data.get(0).getEventNotify().equals("0"))
                    {
                        binding.toggleEvents.setChecked(false);
                    }else
                    {
                        binding.toggleEvents.setChecked(true);
                    }
                    if(data.get(0).getMessage().equals("0"))
                    {
                        binding.toggleMessages.setChecked(false);
                    }else
                    {
                        binding.toggleMessages.setChecked(true);
                    }
                    if(data.get(0).getEmailSms().equals("0"))
                    {
                        binding.toggleEmail.setChecked(false);
                    }else
                    {
                        binding.toggleEmail.setChecked(true);
                    }
                    if(data.get(0).getRequest().equals("0"))
                    {
                        binding.toggleRequests.setChecked(false);
                    }else
                    {
                        binding.toggleRequests.setChecked(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationView> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });
    }
}