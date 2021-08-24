package com.us.clique.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.us.clique.R;
import com.us.clique.SessionManagers.UserSessionManager;
import com.us.clique.adapter.AdminPeople;
import com.us.clique.adapter.AdminPeopleAdapter;
import com.us.clique.adapter.LocationAutoAdapter;
import com.us.clique.adapter.MoviesListAdapter;
import com.us.clique.adapter.RemoveParticipant;
import com.us.clique.adapter.RemoveParticipantAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.interfaces.SelectLocationName;
import com.us.clique.bottomNavigation.modle.ChooseYourInterestModle;
import com.us.clique.bottomNavigation.modle.ExperinceModle;
import com.us.clique.databinding.ActivityEditeExperienceBinding;
import com.us.clique.interfaces.SelectMoviesName;
import com.us.clique.location.LocationAutoSuggestionModle;
import com.us.clique.map.AutoCompleteAdapter;
import com.us.clique.modle.EditeExperienceModle;
import com.us.clique.networkUtils.Api;
import com.us.clique.networkUtils.ApiClient;
import com.us.clique.utils.ImageUtil;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import mabbas007.tagsedittext.TagsEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class EditeExperienceActivity extends AppCompatActivity implements View.OnClickListener, SelectMoviesName, TagsEditText.TagsEditListener, SelectLocationName {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;
    ActivityEditeExperienceBinding binding;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    //    private static int REQUEST_PICK_IMAGE = 2;
    private static final int PICK_FROM_GALLERY = 1;
    DatePickerDialog datepicker;
    AutoCompleteAdapter adapter;
    PlacesClient placesClient;
    Double ven_lat=0.0,ven_long=0.0;
    private static final int REQUEST_PERMISSIONS = 100;

    ArrayList<ChooseYourInterestModle.Data.Category> moviesListModles = new ArrayList<>();
    MoviesListAdapter moviesListAdapter;
    Bitmap bitmap;
    String img,encode;


    LocationAutoAdapter locationAutoAdapter;
    ArrayList<LocationAutoSuggestionModle.Datum> autoSuggestiolocationList = new ArrayList<>();
    String locationId, tagsName;
    ProgressDialog loading, loading1, loading2;
    UserSessionManager session;
    String access_token = "", userId = "", experinceEventsId, date,catId;
    String SelectedMoviveName = "", event_time;
    ExperinceModle experinceModle;
    String amPm;
    String apiKey,location_name,city_name,city_latitude,city_longitude,postalcode,countryName,stateName,countryShortName,stateShortName;

    TimePickerDialog timePickerDialog;
    ArrayList<ExperinceModle.Datum> experinceList = new ArrayList<>();
    ChooseYourInterestModle yourInterestModle;
    ArrayList<String> tagsList = new ArrayList<>();
    /*------------------------ New Designs ----------------------------------*/
    ArrayList<AdminPeople> adminPeople=new ArrayList<>();
    ArrayList<RemoveParticipant> removeParticipants=new ArrayList<>();
    TextView tvYes,tvNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(EditeExperienceActivity.this,
                R.layout.activity_edite_experience);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setEditeEvent(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        userId = session.getUserId();
        OnlineUserStatus onlineUserStatus = new OnlineUserStatus(getApplicationContext());
        onlineUserStatus.API();
        if (getIntent().getExtras() != null) {
            experinceEventsId = getIntent().getStringExtra("experinceId");
        }

        binding.tvSelectedClass.setOnClickListener(this);
        binding.ivOnBackPress.setOnClickListener(this);
        binding.rlSelectClass.setOnClickListener(this);
        binding.ivOnBackPress.setOnClickListener(this);
        binding.btnCreateEcperince.setOnClickListener(this);
        binding.btnDeleteExp.setOnClickListener(this);
        binding.btnExitExp.setOnClickListener(this);
        binding.llMore.setOnClickListener(this);
        apiKey = getString(R.string.mapkey);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(),apiKey);
        }
        placesClient = Places.createClient(getApplicationContext());
        initAutoCompleteTextView();
//        callApiLocationList();
        callGetExperinceDetailsApi();
        callAllCatsApi();
        recyclerview();
        binding.tagsEditText.setHint("Enter Your Tags");
        binding.tagsEditText.setTagsListener(this);
        binding.tagsEditText.setTagsWithSpacesEnabled(true);
        binding.tagsEditText.setThreshold(1);
        tagsName = binding.tagsEditText.getText().toString();
        binding.ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission not available requesting permission");
                    ActivityCompat.requestPermissions(EditeExperienceActivity.this,
                            new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
                } else {
                    Log.d(TAG, "Permission has already granted");
                    selectImage();
                }

            }
        });
        binding.rlEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                datepicker = new DatePickerDialog(EditeExperienceActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cldr.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
                                String dateString = sdf.format(cldr.getTime());
                                //   binding.tvDate.setText(dateString);
                                binding.tvDate.setText(year +"-" +(monthOfYear+1) + "-" + dayOfMonth);
                                date = binding.tvDate.getText().toString();

                                //binding.tvDate.setText(cldr.get(Calendar.MONTH) + "/" + (monthOfYear + 1));
                            }
                        }, year, month, day);
                // datepicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datepicker.getDatePicker().setMinDate(cldr.getTimeInMillis());
                datepicker.show();
            }
        });

        binding.rlStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minute = cldr.get(Calendar.MINUTE);
//                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                timePickerDialog = new TimePickerDialog(EditeExperienceActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        boolean isPM = (selectedHour >= 12);
                        if(selectedHour>=12){
                            amPm=" PM";
                        } else {
                            amPm = " AM";
                        }
                        binding.tvTime.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute,isPM  ? "PM" : "AM"));
                        event_time=binding.tvTime.getText().toString();
                    }
                }, hour, minute, false);//Yes 24 hour time

                timePickerDialog.show();
            }

        });

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aaa");
        String currentTime = sdf.format(new Date());
        binding.tvTime.setText(currentTime);

//        binding.auto.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (binding.auto.getText().toString().isEmpty()) {
////                    binding.rvLocationDetails.setVisibility(View.GONE);
//                } else {
//                    binding.rvLocationDetails.setVisibility(View.VISIBLE);
//                }
//                if (locationAutoAdapter != null) {
//                    locationAutoAdapter.getFilter().filter(charSequence.toString());
//                } else {
//                    Toast.makeText(getApplicationContext(), "Address not found", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//
//        });
        binding.etMovieName.setOnKeyListener(new View.OnKeyListener() {
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
        textWatcher();
        binding.etMinMember.setFilters(new InputFilter[]{new MinMaxFilter("1", "1")});
//        binding.etMax.setFilters( new InputFilter[]{ new MinMaxFilter("2","7")}) ;
    }
    private void initAutoCompleteTextView() {
        binding.auto.setThreshold(1);
        binding.auto.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(getApplicationContext(), placesClient);
        binding.auto.setAdapter(adapter);
    }

    private void processButtonByTextLength() {
        if (binding.etMovieName.getText().toString().startsWith(" ")) {
            binding.tvError.setVisibility(View.VISIBLE);
            binding.tvError.setText("Space Not Valid");
        }
        String inputText = binding.etMovieName.getText().toString();
        String inputText2 = binding.tvSelectedClass.getText().toString();
        String inputText3 = binding.auto.getText().toString();
        String inputText4 = binding.tvDate.getText().toString();
        String inputText5 = binding.etMinMember.getText().toString();
        String inputText6 = binding.etMax.getText().toString();
        String inputText7 = binding.tvTags.getText().toString();

        if (!(inputText.length() > 0 && inputText2.length() > 0 && inputText3.length() > 0
                && inputText4.length() > 0 && inputText5.length() > 0 && inputText6.length() > 0)) {
            binding.btnCreateEcperince.setEnabled(false);
            binding.btnCreateEcperince.setBackgroundResource(R.drawable.button_bg_enable);

        } else {
            binding.btnCreateEcperince.setEnabled(true);
            binding.btnCreateEcperince.setBackgroundResource(R.drawable.button_background);


        }
    }

    private void textWatcher() {
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.tvError.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        };
        binding.etMovieName.addTextChangedListener(tw);
        binding.tvSelectedClass.addTextChangedListener(tw);
        binding.auto.addTextChangedListener(tw);
        binding.tvDate.addTextChangedListener(tw);
        binding.etMinMember.addTextChangedListener(tw);
        binding.etMax.addTextChangedListener(tw);

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
                     /*   if (selectedImage != null){
                            binding.ivUploadImage.setVisibility(View.VISIBLE);
                            binding.rlUploadRl.setVisibility(View.GONE);
                            binding.ivUploadImage.setImageBitmap(selectedImage);
                        }else {
                            binding.ivUploadImage.setVisibility(View.GONE);
                            binding.rlUploadRl.setVisibility(View.VISIBLE);
                        }*/
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
                                    binding.ivUploadImage.setVisibility(View.VISIBLE);
                                    binding.rlUploadRl.setVisibility(View.GONE);
                                    binding.ivUploadImage.setImageURI(selectedImage);
                                  //  binding.ivUploadImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                }else {
                                    binding.ivUploadImage.setVisibility(View.GONE);
                                    binding.rlUploadRl.setVisibility(View.VISIBLE);
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
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (bitmap != null) {
            binding.ivUploadImage.setVisibility(View.VISIBLE);
            binding.rlUploadRl.setVisibility(View.GONE);
//            binding.ivUploadImage.setImageURI(bitmap);
            binding.ivUploadImage.setImageBitmap(bitmap);
        } else {
            binding.ivUploadImage.setVisibility(View.GONE);
            binding.rlUploadRl.setVisibility(View.VISIBLE);
        }

        img = getStringImage(bitmap);

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
            binding.ivUploadImage.setVisibility(View.VISIBLE);
            binding.rlUploadRl.setVisibility(View.GONE);
            binding.ivUploadImage.setImageBitmap(bitmap);
        } else {
            binding.ivUploadImage.setVisibility(View.GONE);
            binding.rlUploadRl.setVisibility(View.VISIBLE);
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
        //  moviesListModles.clear();
        if (moviesListModles!= null){
            moviesListAdapter = new MoviesListAdapter(getApplicationContext(), moviesListModles, "Select Movie Name", this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvSelectClass.setLayoutManager(mLayoutManager);
            binding.rvSelectClass.setItemAnimator(new DefaultItemAnimator());
            binding.rvSelectClass.setAdapter(moviesListAdapter);
        }

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
    public void setSelectMoviesName(String name, String categoryId) {
        catId=categoryId;
        binding.tvSelectedClass.setText(name);
        SelectedMoviveName = binding.tvSelectedClass.getText().toString();
        binding.expandableLayoutSelectClass.collapse();
        binding.ivSelectClass.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_expend));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_selected_class:
                selectClass();
                initializeNewChatClassRecycler();
                binding.tvSelectedClass.setInputType(InputType.TYPE_NULL);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                break;
            case R.id.iv_onBackPress:
                onBackPressed();
                break;
            case R.id.btn_createEcperince:
             /*   tagsName = binding.tagsEditText.getText().toString();
                tagsName = tagsName.replace(",$","");
                if (tagsName.endsWith(",")) {
                    tagsName = tagsName.substring(0, tagsName.length() - 1);
                }*/
                tagsList.add(binding.tagsEditText.getText().toString());

                tagsName = binding.tagsEditText.getText().toString().replace(' ', ',');
                if (tagsName.endsWith(",")) {
                    tagsName = tagsName.substring(0, tagsName.length() - 1);
                }
                callApi();
                break;

            case R.id.btn_delete_exp:
                delectAlert();
                break;
            case R.id.btn_exit_exp:
                adminPeople.clear();
                Alert();
                break;
            case R.id.ll_more:
                if (binding.expandRecyclerview.isExpanded()) {
                    binding.expandRecyclerview.collapse();
                } else {
                    binding.expandRecyclerview.expand();
                    binding.othersRecyclerview.setVisibility(View.GONE);
                    binding.othersRecyclerview2.setLayoutManager(new LinearLayoutManager
                            (getApplicationContext(), RecyclerView.VERTICAL, false));
                    removeParticipants.add(new RemoveParticipant(R.drawable.event_profile_1,"chetu"));
                    removeParticipants.add(new RemoveParticipant(R.drawable.event_profile_3,"neha"));
                    RemoveParticipantAdapter adapter1 = new RemoveParticipantAdapter(context, removeParticipants);
                    binding.othersRecyclerview2.setAdapter(adapter1);
                    binding.othersRecyclerview2.setVisibility(View.VISIBLE);
                    binding.llMore.setVisibility(View.GONE);
                }
                break;
                }
        }
    private void recyclerview(){
        binding.othersRecyclerview.setLayoutManager(new LinearLayoutManager
                (getApplicationContext(), RecyclerView.VERTICAL, false));
        removeParticipants.add(new RemoveParticipant(R.drawable.event_profile_1,"Mounesh"));
        removeParticipants.add(new RemoveParticipant(R.drawable.event_profile_2,"supreetha"));
        removeParticipants.add(new RemoveParticipant(R.drawable.event_profile_3,"keerthi"));
        RemoveParticipantAdapter adapter = new RemoveParticipantAdapter(context, removeParticipants);
        binding.othersRecyclerview.setAdapter(adapter);
        for(int i=0;i<removeParticipants.size();i++){
            if(i<2){
                binding.llMore.setVisibility(View.GONE);
            }else{
                binding.llMore.setVisibility(View.VISIBLE);
            }
        }
    }
        public void Alert(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            View dialogView = (View) getLayoutInflater().inflate(R.layout.layout_exit_experience, null);
            alertDialogBuilder.setView(dialogView);
            Button btnadmin=(Button)dialogView.findViewById(R.id.btn_admin);
            RecyclerView recyclerView=(RecyclerView)dialogView.findViewById(R.id.recyclerview);
            recyclerView.setLayoutManager(new LinearLayoutManager
                    (getApplicationContext(),RecyclerView.HORIZONTAL,false));
            adminPeople.add(new AdminPeople(R.drawable.event_profile_1));
            adminPeople.add(new AdminPeople(R.drawable.event_profile_2));
            adminPeople.add(new AdminPeople(R.drawable.event_profile_3));
            AdminPeopleAdapter adapter=new AdminPeopleAdapter(context,adminPeople);
            recyclerView.setAdapter(adapter);

            AlertDialog dialog = alertDialogBuilder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            btnadmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();


                }
            });
            dialog.show();
        }
    public void  delectAlert(){
        AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(this);
        final View custom_layout1 = getLayoutInflater().inflate(R.layout.layout_delete_experience, null);
        alertDialogBuilder1.setView(custom_layout1);

        AlertDialog alertDialog1 = alertDialogBuilder1.create();
        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tvNo= (TextView)custom_layout1.findViewById(R.id.tv_no);
        tvYes= (TextView)custom_layout1.findViewById(R.id.tv_yes);
        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();


            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.dismiss();
            }
        });
        alertDialog1.show();
    }
    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
        body.addProperty("update_type", "Edit");
        body.addProperty("title", binding.etMovieName.getText().toString());
        body.addProperty("location_name",location_name);
        body.addProperty("city_name",city_name);
        body.addProperty("city_latitude",city_latitude);
        body.addProperty("city_longitude",city_longitude);
        body.addProperty("postalcode","");
        body.addProperty("countryName",countryName);
        body.addProperty("stateName",stateName);
        body.addProperty("countryShortName","");
        body.addProperty("stateShortName","");

        body.addProperty("sk_event_id", experinceEventsId);
        body.addProperty("title", binding.etMovieName.getText().toString());
//        body.addProperty("location_id", locationId);
        body.addProperty("event_image", "data:image/png;base64," + img);
        body.addProperty("event_type", "User Events");
        body.addProperty("minimum_member", binding.etMinMember.getText().toString());
        body.addProperty("maximum_member", binding.etMax.getText().toString());
        body.addProperty("category_id", catId);
        body.addProperty("tags", tagsName);
        body.addProperty("description", binding.etDescription.getText().toString());
        body.addProperty("event_date", date);
        body.addProperty("event_time", event_time);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<EditeExperienceModle> call = api.EditeYourExperince(access_token, "application/json", body);
        call.enqueue(new Callback<EditeExperienceModle>() {
            @Override
            public void onResponse(Call<EditeExperienceModle> call, Response<EditeExperienceModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), YourExperienceActivity.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_LONG).show();


                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Events Updated Successfully")) {
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
            public void onFailure(Call<EditeExperienceModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callApiLocationList() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<LocationAutoSuggestionModle> call = api.GetLocationList(access_token, "application/json", "All", "1", "location");
        call.enqueue(new Callback<LocationAutoSuggestionModle>() {
            @Override
            public void onResponse(Call<LocationAutoSuggestionModle> call, Response<LocationAutoSuggestionModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    LocationAutoSuggestionModle locationList = response.body();
                    autoSuggestiolocationList = (ArrayList<LocationAutoSuggestionModle.Datum>) locationList.getData();
                    LocationList();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Location Details")) {
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
            public void onFailure(Call<LocationAutoSuggestionModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void LocationList() {
        if (autoSuggestiolocationList!=null){
            locationAutoAdapter = new LocationAutoAdapter(getApplicationContext(), autoSuggestiolocationList, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvLocationDetails.setLayoutManager(layoutManager);
            binding.rvLocationDetails.setAdapter(locationAutoAdapter);
        }

    }

    public void selectLocation(String address, int position) {
        binding.auto.setText(address);
    }

    public void selectId(String locationID, int position) {
        locationId = locationID;
    }

    @Override
    public void onTagsChanged(Collection<String> tags) {

    }

    @Override
    public void onEditingFinished() {

    }

    private void setData() {
        for (int i = 0; i < experinceList.size(); i++) {
            if (!experinceList.get(i).getEventImagePic().isEmpty()) {
                Picasso.get().load(experinceList.get(i).getEventImagePic())
                        .into(binding.ivBg);
                // binding.rlUploadRl.setVisibility(View.GONE);
            } else {
                // binding.rlUploadRl.setVisibility(View.VISIBLE);
            }
            binding.etMovieName.setText(experinceList.get(i).getTitle());
            binding.tvSelectedClass.setText(experinceList.get(i).getCategoryName());
            binding.auto.setText(experinceList.get(i).getLocationName()+","+experinceList.get(i).getCityName()+","+experinceList.get(i).getStateName());
            binding.tvTime.setText(experinceList.get(i).getEventTime());
            binding.tvDate.setText(experinceList.get(i).getEventDate());
            catId=experinceList.get(i).getCategoryId();
            date=experinceList.get(i).getEventDate();
            event_time=experinceList.get(i).getEventTime();
            encode=experinceList.get(i).getEventImagePic();
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        URL url = new URL(encode);
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        img = ImageUtil.getStringImage(image);
                    } catch(IOException e) {
                        System.out.println(e);
                    }
                }
            }).start();

            locationId=experinceList.get(i).getLocationId();
            binding.etMinMember.setText(experinceList.get(i).getMinimumMember());
            binding.etMax.setText(experinceList.get(i).getMaximumMember());
            binding.etDescription.setText(experinceList.get(i).getDescription());
            List<String> tag = experinceList.get(i).getTagList();
            if(!tag.isEmpty())
            {
                String[] stockArr = new String[tag.size()];
                stockArr = tag.toArray(stockArr);
                binding.tagsEditText.setTags(stockArr);
            }

        }
    }




    private void callGetExperinceDetailsApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading1 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ExperinceModle> call = api.GetExperincesDetails(access_token, "application/json", experinceEventsId, "All", "All", "EVENT_VIEW", "All");
        call.enqueue(new Callback<ExperinceModle>() {
            @Override
            public void onResponse(Call<ExperinceModle> call, Response<ExperinceModle> response) {
                loading1.cancel();
                if (response.isSuccessful()) {
                    experinceModle = response.body();
                    experinceList = (ArrayList) experinceModle.getData();
                    setData();
                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);
                        if ((jObjError.getString("message")).equals("No Data Available")) {
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
            public void onFailure(Call<ExperinceModle> call, Throwable t) {
                loading1.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callAllCatsApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading2 = ProgressDialog.show(this, "Loading .....", "wait....", false, false);
        Call<ChooseYourInterestModle> call = api.GetCategoryResponse(access_token, "All", "All", "category_subcategory");
        call.enqueue(new Callback<ChooseYourInterestModle>() {
            @Override
            public void onResponse(Call<ChooseYourInterestModle> call, Response<ChooseYourInterestModle> response) {
                loading2.cancel();
                if (response.isSuccessful()) {
                    yourInterestModle = response.body();
                    moviesListModles = (ArrayList) yourInterestModle.getData().getCategory();
                    initializeNewChatClassRecycler();

                } else {
                    try {
                        String error_message = response.errorBody().string();
                        JSONObject jObjError = new JSONObject(error_message);

                        if ((jObjError.getString("message")).equals("Invalid Emaileee Id")) {
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
            public void onFailure(Call<ChooseYourInterestModle> call, Throwable t) {
                loading2.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }
    private AdapterView.OnItemClickListener autocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            try {
                final AutocompletePrediction item = adapter.getItem(i);
                String placeID = null;
                if (item != null) {
                    placeID = item.getPlaceId();


                }

//                To specify which data types to return, pass an array of Place.Fields in your FetchPlaceRequest
//                Use only those fields which are required.

                List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS
                        , Place.Field.LAT_LNG);

                FetchPlaceRequest request = null;
                if (placeID != null) {
                    request = FetchPlaceRequest.builder(placeID, placeFields)
                            .build();
                }

                if (request != null) {
                    placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onSuccess(FetchPlaceResponse task) {
                            location_name= String.valueOf(task.getPlace().getName());
                            String Address=String.valueOf(task.getPlace().getAddress());
                            String[] values = Address.split(",");
                            city_name=values[0];
                            countryName=values[2];
                            stateName=values[1];
                            System.out.println(values[0]);

                            String searchlat = (task.getPlace().getLatLng().toString());
                            searchlat = searchlat.substring(8);
                            searchlat = searchlat.replaceAll("[\\p{Ps}\\p{Pe}]", "");
                            String[] latLng = searchlat.split(",");
                            city_latitude=latLng[0];
                            city_longitude=latLng[1];
                            ven_lat = Double.parseDouble(latLng[0]);
                            ven_long = Double.parseDouble(latLng[1]);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            e.printStackTrace();
                            binding.response.setText(e.getMessage());
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

}