package com.us.clique.forgotPassword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.ActivityForgotPwd2Binding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Forgot_pwd_2Activity extends AppCompatActivity implements View.OnClickListener {
    ActivityForgotPwd2Binding binding;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ProgressDialog loading;
    UserSessionManager session;
    String accesss_token = "";
    String globalAccesss_token = " ", email = "";
    String otp;
    String access_token = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_forgot_pwd_2);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(Forgot_pwd_2Activity.this,
                R.layout.activity_forgot_pwd_2);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setForgotPwd2(bottomNavigationViewModel);
        globalAccesss_token = "cpk5sre43kpsprytujhjatquwevgtpljkg?e21nfo5k#qaqwe6thnbde";
        session = new UserSessionManager(this);

        if (getIntent().getExtras() != null) {
            otp = getIntent().getStringExtra("otp");
            email = getIntent().getStringExtra("email");
        }

       binding.otpbackbtn.setOnClickListener(this);
        setOTPInputs();

        binding.otp4.setOnKeyListener(new View.OnKeyListener() {
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

       /* mtext3=mtext1.concat().mtext2;
        String otp1,opt2,otp3,otp4;
        otp1 = binding.otp1.getText().toString();
        opt2 = binding.otp2.getText().toString();
        otp3 = binding.otp3.getText().toString();
        otp4 = binding.otp4.getText().toString();*/
        binding.ckOtpAutoFill.setOnClickListener(this);

    }
    private void processButtonByTextLength() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i=start; i < end; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };


        binding.otp1.setFilters(new InputFilter[]{filter});
        binding.otp2.setFilters(new InputFilter[]{filter});
        binding.otp3.setFilters(new InputFilter[]{filter});
        binding.otp4.setFilters(new InputFilter[]{filter});
        String inputText = binding.otp1.getText().toString();
        String inputText2 = binding.otp2.getText().toString();
        String inputText3 = binding.otp3.getText().toString();
        String inputText4 = binding.otp4.getText().toString();

        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(1);
        binding.otp1.setFilters(filterArray);

        InputFilter[] filterArray1 = new InputFilter[1];
        filterArray1[0] = new InputFilter.LengthFilter(1);
        binding.otp2.setFilters(filterArray);

        InputFilter[] filterArray2 = new InputFilter[1];
        filterArray2[0] = new InputFilter.LengthFilter(1);
        binding.otp3.setFilters(filterArray);

        InputFilter[] filterArray3 = new InputFilter[1];
        filterArray3[0] = new InputFilter.LengthFilter(1);
        binding.otp4.setFilters(filterArray);


        if(!(inputText.length()>0 && inputText2.length()>0 && inputText3.length()>0 && inputText4.length()>0))
        {
            binding.btnCreate.setEnabled(false);
            binding.btnCreate.setBackgroundResource(R.drawable.button_bg_enable);

        }else {
            binding.btnCreate.setEnabled(true);
            binding.btnCreate.setBackgroundResource(R.drawable.button_background);
            binding.btnCreate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (SetValidation()) {
                    otp = binding.otp1.getText().toString() + binding.otp2.getText().toString() + binding.otp3.getText().toString() + binding.otp4.getText().toString();
                    callApi();
//                    }
                }
            });

        }
    }
    private void setOTPInputs() {
        binding.otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        });
        binding.otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp3.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        });
        binding.otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().trim().isEmpty()) {
                    binding.otp4.requestFocus();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        });
        binding.otp4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!s.toString().trim().isEmpty()) {
                    binding.otp4.requestFocus();
//                    binding.btnCreate.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            otp = binding.otp1.getText().toString() + binding.otp2.getText().toString() + binding.otp3.getText().toString() + binding.otp4.getText().toString();
//                            callApi();
//
//                     /*       Toast.makeText(getApplicationContext(), "Successfull", Toast.LENGTH_SHORT).show();
//                            Intent i = new Intent(getApplicationContext(), NewPwdActivity.class);
//                            startActivity(i);*/
//                        }
//
//                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        });
    }

    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("otp", otp);
        body.addProperty("email", email);
        body.addProperty("pwd_type", "OTP_Password");

        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ResetOtpModle> call = api.postResetOtpResponse(globalAccesss_token, "application/json", body);
        call.enqueue(new Callback<ResetOtpModle>() {
            @Override
            public void onResponse(Call<ResetOtpModle> call, Response<ResetOtpModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    ResetOtpModle resetOtpModle = response.body();
                    if (!resetOtpModle.getStatus().equals(true)){
                        Intent i = new Intent(getApplicationContext(), NewPwdActivity.class);
                        startActivity(i);
                    }else {
                        binding.otpError.setVisibility(View.VISIBLE);
                        binding.otpError.setText("Invalid OTP");
                    }


                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Invalid OTP")) {
                        //    Toast.makeText(getApplicationContext(), "Invalid OTP", Toast.LENGTH_SHORT).show();
                            binding.otpError.setVisibility(View.VISIBLE);
                            binding.otpError.setText("Invalid OTP");
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
            public void onFailure(Call<ResetOtpModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

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
            case R.id.ck_otpAutoFill:
                char[] ch = new char[otp.length()];
                for (int i = 0; i < otp.length(); i++) {
                    ch[i] = otp.charAt(i);
                }
                binding.otp1.setText(String.valueOf(ch[0]));
                binding.otp2.setText(String.valueOf(ch[1]));
                binding.otp3.setText(String.valueOf(ch[2]));
                binding.otp4.setText(String.valueOf(ch[3]));
                break;

            case R.id.otpbackbtn:
                onBackPressed();
                break;
        }
    }
}