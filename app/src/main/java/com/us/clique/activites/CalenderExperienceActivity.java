package com.us.clique.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.EventsAroundYouDetailsTagsAdapter;
import com.us.clique.adapter.EventsDetailAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.us.clique.bottomNavigation.modle.RequestsResponceModle;
import com.us.clique.databinding.ActivityCalenderExperienceBinding;
import com.us.clique.modle.EventsDetailModle;
import com.us.clique.modle.RequestModle;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.us.clique.utils.NetworkConnection;
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

public class CalenderExperienceActivity extends AppCompatActivity implements View.OnClickListener {
TextView tvYes,tvNo;
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ArrayList<EventsDetailModle> eventsDetailModles = new ArrayList<>();
    EventsDetailAdapter eventsDetailAdapter;
    ActivityCalenderExperienceBinding binding;
    ArrayList<RequestModle> requestModles = new ArrayList<>();
    ProgressDialog loading;
    ProgressDialog loading1;
    ProgressDialog loading2;
    ProgressDialog loading3;

    UserSessionManager session;
    String access_token = " ",experinceEventsId,userId,eventUserId,requesteventUserId;
    ExperinceModle experinceModle;
    ArrayList<ExperinceModle.Datum> experinceList = new ArrayList<>();
    EventsAroundYouDetailsTagsAdapter eventsAroundYouDetailsTagsAdapter;
    ArrayList<String> tagsList = new ArrayList<>();
    RequestsResponceModle requestsResponceModle;
    ArrayList<RequestsResponceModle.Datum> requestsOtherViewList = new ArrayList<>();
    ArrayList<RequestsResponceModle.Datum> requestsViewsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(CalenderExperienceActivity.this,
                R.layout.activity_calender_experience);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setCalenderExperience(bottomNavigationViewModel);

        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        if (getIntent().getExtras() != null) {
            experinceEventsId = getIntent().getStringExtra("experinceId");
        }
        OnlineUserStatus onlineUserStatus=new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
//        recyclerPeopleList();
        binding.ivOnBackPress.setOnClickListener(this);
        binding.btnExitExperience.setOnClickListener(this);
//
        NetworkConnection networkConnection=new NetworkConnection(getApplicationContext());
        if(networkConnection.isInternetAvailable())
        {
            callGetExperinceDetailsApi();
            callOthersViewApi();
        }else
        {
            setContentView(R.layout.activity_networksignal);
        }



    }

    private void recyclerPeopleList(){

        eventsDetailAdapter = new EventsDetailAdapter(getApplicationContext(),requestsOtherViewList,session);
        binding.rvPeopleList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvPeopleList.setAdapter(eventsDetailAdapter);


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
            case R.id.btn_exit_experience:
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);
                final View custom_layout1 = getLayoutInflater().inflate(R.layout.layout_calender_exit_exp, null);
                alertDialogBuilder1.setView(custom_layout1);
                AlertDialog alertDialog1 = alertDialogBuilder1.create();
                tvNo= (TextView)custom_layout1.findViewById(R.id.tv_no);
                tvYes= (TextView)custom_layout1.findViewById(R.id.tv_yes);
                tvYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        NetworkConnection networkConnection=new NetworkConnection(getApplicationContext());
//                        if(networkConnection.isInternetAvailable())
//                        {
                            alertDialog1.dismiss();
//                        }else
//                        {
//                            setContentView(R.layout.activity_networksignal);
//                        }


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
    private void setData(){
        for (int i=0;i<experinceList.size();i++){
            String  members,timeDate;
            // requesteventUserId = experinceList.get(i);
            members = experinceList.get(i).getMinimumMember()+","+experinceList.get(i).getMaximumMember();
            timeDate = experinceList.get(i).getEventTime()+","+experinceList.get(i).getEventDate();
            binding.tvMovieTitle.setText(experinceList.get(i).getTitle());
            binding.tvTime.setText(timeDate);
            binding.tvAddress.setText(experinceList.get(i).getCityName());
            binding.tvPeoples.setText(members);
            binding.tvUserName.setText(experinceList.get(i).getUserName());
            binding.tvDescription.setText(experinceList.get(i).getDescription());
            Picasso.get().load(experinceList.get(i).getEventImagePic())
                    .into(binding.ivDetails);
            Picasso.get().load(experinceList.get(i).getProfileImage())
                    .into(binding.cvProfile);

            tagsList = (ArrayList<String>) experinceList.get(i).getTagList();
            eventsAroundYouDetailsTagsAdapter = new EventsAroundYouDetailsTagsAdapter(this, tagsList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            binding.rvTagsList.setLayoutManager(linearLayoutManager);
            binding.rvTagsList.setAdapter(eventsAroundYouDetailsTagsAdapter);
        }
    }


    private void callGetExperinceDetailsApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ExperinceModle> call = api.GetExperincesDetails(access_token,"application/json",experinceEventsId,"All","All","EVENT_VIEW","All");
        call.enqueue(new Callback<ExperinceModle>() {
            @Override
            public void onResponse(Call<ExperinceModle> call, Response<ExperinceModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    experinceModle = response.body();
                    experinceList = (ArrayList)experinceModle.getData();
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
            public void onFailure(Call<ExperinceModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callOthersViewApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading3 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<RequestsResponceModle> call = api.GetOthersView(access_token,"Joined","All",experinceEventsId);
        call.enqueue(new Callback<RequestsResponceModle>() {
            @Override
            public void onResponse(Call<RequestsResponceModle> call, Response<RequestsResponceModle> response) {
                loading3.cancel();
                if (response.isSuccessful()) {
                    requestsResponceModle = response.body();
                    requestsOtherViewList = (ArrayList) requestsResponceModle.getData();
                    recyclerPeopleList();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("No Data Available"))
                        {
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
            public void onFailure(Call<RequestsResponceModle> call, Throwable t) {
                loading3.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

}