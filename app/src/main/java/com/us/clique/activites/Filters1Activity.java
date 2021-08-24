package com.us.clique.activites;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.FilterAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.CalenderResponceModle;
import com.us.clique.databinding.ActivityFilters1Binding;
import com.us.clique.modle.FiltersModle;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Filters1Activity extends AppCompatActivity implements View.OnClickListener {
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityFilters1Binding binding;
    FilterAdapter filterAdapter;
    ArrayList<FiltersModle> filtersModles = new ArrayList<>();
    Boolean check1 = false, check2 = false, check3 = false, check4 = false, check5 = false;
    ProgressDialog loading3;
    private Context context;
    String access_token, userId, reciverId;
    UserSessionManager session;
    int count = 0;
    CalenderResponceModle calenderResponceModle;
    ArrayList<CalenderResponceModle.Datum> calenderModles = new ArrayList<>();
    ArrayList<String> checkBoxList = new ArrayList<>();
    String pastck = "", upCommingck = "", myCk = "", ckJoinedCk = "", ckSaveTextCk = "", checkBoxHeader;
    CalenderActivity calenderActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters1);

        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(Filters1Activity.this,
                R.layout.activity_filters1);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setFilters1(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        OnlineUserStatus onlineUserStatus = new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();

        binding.ivOnBackPress.setOnClickListener(this);
        binding.btnApply.setOnClickListener(this);

        checkboxValues();

        //RecyclerCheck();
    }
   /* private void RecyclerCheck(){
        filtersModles.add(new FiltersModle("Past Experiences",false));
        filtersModles.add(new FiltersModle("Upcoming Experiences",false));
        filtersModles.add(new FiltersModle("My Experiences",false));
        filtersModles.add(new FiltersModle("Joined Experiences",false));
        filtersModles.add(new FiltersModle("Saved Experiences",false));

        filterAdapter = new FilterAdapter(this,filtersModles);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.rvCheckList.setLayoutManager(mLayoutManager);
        binding.rvCheckList.setItemAnimator(new DefaultItemAnimator());
        binding.rvCheckList.setAdapter(filterAdapter);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void checkboxValues() {

        binding.ckPastEx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.ckPastEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.tab_text)));
                    binding.ckPastEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_text));
                    pastck = "Past Experiences";
                    checkBoxList.add(pastck);
                    binding.btnApply.setEnabled(true);
                    binding.btnApply.setBackgroundResource(R.drawable.button_background);
                }
                if (!isChecked) {
                    if (!(binding.ckJoinedEx.isChecked() || binding.ckMyEx.isChecked() ||
                            binding.ckSavedEx.isChecked() || binding.ckUpcomingEx.isChecked())) {
                        binding.ckPastEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.check_box2)));
                        binding.ckPastEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.check_box));
                        binding.ckJoinedEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.check_box2)));
                        binding.ckJoinedEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.check_box));
                        binding.ckMyEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.check_box2)));
                        binding.ckMyEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.check_box));
                        binding.ckSavedEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.check_box2)));
                        binding.ckSavedEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.check_box));
                        binding.ckUpcomingEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.check_box2)));
                        binding.ckUpcomingEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.check_box));
                        checkBoxList.remove(pastck);
                        binding.btnApply.setEnabled(false);
                        binding.btnApply.setBackgroundResource(R.drawable.button_bg_enable);
                    }
                }
            }
        });
        binding.ckUpcomingEx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    binding.ckUpcomingEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.tab_text)));
                    binding.ckUpcomingEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_text));
                    upCommingck = "Upcoming Experiences";
                    checkBoxList.add(upCommingck);
                }
                if (!isChecked) {
                    binding.ckUpcomingEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.check_box)));
                    binding.ckUpcomingEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.check_box));
                    checkBoxList.remove(upCommingck);
                }
                if (binding.ckJoinedEx.isChecked() || binding.ckMyEx.isChecked() ||binding.ckPastEx.isChecked() ||
                        binding.ckSavedEx.isChecked() || binding.ckUpcomingEx.isChecked()) {
                    binding.btnApply.setEnabled(true);
                    binding.btnApply.setBackgroundResource(R.drawable.button_background);
                } else {
                    binding.btnApply.setEnabled(false);
                    binding.btnApply.setBackgroundResource(R.drawable.button_bg_enable);
                }
            }
        });
        binding.ckMyEx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.ckMyEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.tab_text)));
                    binding.ckMyEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_text));
                    myCk = "My Experiences";
                    checkBoxList.add(myCk);
                    binding.btnApply.setEnabled(true);
                    binding.btnApply.setBackgroundResource(R.drawable.button_background);
                } else {
                    binding.ckMyEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.check_box2)));
                    binding.ckMyEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.check_box));
                    checkBoxList.remove(myCk);
                    binding.btnApply.setEnabled(false);
                    binding.btnApply.setBackgroundResource(R.drawable.button_bg_enable);
                }
            }
                    });


        binding.ckJoinedEx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.ckJoinedEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.tab_text)));
                    binding.ckJoinedEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_text));
                    ckJoinedCk = "Joined Experiences";
                    checkBoxList.add(ckJoinedCk);
                    binding.btnApply.setEnabled(true);
                    binding.btnApply.setBackgroundResource(R.drawable.button_background);
                }else
                {

                    binding.ckJoinedEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.check_box)));
                    binding.ckJoinedEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.check_box));
                    checkBoxList.remove(ckJoinedCk);
                }
               if (binding.ckJoinedEx.isChecked() || binding.ckMyEx.isChecked() ||binding.ckPastEx.isChecked() ||
                        binding.ckSavedEx.isChecked() || binding.ckUpcomingEx.isChecked()) {
                    binding.btnApply.setEnabled(true);
                    binding.btnApply.setBackgroundResource(R.drawable.button_background);
                } else {
                    binding.btnApply.setEnabled(false);
                    binding.btnApply.setBackgroundResource(R.drawable.button_bg_enable);

                }
            }
        });
        binding.ckSavedEx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    binding.ckSavedEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.tab_text)));
                    binding.ckSavedEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_text));
                    ckSaveTextCk = "Saved Experiences";
                    checkBoxList.add(ckSaveTextCk);
                }
                if (!isChecked) {
                    binding.ckSavedEx.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(getApplicationContext(), R.color.check_box)));
                    binding.ckSavedEx.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.check_box));
                    checkBoxList.remove(ckSaveTextCk);
                }
                if (binding.ckJoinedEx.isChecked() || binding.ckMyEx.isChecked() ||binding.ckPastEx.isChecked() ||
                        binding.ckSavedEx.isChecked() || binding.ckUpcomingEx.isChecked()) {
                    binding.btnApply.setEnabled(true);
                    binding.btnApply.setBackgroundResource(R.drawable.button_background);
                } else {
                    binding.btnApply.setEnabled(false);
                    binding.btnApply.setBackgroundResource(R.drawable.button_bg_enable);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_onBackPress:
                onBackPressed();

                break;
            case R.id.btn_apply:
                checkBoxHeader = checkBoxList.toString().replace("[", "")
                        .replace("]", "").replace(", ", ",  ");


//                NetworkConnection networkConnection = new NetworkConnection(getApplicationContext());
//                if (networkConnection.isInternetAvailable()) {
                    callCalnderApi();
//                } else {
//                    setContentView(R.layout.activity_networksignal);
//                }
//                break;


        }
    }

    private void callCalnderApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading3 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<CalenderResponceModle> call = api.GetFilterCalender(access_token, "application/json", "All", "All", "All", "EVENT_CAL", userId, checkBoxHeader);
        call.enqueue(new Callback<CalenderResponceModle>() {
            @Override
            public void onResponse(Call<CalenderResponceModle> call, Response<CalenderResponceModle> response) {
                loading3.cancel();
                if (response.isSuccessful()) {
                    calenderResponceModle = response.body();
                    calenderModles = (ArrayList) calenderResponceModle.getData();
                    Log.d("value", String.valueOf(calenderModles));
                    Intent i = new Intent(getApplicationContext(), CalenderActivity.class);
                    i.putExtra("list", (Serializable) calenderResponceModle.getData());
                    startActivity(i);
                    finish();

                    // InitRecycler();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("No Data Available")) {
//                            binding.tvNoData.setVisibility(View.VISIBLE);
//                            binding.tvNoData.setText(jObjError.getString("message"));

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
            public void onFailure(Call<CalenderResponceModle> call, Throwable t) {
                loading3.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


}