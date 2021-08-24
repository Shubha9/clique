package com.us.clique.copyPose;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.BaseActivity;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.databinding.ActivityCopyPoseBinding;
import com.us.clique.location.LocationActivity;
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
import java.net.URL;
import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CopyPoseActivity extends BaseActivity implements View.OnClickListener {
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityCopyPoseBinding binding;
    RefreshPitctureModle refreshPitctureModle;

    private static final int REQUEST_PERMISSIONS = 100;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    Bitmap bitmap;
    ProgressDialog loading;
    UserSessionManager session;
    String access_token = "";
    int j = 0;
    int k=0;
    String CopyPoseImage;
    String base64 = "";
    BaseActivity baseActivity;
    ArrayList<String> refreshPitctureList = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
////        setContentView(R.layout.activity_copy_pose);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(CopyPoseActivity.this,
                R.layout.activity_copy_pose);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setCopyPose(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);

        callApi();

        //for (k=0;k<refreshPitctureList.size();)

        binding.ivAddimages.setOnClickListener(this);
        binding.btnSave.setOnClickListener(this);
        binding.onBackPress.setOnClickListener(this);
        binding.llRefresh.setOnClickListener(this);
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
               /*         Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        if (selectedImage != null){
                            binding.ivProfile.setVisibility(View.VISIBLE);
                            binding.ivProfile.setImageBitmap(selectedImage);
                            binding.editbtn.setVisibility(View.VISIBLE);
                            binding.profilecam.setVisibility(View.GONE);
                        }else {
                            binding.editbtn.setVisibility(View.GONE);
                            binding.profilecam.setVisibility(View.VISIBLE);
                        }

*/
                        onCaptureImageResult(data);

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                       /* Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                if (picturePath != null){
                                    //  binding.ivGalleryImage.setVisibility(View.VISIBLE);
                                    binding.ivProfile.setImageURI(selectedImage);
                                    binding.editbtn.setVisibility(View.VISIBLE);
                                    binding.profilecam.setVisibility(View.GONE);
                                       }else {
                                    binding.editbtn.setVisibility(View.GONE);
                                    binding.profilecam.setVisibility(View.VISIBLE);

                                }
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

        if (bitmap != null){
            binding.profile2.setImageBitmap(bitmap);
            binding.ivAddimages.setVisibility(View.GONE);
            binding.btnSave.setEnabled(true);
            binding.btnSave.setBackgroundResource(R.drawable.button_background);

        }else {
            binding.ivAddimages.setVisibility(View.VISIBLE);
            binding.btnSave.setEnabled(false);
            binding.btnSave.setBackgroundResource(R.drawable.button_bg_enable);
//            binding.profilecam.setVisibility(View.VISIBLE);
        }

        CopyPoseImage=getStringImage(bitmap);

        Log.v(TAG,"base64"+CopyPoseImage);//checking in log base64 string to image cropeed image found

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
            binding.profile2.setImageBitmap(bitmap);
            binding.ivAddimages.setVisibility(View.GONE);
            binding.btnSave.setEnabled(true);
            binding.btnSave.setBackgroundResource(R.drawable.button_background);

        }else {
            binding.ivAddimages.setVisibility(View.VISIBLE);
            binding.btnSave.setEnabled(false);
            binding.btnSave.setBackgroundResource(R.drawable.button_bg_enable);
//            binding.profilecam.setVisibility(View.VISIBLE);
        }

        CopyPoseImage=getStringImage(bitmap);

        Log.v(TAG,"base64"+CopyPoseImage);//perfectly string base64 to image


    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
    private void showToastMessage(String message) {
        Log.v(TAG, String.format("showToastMessage :: message = %s", message));
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_addimages:
               /* Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_LONG).show();*/
                if (ContextCompat.checkSelfPermission(CopyPoseActivity.this, Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG,"Permission not available requesting permission");
                    ActivityCompat.requestPermissions(CopyPoseActivity.this,
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
                } else {
                    Log.d(TAG,"Permission has already granted");
                    selectImage();
                }
                break;

            case R.id.btn_save:
               callCopyPoseApi();
                break;
            case R.id.onBackPress:
                onBackPressed();
                break;

            case R.id.ll_refresh:
                /*for (int k=0;k<refreshPitctureList.size()-1;k++)*/

                if(k<refreshPitctureList.size()) {
                    Picasso.get().load(refreshPitctureList.get(k))
                            .into(binding.profilesample);
                   convertUrlToBase64(refreshPitctureList.get(k));
                    k++;


                }
                if(refreshPitctureList.size()==k)
                {
                    k=0;
                    callApi();
                }

                break;

        }
    }

    public String convertUrlToBase64(String url) {
        URL newurl;
        Bitmap bitmap;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            newurl = new URL(url);
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            base64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
            Log.d("base64",base64);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<RefreshPitctureModle> call = api.GetAllRefreshPitcture(access_token,"All","1","Pictures","All");
        call.enqueue(new Callback<RefreshPitctureModle>() {
            @Override
            public void onResponse(Call<RefreshPitctureModle> call, Response<RefreshPitctureModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    refreshPitctureModle = response.body();
                    for (int i=0;i<refreshPitctureModle.getData().size();i++){
                        refreshPitctureList.add(refreshPitctureModle.getData().get(i).getPicImage());
                        Picasso.get().load(refreshPitctureModle.getData().get(i).getPicImage())
                                .into(binding.profilesample);

//                        Intent j = new Intent(getApplicationContext(), LocationActivity.class);
//                        startActivity(j);
                    }

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
            public void onFailure(Call<RefreshPitctureModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());
                baseActivity.showSomething(getApplicationContext());
            }
        });

    }

    private void callCopyPoseApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("reference_pic", "data:image/png;base64,"+base64);
        body.addProperty("copy_pose", "data:image/png;base64,"+CopyPoseImage);
        body.addProperty("signup_stage", 4);
        body.addProperty("update_type", "CopyPose");

        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<CopyPoseModle> call = api.PutCopyPose(access_token,"application/json",body);
        call.enqueue(new Callback<CopyPoseModle>() {
            @Override
            public void onResponse(Call<CopyPoseModle> call, Response<CopyPoseModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), LocationActivity.class);
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
            public void onFailure(Call<CopyPoseModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());
                showSomething(CopyPoseActivity.this);
            }
        });

    }


}