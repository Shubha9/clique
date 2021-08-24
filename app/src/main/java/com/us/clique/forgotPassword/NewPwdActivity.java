package com.us.clique.forgotPassword;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.ActivityNewPwdBinding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.us.clique.signIn.SignInActivity;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPwdActivity extends AppCompatActivity implements View.OnClickListener{
    boolean isPasswordValid = false,isConfirmValid=false;
    ActivityNewPwdBinding binding;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;

    ProgressDialog loading;
    UserSessionManager session;
    String accesss_token = "",email =" ";
    String globalAccesss_token = " ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_new_pwd);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(NewPwdActivity.this,
                R.layout.activity_new_pwd);
        bottomNavigationViewModel= new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setNewPwd(bottomNavigationViewModel);
        binding.ivOnBackPressd.setOnClickListener(this);

        globalAccesss_token = "cpk5sre43kpsprytujhjatquwevgtpljkg?e21nfo5k#qaqwe6thnbde";
        session = new UserSessionManager(this);
        email = session.getEmail();
        binding.newpwd.setOnKeyListener(new View.OnKeyListener() {
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

        textWatcher();
    }
    public void showHideNewPassword(View view){

        if(view.getId()==R.id.iv_eye_enable){

            if(binding.newpwd.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.eyeimg);

                //Show Password
                binding.newpwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.newpwd.setSelection(binding.newpwd.getText().length());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.eyedisable);


                binding.newpwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.newpwd.setSelection(binding.newpwd.getText().length());
            }
        }
        if(view.getId()==R.id.iv_eye_enable2){

            if(binding.newpwd2.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.eyeimg);

                //Show Password
                binding.newpwd2.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.newpwd2.setSelection(binding.newpwd2.getText().length());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.eyedisable);

                //Hide Password
                binding.newpwd2.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.newpwd2.setSelection(binding.newpwd2.getText().length());
            }
        }
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
        InputFilter filter1 = new InputFilter() {
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
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(6); //Filter to 10 characters
        binding.newpwd .setFilters(filters);

        InputFilter[] filters1 = new InputFilter[1];
        filters1[0] = new InputFilter.LengthFilter(6); //Filter to 10 characters
        binding.newpwd2 .setFilters(filters1);

        binding.newpwd.setFilters(new InputFilter[]{filter});
        binding.newpwd2.setFilters(new InputFilter[]{filter1});
        if (binding.newpwd.getText().toString().startsWith(" ")) {
            binding.error2.setVisibility(View.VISIBLE);
            binding.error2.setText("Space Not Valid");

        } if (binding.newpwd2.getText().toString().startsWith(" ")) {
            binding.error1.setVisibility(View.VISIBLE);
            binding.error1.setText("Space Not Valid");

        }

        String inputText = binding.newpwd.getText().toString();
        String inputText2 = binding.newpwd2.getText().toString();

        if(!(inputText.length()>0 && inputText2.length()>0) )
        {
            binding.newsigninbtn.setEnabled(false);
            binding.newsigninbtn.setBackgroundResource(R.drawable.button_bg_enable);

        }else {
            binding.newsigninbtn.setEnabled(true);
            binding.newsigninbtn.setBackgroundResource(R.drawable.button_background);
            binding.newsigninbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (setPwdValidation()) {
                        callApi();
                    }
                }
            });

        }
    }
    public boolean setPwdValidation() {
        if (binding.newpwd.getText().toString().isEmpty()) {
            binding.error2.setVisibility(View.VISIBLE);
            binding.error2.setText(getResources().getString(R.string.password_error));
            isPasswordValid= false;
        }  else if ((binding.newpwd.getText().toString().matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"))) {
            isPasswordValid = true;
        }else if (binding.newpwd.getText().length() < 6) {
            binding.error2.setVisibility(View.VISIBLE);
            binding.error2.setText(getResources().getString(R.string.error_invalid_password));
            isPasswordValid= false;
        } else{
            binding.error2.setVisibility(View.VISIBLE);
            binding.error2.setText("Please enter atleast 1 Capital letter, special character and  number");
            isPasswordValid=false;
        }
        if (binding.newpwd2.getText().toString().isEmpty()) {
            binding.error1.setVisibility(View.VISIBLE);
            binding.error1.setText("Please enter Confirm password");
            isConfirmValid= false;
        } else if (binding.newpwd2.getText().length() < 6) {
            binding.error1.setVisibility(View.VISIBLE);
            binding.error1.setText(getResources().getString(R.string.error_invalid_password));
            isConfirmValid= false;
        } else if (!binding.newpwd2.getText().toString().matches(binding.newpwd.getText().toString())) {
            binding.error1.setVisibility(View.VISIBLE);
            binding.error1.setText(getResources().getString(R.string.confirm_pwd_error));
            isConfirmValid= false;
        } else {
            isConfirmValid= true;

        }
        if(isPasswordValid&&isConfirmValid==true){
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
                binding.error2.setVisibility(View.GONE);
                binding.error1.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        };
        binding.newpwd.addTextChangedListener(tw);
        binding.newpwd2.addTextChangedListener(tw);

    }
    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("new_password", binding.newpwd.getText().toString());
        body.addProperty("email", email);
        body.addProperty("pwd_type", "ResetPassword");

        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<NewPasswordModle> call = api.postNewPasswordResponse(globalAccesss_token,"application/json",body);
        call.enqueue(new Callback<NewPasswordModle>() {
            @Override
            public void onResponse(Call<NewPasswordModle> call, Response<NewPasswordModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Intent i;
                    i = new Intent(NewPwdActivity.this, SignInActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Invalid Emaileee Id"))
                        {
                            binding.error2.setVisibility(View.VISIBLE);
                            binding.error2.setText(jObjError.getString("message"));
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
            public void onFailure(Call<NewPasswordModle> call, Throwable t) {
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
        switch (v.getId()){
            case R.id.iv_onBackPressd:
                onBackPressed();
                break;
        }
    }
}