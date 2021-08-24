package com.us.clique.joinNowProfile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.activites.BaseActivity;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.copyPose.CopyPoseActivity;
import com.us.clique.databinding.ActivityProfileJoinNowBinding;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.appevents.AppEventsLogger.getUserData;

public class JoinNowProfileActivity extends BaseActivity implements View.OnClickListener {

    static final int REQUEST_TAKE_PHOTO = 1;
    static final int REQUEST_GALLERY_PHOTO = 2;
    private static final int REQUEST_PERMISSIONS = 100;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;

    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityProfileJoinNowBinding binding;
    Bitmap bitmap;
    String img;
    ProgressDialog loading;
    UserSessionManager session;
    String access_token = "";
    //     BaseActivity baseActivity;
    boolean stop = false;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.activity_copy_pose);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(JoinNowProfileActivity.this,
                R.layout.activity_profile_join_now);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setProfile(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        binding.btnSave.setOnClickListener(this);
        binding.ivOnBackPress.setOnClickListener(this);
        binding.profilecam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(JoinNowProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission not available requesting permission");
                    ActivityCompat.requestPermissions(JoinNowProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
                } else {
                    Log.d(TAG, "Permission has already granted");
                    selectImage();
                }

            }
        });
        binding.editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();

            }
        });
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //your method
            }
        }, 0, 30000);
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

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
                    startActivityForResult(pickPhoto, 1);

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

        if (resultCode != RESULT_CANCELED) {
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

        if (bitmap != null) {
            binding.ivProfile.setVisibility(View.VISIBLE);
            binding.ivProfile.setImageBitmap(bitmap);
            binding.editbtn.setVisibility(View.VISIBLE);
            binding.profilecam.setVisibility(View.GONE);
            binding.btnSave.setEnabled(true);
            binding.btnSave.setBackgroundResource(R.drawable.button_background);
            binding.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callApi();
                }
            });
        } else {
            binding.editbtn.setVisibility(View.GONE);
            binding.profilecam.setVisibility(View.VISIBLE);
            binding.btnSave.setEnabled(false);
            binding.btnSave.setBackgroundResource(R.drawable.button_bg_enable);
        }
        String img = getStringImage(bitmap);

        Log.v(TAG, "base64" + img);//checking in log base64 string to image cropeed image found

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
        if (bitmap != null) {
            binding.ivProfile.setVisibility(View.VISIBLE);
            binding.ivProfile.setImageBitmap(bitmap);
            binding.editbtn.setVisibility(View.VISIBLE);
            binding.profilecam.setVisibility(View.GONE);
            binding.btnSave.setEnabled(true);
            binding.btnSave.setBackgroundResource(R.drawable.button_background);
            binding.btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callApi();
                }
            });
        } else {
            binding.editbtn.setVisibility(View.GONE);
            binding.profilecam.setVisibility(View.VISIBLE);
            binding.btnSave.setEnabled(false);
            binding.btnSave.setBackgroundResource(R.drawable.button_bg_enable);
        }
        img = getStringImage(bitmap);

        Log.v(TAG, "base64" + img);//perfectly string base64 to image


    }

    public String getStringImage(Bitmap bmp) {
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

    boolean shouldStopLoop = false;
    Handler mHandler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getUserData();
            if (!shouldStopLoop) {
                mHandler.postDelayed(this, 5000);
            }
        }
    };

    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("profile_pic", "data:image/png;base64," + img);
        body.addProperty("update_type", "ProfilePic");
        body.addProperty("signup_stage", 3);
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (stop == false) {
//                    Toast.makeText(getApplicationContext(), "please refresh", Toast.LENGTH_LONG).show();
//                    dialogBox();
//                }
//
//            }
//        }, 30000);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);

        Call<JoinNowProfileModle> call = api.PutJoinProfile(access_token, "application/json", body);
        call.enqueue(new Callback<JoinNowProfileModle>() {
            @Override
            public void onResponse(Call<JoinNowProfileModle> call, Response<JoinNowProfileModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), CopyPoseActivity.class);
                    startActivity(i);
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Invalid Emaileee Id")) {
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
            public void onFailure(Call<JoinNowProfileModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());
                showSomething(JoinNowProfileActivity.this);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void dialogBox() {
        stop = false;
        loading.cancel();
        //experinceList.clear();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AlertDialog");
        builder.setMessage("Please Refresh");

        // add the buttons
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callApi();
            }
        });
        builder.setNegativeButton("Cancel", null);

        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                callApi();
                break;
            case R.id.iv_onBackPress:
                onBackPressed();
//                Intent i = new Intent(this, JoinNowActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(i);
                break;
        }
    }
}