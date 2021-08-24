package com.us.clique.bottomNavigation.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.ChatsAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.ChatIndexModle;
import com.us.clique.databinding.FragmentChatsBinding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;

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

public class ChatsFragment extends Fragment {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentChatsBinding binding;
    ChatIndexModle chatIndexModle;
    ArrayList<ChatIndexModle.Datum> chatsModles = new ArrayList<>();
    ChatsAdapter chatsAdapter;
    ProgressDialog loading5;
    UserSessionManager session;
    String access_token = " ",userId="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setChatsFragment(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
//        NetworkConnection networkConnection=new NetworkConnection(getApplicationContext());
//        if(networkConnection.isInternetAvailable())
//        {
            callChatIndexApi();

//        }else
//        {
//            View v = inflater.inflate(R.layout.activity_networksignal, container, false);
//            return v;
//        }
        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                    chatsAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //messageAdapter.setFilter((ArrayList<MessageModule>) messageModuleList,editable.toString());


            }
        });
       // initRecycler();
        return binding.getRoot();
    }
    private void initRecycler(){
        if (chatsModles!= null){
            binding.rvInboxList.setVisibility(View.VISIBLE);
            binding.tvNoData.setVisibility(View.GONE);
            chatsAdapter = new ChatsAdapter(getContext(),chatsModles);
            binding.rvInboxList.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.rvInboxList.setAdapter(chatsAdapter);
        }else {
            binding.rvInboxList.setVisibility(View.GONE);
        }

    }

    private void callChatIndexApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading5 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ChatIndexModle> call = api.GetChatIndexModle(access_token,"DistinctUsers",userId,"All","All");
        call.enqueue(new Callback<ChatIndexModle>() {
            @Override
            public void onResponse(Call<ChatIndexModle> call, Response<ChatIndexModle> response) {
                loading5.cancel();
                if (response.isSuccessful()) {
                    chatIndexModle = response.body();
                    chatsModles = (ArrayList) chatIndexModle.getData();
                    initRecycler();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("No Data Available"))
                        {
                            binding.tvNoData.setVisibility(View.VISIBLE);
                            binding.tvNoData.setText(jObjError.getString("message"));

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
            public void onFailure(Call<ChatIndexModle> call, Throwable t) {
                loading5.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

}
