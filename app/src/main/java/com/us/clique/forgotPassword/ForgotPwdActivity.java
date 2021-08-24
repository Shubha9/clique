package com.us.clique.forgotPassword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Patterns;
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
import com.us.clique.databinding.ActivityForgotPwdBinding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPwdActivity extends AppCompatActivity {
    ActivityForgotPwdBinding binding;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;

    ProgressDialog loading;
    UserSessionManager session;
    String accesss_token = "";
    String globalAccesss_token = " ";
    String email="";
boolean isEmailValid=false,isPhoneValid=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_forgot_pwd);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(ForgotPwdActivity.this,
                R.layout.activity_forgot_pwd);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setForgotPwd(bottomNavigationViewModel);
        globalAccesss_token = "cpk5sre43kpsprytujhjatquwevgtpljkg?e21nfo5k#qaqwe6thnbde";
        textWatcher() ;
     /*   session = new UserSessionManager(getApplication());
        accesss_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);*/
//        Log.d("accesss_token", accesss_token.toString());


        binding.pwdbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.frwdedit.setOnKeyListener(new View.OnKeyListener() {
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
        binding.frwdedit.setFilters(new InputFilter[]{filter});
        if (binding.frwdedit.getText().toString().startsWith(" ")) {
            binding.error.setVisibility(View.VISIBLE);
            binding.error.setText("Space Not Valid");

        }
        String inputText = binding.frwdedit.getText().toString();

        if(!(inputText.length()>0) )
        {
            binding.frwdbtn.setEnabled(false);
            binding.frwdbtn.setBackgroundResource(R.drawable.button_bg_enable);

        }else {
            binding.frwdbtn.setEnabled(true);
            binding.frwdbtn.setBackgroundResource(R.drawable.button_background);
            binding.frwdbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SetValidation()) {
                        callApi();
                    }
                }
            });

        }
    }


    public boolean SetValidation() {
        String phone=binding.frwdedit.getText().toString().trim();
        if (binding.frwdedit.getText().toString().isEmpty()) {
            binding.error.setVisibility(View.VISIBLE);
            binding.error.setText(getResources().getString(R.string.email_error));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.frwdedit.getText().toString()).matches()&& !Pattern.matches("[0-9]+", phone)) {
            binding.error.setVisibility(View.VISIBLE);
            binding.error.setText("Invalid Email or Phone Number");
            return false;
        }
//        }else if(Pattern.matches("[0-9]+", phone)) {
//            if (phone.length() < 6 || phone.length() > 13) {
//
//                binding.error.setVisibility(View.VISIBLE);
//                binding.error.setText("Invalid Phone Number");
//                return false;
//            }
//            return false;
//        }
        else
        {
            return true;
        }


    }
    private void textWatcher() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.error.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        };
        binding.frwdedit.addTextChangedListener(tw);

    }
    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("email", binding.frwdedit.getText().toString());
        body.addProperty("pwd_type", "ResetLink");
        email = binding.frwdedit.getText().toString();
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ForgotPasswordModle> call = api.postForgotPasswordResponse(globalAccesss_token,"application/json",body);
        call.enqueue(new Callback<ForgotPasswordModle>() {
            @Override
            public void onResponse(Call<ForgotPasswordModle> call, Response<ForgotPasswordModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Integer otp;
                    ForgotPasswordModle forgotPasswordModle = response.body();
                    otp = forgotPasswordModle.getData().getOtp();
                    String otpString;
                    otpString = String.valueOf(otp);;
                    Intent i = new Intent(ForgotPwdActivity.this, Forgot_pwd_2Activity.class);
                    i.putExtra("email",email);
                    i.putExtra("otp", otpString);
                    startActivity(i);
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Invalid Email Id"))
                        {
                            binding.error.setVisibility(View.VISIBLE);
                            binding.error.setText(jObjError.getString("message"));
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
            public void onFailure(Call<ForgotPasswordModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

}

