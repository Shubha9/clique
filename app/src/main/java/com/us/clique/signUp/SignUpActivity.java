package com.us.clique.signUp;

import static com.us.clique.utils.Utility.isPasswordValidMethod;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.BrowserActivity;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.ActivitySignUpBinding;
import com.us.clique.joinNow.JoinNowActivity;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;
import com.us.clique.networkUtils.Constants;
import com.us.clique.utils.Utility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivitySignUpBinding binding;
    boolean isEmailValid = false, isPasswordValid = false, isConfirmValid = false, isCheckbox = false;

    ProgressDialog loading;
    UserSessionManager session;
    String globalAccesss_token = "cpk5sre43kpsprytujhjatquwevgtpljkg?e21nfo5k#qaqwe6thnbde", data = "false", inputText, inputText2, inputText3;
    //  String access_token=" ",userId=" ";
    SignUpModle signUpModle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(SignUpActivity.this,
                R.layout.activity_sign_up);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setSignupView(bottomNavigationViewModel);
        binding.backbtn.setOnClickListener(this);
        binding.signupbutton.setOnClickListener(this);
        binding.tvTermsCond.setOnClickListener(this);
        session = new UserSessionManager(this);

        textWatcher();

        binding.checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    isCheckbox = true;
//                    binding.tvTermsCond.setVisibility(View.INVISIBLE);
                    data = "true";

                } else {
                    isCheckbox = false;
//                    binding.tvTermsCond.setTextColor(ContextCompat.getColor(context, R.color.red));                    data = "true";
                    data = "false";

                }
                if(inputText!=null) {
                    if ((inputText.length() > 0 && inputText2.length() > 0 && inputText3.length() > 0 && data.equals("true"))) {
                        binding.signupbutton.setEnabled(true);
                        binding.signupbutton.setBackgroundResource(R.drawable.button_background);
                    } else {
                        binding.signupbutton.setEnabled(false);
                        binding.signupbutton.setBackgroundResource(R.drawable.button_bg_enable);

                    }
                }else{
                    binding.signupbutton.setEnabled(false);
                    binding.signupbutton.setBackgroundResource(R.drawable.button_bg_enable);
                }

            }
        });

        binding.editsignup.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                int action = event.getAction();
                // Only process key up action, otherwise this listener will be triggered twice because of key down action.
                if (action == KeyEvent.ACTION_UP) {
                    processButtonByTextLength();
                }
                return false;
            }


        });

    }

    public void ShowHidePassword(View view) {

        if (view.getId() == R.id.iv_eye_enable) {

            if (binding.password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.eyeimg);

                //Show Password
                binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.password.setSelection(binding.password.getText().length());

            } else {
                ((ImageView) (view)).setImageResource(R.drawable.eyedisable);

                //Hide Password
                binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.password.setSelection(binding.password.getText().length());

            }
        }
        if (view.getId() == R.id.iv_eye_enable2) {

            if (binding.confirmpassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.eyeimg);

                //Show Password
                binding.confirmpassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.confirmpassword.setSelection(binding.confirmpassword.getText().length());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.eyedisable);
                binding.confirmpassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.confirmpassword.setSelection(binding.confirmpassword.getText().length());
            }
        }
    }

    private void processButtonByTextLength() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < start - 1; i++) {
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
                for (int i = start; i < start - 1; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter filter3 = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i = start; i < start - 1; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(6); //Filter to 10 characters
        binding.password .setFilters(filters);

        InputFilter[] filters1 = new InputFilter[1];
        filters1[0] = new InputFilter.LengthFilter(6); //Filter to 10 characters
        binding.confirmpassword .setFilters(filters1);
        binding.editsignup.setFilters(new InputFilter[]{filter});
        binding.password.setFilters(new InputFilter[]{filter2});
        binding.confirmpassword.setFilters(new InputFilter[]{filter3});
        if (binding.editsignup.getText().toString().startsWith(" ")) {
            binding.tvError.setVisibility(View.VISIBLE);
            binding.tvError.setText("Space Not Valid");
        }
        if (binding.password.getText().toString().startsWith(" ")) {
            binding.pwderror.setVisibility(View.VISIBLE);
            binding.pwderror.setText("Space Not Valid");
        }
        if (binding.confirmpassword.getText().toString().startsWith(" ")) {
            binding.pwderror2.setVisibility(View.VISIBLE);
            binding.pwderror2.setText("Space Not Valid");
        }

        inputText = binding.editsignup.getText().toString();
        inputText2 = binding.password.getText().toString();
        inputText3 = binding.confirmpassword.getText().toString();
        if ((inputText.length() > 0 && inputText2.length() > 0 && inputText3.length() > 0 && data.equals("true"))) {
            binding.signupbutton.setEnabled(true);
            binding.signupbutton.setBackgroundResource(R.drawable.button_background);
        } else {
            binding.signupbutton.setEnabled(false);
            binding.signupbutton.setBackgroundResource(R.drawable.button_bg_enable);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbtn:
                onBackPressed();

                break;
            case R.id.signupbutton:
                if (SetValidation()) {
                    callApi();
                }
                break;
            case R.id.tv_terms_cond:
                Utility.preventTwoClick(v);
                Intent k = new Intent(this, BrowserActivity.class)
                        .putExtra(Constants.TITLE, this.getString(R.string.terms_and_conditions))
                        .putExtra(Constants.URL, Constants.TOS);
                startActivity(k);
                break;
        }
    }

    public boolean SetValidation() {
        if (binding.editsignup.getText().toString().isEmpty()) {
            binding.tvError.setVisibility(View.VISIBLE);
            binding.tvError.setText("Please enter email address");
            isEmailValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editsignup.getText().toString()).matches()) {
            binding.tvError.setVisibility(View.VISIBLE);
            binding.tvError.setText(getResources().getString(R.string.error_invalid_email));
            isEmailValid = false;
        } else {
            isEmailValid = true;
        }

        if (binding.password.getText().toString().isEmpty()) {
            binding.pwderror.setVisibility(View.VISIBLE);
            binding.pwderror.setText(getResources().getString(R.string.password_error));
            isPasswordValid = false;
      } else if ((binding.password.getText().toString().matches("^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$"))) {
            isPasswordValid = true;
        }else if(binding.password.getText().length()<6 && binding.password.getText().length()>6 ){
            binding.pwderror.setVisibility(View.VISIBLE);
            binding.pwderror.setText(getResources().getString(R.string.error_invalid_password));
            isPasswordValid = false;
        } else {
            binding.pwderror.setVisibility(View.VISIBLE);
            binding.pwderror.setText("Please enter atleast 1 Capital letter, special character and  number");
            isPasswordValid = false;
        }

        if (binding.confirmpassword.getText().toString().isEmpty()) {
            binding.pwderror2.setVisibility(View.VISIBLE);
            binding.pwderror2.setText("Please enter Confirm password");
            isConfirmValid = false;
        } else if (binding.confirmpassword.getText().length() < 6) {
            binding.pwderror2.setVisibility(View.VISIBLE);
            binding.pwderror2.setText(getResources().getString(R.string.error_invalid_password));
            isConfirmValid = false;
        } else if (!binding.confirmpassword.getText().toString().matches(binding.password.getText().toString())) {
            binding.pwderror2.setVisibility(View.VISIBLE);
            binding.pwderror2.setText(getResources().getString(R.string.confirm_pwd_error));
            isConfirmValid = false;
        } else {
            isConfirmValid = true;
        }

        if (!binding.checkbox1.isChecked()) {
            binding.tvTermsCond.setVisibility(View.VISIBLE);
            binding.tvTermsCond.setTextColor(ContextCompat.getColor(context, R.color.red));
            binding.tvTermsCond.setText("Please Accept Terms and Conditions");
            isCheckbox = false;

        } else {
            isCheckbox = true;

        }


        if (isEmailValid && isPasswordValid && isConfirmValid && isCheckbox == true) {
//            callApi();
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
                binding.tvError.setVisibility(View.GONE);
                binding.pwderror.setVisibility(View.GONE);
                binding.pwderror2.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        };
        binding.editsignup.addTextChangedListener(tw);
        binding.password.addTextChangedListener(tw);
        binding.confirmpassword.addTextChangedListener(tw);

    }

    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("email", binding.editsignup.getText().toString());
        body.addProperty("password", binding.password.getText().toString());
        body.addProperty("media_type", "Email");
        body.addProperty("auth_token", "");
        body.addProperty("signup_stage", 1);

        loading = ProgressDialog.show(this, "Signing Up.....", "wait....", false, false);
        Call<SignUpModle> call = api.postSignUpResponse(globalAccesss_token, "application/json", body);
        call.enqueue(new Callback<SignUpModle>() {
            @Override
            public void onResponse(Call<SignUpModle> call, Response<SignUpModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    signUpModle = response.body();
                    session.createUserLoginSession(signUpModle.getData().getAccesstoken());
                    session.createUserId(signUpModle.getData().getUserid());
                    Intent i = new Intent(SignUpActivity.this, JoinNowActivity.class);
                    startActivity(i);
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("User already exists by these details")) {
                            binding.tvError.setVisibility(View.VISIBLE);
                            binding.tvError.setText(jObjError.getString("message"));
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
            public void onFailure(Call<SignUpModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }


}




