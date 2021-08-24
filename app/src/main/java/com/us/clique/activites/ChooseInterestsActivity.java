package com.us.clique.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.ChooseYourInterestModle;

import com.us.clique.databinding.ActivityChooseInterestsBinding;
import com.us.clique.joinNow.JoinNowModle;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.android.material.chip.Chip;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseInterestsActivity extends AppCompatActivity implements View.OnClickListener {

    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityChooseInterestsBinding binding;
    ProgressDialog loading;
    UserSessionManager session;
    String access_token = "";
    ArrayList<String> movielist = new ArrayList<>();
    ArrayList<String> outdoor = new ArrayList<>();
    ArrayList<String> sportslist = new ArrayList<>();
    ArrayList<String> artslist = new ArrayList<>();
    ArrayList<String> foodlist = new ArrayList<>();
    ArrayList<String> musiclist = new ArrayList<>();
    ArrayList<String> selectedlist = new ArrayList<>();
    ChooseYourInterestModle yourInterestModle;
    int j,l;
    ArrayList<String> categoriesList = new ArrayList<>();
    ArrayList<String> catIdlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(ChooseInterestsActivity.this,
                R.layout.activity_choose_interests);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        Intent intent = getIntent();
        l = intent.getIntExtra("root", 0);
        if ( (ArrayList<String>) getIntent().getStringArrayListExtra("selected")!=null){
            selectedlist = (ArrayList<String>) getIntent().getStringArrayListExtra("selected");
        }
        if ((ArrayList<String>) getIntent().getStringArrayListExtra("selectedId")!=null){
            categoriesList = (ArrayList<String>) getIntent().getStringArrayListExtra("selectedId");

        }
        OnlineUserStatus onlineUserStatus = new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
        // Log.d("access_token",access_token);
        // access_token="eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.IjEzIg.oGvFb78VEijTxwExo7VZNvnvRjrOjD3UCmt-hQRrknI";
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setChoseyour(bottomNavigationViewModel);
        callApi();

        binding.rlArt.setOnClickListener(this);
        binding.rlFood.setOnClickListener(this);
        binding.rlMovie.setOnClickListener(this);
        binding.rlMusic.setOnClickListener(this);
        binding.rlOutdoor.setOnClickListener(this);
        binding.rlSport.setOnClickListener(this);
        binding.ivOnBackPress.setOnClickListener(this);
        binding.btnNext.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void setMovieTags() {


        for (int index = 0; index < movielist.size(); index++) {
            final String tagName = movielist.get(index);
            final Chip chip = new Chip(this);
//            chip.setChipIcon(getResources().getDrawable(R.drawable.ic_information));
            chip.setCheckable(false);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 0,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, 0, paddingDp, 0);

//            chip.setPadding(paddingDp, 0, paddingDp, 0);
            chip.setChipStartPadding(0);
            chip.setChipEndPadding(0);
            chip.setText(tagName);
            chip.setTextSize(12f);
            chip.setCloseIconEnabled(false);
            chip.setChipStrokeWidth(2);
            chip.setChipCornerRadius(10f);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            for (int i = 0; i < selectedlist.size(); i++) {
                if (chip.getText().equals(selectedlist.get(i))) {
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.movie_bg)));
                    chip.setChecked(true);
                    break;
                } else {
                    chip.setChipBackgroundColorResource(R.color.white);
                }
            }

            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        String data = chip.getText().toString();
                        for (int k = 0; k < selectedlist.size(); k++) {
                            if (data.equals(selectedlist.get(k))) {
                                selectedlist.add(data);
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.movie_bg)));
                        for (int i = 0; i < yourInterestModle.getData().getCategory().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(0).getSubCat().get(i).getSubCategoryName().equals(data)) {
                                categoriesList.add(yourInterestModle.getData().getCategory().get(0).getSubCat().get(i).getSkSubCategoryId());
                            }
                        }

//                        selecteddata.add(data);
//                        Toast.makeText(getApplicationContext(), chip.getText().toString(), Toast.LENGTH_LONG).show();
                    } else {

//                        for (int k = 0; k < selectedlist.size(); k++) {
//                            if (chip.getText().equals(selectedlist.get(k))) {
//                                selectedlist.remove(k);
//                                break;
//                            }
//                        }


                        for (int i = 0; i < yourInterestModle.getData().getCategory().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(0).getSubCat().get(i).getSubCategoryName().equals(chip.getText().toString())) {
                                categoriesList.remove(yourInterestModle.getData().getCategory().get(0).getSubCat().get(i).getSkSubCategoryId());
                                break;

                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                    }

                }
            });


            binding.tagGroup.addView(chip);

        }


    }

    private void setOutTags() {
        for (int index = 0; index < outdoor.size(); index++) {
            final String tagName = outdoor.get(index);
            final Chip chip = new Chip(this);
//            chip.setChipIcon(getResources().getDrawable(R.drawable.ic_information));
            chip.setCheckable(false);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 20,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, 0, paddingDp, 0);
            //chip.setPadding(20, 20, 20, 20);
            chip.setText(tagName);
            chip.setTextSize(12f);
            chip.setCloseIconEnabled(false);
            chip.setChipStrokeWidth(2);
            chip.setChipCornerRadius(10f);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            for (int i = 0; i < selectedlist.size(); i++) {
                if (chip.getText().equals(selectedlist.get(i))) {
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.outdoor_bg)));
                    break;
                } else {
                    chip.setChipBackgroundColorResource(R.color.white);
                }
            }
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        String data = chip.getText().toString();
                        for (int k = 0; k < selectedlist.size(); k++) {
                            if (data.equals(selectedlist.get(k))) {
                                selectedlist.add(data);
                                break;
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.outdoor_bg)));
                        for (int i = 0; i < yourInterestModle.getData().getCategory().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(1).getSubCat().get(i).getSubCategoryName().equals(data)) {
                                categoriesList.add(yourInterestModle.getData().getCategory().get(1).getSubCat().get(i).getSkSubCategoryId());
                            }
                        }

//                        selecteddata.add(data);
                    } else {
                        for (int k = 0; k < selectedlist.size(); k++) {
                            if (chip.getText().equals(selectedlist.get(k))) {
                                selectedlist.remove(k);
                                break;
                            }
                        }
                        for (int i = 0; i < yourInterestModle.getData().getCategory().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(1).getSubCat().get(i).getSubCategoryName().equals(chip.getText().toString())) {
                                categoriesList.remove(yourInterestModle.getData().getCategory().get(1).getSubCat().get(i).getSkSubCategoryId());
                                break;
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                    }

                }
            });


            binding.cgOutdoor.addView(chip);


        }

    }

    private void setSportsTags() {
        for (int index = 0; index < sportslist.size(); index++) {
            final String tagName = sportslist.get(index);
            final Chip chip = new Chip(this);
//            chip.setChipIcon(getResources().getDrawable(R.drawable.ic_information));
            chip.setCheckable(false);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 10,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, 0, paddingDp, 0);
            chip.setText(tagName);
            chip.setTextSize(12f);
            chip.setCloseIconEnabled(false);
            chip.setChipStrokeWidth(2);
            chip.setChipCornerRadius(10f);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            for (int i = 0; i < selectedlist.size(); i++) {
                if (chip.getText().equals(selectedlist.get(i))) {
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.sports_bg)));
                    break;
                } else {
                    chip.setChipBackgroundColorResource(R.color.white);
                }
            }
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        String data = chip.getText().toString();
                        for (int k = 0; k < selectedlist.size(); k++) {
                            if (data.equals(selectedlist.get(k))) {
                                selectedlist.add(data);
                                break;
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.sports_bg)));
                        for (int i = 0; i < yourInterestModle.getData().getCategory().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(2).getSubCat().get(i).getSubCategoryName().equals(data)) {
                                categoriesList.add(yourInterestModle.getData().getCategory().get(2).getSubCat().get(i).getSkSubCategoryId());
                            }
                        }

//                        selecteddata.add(data);
                    } else {
//                        for (int k = 0; k < selectedlist.size(); k++) {
//                            if (chip.getText().equals(categoriesList.get(k))) {
//                                categoriesList.remove(k);
//                                break;
//                            }
//                        }

                        String name=chip.getText().toString();
                        for (int i = 0; i < yourInterestModle.getData().getCategory().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(2).getSubCat().get(i).getSubCategoryName().equals(name)) {
                                categoriesList.remove(yourInterestModle.getData().getCategory().get(2).getSubCat().get(i).getSkSubCategoryId());
                                break;
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                    }

                }
            });


            binding.cgSports.addView(chip);


        }
    }

    private void setArtTags() {
        for (int index = 0; index < artslist.size(); index++) {
            final String tagName = artslist.get(index);
            final Chip chip = new Chip(this);
//            chip.setChipIcon(getResources().getDrawable(R.drawable.ic_information));
            chip.setCheckable(false);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 0,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, 0, paddingDp, 0);
            chip.setText(tagName);
            chip.setTextSize(12f);
            chip.setCloseIconEnabled(false);
            chip.setChipStrokeWidth(2);
            chip.setChipCornerRadius(10f);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            for (int i = 0; i < selectedlist.size(); i++) {
                if (chip.getText().equals(selectedlist.get(i))) {
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.art_bg)));
                    break;
                } else {
                    chip.setChipBackgroundColorResource(R.color.white);
                }
            }
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        String data = chip.getText().toString();
                        for (int k = 0; k < selectedlist.size(); k++) {
                            if (data.equals(selectedlist.get(k))) {
                                selectedlist.add(data);
                                break;
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.art_bg)));
                        for (int i = 0; i < yourInterestModle.getData().getCategory().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(3).getSubCat().get(i).getSubCategoryName().equals(data)) {
                                categoriesList.add(yourInterestModle.getData().getCategory().get(3).getSubCat().get(i).getSkSubCategoryId());
                            }
                        }

//                        selecteddata.add(data);
                    } else {
//                        for (int k = 0; k < selectedlist.size(); k++) {
//                            if (chip.getText().equals(selectedlist.get(k))) {
//                                selectedlist.remove(k);
//                                break;
//                            }
//                        }
                        for (int i = 0; i < yourInterestModle.getData().getCategory().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(3).getSubCat().get(i).getSubCategoryName().equals(chip.getText().toString())) {
                                categoriesList.remove(yourInterestModle.getData().getCategory().get(3).getSubCat().get(i).getSkSubCategoryId());
                                break;
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                    }

                }
            });


            binding.cgArts.addView(chip);

        }

    }

    private void setFoodTags() {
        for (int index = 0; index < foodlist.size(); index++) {
            final String tagName = foodlist.get(index);
            final Chip chip = new Chip(this);
//            chip.setChipIcon(getResources().getDrawable(R.drawable.ic_information));
            chip.setCheckable(false);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 20,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, 0, paddingDp, 0);
            // chip.setPadding(20, 20, 20, 20);
            chip.setText(tagName);
            chip.setTextSize(12f);
            chip.setCloseIconEnabled(false);
            chip.setChipStrokeWidth(2);
            chip.setChipCornerRadius(10f);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            for (int i = 0; i < selectedlist.size(); i++) {
                if (chip.getText().equals(selectedlist.get(i))) {
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.food_bg)));
                    break;
                } else {
                    chip.setChipBackgroundColorResource(R.color.white);
                }
            }
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        String data = chip.getText().toString();
                        for (int k = 0; k < selectedlist.size(); k++) {
                            if (data.equals(selectedlist.get(k))) {
                                selectedlist.add(data);
                                break;
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.food_bg)));
                        for (int i = 0; i < yourInterestModle.getData().getCategory().get(4).getSubCat().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(4).getSubCat().get(i).getSubCategoryName().equals(data)) {
                                categoriesList.add(yourInterestModle.getData().getCategory().get(4).getSubCat().get(i).getSkSubCategoryId());
                            }
                        }

//                        selecteddata.add(data);
                    } else {
//                        for (int k = 0; k < selectedlist.size(); k++) {
//                            if (chip.getText().equals(selectedlist.get(k))) {
//                                selectedlist.remove(k);
//                                break;
//                            }
//                        }
                        for (int i = 0; i < yourInterestModle.getData().getCategory().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(4).getSubCat().get(i).getSubCategoryName().equals(chip.getText().toString())) {
                                categoriesList.remove(yourInterestModle.getData().getCategory().get(4).getSubCat().get(i).getSkSubCategoryId());
                                break;
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));

                    }

                }
            });


            binding.cgFood.addView(chip);

        }

    }

    private void setMusicTags() {
        for (int index = 0; index < musiclist.size(); index++) {
            final String tagName = musiclist.get(index);
            final Chip chip = new Chip(this);
//            chip.setChipIcon(getResources().getDrawable(R.drawable.ic_information));
            chip.setCheckable(false);
            int paddingDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 20,
                    getResources().getDisplayMetrics()
            );
            chip.setPadding(paddingDp, 0, paddingDp, 0);

            //  chip.setPadding(20, 20, 20, 20);
            chip.setText(tagName);
            chip.setTextSize(12f);
            chip.setCloseIconEnabled(false);
            chip.setChipStrokeWidth(2);
            chip.setChipCornerRadius(10f);
            chip.setCheckable(true);
            chip.setCheckedIconVisible(false);
            chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
            chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            for (int i = 0; i < selectedlist.size(); i++) {
                if (chip.getText().equals(selectedlist.get(i))) {
                    chip.setChecked(true);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.music_bg)));
                    break;
                } else {
                    chip.setChipBackgroundColorResource(R.color.white);
                }
            }
            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        String data = chip.getText().toString();
                        for (int k = 0; k < selectedlist.size(); k++) {
                            if (data.equals(selectedlist.get(k))) {
                                selectedlist.add(data);
                                break;
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.music_bg)));
                        for (int i = 0; i < yourInterestModle.getData().getCategory().get(5).getSubCat().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(5).getSubCat().get(i).getSubCategoryName().equals(data)) {
                                categoriesList.add(yourInterestModle.getData().getCategory().get(5).getSubCat().get(i).getSkSubCategoryId());
                            }
                        }

//                        selecteddata.add(data);
                    } else {
//                        for (int k = 0; k < selectedlist.size(); k++) {
//                            if (chip.getText().equals(selectedlist.get(k))) {
//                                selectedlist.remove(k);
//                                break;
//                            }
//                        }
                        for (int i = 0; i < yourInterestModle.getData().getCategory().size() - 1; i++) {
                            if (yourInterestModle.getData().getCategory().get(5).getSubCat().get(i).getSubCategoryName().equals(chip.getText().toString())) {
                                categoriesList.remove(yourInterestModle.getData().getCategory().get(5).getSubCat().get(i).getSkSubCategoryId());
                                break;
                            }
                        }
                        chip.setChipStrokeColor(ColorStateList.valueOf(getResources().getColor(R.color.cat_bg)));
                        chip.setChipBackgroundColor(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    }

                }
            });


            binding.cgMusic.addView(chip);

        }
        ArrayList<String> outCatList = new ArrayList<>();

    }


    private void selectMoveiCat() {
        if (binding.expandableLayoutMovieCat.isExpanded()) {
            binding.expandableLayoutMovieCat.collapse();
        } else {
            binding.expandableLayoutMovieCat.expand();
        }
    }

    private void selectOutCat() {
        if (binding.expandableLayoutOutCat.isExpanded()) {
            binding.expandableLayoutOutCat.collapse();
        } else {
            binding.expandableLayoutOutCat.expand();
        }
    }

    private void selectSportsCat() {
        if (binding.expandableLayoutSportsCat.isExpanded()) {
            binding.expandableLayoutSportsCat.collapse();
        } else {
            binding.expandableLayoutSportsCat.expand();
        }
    }

    private void selectArtCat() {
        if (binding.expandableLayoutArtsCat.isExpanded()) {
            binding.expandableLayoutArtsCat.collapse();
        } else {
            binding.expandableLayoutArtsCat.expand();
        }
    }

    private void selectFoodCat() {
        if (binding.expandableLayoutFoodsCat.isExpanded()) {
            binding.expandableLayoutFoodsCat.collapse();
        } else {
            binding.expandableLayoutFoodsCat.expand();
        }
    }

    private void selectMusicCat() {
        if (binding.expandableLayoutMusicCat.isExpanded()) {
            binding.expandableLayoutMusicCat.collapse();
        } else {
            binding.expandableLayoutMusicCat.expand();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_movie:
                selectMoveiCat();
                setMovieTags();
                movielist.clear();
                break;
            case R.id.rl_art:
                selectArtCat();
                setArtTags();
                artslist.clear();
                break;
            case R.id.rl_food:
                selectFoodCat();
                setFoodTags();
                foodlist.clear();
                break;
            case R.id.rl_music:
                selectMusicCat();
                setMusicTags();
                musiclist.clear();
                break;
            case R.id.rl_outdoor:
                selectOutCat();
                setOutTags();
                outdoor.clear();
                break;
            case R.id.rl_sport:
                setSportsTags();
                selectSportsCat();
                sportslist.clear();
                break;
            case R.id.iv_onBackPress:
                onBackPressed();
                break;
            case R.id.btn_next:
                Set<String> listWithoutDuplicates = new LinkedHashSet<String>(categoriesList);
                categoriesList.clear();
                categoriesList.addAll(listWithoutDuplicates);
                if (categoriesList.size() >= 3 && categoriesList.size()<=6 && binding.etTage.getText().toString().length()>=1) {
                    callCatApi();
                    binding.pwderror.setVisibility(View.VISIBLE);
                    binding.pwderror.setText("Please enter text field");

                }
                else {
                    Toast.makeText(getApplicationContext(), "Select Min 3 and max 6  Categories", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }


    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ChooseYourInterestModle> call = api.GetCategoryResponse(access_token, "All", "All", "category_subcategory");
        call.enqueue(new Callback<ChooseYourInterestModle>() {
            @Override
            public void onResponse(Call<ChooseYourInterestModle> call, Response<ChooseYourInterestModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    yourInterestModle = response.body();
                    for (j = 0; j < yourInterestModle.getData().getCategory().get(0).getSubCat().size(); j++) {
                        movielist.add(yourInterestModle.getData().getCategory().get(0).getSubCat().get(j).getSubCategoryName());
                    }
                    for (j = 0; j < yourInterestModle.getData().getCategory().get(1).getSubCat().size(); j++) {
                        outdoor.add(yourInterestModle.getData().getCategory().get(1).getSubCat().get(j).getSubCategoryName());
                    }
                    for (j = 0; j < yourInterestModle.getData().getCategory().get(2).getSubCat().size(); j++) {
                        sportslist.add(yourInterestModle.getData().getCategory().get(2).getSubCat().get(j).getSubCategoryName());
                    }
                    for (j = 0; j < yourInterestModle.getData().getCategory().get(3).getSubCat().size(); j++) {
                        artslist.add(yourInterestModle.getData().getCategory().get(3).getSubCat().get(j).getSubCategoryName());
                    }
                    for (j = 0; j < yourInterestModle.getData().getCategory().get(4).getSubCat().size(); j++) {
                        foodlist.add(yourInterestModle.getData().getCategory().get(4).getSubCat().get(j).getSubCategoryName());
                    }
                    for (j = 0; j < yourInterestModle.getData().getCategory().get(5).getSubCat().size(); j++) {
                        musiclist.add(yourInterestModle.getData().getCategory().get(5).getSubCat().get(j).getSubCategoryName());
                    }

                    for (int i = 0; i < yourInterestModle.getData().getCategory().size(); i++) {
                        binding.tvMovie.setText(yourInterestModle.getData().getCategory().get(0).getCategoryName());
                        if (yourInterestModle.getData().getCategory().get(0).getCategoryImagePic() != null && !yourInterestModle.getData().getCategory().get(0).getCategoryImagePic().isEmpty()) {
                            Picasso.get().load(yourInterestModle.getData().getCategory().get(0).getCategoryImagePic())
                                    .into(binding.ivMovies);
                        }
                        binding.tvOutdoor.setText(yourInterestModle.getData().getCategory().get(1).getCategoryName());
                        if (yourInterestModle.getData().getCategory().get(1).getCategoryImagePic() != null && !yourInterestModle.getData().getCategory().get(1).getCategoryImagePic().isEmpty()) {
                            Picasso.get().load(yourInterestModle.getData().getCategory().get(1).getCategoryImagePic())
                                    .into(binding.ivOutdoor);
                        }
                        binding.tvSports.setText(yourInterestModle.getData().getCategory().get(2).getCategoryName());
                        if (yourInterestModle.getData().getCategory().get(2).getCategoryImagePic() != null && !yourInterestModle.getData().getCategory().get(1).getCategoryImagePic().isEmpty()) {
                            Picasso.get().load(yourInterestModle.getData().getCategory().get(2).getCategoryImagePic())
                                    .into(binding.ivSports);
                        }
                        binding.tvArt.setText(yourInterestModle.getData().getCategory().get(3).getCategoryName());
                        if (yourInterestModle.getData().getCategory().get(3).getCategoryImagePic() != null && !yourInterestModle.getData().getCategory().get(1).getCategoryImagePic().isEmpty()) {
                            Picasso.get().load(yourInterestModle.getData().getCategory().get(3).getCategoryImagePic())
                                    .into(binding.ivArt);
                        }
                        binding.tvFood.setText(yourInterestModle.getData().getCategory().get(4).getCategoryName());
                        if (yourInterestModle.getData().getCategory().get(4).getCategoryImagePic() != null && !yourInterestModle.getData().getCategory().get(1).getCategoryImagePic().isEmpty()) {
                            Picasso.get().load(yourInterestModle.getData().getCategory().get(4).getCategoryImagePic())
                                    .into(binding.iv1);
                        }
                        binding.tvMusic.setText(yourInterestModle.getData().getCategory().get(5).getCategoryName());
                        if (yourInterestModle.getData().getCategory().get(5).getCategoryImagePic() != null && !yourInterestModle.getData().getCategory().get(1).getCategoryImagePic().isEmpty()) {
                            Picasso.get().load(yourInterestModle.getData().getCategory().get(5).getCategoryImagePic())
                                    .into(binding.iv12);
                        }
                    }


                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Invalid Emaileee Id")) {
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
            public void onFailure(Call<ChooseYourInterestModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callCatApi() {

        String Catid = categoriesList.toString().replace("[", "")
                .replace("]", "").replace(", ", ",  ");
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("user_interest", Catid);
        body.addProperty("user_own_interest", binding.etTage.getText().toString());
        body.addProperty("update_type", "Interests");
        body.addProperty("signup_stage", 6);

        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<JoinNowModle> call = api.putJoinNowResponse(access_token, "application/json", body);
        call.enqueue(new Callback<JoinNowModle>() {
            @Override
            public void onResponse(Call<JoinNowModle> call, Response<JoinNowModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    if(l==2)
                    {
                        onBackPressed();
                    }else
                    {
                        Intent i = new Intent(getApplicationContext(), BottomNavigationActivity.class);
                        startActivity(i);
                        finish();
                    }

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Your details added successfully")) {
                          /*  binding.otpError.setVisibility(View.VISIBLE);
                            binding.otpError.setText(jObjError.getString("message"));
*/
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
            public void onFailure(Call<JoinNowModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


}