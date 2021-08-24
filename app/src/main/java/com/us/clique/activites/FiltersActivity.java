package com.us.clique.activites;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.SessionManagers.ListSaveManager;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.us.clique.databinding.ActivityFiltersBinding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.us.clique.utils.NetworkConnection;
import com.mohammedalaa.seekbar.DoubleValueSeekBarView;
import com.mohammedalaa.seekbar.OnDoubleValueSeekBarChangeListener;
import com.mohammedalaa.seekbar.OnRangeSeekBarChangeListener;
import com.mohammedalaa.seekbar.RangeSeekBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FiltersActivity extends AppCompatActivity implements View.OnClickListener {

    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityFiltersBinding binding;
    DatePickerDialog datepicker;
    TimePickerDialog timePickerDialog;
    ProgressDialog loading;
    UserSessionManager session;
    ListSaveManager listSaveManager;
    String amPm;
    int movie=0,outddor=0,sports=0,art=0,food=0,music=0;
    String access_token = "", userId = "", location, age, cat_type, gender="female", event_date, event_time,expmax,expmin;
    ArrayList cat=new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(FiltersActivity.this,
                R.layout.activity_filters);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setFilters(bottomNavigationViewModel);
        listSaveManager=new ListSaveManager(getApplicationContext());
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        OnlineUserStatus onlineUserStatus=new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
        binding.tvApply.setOnClickListener(this);
        binding.ivOnBackPress.setOnClickListener(this);
//        RangeSeekBarView rangeSeekbar = (RangeSeekBarView) findViewById(R.id.range_seekbar);
//        DoubleValueSeekBarView doubleValueRangeSeekbar = findViewById(R.id.double_range_seekbar);
//        binding.doubleRangeSeekbar.setCurrentMinValue(18);
//        binding.doubleRangeSeekbar.setCurrentMaxValue(100);
        binding.ageRangeSeekbar.getCurrentMinValue();
        binding.ageRangeSeekbar.getCurrentMaxValue();
        binding.tvType.setOnClickListener(this);
        binding.tvMovies.setOnClickListener(this);
        binding.tvMusic.setOnClickListener(this);
        binding.tvOutdoor.setOnClickListener(this);
        binding.tvSports.setOnClickListener(this);
        binding.tvArt.setOnClickListener(this);
        binding.tvFood.setOnClickListener(this);
        binding.btMale.setOnClickListener(this);
        binding.btFemale.setOnClickListener(this);
        binding.ageRangeSeekbar.setOnRangeSeekBarViewChangeListener(new OnDoubleValueSeekBarChangeListener() {
            @Override
            public void onValueChanged(@Nullable DoubleValueSeekBarView seekBar, int min, int max, boolean fromUser) {
                String maxval = String.valueOf(max);
                String minval= String.valueOf(min);
                age=minval+","+maxval;

            }

            @Override
            public void onStartTrackingTouch(@Nullable DoubleValueSeekBarView seekBar, int min, int max) {

            }

            @Override
            public void onStopTrackingTouch(@Nullable DoubleValueSeekBarView seekBar, int min, int max) {

            }
        });
//        rangeSeekbar.setAnimated(true, 3000L);
//        rangeSeekbar.setCurrentValue(30);
//        rangeSeekbar.getCurrentValue();
        binding.rangeSeekbarLocation.setOnRangeSeekBarViewChangeListener(new OnRangeSeekBarChangeListener() {
            @Override
            public void onProgressChanged(@Nullable RangeSeekBarView seekBar, int progress, boolean fromUser) {
                location = String.valueOf(progress);
            }

            @Override
            public void onStartTrackingTouch(@Nullable RangeSeekBarView seekBar, int progress) {

            }

            @Override
            public void onStopTrackingTouch(@Nullable RangeSeekBarView seekBar, int progress) {

            }
        });
        binding.expRangeSeekbar.getCurrentMinValue();
        binding.expRangeSeekbar.getCurrentMaxValue();
        binding.expRangeSeekbar.setOnRangeSeekBarViewChangeListener(new OnDoubleValueSeekBarChangeListener() {
            @Override
            public void onValueChanged(@Nullable DoubleValueSeekBarView seekBar, int min, int max, boolean fromUser) {
                expmax=String.valueOf(max);
                expmin=String.valueOf(min);
            }

            @Override
            public void onStartTrackingTouch(@Nullable DoubleValueSeekBarView seekBar, int min, int max) {

            }

            @Override
            public void onStopTrackingTouch(@Nullable DoubleValueSeekBarView seekBar, int min, int max) {

            }
        });


        binding.rlStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(FiltersActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
                               // String dateString = sdf.format(cldr.getTime());
//                                binding.tvStartDate.setText(dateString);
//
//                                event_date=dateString;

                                binding.tvStartDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                                event_date = cldr.get(Calendar.YEAR) + "-" + (monthOfYear + 1) + "-" + day;
                                //   binding.tvStartDate.setText(cldr.get(Calendar.MONTH)+ "/" + (monthOfYear + 1));
                            }
                        }, year, month, day);
                datepicker.show();
            }
        });
        binding.rlEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);
//                int year = cldr.get(Calendar.YEAR);

                // date picker dialog
                timePickerDialog = new TimePickerDialog(FiltersActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        boolean isPM = (selectedHour >= 12);
                        if(selectedHour>=12){
                            amPm=" PM";
                        } else {
                            amPm = " AM";
                        }
                        binding.tvTime.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute,isPM  ? "PM" : "AM"));
                        event_time=binding.tvTime.getText().toString();
                    }
                }, hour, minute, false);//Yes 24 hour time

                timePickerDialog.show();
            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_apply:

                NetworkConnection networkConnection=new NetworkConnection(getApplicationContext());
                if(networkConnection.isInternetAvailable())
                {
                    callFilterApi();
                }else
                {
//                    setContentView(R.layout.activity_networksignal);
                }
                break;

            case R.id.iv_onBackPress:
                onBackPressed();
                break;
            case R.id.bt_male:
                binding.btMale.setBackgroundResource(R.drawable.message_btn_bg);
                binding.btFemale.setBackgroundResource(R.drawable.learnfun_btn_bg);
                binding.btMale.setTextColor(getResources().getColor(R.color.white));
                binding.btFemale.setTextColor(getResources().getColor(R.color.blue));
                gender = "male";
                break;
            case R.id.bt_female:
                binding.btFemale.setBackgroundResource(R.drawable.message_btn_bg);
                binding.btMale.setBackgroundResource(R.drawable.learnfun_btn_bg);
                binding.btFemale.setTextColor(getResources().getColor(R.color.white));
                binding.btMale.setTextColor(getResources().getColor(R.color.blue));
                gender = "female";
                break;
            case R.id.tv_movies:
                if(movie==0)
                {
                    cat.add(1);
                    movie=1;
                    binding.tvMovies.setBackground(getResources().getDrawable(R.drawable.movies_text_bg));
                }else
                {
                    cat.remove(new Integer(1));
                    movie=0;
                    binding.tvMovies.setBackground(getResources().getDrawable(R.drawable.fun_movie_unselect));
                }

                break;
            case R.id.tv_outdoor:

                if(outddor==0)
                {
                    cat.add(2);
                    outddor=1;
                    binding.tvOutdoor.setBackground(getResources().getDrawable(R.drawable.outdoor_text_bg));
                }else
                {
                    cat.remove(new Integer(2));
                    outddor=0;
                    binding.tvOutdoor.setBackground(getResources().getDrawable(R.drawable.unselectoutddor));
                }

                break;
            case R.id.tv_sports:
                if(sports==0)
                {
                    cat.add(3);
                    sports=1;
                    binding.tvSports.setBackground(getResources().getDrawable(R.drawable.sports_text_bg));
                }else
                {
                    cat.remove(new Integer(3));
                    sports=0;
                    binding.tvSports.setBackground(getResources().getDrawable(R.drawable.music_btn_bg));
                }
                break;
            case R.id.tv_art:
                if(art==0)
                {
                    cat.add(4);
                    art=1;
                    binding.tvArt.setBackground(getResources().getDrawable(R.drawable.art_text_bg));
                }else
                {
                    cat.remove(new Integer(4));
                    art=0;
                    binding.tvArt.setBackground(getResources().getDrawable(R.drawable.art_unselect));
                }
                break;
            case R.id.tv_food:
                if(food==0)
                {
                    cat.add(5);
                    food=1;
                    binding.tvFood.setBackground(getResources().getDrawable(R.drawable.food_select_bg));
                }else
                {
                    cat.remove(new Integer(5));
                    food=0;
                    binding.tvFood.setBackground(getResources().getDrawable(R.drawable.food_btn_bg));
                }

                break;
            case R.id.tv_music:
                if(music==0)
                {
                    cat.add(6);
                    music=1;
                    binding.tvMusic.setBackground(getResources().getDrawable(R.drawable.sports_text_bg));
                }else
                {
                    cat.remove(new Integer(6));
                    music=0;
                    binding.tvMusic.setBackground(getResources().getDrawable(R.drawable.music_btn_bg));
                }

                break;
        }
    }

    private void callFilterApi() {
        cat_type= TextUtils.join(", ", cat);
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading......", "wait....", false, false);
        Call<ExperinceModle> call = api.filter(access_token, "application/json", "All", "All", "All", "EVENT_FILTER", userId, location, age, gender, cat_type, expmin,expmax, event_date, event_time);
        call.enqueue(new Callback<ExperinceModle>() {
            @Override
            public void onResponse(Call<ExperinceModle> call, Response<ExperinceModle> response) {
                loading.cancel();
                List<ExperinceModle.Datum> data=response.body().getData();
                listSaveManager.setList((ArrayList<ExperinceModle.Datum>) data);
                Intent intent=new Intent(getApplicationContext(),BottomNavigationActivity.class);
                startActivity(intent);

            }

            @Override
            public void onFailure(Call<ExperinceModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());
                Toast.makeText(getApplicationContext(),"No Data Available",Toast.LENGTH_SHORT).show();
            }
        });
    }
}