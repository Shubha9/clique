package com.us.clique.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.CalenderAdapter;
import com.us.clique.adapter.CalenderNestedAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.CalenderResponceModle;
import com.us.clique.databinding.ActivityCalenderBinding;
import com.us.clique.modle.CalenderNestedModle;
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

public class CalenderActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityCalenderBinding binding;

    CalenderAdapter calenderAdapter;
    ArrayList<CalenderNestedModle> nestedModles = new ArrayList<>();
    CalenderNestedAdapter nestedAdapter;
    ProgressDialog loading3;
    String access_token ,userId,reciverId;
    UserSessionManager session;
    CalenderResponceModle calenderResponceModle;
    ArrayList<CalenderResponceModle.Datum> calenderModles;
    ArrayList<CalenderResponceModle.Datum> filterList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(CalenderActivity.this,
                R.layout.activity_calender);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setCalender(bottomNavigationViewModel);
        //InitRecycler();
        binding.ivOnBackPress.setOnClickListener(this);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId=session.getUserId();
        OnlineUserStatus onlineUserStatus=new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
        calenderModles = (ArrayList<CalenderResponceModle.Datum>) getIntent().getSerializableExtra("list");
        if(calenderModles==null)
        {
                callCalnderApi();
       }else
        {
            InitRecycler();
        }
        binding.rlFilterHistory.setOnClickListener(this);

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {

                doFilter(s);

            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void doFilter(CharSequence s) {
        if (calenderAdapter != null)
            if (calenderAdapter.getItemCount() > 0) {
                calenderAdapter.searchString(s);
            }

    }
        private void InitRecycler(){
      /*  calenderModles.add(new CalenderModle("6 April, Today"));
        calenderModles.add(new CalenderModle("6 April, Today"));*/
        if (calenderModles != null){
            binding.tvNoData.setVisibility(View.GONE);
            binding.rvDate.setVisibility(View.VISIBLE);
            calenderAdapter = new CalenderAdapter(getApplicationContext(),calenderModles);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvDate.setLayoutManager(layoutManager);
            binding.rvDate.setAdapter(calenderAdapter);
        }else {
            binding.tvNoData.setVisibility(View.VISIBLE);
            binding.rvDate.setVisibility(View.GONE);
        }

    }

    @Override
    public void finish() {
        super.finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_onBackPress:
                onBackPressed();
                break;
            case R.id.rl_filterHistory:
                Intent i = new Intent(this,Filters1Activity.class);
                startActivity(i);
                break;
        }
    }

    private void callCalnderApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading3 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<CalenderResponceModle> call = api.GetCalender(access_token,"application/json","All","All","All","EVENT_CAL",userId,"All");
        call.enqueue(new Callback<CalenderResponceModle>() {
            @Override
            public void onResponse(Call<CalenderResponceModle> call, Response<CalenderResponceModle> response) {
                loading3.cancel();
                if (response.isSuccessful()) {
                    calenderResponceModle = response.body();
                    calenderModles = (ArrayList) calenderResponceModle.getData();
                    InitRecycler();
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
            public void onFailure(Call<CalenderResponceModle> call, Throwable t) {
                loading3.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


    public void setFilterData(ArrayList<CalenderResponceModle.Datum> calenderModles) {
        if (calenderModles != null){
            binding.tvNoData.setVisibility(View.GONE);
            binding.rvDate.setVisibility(View.VISIBLE);
            calenderAdapter = new CalenderAdapter(getApplicationContext(),calenderModles);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvDate.setLayoutManager(layoutManager);
            binding.rvDate.setAdapter(calenderAdapter);
        }else {
//            binding.tvNoData.setVisibility(View.VISIBLE);
            binding.rvDate.setVisibility(View.GONE);
        }
    }
}