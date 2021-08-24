package com.us.clique.bottomNavigation.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.ConnectAndRejectMoreAdapter;
import com.us.clique.adapter.ExperinceMessagesRequestMoreAdapter;
import com.us.clique.adapter.MessagesRequestAdapter;
import com.us.clique.adapter.RequestMessagesAdapter;
import com.us.clique.adapter.RequestedAdapter;
import com.us.clique.adapter.RequestedMoreAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.interfaces.GotoMessagesList;
import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.us.clique.bottomNavigation.modle.RequestAcceptedModle;
import com.us.clique.bottomNavigation.modle.RequestsResponceModle;
import com.us.clique.databinding.FragmentRequestBinding;
import com.us.clique.modle.ConnectAndRejectModle;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class RequestsFragment extends Fragment implements View.OnClickListener, GotoMessagesList {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentRequestBinding binding;
    ArrayList<RequestsResponceModle.Datum> chatsModles = new ArrayList<>();
    RequestedAdapter requestedAdapter;
    RequestedMoreAdapter requestedMoreAdapter;
    MessagesRequestAdapter messagesRequestAdapter;
    RequestMessagesAdapter requestMessagesAdapter;
    ConnectAndRejectMoreAdapter connectAndRejectMoreAdapter;
    ExperinceMessagesRequestMoreAdapter experinceMessagesRequestMoreAdapter;
    ArrayList<RequestsResponceModle.Datum> requestModles = new ArrayList<>();
    ProgressDialog loading, loading1, loading2, getLoading3;
    UserSessionManager session;
    ExperinceModle experinceModle;
    ArrayList<ExperinceModle.Datum> experinceList = new ArrayList<>();
    String access_token = " ", EventsId, userId, eventUserId, requestUserId;
    RequestsResponceModle requestsResponceModle;
    ConnectAndRejectModle connectAndRejectModle;

    ArrayList<RequestsResponceModle.Datum> requestsViewsList = new ArrayList<>();
    ArrayList<ConnectAndRejectModle.Datum> connectAndrejectList = new ArrayList<>();
    String eREventId, erRequestId, eREventUserId;
    String mcrEventId, mcrRequestId, mcrEventUserId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_request, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setRequestFragment(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        binding.rlMore.setOnClickListener(this);
        binding.rlMoreRequest.setOnClickListener(this);
        binding.rlMoreMessages.setOnClickListener(this);
        //initRecycler();
        //initRequest1List();

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (requestedAdapter!=null){
                    requestedAdapter.getFilter().filter(charSequence.toString());
                }else if (requestedMoreAdapter != null){
                    requestedMoreAdapter.getFilter().filter(charSequence.toString());
                }else {
                    Toast.makeText(getApplicationContext(),"No Data Available",Toast.LENGTH_LONG).show();

                }
                ///////////////////////////// Accept And Reject /////////////////////////////////////
                if (messagesRequestAdapter!=null){
                    messagesRequestAdapter.getFilter().filter(charSequence.toString());
                }else if (connectAndRejectMoreAdapter != null){
                    connectAndRejectMoreAdapter.getFilter().filter(charSequence.toString());
                    }else {
                    Toast.makeText(getApplicationContext(),"No Data Available",Toast.LENGTH_LONG).show();
                }
                ///////////////////////////// Connect And Reject /////////////////////////////////////

                if (requestMessagesAdapter != null){
                    requestMessagesAdapter.getFilter().filter(charSequence.toString());
                }else if (connectAndRejectMoreAdapter !=null){
                        connectAndRejectMoreAdapter.getFilter().filter(charSequence.toString());
                }else {
                        Toast.makeText(getApplicationContext(),"No Data Available",Toast.LENGTH_LONG).show();
                    }


//                requestedAdapter.getFilter().filter(charSequence.toString());
//                requestedMoreAdapter.getFilter().filter(charSequence.toString());
//                messagesRequestAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //messageAdapter.setFilter((ArrayList<MessageModule>) messageModuleList,editable.toString());


            }
        });

//        NetworkConnection networkConnection = new NetworkConnection(getApplicationContext());
//        if (networkConnection.isInternetAvailable()) {
            callRequestsApi();
            callRequestsMessagesListApi();
            callConnectRejectApi();
//        } else {
//            View v = inflater.inflate(R.layout.fragment_internet_connection, container, false);
//            return v;
//        }
        if (chatsModles.size() <= 3){
            binding.rlMoreMessages.setVisibility(View.GONE);
        }
        if (requestsViewsList.size() <= 3){
            binding.rlMore.setVisibility(View.GONE);
        }
        if (connectAndrejectList.size() <= 3){
            binding.rlMoreRequest.setVisibility(View.GONE);
        }
        return binding.getRoot();
    }
    ////////////////////////// Messages Adapters //////////////////////////////////

    private void initRecycler() {
        requestedAdapter = new RequestedAdapter(getContext(), chatsModles, this);
        binding.rvInboxList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvInboxList.setAdapter(requestedAdapter);

    }

    ////////////////////////// Accept and Reject Adapters //////////////////////////////////


    private void showRequestRecycler() {
        messagesRequestAdapter = new MessagesRequestAdapter(getContext(), requestsViewsList, this);
        binding.rvRequestList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRequestList.setAdapter(messagesRequestAdapter);
//        binding.rvRequestList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        binding.rvRequestList.setAdapter(new MessagesRequestAdapter(getApplicationContext(), requestsViewsList, this));
    }

    ////////////////////////// Connect and Reject Adapters //////////////////////////////////

    private void showRequestMessagesRecycler() {
        requestMessagesAdapter = new RequestMessagesAdapter(getContext(), connectAndrejectList, this);
        binding.rvConnectRejectList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvConnectRejectList.setAdapter(requestMessagesAdapter);

//        binding.rvConnectRejectList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        binding.rvConnectRejectList.setAdapter(new RequestMessagesAdapter(getApplicationContext(), connectAndrejectList, this));
    }


    ////////////////////////// Messages More Adapters //////////////////////////////////
    private void RequestRecyclerMessagesMore() {
        if (chatsModles!=null){
            requestedMoreAdapter = new RequestedMoreAdapter(getContext(), chatsModles);
            binding.rvInboxMoreList.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvInboxMoreList.setAdapter(requestedMoreAdapter);
        }
//        binding.rvInboxMoreList.setLayoutManager(new LinearLayoutManager(getContext()));
//        binding.rvInboxMoreList.setAdapter(new RequestedMoreAdapter(getContext(), chatsModles));
    }

    ////////////////////////// Connect and Reject More Adapters //////////////////////////////////

    private void showConnectionAndRelectRecycler() {
        connectAndRejectMoreAdapter = new ConnectAndRejectMoreAdapter(getApplicationContext(), connectAndrejectList, this);
        binding.rvConnectRejectListMore.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvConnectRejectListMore.setAdapter(connectAndRejectMoreAdapter);

//        binding.rvConnectRejectListMore.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        binding.rvConnectRejectListMore.setAdapter(new ConnectAndRejectMoreAdapter(getApplicationContext(), connectAndrejectList, this));
    }

    ////////////////////////// Accept and Reject More Adapters //////////////////////////////////

    private void showRequestRecyclerMore() {
        experinceMessagesRequestMoreAdapter = new ExperinceMessagesRequestMoreAdapter(getApplicationContext(), requestsViewsList, this);
        binding.rvRequestListMore.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvRequestListMore.setAdapter(experinceMessagesRequestMoreAdapter);

//        binding.rvRequestListMore.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        binding.rvRequestListMore.setAdapter(new ExperinceMessagesRequestMoreAdapter(getApplicationContext(), requestsViewsList, this));
    }

    private void MCRSetData() {
        for (int i = 0; i < chatsModles.size(); i++) {
            mcrEventId = chatsModles.get(i).getEventId();
            mcrRequestId = chatsModles.get(i).getRequestUserId();
            mcrEventUserId = chatsModles.get(i).getEventUserId();
        }

    }

    private void callRequestsMessagesListApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading2 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<RequestsResponceModle> call = api.GetRequestsView(access_token, "Request", userId, "All");
        call.enqueue(new Callback<RequestsResponceModle>() {
            @Override
            public void onResponse(Call<RequestsResponceModle> call, Response<RequestsResponceModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    requestsResponceModle = response.body();
                    chatsModles = (ArrayList) requestsResponceModle.getData();
                    initRecycler();
                    MCRSetData();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("No Data Available")) {
                            binding.tvNodaMessa.setVisibility(View.VISIBLE);
                            binding.tvNodaMessa.setText(jObjError.getString("message"));

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
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


    private void setDataExperinceRequest() {
        for (int i = 0; i < requestsViewsList.size(); i++) {
            eREventUserId = requestsViewsList.get(i).getEventUserId();
            erRequestId = requestsViewsList.get(i).getRequestUserId();
            eREventId = requestsViewsList.get(i).getEventId();
        }
    }

    private void callRequestsApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading1 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<RequestsResponceModle> call = api.GetRequestsView(access_token, "Join", userId, "All");
        call.enqueue(new Callback<RequestsResponceModle>() {
            @Override
            public void onResponse(Call<RequestsResponceModle> call, Response<RequestsResponceModle> response) {
                loading1.cancel();
                if (response.isSuccessful()) {
                    requestsResponceModle = response.body();
                    requestsViewsList = (ArrayList) requestsResponceModle.getData();
                    showRequestRecycler();
                    setDataExperinceRequest();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("No Data Available")) {
                            binding.tvNodaAccet.setVisibility(View.VISIBLE);
                            binding.tvNodaAccet.setText(jObjError.getString("message"));

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

    private void setData() {
        for (int i = 0; i < connectAndrejectList.size(); i++) {
            eventUserId = connectAndrejectList.get(i).getUserId();
//            EventsId = connectAndrejectList.get(i).getEventId();
            requestUserId = connectAndrejectList.get(i).getFollowerUserId();
        }
    }

    /////////////// Connect And Reject Api  ////////////////////////////////////
    private void callConnectRejectApi() {
        Api api = ApiClient.getClient().create(Api.class);
        getLoading3 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ConnectAndRejectModle> call = api.GetConnectAndRejectView(access_token, "Request", userId);
        call.enqueue(new Callback<ConnectAndRejectModle>() {
            @Override
            public void onResponse(Call<ConnectAndRejectModle> call, Response<ConnectAndRejectModle> response) {
                getLoading3.cancel();
                if (response.isSuccessful()) {
                    connectAndRejectModle = response.body();
                    connectAndrejectList = (ArrayList) connectAndRejectModle.getData();
                    showRequestMessagesRecycler();
                    setData();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("No Data Available")) {
                            binding.tvNodaRequest.setVisibility(View.VISIBLE);
                            binding.tvNodaRequest.setText(jObjError.getString("message"));

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
            public void onFailure(Call<ConnectAndRejectModle> call, Throwable t) {
                getLoading3.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_more:
                binding.rvConnectRejectListMore.setVisibility(View.VISIBLE);
                binding.rlMore.setVisibility(View.GONE);
                binding.rvInboxList.setVisibility(View.GONE);
                binding.rvConnectRejectList.setVisibility(View.GONE);
                showConnectionAndRelectRecycler();
                break;
            case R.id.rl_moreRequest:
                binding.rlMoreRequest.setVisibility(View.GONE);
                binding.rvRequestList.setVisibility(View.GONE);
                binding.rvRequestListMore.setVisibility(View.VISIBLE);
                showRequestRecyclerMore();
                break;
            case R.id.rl_moreMessages:

                binding.rlMoreMessages.setVisibility(View.GONE);
                binding.rvInboxList.setVisibility(View.GONE);
                binding.rvInboxMoreList.setVisibility(View.VISIBLE);
                RequestRecyclerMessagesMore();
                break;


        }
    }

    /////////////////// Accept and Reject /////////////////////////////////

    private void callRequestAcceptApi() {

        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("request_type", "RequestToJoin");
        body.addProperty("request_user_id", erRequestId);
        body.addProperty("event_id", eREventId);
        body.addProperty("event_user_id", eREventUserId);
        body.addProperty("requesting_status", "Accepted");
        body.addProperty("update_type", "Edit");
        loading = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<RequestAcceptedModle> call = api.PutRequestsAccepted(access_token, "application/json", body);
        call.enqueue(new Callback<RequestAcceptedModle>() {
            @Override
            public void onResponse(Call<RequestAcceptedModle> call, Response<RequestAcceptedModle> response) {
                loading.cancel();
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
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callRequestRejectedApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("request_type", "RequestToJoin");
        body.addProperty("request_user_id", erRequestId);
        body.addProperty("event_id", eREventId);
        body.addProperty("event_user_id", eREventUserId);
        body.addProperty("requesting_status", "Rejected");
        body.addProperty("update_type", "Edit");
        loading = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<RequestAcceptedModle> call = api.PutRequestsAccepted(access_token, "application/json", body);
        call.enqueue(new Callback<RequestAcceptedModle>() {
            @Override
            public void onResponse(Call<RequestAcceptedModle> call, Response<RequestAcceptedModle> response) {
                loading.cancel();
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
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    public void acceptRequest(int position) {
        callRequestAcceptApi();
    }

    public void rejected(int position) {
        callRequestRejectedApi();
    }

    /////////////////// Connection and Reject /////////////////////////////////
    private void callConnectionAcceptApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("request_type", "Follow");
        body.addProperty("request_user_id", eventUserId);
        body.addProperty("event_id", 0);
        body.addProperty("event_user_id", requestUserId);
        body.addProperty("requesting_status", "Accepted");
        body.addProperty("update_type", "Edit");
        loading = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<RequestAcceptedModle> call = api.PutRequestsAccepted(access_token, "application/json", body);
        call.enqueue(new Callback<RequestAcceptedModle>() {
            @Override
            public void onResponse(Call<RequestAcceptedModle> call, Response<RequestAcceptedModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "Your Request Accepted", Toast.LENGTH_SHORT).show();

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
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callConnectionRejectApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("request_type", "Follow");
        body.addProperty("request_user_id", eventUserId);
        body.addProperty("event_id", 0);
        body.addProperty("event_user_id", requestUserId);
        body.addProperty("requesting_status", "Rejected");
        body.addProperty("update_type", "Edit");
        loading = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<RequestAcceptedModle> call = api.PutRequestsAccepted(access_token, "application/json", body);
        call.enqueue(new Callback<RequestAcceptedModle>() {
            @Override
            public void onResponse(Call<RequestAcceptedModle> call, Response<RequestAcceptedModle> response) {
                loading.cancel();
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
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


    public void connectRequest(int position) {
        callConnectionAcceptApi();
    }

    public void ConnectionRejected(int position) {
        callConnectionRejectApi();
    }

    /////////////////// Goto messages List (inbox) /////////////////////////////////

    private void callApiGotoMessagesList() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("request_type", "RequestToMessage");
        body.addProperty("request_user_id", mcrRequestId);
        body.addProperty("event_id", mcrEventId);
        body.addProperty("event_user_id", mcrEventUserId);
        body.addProperty("requesting_status", "Accepted");
        body.addProperty("update_type", "Edit");
        loading = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<RequestAcceptedModle> call = api.PutRequestsAccepted(access_token, "application/json", body);
        call.enqueue(new Callback<RequestAcceptedModle>() {
            @Override
            public void onResponse(Call<RequestAcceptedModle> call, Response<RequestAcceptedModle> response) {
                loading.cancel();
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
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    @Override
    public void gotoMessagesList(int position) {
        callApiGotoMessagesList();
    }
}
