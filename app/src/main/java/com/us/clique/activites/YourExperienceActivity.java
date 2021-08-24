
package com.us.clique.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.YourExperienceAdapter;
import com.us.clique.bottomNavigation.fragments.ShareFragment;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.interfaces.SaveAndCopyLinkInterface;
import com.us.clique.bottomNavigation.interfaces.ShareInterface;
import com.us.clique.bottomNavigation.modle.ReportPopupModle;
import com.us.clique.bottomNavigation.modle.YourExperienceResponseModle;
import com.us.clique.databinding.ActivityYourExperienceBinding;
import com.us.clique.modle.DateModle;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YourExperienceActivity extends AppCompatActivity implements View.OnClickListener, SaveAndCopyLinkInterface, ShareInterface {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityYourExperienceBinding binding;

    YourExperienceAdapter yourExperienceAdapter;
    ArrayList<DateModle> dateModles = new ArrayList<>();
    ProgressDialog loading, loading2;
    UserSessionManager session;
    String access_token = "", userId = "",eventURL;
    String eventID, copyLink = "Link Successfully Copied", savedText = "Experience Saved in your Profile";
    ;

    List<YourExperienceResponseModle.EventDescription> yourExperienceResponseModles = new ArrayList<>();
    YourExperienceResponseModle yourExperienceResponseModle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(YourExperienceActivity.this,
                R.layout.activity_your_experience);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setYourExperience(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        OnlineUserStatus onlineUserStatus = new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
        binding.ivOnBackPress.setOnClickListener(this);

        callAllApi();

        binding.ckPast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ckUpComing.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.other_text));
                binding.ckPast.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_text));
                yourExperienceResponseModles.clear();
                callPastApi();
                binding.rvDateList.setVisibility(View.VISIBLE);
            }
        });
        binding.ckUpComing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.ckPast.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.other_text));
                binding.ckUpComing.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab_text));
                yourExperienceResponseModles.clear();
                callUpcommingApi();
                binding.rvDateList.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initRecyclre() {
       /* dateModles.add(new DateModle("6th April Today"));
        dateModles.add(new DateModle("6th April Tomorrow"));
     */
        if (!yourExperienceResponseModle.getStatus().equals(true)) {
            binding.rvDateList.setVisibility(View.GONE);
            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
            binding.tvNoDataAvailable.setText("No Data Available");
        } else {
            if (yourExperienceResponseModles != null) {
                binding.tvNoDataAvailable.setVisibility(View.GONE);
                binding.rvDateList.setVisibility(View.VISIBLE);
                yourExperienceAdapter = new YourExperienceAdapter(getApplicationContext(), yourExperienceResponseModles, this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                binding.rvDateList.setLayoutManager(layoutManager);
                binding.rvDateList.setAdapter(yourExperienceAdapter);

            }
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
        }
    }

    public void showBottomSheetShare() {
        ShareFragment addPhotoBottomDialogFragment =
                ShareFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                ShareFragment.TAG);
    }

    @Override
    public void ShareInterFace() {
        showBottomSheetShare();
        binding.rlSaveCopy.setVisibility(View.GONE);
    }

    private void callPastApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<YourExperienceResponseModle> call = api.GetYourExperince(access_token, "application/json", "All", "Past Experience", "All", "All", "EVENT_DATE", userId);
        call.enqueue(new Callback<YourExperienceResponseModle>() {
            @Override
            public void onResponse(Call<YourExperienceResponseModle> call, Response<YourExperienceResponseModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    yourExperienceResponseModle = response.body();
                    for (int i = 0; i < yourExperienceResponseModle.getData().size(); i++) {
                        yourExperienceResponseModles = (ArrayList) yourExperienceResponseModle.getData();
                    }
                    initRecyclre();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("No Data Available")) {
                            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
                            binding.tvNoDataAvailable.setText(jObjError.getString("message"));
                        } else {
                            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
                            binding.tvNoDataAvailable.setText(jObjError.getString("No Data Available"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();


                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<YourExperienceResponseModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callUpcommingApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<YourExperienceResponseModle> call = api.GetYourExperince(access_token, "application/json", "All", "Upcoming Experience", "All", "All", "EVENT_DATE", userId);
        call.enqueue(new Callback<YourExperienceResponseModle>() {
            @Override
            public void onResponse(Call<YourExperienceResponseModle> call, Response<YourExperienceResponseModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    yourExperienceResponseModle = response.body();
                    for (int i = 0; i < yourExperienceResponseModle.getData().size(); i++) {
                        yourExperienceResponseModles = (ArrayList) yourExperienceResponseModle.getData();
                    }
                    initRecyclre();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("No Data Available")) {
                            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
                            binding.tvNoDataAvailable.setText(jObjError.getString("message"));
                        } else {
                            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
                            binding.tvNoDataAvailable.setText(jObjError.getString("No Data Available"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();


                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<YourExperienceResponseModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }
 private void setData(){
     for (int i=0;i<yourExperienceResponseModles.size();i++){
          copyLink = yourExperienceResponseModles.get(i).getEventDetails().get(0).getEventUrl();
     }

 }
    private void callAllApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<YourExperienceResponseModle> call = api.GetYourExperince(access_token, "application/json", "All", "All", "All", "All", "EVENT_DATE", userId);
        call.enqueue(new Callback<YourExperienceResponseModle>() {
            @Override
            public void onResponse(Call<YourExperienceResponseModle> call, Response<YourExperienceResponseModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    yourExperienceResponseModle = response.body();
                    for (int i = 0; i < yourExperienceResponseModle.getData().size(); i++) {
                        yourExperienceResponseModles = (ArrayList) yourExperienceResponseModle.getData();
                    }

                    initRecyclre();
                    setData();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("No Data Available")) {
                            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
                            binding.tvNoDataAvailable.setText(jObjError.getString("message"));
                        } else {
                            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
                            binding.tvNoDataAvailable.setText(jObjError.getString("No Data Available"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();


                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                }
            }

            @Override
            public void onFailure(Call<YourExperienceResponseModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    public void showtooltip(String skEventId) {
        showBottomSheetShare();
      /*  eventID = skEventId;
        callSaveApi();
        binding.llCopylinkPopup.setVisibility(View.GONE);
        binding.llSavePopup.setVisibility(View.VISIBLE);
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.llSavePopup.setVisibility(View.GONE);
            }
        },5000);*/
    }

    public void showtooltip2(String skEventId,String eventUrl) {
        eventID = skEventId;
        //callCopyLinkApi();
        binding.llSavePopup.setVisibility(View.GONE);
        binding.llCopylinkPopup.setVisibility(View.VISIBLE);
        Object clipboardService = this.getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager)clipboardService;
        String srcText="mmmmmmm";
        eventURL  = eventUrl;
        // Create a new ClipData.
        ClipData clipData = ClipData.newPlainText("Source Text", eventURL);
        // Set it as primary clip data to copy text to system clipboard.
        clipboardManager.setPrimaryClip(clipData);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.llCopylinkPopup.setVisibility(View.GONE);
            }
        }, 5000);
    }

    private void callSaveApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("popup_type", "Save");
        body.addProperty("popup_reason", savedText);
        body.addProperty("event_id", eventID);
        body.addProperty("userId", userId);
        loading2 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token, "application/json", body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Report already exists for this event")) {
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
            public void onFailure(Call<ReportPopupModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }



    private void callCopyLinkApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("popup_type", "Block");
        body.addProperty("popup_reason", copyLink);
        body.addProperty("event_id", eventID);
        body.addProperty("userId", userId);
        loading2 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token, "application/json", body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {

                    Toast.makeText(getApplicationContext(), "copied link", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Report already exists for this event")) {
                            Toast.makeText(getApplicationContext(), "copied link", Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<ReportPopupModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


}