package com.us.clique.joinNow;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.BrowserActivity;
import com.us.clique.activites.LoginActivity;
import com.us.clique.adapter.GenderAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.interfaces.MovieNameInterface;
import com.us.clique.databinding.ActivityJoinNowBinding;
import com.us.clique.joinNowProfile.JoinNowProfileActivity;
import com.us.clique.modle.MoviesListModle;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;
import com.us.clique.networkUtils.Constants;
import com.us.clique.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class JoinNowActivity extends AppCompatActivity implements MovieNameInterface,View.OnClickListener {
Boolean isvalid=false;
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;

    ActivityJoinNowBinding binding;

    ProgressDialog loading;
    UserSessionManager session;
    String access_token = "",dOfB;
//    String globalAccesss_token = " ",email = " ";
    GenderAdapter genderAdapter;
    ArrayList<MoviesListModle> moviesListModles = new ArrayList<>();
    String  SelectedMoviveName = "",Dob="";
    boolean isNameValid=false,isDobValid=false,isGender=false,isBioValid=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(JoinNowActivity.this,
                R.layout.activity_join_now);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setJoinNow(bottomNavigationViewModel);
       // globalAccesss_token = "cpk5sre43kpsprytujhjatquwevgtpljkg?e21nfo5k#qaqwe6thnbde";
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);


        binding.etGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectClass();
                initializeNewChatClassRecycler();
            }
        });

        binding.etName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int action =event.getAction();
                // Only process key up action, otherwise this listener will be triggered twice because of key down action.
                if(action == KeyEvent.ACTION_UP)
                {
                    processButtonByTextLength();
                }
                return false;
            }


        });
        binding.etDob.setInputType(InputType.TYPE_NULL);
        binding.etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar cldr = Calendar.getInstance();
                cldr.add(Calendar.YEAR, -18);
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog

                DatePickerDialog datepicker = new DatePickerDialog(JoinNowActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                               // binding.etDob.setText(  dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                binding.etDob.setText(  year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, year, month, day);
                datepicker.getDatePicker().setMaxDate(cldr.getTimeInMillis());
//                datepicker.getDatePicker().setMinDate(System.currentTimeMillis().minusYears(14));
//                datepicker.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datepicker.show();
            }
        });
        binding.joinbackbtn1.setOnClickListener(this);
        binding.privacytxt.setOnClickListener(this);
        textWatcher();
    }

    private void processButtonByTextLength() {

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i=start; i < start-1; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter filter2 = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i=start; i < start-1; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter filter3= new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i=start; i < start-1; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter filter4 = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i=start; i < start-1; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        binding.etName.setFilters(new InputFilter[]{filter});
        binding.etDob.setFilters(new InputFilter[]{filter2});
        binding.etBio.setFilters(new InputFilter[]{filter3});
        binding.etGender.setFilters(new InputFilter[]{filter4});

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(500); //Filter to 10 characters
        binding.etBio .setFilters(filters);

        binding.etName.setFilters(new InputFilter[]{filter});
        if (binding.etName.getText().toString().startsWith(" ")) {
            binding.error1.setVisibility(View.VISIBLE);
            binding.error1.setText("Space Not Valid");

        } else if (binding.etName.getText().toString().matches("[a-zA-Z ]+")) {

        } else if (binding.etName.getText().toString().isEmpty()) {

        } else {
            binding.error1.setVisibility(View.VISIBLE);
            binding.error1.setText("Please Enter name in Alphabets only");
        }
        if (binding.etGender.getText().toString().startsWith(" ")) {
            binding.error3.setVisibility(View.VISIBLE);
            binding.error3.setText("Space Not Valid");

        }

        if (binding.etDob.getText().toString().startsWith(" ")) {
            binding.error2.setVisibility(View.VISIBLE);
            binding.error2.setText("Space Not Valid");

        }
        if (binding.etBio.getText().toString().startsWith(" ")) {
            binding.error4.setVisibility(View.VISIBLE);
            binding.error4.setText("Space Not Valid");

        }
        String inputText = binding.etName.getText().toString();
        String inputText2 = binding.etGender.getText().toString();
        String inputText3 = binding.etDob.getText().toString();
        String inputText4 = binding.etBio.getText().toString();
        if(!(inputText.length()>0 && (inputText2.length())>0 && inputText3.length()>0 ) )
        {
            binding.signinjoin.setEnabled(false);
            binding.signinjoin.setBackgroundResource(R.drawable.button_bg_enable);

        }else {
            binding.signinjoin.setEnabled(true);
            binding.signinjoin.setBackgroundResource(R.drawable.button_background);
            binding.signinjoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (setValidation()) {
                        callApi();
                    }
                }
            });
        }
    }
    private void initializeNewChatClassRecycler() {
        moviesListModles.clear();
        genderAdapter = new GenderAdapter(getApplicationContext(), moviesListModles, "Select Gender",this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.rvSelectClass.setLayoutManager(mLayoutManager);
        binding.rvSelectClass.setItemAnimator(new DefaultItemAnimator());
        moviesListModles.add(new MoviesListModle("Male"));
        moviesListModles.add(new MoviesListModle("female"));
        moviesListModles.add(new MoviesListModle("other"));
        binding.rvSelectClass.setAdapter(genderAdapter);
    }
    private void selectClass() {
        if (binding.expandableLayoutSelectClass.isExpanded()) {
            binding.ivSelectClass.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_expend));
            binding.expandableLayoutSelectClass.collapse();
        } else {
            binding.ivSelectClass.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_collapse));
            binding.expandableLayoutSelectClass.expand();
        }
    }
    @Override
    public void setSelectMoviesName(String name) {
        binding.etGender.setText(name);
        SelectedMoviveName= binding.etGender.getText().toString();
        binding.expandableLayoutSelectClass.collapse();
        binding.ivSelectClass.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_expend));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean setValidation() {

        if (binding.etName.getText().toString().isEmpty()) {
            binding.error1.setVisibility(View.VISIBLE);
            binding.error1.setText("Please Enter Name");
            isNameValid= false;
        }else{
            isNameValid=true;
        }

        if (binding.etDob.getText().toString().isEmpty()) {
            binding.error2.setVisibility(View.VISIBLE);
            binding.error2.setText("Please Enter Bithday date");
            isDobValid= false;
        }else{
            isDobValid=true;
        }
        if (SelectedMoviveName.isEmpty()) {
            binding.error3.setVisibility(View.VISIBLE);
            binding.error3.setText("Please enter gender");
            isGender= false;
        }else{
            isGender=true;
        }
        //
//        if (binding.etBio.getText().toString().isEmpty()) {
//            binding.error4.setVisibility(View.VISIBLE);
//            binding.error4.setText("Please Enter Bio");
//            isBioValid= false;
//        }else{
//            isBioValid= true;
//        }
        if(isNameValid && isDobValid && isGender ==true){
            return true;
        }
        return false;

    }
    private void textWatcher() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.error1.setVisibility(View.GONE);
                binding.error2.setVisibility(View.GONE);
                binding.error3.setVisibility(View.GONE);
                binding.error4.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        };
        binding.etName.addTextChangedListener(tw);
        binding.etDob.addTextChangedListener(tw);
        binding.etGender.addTextChangedListener(tw);
        binding.etBio.addTextChangedListener(tw);
    }


    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("name", binding.etName.getText().toString());
        body.addProperty("gender", binding.etGender.getText().toString());
        body.addProperty("bio_data", binding.etBio.getText().toString());
        body.addProperty("dob", binding.etDob.getText().toString());
        body.addProperty("update_type", "JoinNow");
        body.addProperty("signup_stage", 2);

        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<JoinNowModle> call = api.putJoinNowResponse(access_token,"application/json",body);
        call.enqueue(new Callback<JoinNowModle>() {
            @Override
            public void onResponse(Call<JoinNowModle> call, Response<JoinNowModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), JoinNowProfileActivity.class);
                    startActivity(i);
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Invalid Emaileee Id"))
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
            public void onFailure(Call<JoinNowModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.joinbackbtn1:
                //  onBackPressed();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.privacytxt:
                Utility.preventTwoClick(v);
                Intent j = new Intent(this, BrowserActivity.class)
                        .putExtra(Constants.TITLE, this.getString(R.string.terms_of_services_and_privacy_policy))
                        .putExtra(Constants.URL, Constants.PRIVACY_POLICY);
                startActivity(j);
                break;
        }
    }
}