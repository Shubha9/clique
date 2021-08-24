package com.us.clique.activites;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.ChatingBodyAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.modle.ChatIndexModle;
import com.us.clique.bottomNavigation.modle.ChatingBodyResponceModle;
import com.us.clique.bottomNavigation.modle.ChatingMessagesModle;
import com.us.clique.databinding.ActivityChatingBinding;
import com.us.clique.modle.MessagesModel;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.us.clique.utils.NetworkConnection;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.vanniktech.emoji.EmojiPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ChatingActivity extends AppCompatActivity implements View.OnClickListener {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityChatingBinding binding;
    ArrayList<ChatingMessagesModle> chatingBodyModles = new ArrayList<>();
    ChatingBodyAdapter chatingBodyAdapter;
    FirebaseDatabase database;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    String personName;
    ProgressBar mProgress;
    View rootView;
    int a = 0;
    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_PERMISSIONS = 100;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    Bitmap bitmap;
    Uri selectedImage;
    private Button buttonCancel, buttonsend;
    private TextView tvGroup, tvText;
    String picasso, ImageString;
    ProgressDialog loading, loading3;
    String access_token = " ", userId, reciverId,chatId;
    UserSessionManager session;
    ArrayList<MessagesModel> messsagesArrayList = new ArrayList<MessagesModel>();
    ArrayList<ChatIndexModle.Datum> chatsModles = new ArrayList<>();
    ChatingBodyResponceModle chatingBodyModle;
    ArrayList<ChatingBodyResponceModle.Datum> chatingBodyModlesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(ChatingActivity.this,R.layout.activity_chating);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setChatingModle(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        database = FirebaseDatabase.getInstance();
        storageReference= FirebaseStorage.getInstance().getReference("uploads" );
        databaseReference=FirebaseDatabase.getInstance().getReference("uploads");
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        binding.ivSend.setOnClickListener(this);
        binding.ivOnBackPress.setOnClickListener(this);
        OnlineUserStatus onlineUserStatus = new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
//        NetworkConnection networkConnection=new NetworkConnection(getApplicationContext());
//        if(networkConnection.isInternetAvailable())
//        {
//            callChatIndexApi();
//            callChatingApi();

        initRecycler();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            personName = null;
            picasso = null;
            reciverId = null;

        } else {
            personName = extras.getString("name");
            picasso = extras.getString("image");
            reciverId = extras.getString("recieverId");
            chatId=extras.getString("chatid");
            Log.d("id",chatId);
        }
        firestorevalue();
        binding.tvPersonName.setText(personName);
        Picasso.get().load(picasso)
                .into(binding.ivProfile);
        final EmojiPopup popup = EmojiPopup.Builder
                .fromRootView(findViewById(R.id.rootView)).build(binding.etEmojoText);

        binding.ivMog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.toggle();
            }
        });

        binding.ivAttaech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission not available requesting permission");
                    ActivityCompat.requestPermissions(ChatingActivity.this,
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
                } else {
                    Log.d(TAG, "Permission has already granted");
                    selectImage();
                }
            }
        });

    }

    private void firestorevalue() {
        DatabaseReference chatref = database.getReference().child("USERS").child("Chats").child(chatId);
        chatref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messsagesArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MessagesModel messsages = dataSnapshot.getValue(MessagesModel.class);
                    messsagesArrayList.add(messsages);
                    Log.d("list", String.valueOf(messsagesArrayList));
                }
                chatingBodyAdapter.notifyDataSetChanged();
                binding.rvChating.scrollToPosition(chatingBodyAdapter.getItemCount()-1);
//                chatmsgAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public String getUserId() {
        return userId;
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


    private void initRecycler() {
        //  chatingBodyModles.add(new ChatingBodyModle(binding.etEmojoText.getText().toString(),photo,selectedImage));
        chatingBodyAdapter = new ChatingBodyAdapter(getApplicationContext(), messsagesArrayList,userId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.rvChating.setLayoutManager(new LinearLayoutManager(this));
        linearLayoutManager.setStackFromEnd(true);
        binding.rvChating.setAdapter(chatingBodyAdapter);
        binding.etEmojoText.getText().clear();
        chatingBodyAdapter.notifyDataSetChanged();
        binding.rvChating.scrollToPosition(chatingBodyAdapter.getItemCount()-1);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        binding.rvInboxList.setLayoutManager(layoutManager);
//        layoutManager.setStackFromEnd(true);
//        layoutManager.setReverseLayout(false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        /*  photo = (Bitmap) data.getExtras().get("data");*/
                        onCaptureImageResult(data);

                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                    /*    selectedImage  =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                if (picturePath != null){
                                    SelectGallaryImageAlert();
                                   // binding.ivUploadImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                }
                                cursor.close();
                            }
                        }*/

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
            SelectGallaryImageAlert();
        }

        ImageString = getStringImage(bitmap);

        Log.v(TAG, "base64" + ImageString);//checking in log base64 string to image cropeed image found

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
        if (bitmap != null) {
            SelectImageAlert();
        }

        ImageString = getStringImage(bitmap);

        Log.v(TAG, "base64" + ImageString);//perfectly string base64 to image


    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
        byte[] imageBytes = baos.toByteArray();

        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void SelectImageAlert() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.layout_send, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        buttonCancel = view.findViewById(R.id.button_cancel);
        tvGroup = view.findViewById(R.id.tv_delete_chat);
        tvText = view.findViewById(R.id.tv_deleteTextt);
        buttonsend = view.findViewById(R.id.btn_Delete);
        tvGroup.setText("DO YOU WANT'S TO SHARE IMAGE TO " + personName + "!!!");
        tvText.setText("Media Image has been sending !!");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setView(view);
        alertDialog.show();
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initRecycler();
                NetworkConnection networkConnection = new NetworkConnection(getApplicationContext());
                if (networkConnection.isInternetAvailable()) {
                    callChatingApi();
                    alertDialog.dismiss();
                } else {
//                    setContentView(R.layout.activity_networksignal);
                }

            }
        });
    }

    private void SelectGallaryImageAlert() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View view = factory.inflate(R.layout.layout_send, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        buttonCancel = view.findViewById(R.id.button_cancel);
        tvGroup = view.findViewById(R.id.tv_delete_chat);
        tvText = view.findViewById(R.id.tv_deleteTextt);
        buttonsend = view.findViewById(R.id.btn_Delete);
        tvGroup.setText("DO YOU WANT'S TO SHARE Gallery IMAGE TO " + personName + "!!!");
        tvText.setText("Media Image has been sending !!");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setView(view);
        alertDialog.show();
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        buttonsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initRecycler();
                callChatingApi();
                alertDialog.dismiss();
            }
        });
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
        switch (v.getId()) {
            case R.id.iv_send:

                callFireChat();
                binding.etEmojoText.getText().clear();
                break;
            case R.id.iv_onBackPress:
                onBackPressed();
                break;
        }
    }
private String getFileExtenstion(Uri uri)
{
    ContentResolver cr=getContentResolver();
    MimeTypeMap mime= MimeTypeMap.getSingleton();
    return  mime.getExtensionFromMimeType(cr.getType(uri));
}
    private void callFireChat() {
//        StorageReference fileReference=storageReference.child(System.currentTimeMillis()+"."+getFileExtenstion());
//        fileReference.putFile()
//                .addOnCompleteListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Handler handler=new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mProgress.setProgress(0);
//                            }
//                        },5000);
//                        Date date = new Date();
//                        MessagesModel messagesModel = new MessagesModel(binding.etEmojoText.getText().toString(),userId,taskSnapshot.getDownloadUrl., "",date.getTime());
//
//
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                        double progress=(100.0*snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
//                        mProgress.setProgress((int)progress);
//
//                    }
//                });


//
        Date date = new Date();
        MessagesModel messagesModel = new MessagesModel(binding.etEmojoText.getText().toString(),chatId, "","",date.getTime());
        database.getReference().child("USERS").child("Chats").child(chatId)
                .push()
                .setValue(messagesModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                initRecycler();
            }
        });




    }

    private void callChatingApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("chat_message", binding.etEmojoText.getText().toString());
        body.addProperty("chat_file", ImageString);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ChatingMessagesModle> call = api.PostChatingMessages(access_token, userId, reciverId, "application/json", body);
        call.enqueue(new Callback<ChatingMessagesModle>() {
            @Override
            public void onResponse(Call<ChatingMessagesModle> call, Response<ChatingMessagesModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
//                    chatingMessagesModle = response.body();
//                    chatingBodyModles.add(chatingMessagesModle);
                    callChatIndexApi();

                } else {
                    Toast.makeText(getApplicationContext(), "hhhh", Toast.LENGTH_SHORT).show();

                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Already Requested for this event")) {
                            Toast.makeText(getApplicationContext(), "Already Requested for this event", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<ChatingMessagesModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callChatIndexApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading3 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ChatingBodyResponceModle> call = api.GetChatingBodyModle(access_token, "ChatMessage", userId, reciverId, "1");
        call.enqueue(new Callback<ChatingBodyResponceModle>() {
            @Override
            public void onResponse(Call<ChatingBodyResponceModle> call, Response<ChatingBodyResponceModle> response) {
                loading3.cancel();
                if (response.isSuccessful()) {
                    chatingBodyModle = response.body();
                    chatingBodyModlesList = (ArrayList) chatingBodyModle.getData();
                    initRecycler();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("No Data Available")) {
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
            public void onFailure(Call<ChatingBodyResponceModle> call, Throwable t) {
                loading3.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }



}