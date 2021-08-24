package com.us.clique.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.EventsAroundYouAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.IpAddressModle;
import com.us.clique.databinding.ActivityEventsArroundYouBinding;
import com.us.clique.modle.EventsAroundYouModle;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsArroundYouActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityEventsArroundYouBinding binding;
    EventsAroundYouModle eventsAroundYouModle;
    ArrayList<EventsAroundYouModle.Datum> eventsAroundYouModles = new ArrayList<>();
    EventsAroundYouAdapter eventsAroundYouAdapter;
    ProgressDialog loading;
    UserSessionManager session;
    String access_token = " ", globaleAccess_token = "", userId = "";
    String ipAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(EventsArroundYouActivity.this,
                R.layout.activity_events_arround_you);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setEventsAround(bottomNavigationViewModel);

        binding.ivOnBackPress.setOnClickListener(this);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
//        userId = session.getUserId();
        globaleAccess_token = "cpk5sre43kpsprytujhjatquwevgtpljkg?e21nfo5k#qaqwe6thnbde";
        OnlineUserStatus onlineUserStatus = new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
        // callApi();
        callGetEventsApi();
    }

    public void initRecycler() {
        if (!eventsAroundYouModle.getStatus().equals(true)) {
            binding.rvEventsList.setVisibility(View.GONE);
            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
        } else {
            if (eventsAroundYouModles != null) {
                binding.tvNoDataAvailable.setVisibility(View.GONE);
                binding.rvEventsList.setVisibility(View.VISIBLE);
                eventsAroundYouAdapter = new EventsAroundYouAdapter(this, eventsAroundYouModles);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
                binding.rvEventsList.setLayoutManager(mLayoutManager);
                binding.rvEventsList.setItemAnimator(new DefaultItemAnimator());
                binding.rvEventsList.setAdapter(eventsAroundYouAdapter);
            }
        }

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
        }
    }

    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<IpAddressModle> call = api.PostIpAddress(access_token, "application/json");
        call.enqueue(new Callback<IpAddressModle>() {
            @Override
            public void onResponse(Call<IpAddressModle> call, Response<IpAddressModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    IpAddressModle ipAddressModle = response.body();
                    if (ipAddressModle.getStatus()) {
                        ipAddress = ipAddressModle.getData();
//                        callGetEventsApi();
                    } else {
                        Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("IP address")) {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));

                        } else {
//                            tvPassError.setVisibility(View.VISIBLE);
//                            tvPassError.setText(jObjError.getString("message"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();


                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<IpAddressModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callGetEventsApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<EventsAroundYouModle> call = api.GetEvents(access_token, "application/json",/*ipAddress */"117.196.2.117", "1", "MasterEvents", "LocationByIP", "All");
        call.enqueue(new Callback<EventsAroundYouModle>() {
            @Override
            public void onResponse(Call<EventsAroundYouModle> call, Response<EventsAroundYouModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    eventsAroundYouModle = response.body();
                    if (eventsAroundYouModle.getStatus()) {
                        eventsAroundYouModles = (ArrayList) eventsAroundYouModle.getData();
                        initRecycler();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Data Available", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("No Data Available")) {
                            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
                            binding.tvNoDataAvailable.setText(jObjError.getString("message"));


                        } else {
//                            tvPassError.setVisibility(View.VISIBLE);
//                            tvPassError.setText(jObjError.getString("message"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();


                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<EventsAroundYouModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


}