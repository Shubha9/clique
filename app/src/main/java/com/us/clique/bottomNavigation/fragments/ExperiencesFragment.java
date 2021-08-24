package com.us.clique.bottomNavigation.fragments;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.SessionManagers.ListSaveManager;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.ExperiencesAdapter;
import com.us.clique.adapter.ReportAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.fragments.module.ReportModel;
import com.us.clique.bottomNavigation.interfaces.BlockInterface;
import com.us.clique.bottomNavigation.interfaces.CopyLinkInterface;
import com.us.clique.bottomNavigation.interfaces.NotIntrested;
import com.us.clique.bottomNavigation.interfaces.ReportInterface;
import com.us.clique.bottomNavigation.interfaces.SaveInterface;
import com.us.clique.bottomNavigation.interfaces.ShareInterface;
import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.us.clique.bottomNavigation.modle.IpAddressModle;
import com.us.clique.bottomNavigation.modle.MoveModle;
import com.us.clique.bottomNavigation.modle.ReportPopupModle;
import com.us.clique.databinding.FragmentExperiencesBinding;
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

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class ExperiencesFragment extends Fragment implements ReportInterface, View.OnClickListener, NotIntrested, BlockInterface, ShareInterface, SaveInterface, CopyLinkInterface {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    FragmentExperiencesBinding binding;
    ArrayList<MoveModle> moveModlesList = new ArrayList<>();
    ExperiencesAdapter experiencesAdapter;

    ReportAdapter reportAdapter;
    ArrayList<ReportModel> reportModels = new ArrayList<>();

    // RecyclerView rvReportList;
    ProgressDialog loading, loading2, loading3;
    UserSessionManager session;
    String access_token = " ", userId = "", CheckBoxText, eventID,UserName, tellUsWhy = "", savedText = "Experience Saved in your Profile", copyLink = "";
    ;
    ExperinceModle experinceModle;
    ArrayList<String> checkboxText = new ArrayList<String>();
    ArrayList<ExperinceModle.Datum> experinceList = new ArrayList<>();
    BlockFragment addPhotoBottomDialogFragment;
    ListSaveManager listSaveManager;
    boolean stop = false;
    String ipAddress;
    ReportPopupModle reportPopupModle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_experiences, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setExperienceFragment(bottomNavigationViewModel);
        // binding.radiogroup.getCheckedRadioButtonId();
        listSaveManager = new ListSaveManager(getApplicationContext());
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        experinceList = listSaveManager.getList();
        init();
        if (!experinceList.isEmpty()) {
            experinceList.clear();
            InitRecycler();
        } else {

            callApi();
            callGetExperinceApi();
        }


        binding.tvCancel.setOnClickListener(this);
        binding.tvSubmitbtn.setOnClickListener(this);
        binding.tvSubmit.setOnClickListener(this);
        binding.ivSave.setOnClickListener(this);
        binding.cancelbtn.setOnClickListener(this);
        binding.tvUndo.setOnClickListener(this);
        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobtn1:
                        tellUsWhy = "I don't like this experience";
                        binding.tvSubmit.setVisibility(View.VISIBLE);
                        // Toast.makeText(getContext(),"Male selected",Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radiobtn2:
                        binding.tvSubmit.setVisibility(View.VISIBLE);
                        tellUsWhy = "It's too far away";
                        //Toast.makeText(getContext(),"FeMale selected",Toast.LENGTH_LONG).show();
                }
            }
        });

        return binding.getRoot();
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

    public void InitRecycler() {
        if (!experinceModle.getStatus().equals(true)) {
            binding.rvExperince.setVisibility(View.GONE);
            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
            binding.tvNoDataAvailable.setText("No Data Available");
        } else {
            binding.tvNoDataAvailable.setVisibility(View.GONE);
            binding.rvExperince.setVisibility(View.VISIBLE);
            if (experinceList != null) {
                experiencesAdapter = new ExperiencesAdapter(getContext(), experinceList, this, this, this, this,
                        this, this, this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
                binding.rvExperince.setLayoutManager(mLayoutManager);
                binding.rvExperince.setItemAnimator(new DefaultItemAnimator());
                binding.rvExperince.setAdapter(experiencesAdapter);
            }
        }
    }

    private void init() {
        initRecycler();
        binding.onBackPress.setOnClickListener(this);
        binding.tvTellUs.setOnClickListener(this);
    }
   /* public void showBottomSheet(View view) {
        BlockFragment addPhotoBottomDialogFragment =
                BlockFragment.newInstance();
        addPhotoBottomDialogFragment.show(getSupportFragmentManager(),
                BlockFragment.TAG);

    }*/

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
        reportAdapter = new ReportAdapter(getContext(), reportModels, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.rvReportBulets.setLayoutManager(mLayoutManager);
        binding.rvReportBulets.setItemAnimator(new DefaultItemAnimator());
        binding.rvReportBulets.setAdapter(reportAdapter);

    }

    @Override
    public void callReport(View v) {
        if (binding.expandableLayoutReportFragment.isExpanded()) {
            binding.expandableLayoutReportFragment.collapse();
            binding.rlNotIntrest.setVisibility(View.GONE);
        } else {
            binding.nestedScroll.fullScroll(View.FOCUS_UP);
            binding.nestedScroll.scrollTo(0, 0);
            binding.expandableLayoutReportFragment.expand();
            binding.rlNotIntrest.setVisibility(View.GONE);
            binding.rlWhytelus.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                binding.tvSubmit.setVisibility(View.GONE);
                binding.radiogroup.clearCheck();
                break;
            case R.id.tv_submitbtn:
                reportModels.clear();
                binding.tvSubmitbtn.setVisibility(View.GONE);
                initRecycler();
                callReportPoupApi();
//                binding.rlNotIntrest.setVisibility(View.GONE);
                binding.expandableLayoutReportFragment.collapse();

                break;
            case R.id.cancelbtn:
                binding.expandableLayoutReportFragment.collapse();
                reportModels.clear();
                binding.tvSubmitbtn.setVisibility(View.GONE);
                initRecycler();
                break;
            case R.id.tv_submit:
                if (!tellUsWhy.isEmpty()) {
                    callNotInterstedApi();
                    binding.radiogroup.clearCheck();
                    binding.rlWhytelus.setVisibility(View.GONE);
                    binding.tvSubmit.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getContext(), "Select Any one", Toast.LENGTH_LONG).show();
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
                if (!copyLink.isEmpty()) {

                    binding.llCopylinkPopup.setVisibility(View.GONE);
                } else {
                    binding.llCopylinkPopup.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.tv_undo:
                binding.rlNotIntrest.setVisibility(View.GONE);
                break;
        }
    }

    public void showBottomSheetBlock() {
        addPhotoBottomDialogFragment =
                BlockFragment.newInstance();
        addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                BlockFragment.TAG);
        if (addPhotoBottomDialogFragment != null)
            addPhotoBottomDialogFragment.setEventId(eventID,UserName);
    }

    public void showBottomSheetShare() {
        ShareFragment addPhotoBottomDialogFragment =
                ShareFragment.newInstance();
        addPhotoBottomDialogFragment.show(getActivity().getSupportFragmentManager(),
                ShareFragment.TAG);
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
    public void BottomSheetFragment() {
        showBottomSheetBlock();
        binding.expandableLayoutReportFragment.collapse();
        binding.rlNotIntrest.setVisibility(View.GONE);
        binding.rlWhytelus.setVisibility(View.GONE);
    }

    @Override
    public void ShareInterFace() {
        showBottomSheetShare();
        binding.expandableLayoutReportFragment.collapse();
        binding.rlNotIntrest.setVisibility(View.GONE);
        binding.rlWhytelus.setVisibility(View.GONE);
    }

    private void callGetExperinceApi() {
        Api api = ApiClient.getClient().create(Api.class);

        loading = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ExperinceModle> call = api.GetExperinces(access_token, "application/json",/*ipAddress*/"117.196.2.117", "1", "Current Events", "LocationByIP", "All");
        call.enqueue(new Callback<ExperinceModle>() {
            @Override
            public void onResponse(Call<ExperinceModle> call, Response<ExperinceModle> response) {
                stop = true;
                loading.cancel();
                if (response.isSuccessful()) {
                    experinceModle = response.body();
                    experinceList = (ArrayList) experinceModle.getData();
                    InitRecycler();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("No Data Available")) {
                            binding.tvNoDataAvailable.setVisibility(View.VISIBLE);
                            binding.tvNoDataAvailable.setText(jObjError.getString("message"));
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

    private void callReportPoupApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("popup_type", "Report");
        body.addProperty("popup_reason", CheckBoxText);
        body.addProperty("event_id", eventID);
        body.addProperty("userId", userId);
        loading2 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token, "application/json", body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Reported", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Report already exists for this event")) {

                            Toast.makeText(getApplicationContext(), "Report already exists for this event", Toast.LENGTH_SHORT).show();

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

    private void callNotInterstedApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("popup_type", "NotIntersted");
        body.addProperty("popup_reason", tellUsWhy);
        body.addProperty("event_id", eventID);
        body.addProperty("userId", userId);
        loading2 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token, "application/json", body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Reported", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Report already exists for this event")) {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));
                            Toast.makeText(getApplicationContext(), "Report already exists for this event", Toast.LENGTH_SHORT).show();
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

    private  void savedRemove(){
        binding.nestedScroll.fullScroll(View.FOCUS_UP);
        binding.nestedScroll.scrollTo(0, 0);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.llSavePopup.setVisibility(View.GONE);
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
        loading2 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token, "application/json", body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    reportPopupModle = response.body();
                    if (!reportPopupModle.getStatus().equals(true)){
                        Toast.makeText(getApplicationContext(), "Save already exists for this event", Toast.LENGTH_SHORT).show();
                    }else {
                        savedRemove();
                      //  Toast.makeText(getApplicationContext(), "Saved this Event", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Save already exists for this event")) {
                            Toast.makeText(getApplicationContext(), "Save already exists for this event", Toast.LENGTH_SHORT).show();
                        } else {
//                           tvPassError.setText(jObjError.getString("message"));
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
        body.addProperty("popup_type", "CopyLink");
        body.addProperty("popup_reason", copyLink);
        body.addProperty("event_id", eventID);
        body.addProperty("userId", userId);
        loading2 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ReportPopupModle> call = api.PostReportPopup(access_token, "application/json", body);
        call.enqueue(new Callback<ReportPopupModle>() {
            @Override
            public void onResponse(Call<ReportPopupModle> call, Response<ReportPopupModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Reported", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Report already exists for this event")) {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));
                            Toast.makeText(getApplicationContext(), "Already Link Copied", Toast.LENGTH_SHORT).show();
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
//                dialogBox();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


    public void selectText(ArrayList<String> s) {
        if (!s.isEmpty()) {
            binding.tvSubmitbtn.setVisibility(View.VISIBLE);
        } else {
            binding.tvSubmitbtn.setVisibility(View.GONE);
        }
        checkboxText = s;
        CheckBoxText = checkboxText.toString().replace("[", "")
                .replace("]", "").replace(", ", ",  ");

    }

    public void eventId(String eventId, String  userName) {
        eventID = eventId;
        UserName = userName;

    }

    @Override
    public void save() {
        binding.llCopylinkPopup.setVisibility(View.GONE);
        binding.rlNotIntrest.setVisibility(View.GONE);
        binding.rlWhytelus.setVisibility(View.GONE);
        binding.rlNotIntrest.setVisibility(View.GONE);
        callSaveApi();

    }

    @Override
    public void copyLink(String eventUrl) {
        binding.llCopylinkPopup.setVisibility(View.VISIBLE);
        binding.nestedScroll.fullScroll(View.FOCUS_UP);
        binding.nestedScroll.scrollTo(0, 0);
        binding.llSavePopup.setVisibility(View.GONE);
        binding.rlNotIntrest.setVisibility(View.GONE);
        binding.rlWhytelus.setVisibility(View.GONE);
        binding.rlNotIntrest.setVisibility(View.GONE);
        Object clipboardService = getContext().getSystemService(CLIPBOARD_SERVICE);
        final ClipboardManager clipboardManager = (ClipboardManager)clipboardService;
//                String srcText = binding.etMovieName.getText().toString();
        String srcText="mmmmmmm";
        copyLink = eventUrl;

        // Create a new ClipData.
        ClipData clipData = ClipData.newPlainText("Source Text", copyLink);
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

    public void dialogBox() {
        stop = false;
        loading.cancel();
        experinceList.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("AlertDialog");
        builder.setMessage("Please Refresh");

        // add the buttons
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callGetExperinceApi();
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void NetworkDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_internet_connection);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        Button dialogButton = (Button) dialog.findViewById(R.id.bt_refresh);
        TextView textView = (TextView) dialog.findViewById(R.id.tv_message);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callGetExperinceApi();

            }
        });
        dialog.show();
    }

}
