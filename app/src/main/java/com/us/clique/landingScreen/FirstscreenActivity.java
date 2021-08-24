package com.us.clique.landingScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.BaseActivity;
import com.us.clique.activites.BottomNavigationActivity;
import com.us.clique.activites.ChooseInterestsActivity;
import com.us.clique.activites.LoginActivity;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.StagingModle;
import com.us.clique.copyPose.CopyPoseActivity;

import com.us.clique.databinding.ActivityFirstscreenBinding;
import com.us.clique.joinNow.JoinNowActivity;
import com.us.clique.joinNowProfile.JoinNowProfileActivity;
import com.us.clique.location.LocationActivity;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstscreenActivity extends BaseActivity {
    private static int SPLASH_SCREEN_TIME_OUT=2000;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityFirstscreenBinding binding;
    ProgressDialog loading5;
    String access_token = " ",userId,appStaging;
    UserSessionManager session;
    StagingModle stagingModle;
//
    SharedPreferences prefs = null;

    final String splashScreenPref= "SplashScreenShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(FirstscreenActivity.this,
                R.layout.activity_firstscreen);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setFirstScreen(bottomNavigationViewModel);
        session = new UserSessionManager(this);
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();

        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);
        callApi();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!session.isUserLoggedIn())
                {
                    Intent i=new Intent(FirstscreenActivity.this,
                            FrameActivity.class);
                    startActivity(i);
                    finish();
                }

            }
        }, 2000);


        FacebookSdk.sdkInitialize(getApplicationContext());

        facebook_shareId();

       // Log.d("apps",appStaging);
    }


private void check(){
    if (session.isUserLoggedIn()) {
        if (appStaging.equals("1")){
            Intent i = new Intent(FirstscreenActivity.this, JoinNowActivity.class);
            startActivity(i);
        }else if (appStaging.equals("2")){
            Intent i = new Intent(FirstscreenActivity.this, JoinNowProfileActivity.class);
            startActivity(i);
        }else if (appStaging.equals("3")){
            Intent i = new Intent(FirstscreenActivity.this, CopyPoseActivity.class);
            startActivity(i);
        }else if (appStaging.equals("4")){
            Intent i = new Intent(FirstscreenActivity.this, LocationActivity.class);
            startActivity(i);
        }else if (appStaging.equals("5")){
            Intent i = new Intent(FirstscreenActivity.this, ChooseInterestsActivity.class);
            startActivity(i);
        }else if (appStaging.equals("6")){
            Intent i = new Intent(FirstscreenActivity.this, BottomNavigationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
        }

    }else {
        Intent intent = new Intent(FirstscreenActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}

    public void facebook_shareId(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = (MessageDigest.getInstance("SHA"));
                md.update(signature.toByteArray());
                String hsh= new String(Base64.encode(md.digest(), 0));
                Log.e("exception", hsh);
            }
        }catch (Exception e) {
            Log.e("exception", e.toString());
        }
        Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this)+"=");
    }
    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading5 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<StagingModle> call = api.GetStaging(access_token,"application/json","SignupStage");
        call.enqueue(new Callback<StagingModle>() {
            @Override
            public void onResponse(Call<StagingModle> call, Response<StagingModle> response) {
                loading5.cancel();
                if (response.isSuccessful()) {
                    stagingModle = response.body();
                    appStaging =  stagingModle.getData().getSignupStage();
                    check();
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
            public void onFailure(Call<StagingModle> call, Throwable t) {
                loading5.cancel();
                // showToast(getApplicationContext(), t.toString());
                showSomething(FirstscreenActivity.this);
            }
        });

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
}