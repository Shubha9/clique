package com.us.clique.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.EventsAroundYouDetailsTagsAdapter;
import com.us.clique.adapter.EventsAroundYouRequestMoreAdapter;
import com.us.clique.adapter.EventsDetailAdapter;
import com.us.clique.adapter.RequestAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.interfaces.EventsAroundYouDetailsInterface;
import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.us.clique.bottomNavigation.modle.RequestAcceptedModle;
import com.us.clique.bottomNavigation.modle.RequestToJoinModle;
import com.us.clique.bottomNavigation.modle.RequestsResponceModle;
import com.us.clique.databinding.ActivityEventsDetailsBinding;
import com.us.clique.modle.EventsDetailModle;
import com.us.clique.modle.RequestModle;
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

public class EventsDetailsActivity extends AppCompatActivity implements View.OnClickListener, EventsAroundYouDetailsInterface {

    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ArrayList<EventsDetailModle> eventsDetailModles = new ArrayList<>();
    EventsDetailAdapter eventsDetailAdapter;
    ActivityEventsDetailsBinding binding;
    ArrayList<RequestModle> requestModles = new ArrayList<>();
    ProgressDialog loading;
    ProgressDialog loading1;
    ProgressDialog loading2;
    ProgressDialog loading3;

    UserSessionManager session;
    String access_token = " ", experinceEventsId, userId, eventUserId, requesteventUserId;
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
        binding = DataBindingUtil.setContentView(EventsDetailsActivity.this,
                R.layout.activity_events_details);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setEventsDetails(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        if (getIntent().getExtras() != null) {
            experinceEventsId = getIntent().getStringExtra("experinceId");
        }
        OnlineUserStatus onlineUserStatus = new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
//        recyclerPeopleList();
        binding.ivOnBackPress.setOnClickListener(this);
        binding.rlMore.setOnClickListener(this);
        binding.btnRequestToJoin.setOnClickListener(this);
        binding.btnMessages.setOnClickListener(this);

//        NetworkConnection networkConnection=new NetworkConnection(getApplicationContext());
//        if(networkConnection.isInternetAvailable())
//        {
        callGetExperinceDetailsApi();
        callRequestsApi();
        callOthersViewApi();
//        }else
//        {
////            setContentView(R.layout.activity_networksignal);
//        }


    }

    private void recyclerPeopleList() {
        if (requestsOtherViewList != null) {
            eventsDetailAdapter = new EventsDetailAdapter(getApplicationContext(), requestsOtherViewList, session);
            binding.rvPeopleList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.rvPeopleList.setAdapter(eventsDetailAdapter);
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
            case R.id.rl_more:
                if (binding.rlMore.isClickable()) {
                    binding.rlMore.setVisibility(View.GONE);
                    binding.rvRequestList.setVisibility(View.GONE);
                    binding.rvRequestList2.setVisibility(View.VISIBLE);
                    showRequestMoreRecycler();
                } else {
                    binding.rlMore.setVisibility(View.VISIBLE);
                    binding.rvRequestList2.setVisibility(View.GONE);
                    binding.rvRequestList.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_requestToJoin:
                callRequestToJoinApi();
                binding.btnRequestToJoin.setText("Requested");
                break;
            case R.id.btn_messages:
                callRequestToMessageApi();
                binding.btnMessages.setText("Message requested");
                break;
        }
    }

    private void setData() {
        for (int i = 0; i < experinceList.size(); i++) {
            String members, timeDate, userNameAndAge;
            userNameAndAge = experinceList.get(i).getUserName() + "," + experinceList.get(i).getAge();
            // requesteventUserId = experinceList.get(i);
            if (experinceList.get(i).getCategoryId().equals("1")) {
                binding.ivIcon.setImageResource(R.drawable.ic_movie_icon);
            } else if (experinceList.get(i).getCategoryId().equals("2")) {
                binding.ivIcon.setImageResource(R.drawable.outdooricon);
            } else if (experinceList.get(i).getCategoryId().equals("3")) {
                binding.ivIcon.setImageResource(R.drawable.sportsicon);
            } else if (experinceList.get(i).getCategoryId().equals("4")) {
                binding.ivIcon.setImageResource(R.drawable.learnicon);
            } else if (experinceList.get(i).getCategoryId().equals("5")) {
                binding.ivIcon.setImageResource(R.drawable.freelanchicon);
            } else if (experinceList.get(i).getCategoryId().equals("6")) {
                binding.ivIcon.setImageResource(R.drawable.joinfreeicon);
            }
            eventUserId = experinceList.get(i).getEventUserId();
            members = experinceList.get(i).getMinimumMember() + "," + experinceList.get(i).getMaximumMember();
            timeDate = experinceList.get(i).getEventTime() + "," + experinceList.get(i).getEventDate();
            binding.tvMovieTitle.setText(experinceList.get(i).getTitle());
            binding.tvTime.setText(timeDate);
            binding.tvAddress.setText(experinceList.get(i).getCityName());
            binding.tvPeoples.setText(members);
            binding.tvUserName.setText(userNameAndAge);
            binding.tvDescription.setText(experinceList.get(i).getDescription());
            Picasso.get().load(experinceList.get(i).getEventImagePic())
                    .placeholder(R.drawable.events_around_you_bg)
                    .into(binding.ivDetails);
            Picasso.get().load(experinceList.get(i).getProfileImage())
                    .into(binding.cvProfile);

            tagsList = (ArrayList<String>) experinceList.get(i).getTagList();
            eventsAroundYouDetailsTagsAdapter = new EventsAroundYouDetailsTagsAdapter(this, tagsList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            binding.rvTagsList.setLayoutManager(linearLayoutManager);
            binding.rvTagsList.setAdapter(eventsAroundYouDetailsTagsAdapter);
            if (userId.equals(eventUserId)) {
                binding.llRequestAndMessages.setVisibility(View.INVISIBLE);
            } else {
                binding.llRequestAndMessages.setVisibility(View.VISIBLE);
            }

            if (experinceList.get(i).getEventStatus().equals("1")) {
                binding.btnRequestToJoin.setText("Request to Join");
            }

            if (requestsViewsList.size() > 3){
                binding.rlMore.setVisibility(View.VISIBLE);
            }else {
                binding.rlMore.setVisibility(View.GONE);
            }
        }
    }

    public void setAcceptRejectDate() {
        //requestsResponceModle.getData().
        for (int i = 0; i < requestsResponceModle.getData().size(); i++) {
            requesteventUserId = requestsResponceModle.getData().get(i).getRequestUserId();
        }
    }

    private void showRequestRecycler() {

        if (requestsViewsList != null) {
            binding.rvRequestList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.rvRequestList.setAdapter(new RequestAdapter(getApplicationContext(), requestsViewsList, this));
        }
    }

    private void showRequestMoreRecycler() {
        if (requestsViewsList != null) {
            binding.rvRequestList2.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.rvRequestList2.setAdapter(new EventsAroundYouRequestMoreAdapter(getApplicationContext(), requestsViewsList, this));
        }
    }

    private void callGetExperinceDetailsApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ExperinceModle> call = api.GetExperincesDetails(access_token, "application/json", experinceEventsId, "All", "All", "EVENT_VIEW", "All");
        call.enqueue(new Callback<ExperinceModle>() {
            @Override
            public void onResponse(Call<ExperinceModle> call, Response<ExperinceModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    experinceModle = response.body();
                    experinceList = (ArrayList) experinceModle.getData();
                    setData();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("No Data Available")) {
                         /*   binding.otpError.setVisibility(View.VISIBLE);
                            binding.otpError.setText(jObjError.getString("message"));*/
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
            public void onFailure(Call<ExperinceModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callRequestToJoinApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("request_type", "RequestToJoin");
        body.addProperty("request_user_id", userId);
        body.addProperty("event_id", experinceEventsId);
        body.addProperty("event_user_id", eventUserId);
        body.addProperty("requesting_status", "Join");
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<RequestToJoinModle> call = api.PostRequestToJoin(access_token, body);
        call.enqueue(new Callback<RequestToJoinModle>() {
            @Override
            public void onResponse(Call<RequestToJoinModle> call, Response<RequestToJoinModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Requested Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Already Requested for this event", Toast.LENGTH_SHORT).show();

                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Already Requested for this event")) {
                            Toast.makeText(getApplicationContext(), "Already Requested for this event", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<RequestToJoinModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callRequestsApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading1 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<RequestsResponceModle> call = api.GetRequestsView(access_token, "Join", userId, "All");
        call.enqueue(new Callback<RequestsResponceModle>() {
            @Override
            public void onResponse(Call<RequestsResponceModle> call, Response<RequestsResponceModle> response) {
                loading1.cancel();
                if (response.isSuccessful()) {
                    requestsResponceModle = response.body();
                    requestsViewsList = (ArrayList) requestsResponceModle.getData();
                    showRequestRecycler();

                    setAcceptRejectDate();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("No Data Available")) {
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
            public void onFailure(Call<RequestsResponceModle> call, Throwable t) {
                loading1.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callRequestAcceptApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("request_type", "RequestToJoin");
        body.addProperty("request_user_id", requesteventUserId);
        body.addProperty("event_id", experinceEventsId);
        body.addProperty("event_user_id", userId);
        body.addProperty("requesting_status", "Join");
        body.addProperty("update_type", "Accepted");
        loading2 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<RequestAcceptedModle> call = api.PutRequestsAccepted(access_token, "application/json", body);
        call.enqueue(new Callback<RequestAcceptedModle>() {
            @Override
            public void onResponse(Call<RequestAcceptedModle> call, Response<RequestAcceptedModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "your request accepted", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("your request accepted")) {
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
            public void onFailure(Call<RequestAcceptedModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callRequestRejectApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("request_type", "RequestToJoin");
        body.addProperty("request_user_id", requesteventUserId);
        body.addProperty("event_id", experinceEventsId);
        body.addProperty("event_user_id", userId);
        body.addProperty("requesting_status", "Join");
        body.addProperty("update_type", "Rejected");
        loading2 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<RequestAcceptedModle> call = api.PutRequestsAccepted(access_token, "application/json", body);
        call.enqueue(new Callback<RequestAcceptedModle>() {
            @Override
            public void onResponse(Call<RequestAcceptedModle> call, Response<RequestAcceptedModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "your request Rejected", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("your request accepted")) {
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
            public void onFailure(Call<RequestAcceptedModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callOthersViewApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading3 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<RequestsResponceModle> call = api.GetOthersView(access_token, "Joined", "All", experinceEventsId);
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

                        if ((jObjError.getString("message")).equals("No Data Available")) {
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
            public void onFailure(Call<RequestsResponceModle> call, Throwable t) {
                loading3.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });
    }
    private void callRequestToMessageApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("request_type", "RequestToMessage");
        body.addProperty("request_user_id", userId);
        body.addProperty("event_id", experinceEventsId);
        body.addProperty("event_user_id", eventUserId);
        body.addProperty("requesting_status", "Join");
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<RequestToJoinModle> call = api.PostRequestToJoin(access_token, body);
        call.enqueue(new Callback<RequestToJoinModle>() {
            @Override
            public void onResponse(Call<RequestToJoinModle> call, Response<RequestToJoinModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Requested Successfully", Toast.LENGTH_SHORT).show();
//                    RequestsFragment fragment1 = new RequestsFragment();
//                    FragmentManager fragmentManager = getFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.requests_fragment,fragment1);
//                    fragmentTransaction.commit();

                } else {
                    Toast.makeText(getApplicationContext(), "Already Requested for this event", Toast.LENGTH_SHORT).show();

                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Already Requested for this event")) {
                            //Toast.makeText(getApplicationContext(),"Already Requested for this event",Toast.LENGTH_SHORT).show();
//                            binding.otpError.setVisibility(View.VISIBLE);
                            binding.btnMessages.setText(jObjError.getString("message"));

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
            public void onFailure(Call<RequestToJoinModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


    public void acceptRequest(int position) {
        callRequestAcceptApi();
    }

    @Override
    public void rejectRequest(int position) {
        callRequestRejectApi();
    }
}