package com.us.clique.signIn;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;
import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.BottomNavigationActivity;
import com.us.clique.activites.ChooseInterestsActivity;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.copyPose.CopyPoseActivity;
import com.us.clique.databinding.ActivitySignInBinding;
import com.us.clique.forgotPassword.ForgotPwdActivity;
import com.us.clique.joinNow.JoinNowActivity;
import com.us.clique.joinNowProfile.JoinNowProfileActivity;
import com.us.clique.location.LocationActivity;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.us.clique.networkUtils.Constants;
import com.us.clique.utils.NetworkConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    boolean isEmailValid, isPasswordValid;
    TextInputEditText password;
    ActivitySignInBinding binding;

    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ProgressDialog loading;
    String Email;
    UserSessionManager session;
    String globalAccesss_token = "";
    private CallbackManager callbackManager;
    String fb_id, fname, lname,   fb_email, gender, pic, checkid,fb_mob;
    private HashMap<String, String> facebookInput = new HashMap<>();
    private String oauth_type,name,email,fb_access_token="";
    private String checkName = null;
    private static final String EMAIL = "email";

  //google
    GoogleSignInClient mGoogleSignInClient;
    private static int RC_SIGN_IN = 100;
    String personName,personGivenName,personFamilyName,personEmail,personId,googleAccessToken;
    Uri personPhoto;
    SignInModle signInModle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//      setContentView(R.layout.activity_sign_in);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(SignInActivity.this,
                R.layout.activity_sign_in);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setSignIn(bottomNavigationViewModel);
        globalAccesss_token = "cpk5sre43kpsprytujhjatquwevgtpljkg?e21nfo5k#qaqwe6thnbde";
        session = new UserSessionManager(this);
        callbackManager = CallbackManager.Factory.create();
        binding.ivFb.setOnClickListener(this);
        binding.ivGoogle.setOnClickListener(this);
        // accesss_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
            name  = session.getName();
            email = session.getEmail();
//        Log.d("accesss_token", accesss_token.toString());
//
//        if (session.isUserLoggedIn()) {
//            Intent intent = new Intent(SignInActivity.this, BottomNavigationActivity.class);
//            startActivity(intent);
//            finish();
//        }
        textWatcher();


        binding.backbtn1.setOnClickListener(this);
        binding.editsignin.setOnKeyListener(new View.OnKeyListener() {
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
        binding.forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ForgotPwdActivity.class);
                startActivity(i);
            }
        });
        binding.fbLoginButton.setReadPermissions(Arrays.asList(EMAIL));
        binding.fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                LoginManager.getInstance().logInWithReadPermissions(SignInActivity.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken token = AccessToken.getCurrentAccessToken();
                        Log.e("facebook_id", String.valueOf(token.getToken()));
                        if (AccessToken.getCurrentAccessToken() != null) {
                            fb_access_token = String.valueOf(token.getToken());
                            Log.e("facebook_id", "Fb_access_token:" + String.valueOf(token.getToken()));
                        }
                        getUserDetails(loginResult);
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException error) {
                        error.printStackTrace();
                        Log.e("facebook_id", "exception:" + error);
                    }
                });
//                Intent i = new Intent(getApplicationContext(),ChooseInterestsActivity.class);
//                startActivity(i);
            }

            @Override
            public void onCancel() {
//                // App code
//                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivity(i);
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

/*google singin*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestProfile().requestId().requestIdToken(getString(R.string.google_client_id))
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
       // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);



    }

    private void processButtonByTextLength() {
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                for (int i=start; i <start-1; i++) {
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
                for (int i=start; i <start-1; i++) {
                    if (Character.isWhitespace(source.charAt(i))) {
                        return "";
                    }
                }
                return null;
            }
        };

        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(6); //Filter to 10 characters
        binding.password1 .setFilters(filters);

        binding.editsignin.setFilters(new InputFilter[] { filter });
        binding.password1.setFilters(new InputFilter[] { filter2 });
        if (binding.editsignin.getText().toString().startsWith(" ")) {
            binding.emailerror1.setVisibility(View.VISIBLE);
            binding.emailerror1.setText("Space Not Valid");

        }  if (binding.password1.getText().toString().startsWith(" ")) {
            binding.pwderror.setVisibility(View.VISIBLE);
            binding.pwderror.setText("Space Not Valid");

        }
        String inputText = binding.editsignin.getText().toString();
        String inputText2 = binding.password1.getText().toString();
        if(!(inputText.length()>0 && inputText2.length()>0) )
        {
            binding.signinbutton.setEnabled(false);
            binding.signinbutton.setBackgroundResource(R.drawable.button_bg_enable);

        }else {
            binding.signinbutton.setEnabled(true);
            binding.signinbutton.setBackgroundResource(R.drawable.button_background);
            binding.signinbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (SetValidation()) {
                        callApi();
                    }
                }
            });

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public boolean SetValidation() {
//        isEmailValid = false;
//        isPasswordValid = false;
        // Check for a valid email address.
        if (TextUtils.isEmpty(binding.editsignin.getText().toString())) {
//            String str = binding.editsignin.getText().toString();
//            if(str.length() > 0 && str.contains(" "))
//            {
//                binding.emailerror1.setError("Space is not allowed");
//                binding.emailerror1.setText("");
//            }
            binding.emailerror1.setVisibility(View.VISIBLE);
            binding.emailerror1.setText("Please Enter Email Address");
//            isEmailValid = false;
            isEmailValid= false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editsignin.getText().toString()).matches()) {
            binding.emailerror1.setVisibility(View.VISIBLE);
            binding.emailerror1.setText(getResources().getString(R.string.error_invalid_email));
//            isEmailValid = false;
            isEmailValid= false;
        }else {
            isEmailValid=true;
        }
//

         if (binding.password1.getText().toString().isEmpty()) {
             binding.pwderror.setVisibility(View.VISIBLE);
             binding.pwderror.setText(getResources().getString(R.string.password_error));
             isPasswordValid = false;
         }else if(binding.password1.getText().toString().length()<6){
            binding.pwderror.setVisibility(View.VISIBLE);
            binding.pwderror.setText(getResources().getString(R.string.error_invalid_password));
            isPasswordValid= false;
//            isPasswordValid = false;
        } else {

//            binding.emailerror1.setText("");
//            isEmailValid = true;
            isPasswordValid= true;
        }
         if(isEmailValid&&isPasswordValid==true)
         {
             return true;
         }
         return false;
    }
    public void ShowHidePass(View view){

        if(view.getId()==R.id.iv_eye_enable){

            if(binding.password1.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                ((ImageView)(view)).setImageResource(R.drawable.eyeimg);

                //Show Password
                binding.password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                binding.password1.setSelection(binding.password1.getText().length());
            }
            else{
                ((ImageView)(view)).setImageResource(R.drawable.eyedisable);

                binding.password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
                binding.password1.setSelection(binding.password1.getText().length());
            }
        }
    }
    private void textWatcher() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.emailerror1.setVisibility(View.GONE);
                binding.pwderror.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        };
        binding.editsignin.addTextChangedListener(tw);
        binding.password1.addTextChangedListener(tw);
    }


    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("email", binding.editsignin.getText().toString());
        body.addProperty("password", binding.password1.getText().toString());
        body.addProperty("user_type","user");

        loading = ProgressDialog.show(this, "Signing In.....", "wait....", false, false);
        Call<SignInModle> call = api.postSigninResponse(globalAccesss_token,"application/json",body);
        call.enqueue(new Callback<SignInModle>() {
            @Override
            public void onResponse(Call<SignInModle> call, Response<SignInModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    signInModle = response.body();
                    SignInModle.Data data = signInModle.getData();
                        session.createUserLoginSession(data.getAccesstoken());
                        session.createEmail(data.getEmail());
                        session.createName(data.getName());
                        session.createUserId(data.getUserid());
                    signupStage();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("Invalid username/password"))
                        {
                            binding.emailerror1.setVisibility(View.VISIBLE);
                            binding.emailerror1.setText(jObjError.getString("message"));
                        }
                        else {
                            binding.emailerror1.setVisibility(View.VISIBLE);
                            binding.emailerror1.setText(jObjError.getString("message"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();


                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                }
            }
            @Override
            public void onFailure(Call<SignInModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_fb:
                binding.fbLoginButton.performClick();
                break;
            case R.id.iv_google:
                binding.googleSignInButton.performClick();
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.backbtn1:
                onBackPressed();
                break;
        }
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                JSONObject json = response.getJSONObject();
                String email = null;
                Log.d("MainActivity", "json:" + json.toString());

                try {
                    fb_id = json.getString("id");
                    fname = json.getString("first_name");
                    lname = json.getString("last_name");
                    fb_email = json.getString("email");


                    Log.d("TAG", "FaceBook_Login:" + fb_id + fname+fb_email+fb_id+fb_access_token);
//                    session.createUserLoginSession(fb_id, fb_email, "", fname, lname,"",
//                            pic_url,fb_access_token);

                    facebookInput.put("first_name", fname);
                    facebookInput.put("email", fb_email);
                    facebookInput.put("facebook_id", fb_id);
                    checkid = fb_id;
                    checkName = "Facebook";


                    /*callLoader(2);*/
                    oauth_type = Constants.KEY_TYPE_FACEBOOK;

                    NetworkConnection networkConnection=new NetworkConnection(getApplicationContext());
                    if(networkConnection.isInternetAvailable())
                    {

                        callFBApi();
                    }else
                    {
//                        setContentView(R.layout.activity_networksignal);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            private void callFBApi() {
                Api api = ApiClient.getClient().create(Api.class);
                JsonObject body = new JsonObject();
                body.addProperty("name", fname);
                body.addProperty("email", fb_email);
                body.addProperty("password", "");
                body.addProperty("media_type","Facebook");
                body.addProperty("auth_token",fb_access_token);
                body.addProperty("signup_stage",1);
                loading = ProgressDialog.show(SignInActivity.this, "FaceBook Signing In.....", "wait....", false, false);
                Call<SignInModle> call = api.PostFaceBookLogin(globalAccesss_token,"application/json",body);
                call.enqueue(new Callback<SignInModle>() {
                    @Override
                    public void onResponse(Call<SignInModle> call, Response<SignInModle> response) {
                        loading.cancel();
                        if (response.isSuccessful()) {
////                            FaceBookLogin faceBookLogin = response.body();
//                            FaceBookLogin.Data data = faceBookLogin.getData();
//                            session.createUserLoginSession(data.getAccesstoken());
//                            session.createEmail(data.getEmail());
//                            session.createName(data.getName());
//                            session.createUserId(data.getUserid());
                          //  signInModle = response.body();

//                            Intent i = new Intent(SignInActivity.this, JoinNowActivity.class);
//                            startActivity(i);
//                            finish();
                            signInModle = response.body();
                            SignInModle.Data data = signInModle.getData();
                            session.createUserLoginSession(data.getAccesstoken());
                            session.createEmail(data.getEmail());
                            session.createName(data.getName());
                            session.createUserId(data.getUserid());
                            signupStage();
                        } else {
                            try {
                                String error_message = response.errorBody().string();
                                JSONObject jObjError = new JSONObject(error_message);

                                if ((jObjError.getString("message")).equals("Invalid username/password"))
                                {
                                    binding.emailerror1.setVisibility(View.VISIBLE);
                                    binding.emailerror1.setText(jObjError.getString("message"));
                                }
                                else {
                                    binding.emailerror1.setVisibility(View.VISIBLE);
                                    binding.emailerror1.setText(jObjError.getString("message"));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();


                            } catch (IOException e) {
                                e.printStackTrace();

                            }

                        }
                    }



                    @Override
                    public void onFailure(Call<SignInModle> call, Throwable t) {
                        loading.cancel();
                        // showToast(getApplicationContext(), t.toString());

                    }
                });
            }
        });
        Bundle permission_param = new Bundle();
//        parameters.putString("fields", "id,email,first_name,last_name,gender");
        permission_param.putString("fields", "id,first_name,last_name,email,picture.width(250).height(250)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                personName = acct.getDisplayName();
                personGivenName = acct.getGivenName();
                personFamilyName = acct.getFamilyName();
                personEmail = acct.getEmail();
                personId = acct.getId();
                personPhoto = acct.getPhotoUrl();
                googleAccessToken =  acct.getIdToken();
                callGoogleApi();

            }


        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
          Log.d("message",e.toString());
        }
    }
    private void callGoogleApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("name", personName);
        body.addProperty("email", personEmail);
        body.addProperty("password", "");
        body.addProperty("media_type","Google");
        body.addProperty("auth_token",googleAccessToken);
       // body.addProperty("auth_token",123);
        body.addProperty("signup_stage",1);
        loading = ProgressDialog.show(SignInActivity.this, "Google Signing In.....", "wait....", false, false);
        Call<SignInModle> call = api.PostGoogleLogin(globalAccesss_token,"application/json",body);
        call.enqueue(new Callback<SignInModle>() {
            @Override
            public void onResponse(Call<SignInModle> call, Response<SignInModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
//                    FaceBookLogin faceBookLogin = response.body();
//                    FaceBookLogin.Data data = faceBookLogin.getData();
//                    session.createUserLoginSession(data.getAccesstoken());
//                    session.createEmail(data.getEmail());
//                    session.createName(data.getName());
//                    session.createUserId(data.getUserid());
//                    Intent i = new Intent(SignInActivity.this, JoinNowActivity.class);
//                    startActivity(i);

                    signInModle = response.body();
                    SignInModle.Data data = signInModle.getData();
                    session.createUserLoginSession(data.getAccesstoken());
                    session.createEmail(data.getEmail());
                    session.createName(data.getName());
                    session.createUserId(data.getUserid());
                    signupStage();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Invalid username/password"))
                        {
                            binding.emailerror1.setVisibility(View.VISIBLE);
                            binding.emailerror1.setText(jObjError.getString("message"));
                        }
                        else {
                            binding.emailerror1.setVisibility(View.VISIBLE);
                            binding.emailerror1.setText(jObjError.getString("message"));
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();


                    } catch (IOException e) {
                        e.printStackTrace();

                    }

                }
            }



            @Override
            public void onFailure(Call<SignInModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });
    }
        private void signupStage(){
        if (signInModle.getData().getSignupStage().equals("1")){
            Intent i = new Intent(SignInActivity.this, JoinNowActivity.class);
            startActivity(i);
        }else if (signInModle.getData().getSignupStage().equals("2")){
            Intent i = new Intent(SignInActivity.this, JoinNowProfileActivity.class);
            startActivity(i);
        }else if (signInModle.getData().getSignupStage().equals("3")){
            Intent i = new Intent(SignInActivity.this, CopyPoseActivity.class);
            startActivity(i);
        }else if (signInModle.getData().getSignupStage().equals("4")){
            Intent i = new Intent(SignInActivity.this, LocationActivity.class);
            startActivity(i);
        }else if (signInModle.getData().getSignupStage().equals("5")){
            Intent i = new Intent(SignInActivity.this, ChooseInterestsActivity.class);
            startActivity(i);
        }else if (signInModle.getData().getSignupStage().equals("6")){
            Intent i = new Intent(SignInActivity.this, BottomNavigationActivity.class);
            startActivity(i);
        }

        }
}