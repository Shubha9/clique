package com.us.clique.bottomNavigation.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.CalenderActivity;
import com.us.clique.activites.ChooseInterestsActivity;
import com.us.clique.activites.EditeProfileActivity;
import com.us.clique.activites.SettingsActivity;
import com.us.clique.activites.YourExperienceActivity;
import com.us.clique.adapter.ProfileTagsAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.ProfileModle;
import com.us.clique.databinding.FragmentProfileBinding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentProfileBinding binding;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    private static final int REQUEST_PERMISSIONS = 100;
    //ProgressDialog loading;
    UserSessionManager session;
    String access_token = "", userId = "";
    String SelectedMoviveName = "";
    ProfileModle profileModle;
    List<ProfileModle.User> usersDetailsList = new ArrayList<>();
    ProfileTagsAdapter profileTagsAdapter;
    List<ProfileModle.SubCat> catList = new ArrayList<>();
    ArrayList<String>selected=new ArrayList<>();
    ArrayList<String>catIdList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setProfile(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        binding.ivSettings.setOnClickListener(this);
        binding.rlYourExperience.setOnClickListener(this);
        binding.eventCalender.setOnClickListener(this);
        binding.rlFindText.setOnClickListener(this);
        binding.tvEditeProfile.setOnClickListener(this);
        callProfileApi();
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (session.getisDataChanged());
        callProfileApi();
        session.setisDataChanged(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_settings:
                Intent i = new Intent(getContext(), SettingsActivity.class);
                getContext().startActivity(i);
                break;
            case R.id.rl_yourExperience:
                Intent j = new Intent(getContext(), YourExperienceActivity.class);
                getContext().startActivity(j);
                break;
            case R.id.event_calender:
                Intent k = new Intent(getContext(), CalenderActivity.class);
                getContext().startActivity(k);
                break;
            case R.id.rl_findText:
                for(int z=0;z<catList.size();z++)
                {
                    selected.add(catList.get(z).getSubCategoryName());
                    catIdList.add(catList.get(z).getSkSubCategoryId());
                }
                Intent l = new Intent(getContext(), ChooseInterestsActivity.class);
                l.putExtra("selected", selected);
                l.putExtra("selectedId", catIdList);
                l.putExtra("root",2);


                getContext().startActivity(l);
                break;
            case R.id.tv_editeProfile:
                Intent m = new Intent(getContext(), EditeProfileActivity.class);
                getContext().startActivity(m);
                break;
        }
    }

    private void setProfileDetails() {
        for (int i = 0; i < usersDetailsList.size(); i++) {
            if (!usersDetailsList.get(i).getProfileImage().equals("") && !usersDetailsList.get(i).getProfileImage().isEmpty() && !usersDetailsList.get(i).getProfileImage().equals(null)){
                Picasso.get().load(usersDetailsList.get(i).getProfileImage())
                        .into(binding.ivProfile1);
            }

           /* Picasso.get().load(usersDetailsList.get(i).getProfileImage())
                    .into(binding.ivProfile2);
            Picasso.get().load(usersDetailsList.get(i).getProfileImage())
                    .into(binding.ivProfile3);
*/
            binding.tvProfileName.setText(usersDetailsList.get(i).getName());
            binding.tvBioData.setText(usersDetailsList.get(i).getBioData());
            binding.tvTotalConnection.setText(usersDetailsList.get(i).getPeopleCount());
//            if (usersDetailsList.get(i).getCopyPoseStatus().equals(1)){
//                binding.ivCheck.setVisibility(View.VISIBLE);
//            }else {
//                binding.ivCheck.setVisibility(View.GONE);
//            }

        }
        if (catList != null) {
            profileTagsAdapter = new ProfileTagsAdapter(getContext(), catList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            binding.rvSelectedTags.setLayoutManager(linearLayoutManager);
            binding.rvSelectedTags.setAdapter(profileTagsAdapter);
        }

    }

    private void callProfileApi() {
        Api api = ApiClient.getClient().create(Api.class);
      //  loading = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ProfileModle> call = api.GetProfileDetails(access_token, "application/json", userId, "All", "Profile", "user");
        call.enqueue(new Callback<ProfileModle>() {
            @Override
            public void onResponse(Call<ProfileModle> call, Response<ProfileModle> response) {
           //     loading.cancel();
                if (response.isSuccessful()) {
                    ProfileModle profileModle = response.body();
                    usersDetailsList = profileModle.getData().getUser();
                    for (int i = 0; i < profileModle.getData().getUser().size(); i++) {
                        catList = profileModle.getData().getUser().get(i).getSubCat();
                    }
                    setProfileDetails();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Success")) {
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
            public void onFailure(Call<ProfileModle> call, Throwable t) {
         //       loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


    public void refreshFragment(Context context) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container,new ProfileFragment()).addToBackStack(null).commit();
    }
}
