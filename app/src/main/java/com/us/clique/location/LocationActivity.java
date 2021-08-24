package com.us.clique.location;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.ChooseInterestsActivity;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.ActivityLocationBinding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityLocationBinding binding;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ToggleButton toggleButton, toggleButton1;
    TextView text, txt2;
    Context context;
    LocationManager locationManager;
    boolean GpsStatus;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ProgressBar progressBar;
    String address, postcode, location, state, city, country;
    ResultReceiver resultReceiver;
    ProgressDialog loading;
    UserSessionManager session;
    GPSTracker gpsTracker;
    String access_token = "";
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        setContentView(R.layout.activity_location);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(LocationActivity.this,
                R.layout.activity_location);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setMapLocation(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
//        toggleButton=(ToggleButton)findViewById(R.id.toggleButton1);
        context = getApplicationContext();
        binding.tvAllow.setOnClickListener(this);
        //binding.tvDontAllow.setOnClickListener(this);
        //CheckGpsStatus();
        binding.ivOnBackPress.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_Allow:

                if (checkPermission()) {
                    getLocation();
                    callLocationApi();
                }else {
                    Toast.makeText(getApplicationContext(),"Please Enable Your Location Permission",Toast.LENGTH_SHORT).show();
                }
                break;
       /*     case R.id.tv_dontAllow:
                Intent i = new Intent(this, ChooseInterestsActivity.class);
                startActivity(i);
                break;*/

            case R.id.iv_onBackPress:
                onBackPressed();
                break;
        }
    }

    private boolean checkPermission() {
        int result = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return false;
        }
    }

    private void getLocation() {

        gpsTracker = new GPSTracker(this);
        if (gpsTracker.canGetLocation()) {
            this.latitude = gpsTracker.getLatitude();
            this.longitude = gpsTracker.getLongitude();
            Log.d("Tag", "lat+Long" + latitude + " " + longitude);
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                // String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
               if (addresses!=null || !addresses.isEmpty()){
                   city = addresses.get(0).getLocality();
                   state = addresses.get(0).getAdminArea();
                   country = addresses.get(0).getCountryName();
                   postcode = addresses.get(0).getPostalCode();
                   location = addresses.get(0).getSubAdminArea();
               }

                //mylocation=knownName+","+city+","+","+state+","+postalCode;
                //Log.e("name",mylocation);

                // Utility.showToast(city, getApplicationContext());

            } catch (IOException e) {
                this.latitude = Double.valueOf(0.0);
                this.longitude = Double.valueOf(0.0);
            }

        } else {
            gpsTracker.showSettingsAlert();
        }
    }



    private void callLocationApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("country_name", country);
        body.addProperty("state_name", state);
        body.addProperty("city_name", city);
        body.addProperty("location", location);
        body.addProperty("pincode", postcode);
        body.addProperty("latitude", latitude);
        body.addProperty("longitude", longitude);
        body.addProperty("update_type", "Location");
        body.addProperty("location_enable_disable", 1);
        body.addProperty("signup_stage", 5);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<LocationModle> call = api.PutLocation(access_token,"application/json",body);
        call.enqueue(new Callback<LocationModle>() {
            @Override
            public void onResponse(Call<LocationModle> call, Response<LocationModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), ChooseInterestsActivity.class);
                    i.putExtra("root",1);
                    startActivity(i);
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Location Settings Updated successfully"))
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
            public void onFailure(Call<LocationModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

}