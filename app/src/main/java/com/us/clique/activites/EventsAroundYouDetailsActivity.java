package com.us.clique.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.CloneExperinceModle;
import com.us.clique.bottomNavigation.modle.EventsAroundYouDetailsModle;
import com.us.clique.databinding.ActivityEventsAroundYouDetailsBinding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsAroundYouDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityEventsAroundYouDetailsBinding binding;
    String eventsId;
    ProgressDialog loading;
    UserSessionManager session;
    String access_token = " ",globaleAccess_token = "",masterEventsId;
    EventsAroundYouDetailsModle eventsAroundYouDetailsModle;
    ArrayList<EventsAroundYouDetailsModle.Datum> eventsAroundYouModles = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_around_you_details);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(EventsAroundYouDetailsActivity.this,
                R.layout.activity_events_around_you_details);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setEventsDetails(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        OnlineUserStatus onlineUserStatus=new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();

        globaleAccess_token="cpk5sre43kpsprytujhjatquwevgtpljkg?e21nfo5k#qaqwe6thnbde";
        if (getIntent().getExtras() != null) {
            eventsId = getIntent().getStringExtra("eventId");
            //masterEventsId = getIntent().getStringExtra("masterEventId");
        }
            callGetEventsApi();


        binding.ivOnBackPress.setOnClickListener(this);
        binding.btCloneExperince.setOnClickListener(this);
    }

    private void setData(){

        for (int i=0;i<eventsAroundYouModles.size();i++){
            binding.tvMoveTitle.setText(eventsAroundYouModles.get(i).getTitle());
            masterEventsId = (String) eventsAroundYouModles.get(i).getMasterEventId();
            binding.tvTime.setText(eventsAroundYouModles.get(i).getEventTime());
            binding.tvAddress.setText(eventsAroundYouModles.get(i).getCityName());
            binding.tvDescription.setText(eventsAroundYouModles.get(i).getDescription());
            Picasso.get().load(eventsAroundYouModles.get(i).getEventImagePic())
                    .placeholder(R.drawable.events_around_you_bg)
                    .into(binding.ivEvent);
        }
    }

    private void callGetEventsApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<EventsAroundYouDetailsModle> call = api.GetEventsDetails(globaleAccess_token,"application/json",eventsId,"All","All","MasterEvents","All");
        call.enqueue(new Callback<EventsAroundYouDetailsModle>() {
            @Override
            public void onResponse(Call<EventsAroundYouDetailsModle> call, Response<EventsAroundYouDetailsModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    eventsAroundYouDetailsModle = response.body();
                        eventsAroundYouModles = (ArrayList)eventsAroundYouDetailsModle.getData();
                        setData();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("No Data Available"))
                        {
                         /*   binding.otpError.setVisibility(View.VISIBLE);
                            binding.otpError.setText(jObjError.getString("message"));*/
                        }
                        else {
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
            public void onFailure(Call<EventsAroundYouDetailsModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }
    private void callCloneExperinceApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("master_event_id" ,masterEventsId);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<CloneExperinceModle> call = api.PostCloneExperince(access_token,"application/json",body);
        call.enqueue(new Callback<CloneExperinceModle>() {
            @Override
            public void onResponse(Call<CloneExperinceModle> call, Response<CloneExperinceModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Cloned Experince",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), YourExperienceActivity.class);
                    startActivity(i);
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("Failed to add event"))
                        {
                            Toast.makeText(getApplicationContext(),"Failed to add event",Toast.LENGTH_SHORT).show();
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));

                        }
                        else {
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
            public void onFailure(Call<CloneExperinceModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_onBackPress:
                onBackPressed();
                break;

            case R.id.bt_cloneExperince:
                callCloneExperinceApi();
                break;
        }
    }
}