package com.us.clique.activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.GenderAdapter;
import com.us.clique.bottomNavigation.fragments.ProfileFragment;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;

import com.us.clique.bottomNavigation.interfaces.MovieNameInterface;
import com.us.clique.bottomNavigation.modle.EditProfileModle;
import com.us.clique.bottomNavigation.modle.ProfileImageDeleteModle;
import com.us.clique.bottomNavigation.modle.ProfileModle;
import com.us.clique.databinding.ActivityEditeProfileBinding;
import com.us.clique.modle.MoviesListModle;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;


public class EditeProfileActivity extends AppCompatActivity implements MovieNameInterface, View.OnClickListener {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityEditeProfileBinding binding;
    GenderAdapter genderAdapter;
    ArrayList<MoviesListModle> moviesListModles = new ArrayList<>();
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    private static final int REQUEST_PERMISSIONS = 100;
    private static final int REQUEST_PROFILE = 1;
    ProgressDialog loading,loading2;
    UserSessionManager session;
    String access_token = "", userId = "";
    String gender;
    int profileImagePosition = 0;
    String profile1Base64="",profile2Base64="",profile3Base64="",profile4Base64="";
    Bitmap bitmap;
    List<ProfileModle.User> usersDetailsList = new ArrayList<>();
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(EditeProfileActivity.this,
                R.layout.activity_edite_profile);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setEtProfile(bottomNavigationViewModel);
        OnlineUserStatus onlineUserStatus=new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();

        callProfileApi();

        binding.rlSelectClass.setOnClickListener(this);
        binding.ivOnBackPress.setOnClickListener(this);
        binding.ivProfile1.setOnClickListener(this);
        binding.ivProfile2.setOnClickListener(this);
        binding.ivProfile3.setOnClickListener(this);
        binding.ivProfile4.setOnClickListener(this);
        binding.ivEdit.setOnClickListener(this);
        binding.ivProfile2Cross.setOnClickListener(this);
        binding.ivProfile3Cross.setOnClickListener(this);
        binding.ivProfile4Cross.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto, 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        onCaptureImageResult(data);

/*
                        switch (profileImagePosition) {

                            case 1:
                                binding.ivProfile1Bg.setVisibility(View.VISIBLE);
                                binding.ivProfile1Bg.setImageBitmap(selectedImage);
                                binding.ivProfile1.setVisibility(View.GONE);
                                binding.ivProfile1Cross.setVisibility(View.VISIBLE);
                                break;
                            case 2:
                                binding.ivProfile2Bg.setVisibility(View.VISIBLE);
                                binding.ivProfile2Bg.setImageBitmap(selectedImage);
                                binding.ivProfile2.setVisibility(View.GONE);
                                binding.ivProfile2Cross.setVisibility(View.VISIBLE);
                                break;
                            case 3:
                                binding.ivProfile3Bg.setVisibility(View.VISIBLE);
                                binding.ivProfile3Bg.setImageBitmap(selectedImage);
                                binding.ivProfile3.setVisibility(View.GONE);
                                binding.ivProfile3Cross.setVisibility(View.VISIBLE);
                                break;
                            case 4:
                                binding.ivProfile4Bg.setVisibility(View.VISIBLE);
                                binding.ivProfile4Bg.setImageBitmap(selectedImage);
                                binding.ivProfile4.setVisibility(View.GONE);
                                binding.ivProfile4Cross.setVisibility(View.VISIBLE);
                                break;
                        }
*/
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
           /*             Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                if (picturePath != null) {
                                    switch (profileImagePosition) {
                                        case 1:
                                            binding.ivProfile1Bg.setVisibility(View.VISIBLE);
                                            binding.ivProfile1Bg.setImageURI(selectedImage);
                                            binding.ivProfile1.setVisibility(View.GONE);
                                            binding.ivProfile1Cross.setVisibility(View.VISIBLE);
                                            break;
                                        case 2:
                                            binding.ivProfile2Bg.setVisibility(View.VISIBLE);
                                            binding.ivProfile2Bg.setImageURI(selectedImage);
                                            binding.ivProfile2.setVisibility(View.GONE);
                                            binding.ivProfile2Cross.setVisibility(View.VISIBLE);
                                            break;
                                        case 3:
                                            binding.ivProfile3Bg.setVisibility(View.VISIBLE);
                                            binding.ivProfile3Bg.setImageURI(selectedImage);
                                            binding.ivProfile3.setVisibility(View.GONE);
                                            binding.ivProfile3Cross.setVisibility(View.VISIBLE);
                                            break;
                                        case 4:
                                            binding.ivProfile4Bg.setVisibility(View.VISIBLE);
                                            binding.ivProfile4Bg.setImageURI(selectedImage);
                                            binding.ivProfile4.setVisibility(View.GONE);
                                            binding.ivProfile4Cross.setVisibility(View.VISIBLE);
                                            break;
                                    }
                                }
                                    //  binding.ivProfile.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                                cursor.close();
                            }
                        }
*/
                        onSelectFromGalleryResult(data);
                    }
                    break;
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        if (bitmap != null) {
            switch (profileImagePosition) {
                case 1:
                    binding.ivProfile1Bg.setVisibility(View.VISIBLE);
                    binding.ivProfile1Bg.setImageBitmap(bitmap);
                    binding.ivProfile1.setVisibility(View.GONE);
                    binding.ivEdit.setVisibility(View.VISIBLE);
                    profile1Base64=getStringImage(bitmap);
                    Log.v(TAG,"base64 1"+profile1Base64);
                    break;
                case 2:
                    binding.ivProfile2Bg.setVisibility(View.VISIBLE);
                    binding.ivProfile2Bg.setImageBitmap(bitmap);
                    binding.ivProfile2.setVisibility(View.GONE);
                    binding.ivProfile2Cross.setVisibility(View.VISIBLE);
                    profile2Base64=getStringImage(bitmap);
                    Log.v(TAG,"base64 2"+profile2Base64);
                    break;
                case 3:
                    binding.ivProfile3Bg.setVisibility(View.VISIBLE);
                    binding.ivProfile3Bg.setImageBitmap(bitmap);
                    binding.ivProfile3.setVisibility(View.GONE);
                    binding.ivProfile3Cross.setVisibility(View.VISIBLE);
                    profile3Base64=""+getStringImage(bitmap);
                    Log.v(TAG,"base64 3"+profile3Base64);
                    break;
                case 4:
                    binding.ivProfile4Bg.setVisibility(View.VISIBLE);
                    binding.ivProfile4Bg.setImageBitmap(bitmap);
                    binding.ivProfile4.setVisibility(View.GONE);
                    binding.ivProfile4Cross.setVisibility(View.VISIBLE);
                    profile4Base64=getStringImage(bitmap);
                    Log.v(TAG,"base64 4"+profile4Base64);
                    break;
            }

        }

    }
    private void onCaptureImageResult(Intent data) {
        bitmap = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//         binding.ivProfile.setImageBitmap(bitmap);
        if (bitmap != null){
            switch (profileImagePosition) {
                case 1:
                    profile1Base64="";
                    binding.ivProfile1Bg.setVisibility(View.VISIBLE);
                    binding.ivProfile1Bg.setImageBitmap(bitmap);
                    binding.ivProfile1.setVisibility(View.GONE);
                    binding.ivEdit.setVisibility(View.VISIBLE);
                    profile1Base64=getStringImage(bitmap);
                    Log.v(TAG,"base64"+profile1Base64);
                    break;
                case 2:
                    profile2Base64="";
                    binding.ivProfile2Bg.setVisibility(View.VISIBLE);
                    binding.ivProfile2Bg.setImageBitmap(bitmap);
                    binding.ivProfile2.setVisibility(View.GONE);
                    binding.ivProfile2Cross.setVisibility(View.VISIBLE);
                    profile2Base64=getStringImage(bitmap);
                    Log.v(TAG,"base64"+profile2Base64);
                    break;
                case 3:
                    profile3Base64="";
                    binding.ivProfile3Bg.setVisibility(View.VISIBLE);
                    binding.ivProfile3Bg.setImageBitmap(bitmap);
                    binding.ivProfile3.setVisibility(View.GONE);
                    binding.ivProfile3Cross.setVisibility(View.VISIBLE);
                    profile3Base64=getStringImage(bitmap);
                    Log.v(TAG,"base64"+profile3Base64);
                    break;
                case 4:
                    profile4Base64="";
                    binding.ivProfile4Bg.setVisibility(View.VISIBLE);
                    binding.ivProfile4Bg.setImageBitmap(bitmap);
                    binding.ivProfile4.setVisibility(View.GONE);
                    binding.ivProfile4Cross.setVisibility(View.VISIBLE);
                    profile4Base64=getStringImage(bitmap);
                    Log.v(TAG,"base64"+profile4Base64);
                    break;
            }


        }

        //  Log.v(TAG,"base64"+CopyPoseImage);//perfectly string base64 to image


    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 100);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    private void initializeNewChatClassRecycler() {
        moviesListModles.clear();
        genderAdapter = new GenderAdapter(getApplicationContext(), moviesListModles, "Select Gender", this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        binding.rvGender.setLayoutManager(mLayoutManager);
        binding.rvGender.setItemAnimator(new DefaultItemAnimator());
        moviesListModles.add(new MoviesListModle("Male"));
        moviesListModles.add(new MoviesListModle("female"));
        moviesListModles.add(new MoviesListModle("other"));
        binding.rvGender.setAdapter(genderAdapter);
    }


    private void selectClass() {
        if (binding.expandableLayoutGender.isExpanded()) {
            binding.ivSelectGender.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_expend));
            binding.expandableLayoutGender.collapse();
        } else {
            binding.ivSelectGender.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_collapse));
            binding.expandableLayoutGender.expand();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_select_class:
                selectClass();
                initializeNewChatClassRecycler();
                break;
            case R.id.iv_onBackPress:
                onBackPressed();
                break;
            case R.id.iv_profile1:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission not available requesting permission");
                    ActivityCompat.requestPermissions(EditeProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
                } else {
                    Log.d(TAG, "Permission has already granted");
                    profileImagePosition = 1;
                    selectImage();
                }
                break;
            case R.id.iv_profile2:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission not available requesting permission");
                    ActivityCompat.requestPermissions(EditeProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
                } else {
                    Log.d(TAG, "Permission has already granted");
                    profileImagePosition = 2;
                    selectImage();
                }
                break;
            case R.id.iv_profile3:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission not available requesting permission");
                    ActivityCompat.requestPermissions(EditeProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
                } else {
                    Log.d(TAG, "Permission has already granted");
                    profileImagePosition = 3;
                    selectImage();
                }
                break;
            case R.id.iv_profile4:
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission not available requesting permission");
                    ActivityCompat.requestPermissions(EditeProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
                } else {
                    Log.d(TAG, "Permission has already granted");
                    profileImagePosition = 4;
                    selectImage();
                }
                break;
            case R.id.iv_edit:
//                binding.ivProfile1Bg.setImageResource(R.drawable.profile_bg);
//                binding.ivEdit.setVisibility(View.GONE);
//                binding.ivProfile1.setVisibility(View.VISIBLE);
//                callDeletePick1Api();
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission not available requesting permission");
                    ActivityCompat.requestPermissions(EditeProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
                } else {
                    Log.d(TAG, "Permission has already granted");
                    profileImagePosition = 1;
                    selectImage();
                }
                break;
            case R.id.iv_profile2_cross:
//                binding.ivProfile2Bg.setImageDrawable(null);
                binding.ivProfile2Bg.setImageResource(R.drawable.profile_bg);
                binding.ivProfile2Cross.setVisibility(View.GONE);
                binding.ivProfile2.setVisibility(View.VISIBLE);
                callDeletePick2Api();
                break;
            case R.id.iv_profile3_cross:
//                binding.ivProfile3Bg.setImageDrawable(null);
                binding.ivProfile3Bg.setImageResource(R.drawable.profile_bg);
                binding.ivProfile3Cross.setVisibility(View.GONE);
                binding.ivProfile3.setVisibility(View.VISIBLE);
                callDeletePick3Api();
                break;
            case R.id.iv_profile4_cross:
//                binding.ivProfile4Bg.setImageDrawable(null);
                binding.ivProfile4Bg.setImageResource(R.drawable.profile_bg);
                binding.ivProfile4Cross.setVisibility(View.GONE);
                binding.ivProfile4.setVisibility(View.VISIBLE);
                callDeletePick4Api();
                break;
            case R.id.btn_save:
                callApi();
                break;
        }
    }

    @Override
    public void setSelectMoviesName(String name) {
        binding.tvSelectedGender.setText(name);
        gender = name;
        binding.expandableLayoutGender.collapse();
        binding.ivSelectGender.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_expend));
    }

    @Override
    public void finish() {
        super.finish();
    }


    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("update_type", "ProfileUpdate");
        body.addProperty("name", binding.etName.getText().toString());
        body.addProperty("gender", gender);
        body.addProperty("bio_data", binding.etBioData.getText().toString());
        if (!profile1Base64.isEmpty()){
            body.addProperty("profile_pic", "data:image/png;base64,"+profile1Base64);
        }else {
            body.addProperty("profile_pic", "");
        }
        if (!profile2Base64.isEmpty()){
            body.addProperty("profile_pic2", "data:image/png;base64,"+profile2Base64);
        }else {
            body.addProperty("profile_pic2", "");
        }
        if (!profile3Base64.isEmpty()){
            body.addProperty("profile_pic3", "data:image/png;base64,"+profile3Base64);
        }else {
            body.addProperty("profile_pic3", "");
        }
        if (!profile4Base64.isEmpty()){
            body.addProperty("profile_pic4", "data:image/png;base64,"+profile4Base64);
        }else {
            body.addProperty("profile_pic4", "");
        }
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<EditProfileModle> call = api.PutEditProfile(access_token, "application/json", body);
        call.enqueue(new Callback<EditProfileModle>() {
            @Override
            public void onResponse(Call<EditProfileModle> call, Response<EditProfileModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    session.setisDataChanged(true);
                    onBackPressed();
                    finish();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("")) {
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
            public void onFailure(Call<EditProfileModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }
    private void setProfileDetails(){
        for (int i = 0; i < usersDetailsList.size(); i++) {
            try {
                if (!usersDetailsList.get(i).getProfilePic().equals("")/* && !usersDetailsList.get(i).getProfilePic().equals(null)&&!usersDetailsList.get(i).getProfilePic().isEmpty()*/ ){
                    Picasso.get().load(usersDetailsList.get(i).getProfileImage())
                            .into(binding.ivProfile1Bg);
                    binding.ivProfile1.setVisibility(View.GONE);
                    binding.ivEdit.setVisibility(View.VISIBLE);
                }else {
                    binding.ivProfile1.setVisibility(View.VISIBLE);
                    binding.ivEdit.setVisibility(View.GONE);
                }
                if (!usersDetailsList.get(i).getProfilePic2().equals("")){
                    Picasso.get().load(usersDetailsList.get(i).getProfileImage2())
                            .into(binding.ivProfile2Bg);
                    binding.ivProfile2.setVisibility(View.GONE);
                    binding.ivProfile2Cross.setVisibility(View.VISIBLE);
                }else if (usersDetailsList.get(i).getProfilePic2().equals("")){
                    binding.ivProfile2.setVisibility(View.VISIBLE);
                    binding.ivProfile2Cross.setVisibility(View.GONE);
                }
                if (!usersDetailsList.get(i).getProfilePic3().equals("") /*|| !usersDetailsList.get(i).getProfilePic3().equals(null)*//*&&!usersDetailsList.get(i).getProfilePic3().isEmpty()*/ ){
                    Picasso.get().load(usersDetailsList.get(i).getProfileImage3())
                            .into(binding.ivProfile3Bg);
                    binding.ivProfile3.setVisibility(View.GONE);
                    binding.ivProfile3Cross.setVisibility(View.VISIBLE);
                }else {
                    binding.ivProfile3.setVisibility(View.VISIBLE);
                    binding.ivProfile3Cross.setVisibility(View.GONE);
                }
                if (!usersDetailsList.get(i).getProfilePic4().equals("") /*|| !usersDetailsList.get(i).getProfilePic4().equals(null)*//*&&!usersDetailsList.get(i).getProfilePic4().isEmpty()*/ ){
                    Picasso.get().load(usersDetailsList.get(i).getProfileImage4())
                            .into(binding.ivProfile4Bg);
                    binding.ivProfile4.setVisibility(View.GONE);
                    binding.ivProfile4Cross.setVisibility(View.VISIBLE);
                }else {
                    binding.ivProfile4.setVisibility(View.VISIBLE);
                    binding.ivProfile4Cross.setVisibility(View.GONE);
                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            binding.etName.setText(usersDetailsList.get(i).getName());
            binding.etBioData.setText(usersDetailsList.get(i).getBioData());
            binding.tvSelectedGender.setText(usersDetailsList.get(i).getGender());
        }
    }
    private void callProfileApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ProfileModle> call = api.GetProfileDetails(access_token, "application/json", userId, "All", "Profile", "user");
        call.enqueue(new Callback<ProfileModle>() {
            @Override
            public void onResponse(Call<ProfileModle> call, Response<ProfileModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    ProfileModle profileModle = response.body();
                    usersDetailsList = profileModle.getData().getUser();
                    setProfileDetails();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Success")) {
                         /*   binding.otpError.setVisibility(View.VISIBLE);
                            binding.otpError.setText(jObjError.getString("message"));*/
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
            public void onFailure(Call<ProfileModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callDeletePick1Api() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("update_type", "ProfileDelete");
        body.addProperty("profile_pic", "profile_pic");
        body.addProperty("profile_pic2", "");
        body.addProperty("profile_pic3", "");
        body.addProperty("profile_pic4", "");
        loading2 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ProfileImageDeleteModle> call = api.DeleteProfilePic(access_token,"application/json",body);
        call.enqueue(new Callback<ProfileImageDeleteModle>() {
            @Override
            public void onResponse(Call<ProfileImageDeleteModle> call, Response<ProfileImageDeleteModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Profile Pic Deleted Successfully",Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals(""))
                        {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));

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
            public void onFailure(Call<ProfileImageDeleteModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }
    private void callDeletePick2Api() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("update_type", "ProfileDelete");
        body.addProperty("profile_pic", "");
        body.addProperty("profile_pic2", "profile_pic2");
        body.addProperty("profile_pic3", "");
        body.addProperty("profile_pic4", "");
        loading2 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ProfileImageDeleteModle> call = api.DeleteProfilePic(access_token,"application/json",body);
        call.enqueue(new Callback<ProfileImageDeleteModle>() {
            @Override
            public void onResponse(Call<ProfileImageDeleteModle> call, Response<ProfileImageDeleteModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Profile Pic Deleted Successfully",Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals(""))
                        {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));

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
            public void onFailure(Call<ProfileImageDeleteModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }
    private void callDeletePick3Api() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("update_type", "ProfileDelete");
        body.addProperty("profile_pic", "");
        body.addProperty("profile_pic2", "");
        body.addProperty("profile_pic3", "profile_pic3");
        body.addProperty("profile_pic4", "");
        loading2 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ProfileImageDeleteModle> call = api.DeleteProfilePic(access_token,"application/json",body);
        call.enqueue(new Callback<ProfileImageDeleteModle>() {
            @Override
            public void onResponse(Call<ProfileImageDeleteModle> call, Response<ProfileImageDeleteModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Profile Pic Deleted Successfully",Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals(""))
                        {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));

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
            public void onFailure(Call<ProfileImageDeleteModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }
    private void callDeletePick4Api() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("update_type", "ProfileDelete");
        body.addProperty("profile_pic", "");
        body.addProperty("profile_pic2", "");
        body.addProperty("profile_pic3", "");
        body.addProperty("profile_pic4", "profile_pic4");
        loading2 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ProfileImageDeleteModle> call = api.DeleteProfilePic(access_token,"application/json",body);
        call.enqueue(new Callback<ProfileImageDeleteModle>() {
            @Override
            public void onResponse(Call<ProfileImageDeleteModle> call, Response<ProfileImageDeleteModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Profile Pic Deleted Successfully",Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals(""))
                        {
//                            binding.otpError.setVisibility(View.VISIBLE);
//                            binding.otpError.setText(jObjError.getString("message"));

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
            public void onFailure(Call<ProfileImageDeleteModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }



}