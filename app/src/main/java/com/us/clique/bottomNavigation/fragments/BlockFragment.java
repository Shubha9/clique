package com.us.clique.bottomNavigation.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.ReportPopupModle;
import com.us.clique.databinding.FragmentBlockBinding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class BlockFragment extends BottomSheetDialogFragment {

    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentBlockBinding binding;
    public static final String TAG = "ActionBottomDialog";
    ProgressDialog loading2;
    UserSessionManager session;
    String access_token = " ", userId = "", UserName, eventId;
    TextView tvBlockerName;
    Button btnBlock;

    public static BlockFragment newInstance() {

        return new BlockFragment();

    }

      @Override
      public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
          AndroidSupportInjection.inject(this);
          binding = DataBindingUtil.inflate(inflater, R.layout.fragment_block, container, false);
          //setContentView(binding.getRoot());
          bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
          binding.setBlockFragment(bottomNavigationViewModel);
          setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
          session = new UserSessionManager(getApplicationContext());
          access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
          userId = session.getUserId();

  //        tvBlockerName = (TextView)binding.findViewById(R.id.tv_blockerName);
          binding.btnBlock.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  callBlockApi();
                  dismiss();
              }

          });
          binding.tvBlockerName.setText(UserName);
          return binding.getRoot();
      }
   /* @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.fragment_block, null);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);

        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();

        tvBlockerName = (TextView)view.findViewById(R.id.tv_blockerName);
        btnBlock = (Button)view.findViewById(R.id.btn_block);
        btnBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callCopyLinkApi();
                dismiss();
            }

        });
        return view;
    }*/

    private void callBlockApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("popup_type", "Report");
        body.addProperty("popup_reason", "Block");
        body.addProperty("event_id", eventId);
        body.addProperty("userId", userId);
        loading2 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token, "application/json", body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Blocked this Event", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Already Blocked This Event")) {
                            Toast.makeText(getApplicationContext(), "Already Blocked This Event", Toast.LENGTH_SHORT).show();

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


    @Override
    public int getTheme() {
        return R.style.AppBottomSheetDialogTheme;
    }

    public void setEventId(String userName, String eventID) {
        eventId = eventID;
        UserName = userName;
    }
}
