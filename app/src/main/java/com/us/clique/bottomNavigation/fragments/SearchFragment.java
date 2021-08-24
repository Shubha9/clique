package com.us.clique.bottomNavigation.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.ReportAdapter;
import com.us.clique.adapter.ReportSearchAdapter;
import com.us.clique.adapter.SearchAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.fragments.module.ReportModel;
import com.us.clique.bottomNavigation.fragments.module.SearchModle;
import com.us.clique.bottomNavigation.interfaces.BlockInterface;
import com.us.clique.bottomNavigation.interfaces.CopyLinkInterface;
import com.us.clique.bottomNavigation.interfaces.NotIntrested;
import com.us.clique.bottomNavigation.interfaces.ReportInterface;
import com.us.clique.bottomNavigation.interfaces.SaveInterface;
import com.us.clique.bottomNavigation.interfaces.ShareInterface;
import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.us.clique.bottomNavigation.modle.IpAddressModle;
import com.us.clique.bottomNavigation.modle.ReportPopupModle;
import com.us.clique.bottomNavigation.modle.SearchModleResponce;
import com.us.clique.databinding.FragmentSearchBinding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;

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

public class SearchFragment extends Fragment implements ReportInterface, View.OnClickListener, NotIntrested, BlockInterface, ShareInterface, SaveInterface, CopyLinkInterface {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentSearchBinding binding;
    SearchAdapter searchAdapter;
    ArrayList<SearchModle> searchModles = new ArrayList<>();
    ProgressDialog loading,loading2, loading3;
    String access_token = " ", userId, reciverId;
    UserSessionManager session;
    SearchModleResponce searchModleResponce;
    ExperinceModle experinceModle;
    ArrayList<ExperinceModle.Datum> experinceList = new ArrayList<>();
    String CheckBoxText, eventID, tellUsWhy = "", savedText = "", copyLink = "";
    BlockFragment addPhotoBottomDialogFragment;
    ReportAdapter reportAdapter;
    ArrayList<ReportModel> reportModels = new ArrayList<>();
    ArrayList<String> checkboxText = new ArrayList<String>();
    ReportSearchAdapter reportSearchAdapter;
    String ipAddress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setSearchFragment(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        init();

        callSearchApi();


        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (binding.etSearch.getText().toString().isEmpty()) {
                    binding.fragExperince.setVisibility(View.GONE);
                } else {
                    binding.fragExperince.setVisibility(View.VISIBLE);
                }
                if (searchAdapter != null) {
                    searchAdapter.getFilter().filter(charSequence.toString());
                } else {
                    Toast.makeText(getApplicationContext(), "No Data found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

        binding.tvCancel.setOnClickListener(this);
        binding.tvSubmitbtn.setOnClickListener(this);
        binding.tvSubmit.setOnClickListener(this);
        binding.ivSave.setOnClickListener(this);
        binding.cancelbtn.setOnClickListener(this);
        binding.tvUndo.setOnClickListener(this);
        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.radiobtn1:
                        tellUsWhy="I don't like this experience";
                        // Toast.makeText(getContext(),"Male selected",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radiobtn2:
                        tellUsWhy="It's too far away";

                        //Toast.makeText(getContext(),"FeMale selected",Toast.LENGTH_LONG).show();
                }
            }
        });

        return binding.getRoot();

    }

    private void SearchRecycler() {
      /*  searchModles.add(new SearchModle("It is a long established "));
        searchModles.add(new SearchModle("It is a long established "));
        searchModles.add(new SearchModle("It is a long established "));
        searchModles.add(new SearchModle("It is a long established "));
        searchModles.add(new SearchModle("It is a long established "));*/
        searchAdapter = new SearchAdapter(getContext(), experinceList, this, this, this, this,
                this, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.rvExperince.setLayoutManager(layoutManager);
        binding.rvExperince.setAdapter(searchAdapter);
    }

    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading3 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<IpAddressModle> call = api.PostIpAddress(access_token, "application/json");
        call.enqueue(new Callback<IpAddressModle>() {
            @Override
            public void onResponse(Call<IpAddressModle> call, Response<IpAddressModle> response) {
                loading3.cancel();
                if (response.isSuccessful()) {
                    IpAddressModle ipAddressModle = response.body();
                    if (ipAddressModle.getStatus()) {
                        ipAddress = ipAddressModle.getData();

                    } else {
                        Toast.makeText(getApplicationContext(), "false", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("IP address")) {
//                            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
//                            binding.tvNoDataAvailable.setText(jObjError.getString("message"));

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
            public void onFailure(Call<IpAddressModle> call, Throwable t) {
                loading3.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void init() {
        initRecycler();
        binding.onBackPress.setOnClickListener(this);
        binding.tvTellUs.setOnClickListener(this);
    }

    private void callSearchApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ExperinceModle> call = api.GetSearch(access_token, "application/json", binding.etSearch.getText().toString());
        call.enqueue(new Callback<ExperinceModle>() {
            @Override
            public void onResponse(Call<ExperinceModle> call, Response<ExperinceModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    experinceModle = response.body();
                    experinceList = (ArrayList) experinceModle.getData();
                    SearchRecycler();
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
            public void onFailure(Call<ExperinceModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    public void eventId(String eventId) {
        eventID = eventId;
    }

    private void initRecycler() {
        reportModels.add(new ReportModel("It's Spam"));
        reportModels.add(new ReportModel("Nudity or sexual Activity"));
        reportModels.add(new ReportModel("Hate speech or symbols"));
        reportModels.add(new ReportModel("Violence or dangerous organizations"));
        reportModels.add(new ReportModel("Sale of Illegal or regulated goods"));
        reportModels.add(new ReportModel("Bullying or Harassment"));
        reportModels.add(new ReportModel("Intellectual Property Violations"));
        reportModels.add(new ReportModel("Suicide, self - injury or eating disorders"));
        reportModels.add(new ReportModel("Scam or fraud"));
        reportModels.add(new ReportModel("False Information"));
        reportModels.add(new ReportModel("I Just don't like it"));
        reportSearchAdapter = new ReportSearchAdapter(getContext(), reportModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.rvReportBulets.setLayoutManager(mLayoutManager);
        binding.rvReportBulets.setItemAnimator(new DefaultItemAnimator());
        binding.rvReportBulets.setAdapter(reportSearchAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.onBackPress:
                binding.expandableLayoutReportFragment.collapse();
                binding.rlWhytelus.setVisibility(View.GONE);
                break;
            case R.id.tv_tellUs:
                binding.rlNotIntrest.setVisibility(View.GONE);
                binding.rlWhytelus.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_cancel:
                binding.rlNotIntrest.setVisibility(View.GONE);
                binding.rlWhytelus.setVisibility(View.GONE);
                binding.radiogroup.clearCheck();
                break;
            case R.id.tv_submitbtn:
                reportModels.clear();
                binding.tvSubmit.setVisibility(View.GONE);
                initRecycler();
                callReportPoupApi();
//                binding.rlNotIntrest.setVisibility(View.GONE);
                binding.expandableLayoutReportFragment.collapse();

                break;
            case R.id.cancelbtn:
                binding.expandableLayoutReportFragment.collapse();
                reportModels.clear();
                binding.tvSubmit.setVisibility(View.GONE);
                initRecycler();
                break;
            case R.id.tv_submit:
                if (!tellUsWhy.isEmpty()){
                    callNotInterstedApi();
                    binding.radiogroup.clearCheck();
                    binding.rlWhytelus.setVisibility(View.GONE);
                }else {
                    Toast.makeText(getContext(),"Select Any one",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.iv_save:
//                savedText = "";
//                if (!savedText.isEmpty()){
//                    callSaveApi();
//                    binding.llSavePopup.setVisibility(View.GONE);
//                }else {
//                    binding.llSavePopup.setVisibility(View.VISIBLE);
//                }
                break;
            case R.id.iv_copyLink:
                if (!copyLink.isEmpty()){
                    callCopyLinkApi();
                    binding.llCopylinkPopup.setVisibility(View.GONE);
                }else {
                    binding.llCopylinkPopup.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_undo:
                binding.rlNotIntrest.setVisibility(View.GONE);
                break;
        }
    }
    private void callReportPoupApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("popup_type", "Report");
        body.addProperty("popup_reason", CheckBoxText);
        body.addProperty("event_id", eventID);
        body.addProperty("userId", userId);
        loading2 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token,"application/json",body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Reported",Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Report already exists for this event"))
                        {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));
                            Toast.makeText(getApplicationContext(),"Report already exists for this event",Toast.LENGTH_SHORT).show();

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
            public void onFailure(Call<ReportPopupModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callNotInterstedApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("popup_type", "NotIntersted");
        body.addProperty("popup_reason", tellUsWhy);
        body.addProperty("event_id", eventID);
        body.addProperty("userId", userId);
        loading2 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token,"application/json",body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Reported",Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Report already exists for this event"))
                        {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));
                            Toast.makeText(getApplicationContext(),"Report already exists for this event",Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<ReportPopupModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


    private void callSaveApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("popup_type", "Save");
        body.addProperty("popup_reason", savedText);
        body.addProperty("event_id", eventID);
        body.addProperty("userId", userId);
        loading2 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token,"application/json",body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    if (response.code() == 200 ){
                        Toast.makeText(getApplicationContext(),"Saved this Event",Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Report already exists for this event"))
                        {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));
                            Toast.makeText(getApplicationContext(),"Already Event Saved",Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<ReportPopupModle> call, Throwable t) {
                loading2.cancel();
//                dialogBox();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }
    private void callCopyLinkApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("popup_type", "Report");
        body.addProperty("popup_reason", copyLink);
        body.addProperty("event_id", eventID);
        body.addProperty("userId", userId);
        loading2 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token,"application/json",body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    if (response.code() == 200 ){
                        Toast.makeText(getApplicationContext(),"Reported",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Report already exists for this event"))
                        {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));
                            Toast.makeText(getApplicationContext(),"Already Link Copied",Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<ReportPopupModle> call, Throwable t) {
                loading2.cancel();
//                dialogBox();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }



    public void showBottomSheetBlock() {
        addPhotoBottomDialogFragment =
                BlockFragment.newInstance();
        addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                BlockFragment.TAG);
        if (addPhotoBottomDialogFragment != null)
            addPhotoBottomDialogFragment.setEventId(eventID, eventID);
    }

    public void showBottomSheetShare() {
        ShareFragment addPhotoBottomDialogFragment =
                ShareFragment.newInstance();
        addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                ShareFragment.TAG);
    }

    @Override
    public void BottomSheetFragment() {
        showBottomSheetBlock();
        binding.expandableLayoutReportFragment.collapse();
        binding.rlNotIntrest.setVisibility(View.GONE);
        binding.rlWhytelus.setVisibility(View.GONE);
    }

    @Override
    public void save() {
        binding.llSavePopup.setVisibility(View.VISIBLE);
        binding.nestedScroll.fullScroll(View.FOCUS_UP);
        binding.nestedScroll.scrollTo(0,0);
        binding.llCopylinkPopup.setVisibility(View.GONE);
        binding.rlNotIntrest.setVisibility(View.GONE);
        binding.rlWhytelus.setVisibility(View.GONE);
        binding.rlNotIntrest.setVisibility(View.GONE);
        callSaveApi();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.llSavePopup.setVisibility(View.GONE);
            }
        },5000);
    }

    @Override
    public void copyLink(String eventUrl) {
        binding.llCopylinkPopup.setVisibility(View.VISIBLE);
        binding.nestedScroll.fullScroll(View.FOCUS_UP);
        binding.nestedScroll.scrollTo(0,0);
        binding.llSavePopup.setVisibility(View.GONE);
        binding.rlNotIntrest.setVisibility(View.GONE);
        binding.rlWhytelus.setVisibility(View.GONE);
        binding.rlNotIntrest.setVisibility(View.GONE);
        callCopyLinkApi();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.llCopylinkPopup.setVisibility(View.GONE);
            }
        },5000);
    }

    @Override
    public void notIntrested() {
        if (binding.rlNotIntrest.getVisibility() == View.VISIBLE) {
            binding.rlNotIntrest.setVisibility(View.GONE);
            // Its visible
        } else {
            binding.nestedScroll.fullScroll(View.FOCUS_UP);
            binding.nestedScroll.scrollTo(0, 0);
            binding.rlNotIntrest.setVisibility(View.VISIBLE);
            binding.expandableLayoutReportFragment.collapse();
            binding.rlWhytelus.setVisibility(View.GONE);
            binding.llSavePopup.setVisibility(View.GONE);
            binding.llCopylinkPopup.setVisibility(View.GONE);
            // Either gone or invisible
        }
    }

    @Override
    public void callReport(View v) {
        if (binding.expandableLayoutReportFragment.isExpanded()) {
            binding.expandableLayoutReportFragment.collapse();
            binding.rlNotIntrest.setVisibility(View.GONE);
        } else {
            binding.expandableLayoutReportFragment.expand();
            binding.rlNotIntrest.setVisibility(View.GONE);
            binding.rlWhytelus.setVisibility(View.GONE);
        }
    }

    @Override
    public void ShareInterFace() {
        showBottomSheetShare();
        binding.expandableLayoutReportFragment.collapse();
        binding.rlNotIntrest.setVisibility(View.GONE);
        binding.rlWhytelus.setVisibility(View.GONE);
    }


    public void selectText(ArrayList<String> s) {
        if (!s.isEmpty()){
            binding.tvSubmitbtn.setVisibility(View.VISIBLE);
        }else {
            binding.tvSubmitbtn.setVisibility(View.GONE);
        }
        checkboxText=s;
        CheckBoxText = checkboxText.toString().replace("[", "")
                .replace("]", "").replace(", ", ",  ");

    }
}
