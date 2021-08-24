package com.us.clique.bottomNavigation.fragments;

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
import android.os.Build;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
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
import com.us.clique.activites.MinMaxFilter;
import com.us.clique.activites.YourExperienceActivity;
import com.us.clique.adapter.MoviesListAdapter;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationFactory;
import com.us.clique.bottomNavigation.fragments.module.BottomNavigationViewModel;
import com.us.clique.bottomNavigation.interfaces.SelectLocationName;
import com.us.clique.bottomNavigation.modle.ChooseYourInterestModle;
import com.us.clique.bottomNavigation.modle.CreateExperiencesModle;
import com.us.clique.databinding.FragmentAddBinding;
import com.us.clique.interfaces.SelectMoviesName;
import com.us.clique.location.LocationAutoSuggestionAdapter;
import com.us.clique.location.LocationAutoSuggestionModle;
import com.us.clique.map.AutoCompleteAdapter;
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
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import mabbas007.tagsedittext.TagsEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.facebook.FacebookSdk.getApplicationContext;

public class CreateExperienceFragment extends Fragment implements View.OnClickListener, SelectMoviesName, TagsEditText.TagsEditListener, SelectLocationName {
    Context context;
    BottomNavigationViewModel bottomNavigationViewModel;
    @Inject
    BottomNavigationFactory bottomNavigationFactory;

    FragmentAddBinding binding;
    private final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 0x00AF;
    //    private static int REQUEST_PICK_IMAGE = 2;
    private static final int PICK_FROM_GALLERY = 1;
    DatePickerDialog datepicker;
    TimePickerDialog timePickerDialog;
    private static final int REQUEST_PERMISSIONS = 100;
    String amPm;
    ArrayList<ChooseYourInterestModle.Data.Category> moviesListModles = new ArrayList<>();
    ChooseYourInterestModle yourInterestModle;

    MoviesListAdapter moviesListAdapter;
    Bitmap bitmap;
    String img;
    ProgressDialog loading, loading1;
    UserSessionManager session;
    String access_token = "";
    int setBelowMin;
    String SelectedMoviveName = "", catId;
    LocationAutoSuggestionAdapter autoSuggestionAdapter;
    ArrayList<LocationAutoSuggestionModle.Datum> autoSuggestiolocationList = new ArrayList<>();
    String locationId, tagsName, event_time, date,apiKey,location_name,city_name,city_latitude,city_longitude,postalcode,countryName,stateName,countryShortName,stateShortName;
    ArrayList<String> tagsList = new ArrayList<>();
    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteAdapter adapter;
    PlacesClient placesClient;
    Double ven_lat=0.0,ven_long=0.0;

    // TagsEditText mtagsEditText;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        AndroidSupportInjection.inject(this);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add, container, false);
        bottomNavigationViewModel = new ViewModelProvider(this, bottomNavigationFactory).get(BottomNavigationViewModel.class);
        binding.setAddEvent(bottomNavigationViewModel);
        session = new UserSessionManager(getApplicationContext());
        access_token = session.getUserDetails().get(UserSessionManager.KEY_accessToken);
        String time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
        event_time = LocalTime.parse(time).format(DateTimeFormatter.ofPattern("h:mm a"));
        binding.tvTime.setText(event_time);
        binding.auto.setOnClickListener(this);
        //binding.rlSelectClass.setOnClickListener(this);
        binding.tvSelectedClass.setOnClickListener(this);
        binding.btnCreateEcperince.setOnClickListener(this);

     /*   NetworkConnection networkConnection=new NetworkConnection(getApplicationContext());
        if(networkConnection.isInternetAvailable())
        {*/
        apiKey = getString(R.string.mapkey);
        if (!Places.isInitialized()) {
            Places.initialize(getContext(),apiKey);
        }
        placesClient = Places.createClient(getContext());
        initAutoCompleteTextView();
//        callApiLocationList();
        callAllCatsApi();
     /*   }else
        {
//            View v = inflater.inflate(R.layout.activity_networksignal, container, false);
//            return v;
        }*/
        // mtagsEditText = (TagsEditText)findViewById(R.id.tagsEditText);
        binding.tagsEditText.setHint("Enter Your Tags");
        binding.tagsEditText.setTagsListener(this);
        binding.tagsEditText.setTagsWithSpacesEnabled(true);
        binding.tagsEditText.setThreshold(1);
//        tagsName = binding.tagsEditText.getText().toString();
        binding.ivUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Permission not available requesting permission");
                    ActivityCompat.requestPermissions(getActivity(),
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
                datepicker = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                cldr.set(year, monthOfYear, dayOfMonth);
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM");
                                String dateString = sdf.format(cldr.getTime());
                                //   binding.tvDate.setText(dateString);
                                binding.tvDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
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
                final int[] hour = {cldr.get(Calendar.HOUR_OF_DAY)};
                int minute = cldr.get(Calendar.MINUTE);
//                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        boolean isPM = (selectedHour >= 12);
                        if (selectedHour >= 12) {
                            amPm = " PM";
                        } else {
                            amPm = " AM";
                        }
                        binding.tvTime.setText(String.format("%02d:%02d %s", (selectedHour == 12 || selectedHour == 0) ? 12 : selectedHour % 12, selectedMinute, isPM ? "PM" : "AM"));
                        event_time = binding.tvTime.getText().toString();

                    }
                }, hour[0], minute, false);//Yes 24 hour time

                timePickerDialog.show();
            }

        });


//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm  aaa");
//        String currentTime = sdf.format(new Date());
//        event_time=currentTime;


//        binding.etAddress.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                binding.tvError.setVisibility(View.GONE);
//                if (binding.etAddress.getText().toString().isEmpty()) {
//                    binding.rvLocationDetails.setVisibility(View.GONE);
//                } else {
//                    binding.rvLocationDetails.setVisibility(View.VISIBLE);
//                }
//                if (autoSuggestionAdapter != null) {
//                    autoSuggestionAdapter.getFilter().filter(charSequence.toString());
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
        checkAfterComplete();
        binding.etMinMember.setFilters(new InputFilter[]{new MinMaxFilter("1", "7")});
        binding.etMax.setFilters(new InputFilter[]{new MinMaxFilter("1", "7")});
//

        return binding.getRoot();
    }

    private void initAutoCompleteTextView() {
        binding.auto.setThreshold(1);
        binding.auto.setOnItemClickListener(autocompleteClickListener);
        adapter = new AutoCompleteAdapter(getContext(), placesClient);
        binding.auto.setAdapter(adapter);
    }

    public void checkAfterComplete() {

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //  binding.tvError.setVisibility(View.GONE);
                String minvalue = binding.etMinMember.getText().toString();
                if (minvalue != null) {
                    try {

                        setBelowMin = Integer.parseInt(minvalue);
                    } catch (Exception E) {
                        Toast.makeText(getApplicationContext(), "Number format exception", Toast.LENGTH_LONG).show();
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {
                processButtonByTextLength();
            }
        };
        binding.etMinMember.addTextChangedListener(tw);


        TextWatcher tw1 = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String maxvalue = binding.etMax.getText().toString();
                if (maxvalue != null) {

                    try {

                        int setMaxValue = Integer.parseInt(maxvalue);
                        if (setMaxValue < setBelowMin) {
                            binding.tvMinmaxError.setVisibility(View.VISIBLE);
                            binding.tvMinmaxError.setText("Minimum value exceeding");

                        } else {
                            binding.tvMinmaxError.setVisibility(View.GONE);
                        }
                    } catch (Exception E) {
                        Toast.makeText(getApplicationContext(), "Number format exception", Toast.LENGTH_LONG).show();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        binding.etMax.addTextChangedListener(tw1);
    }

    private void processButtonByTextLength() {
        if (binding.etMovieName.getText().toString().startsWith(" ")) {
            binding.tvError.setVisibility(View.VISIBLE);
            binding.tvError.setText("Space Not Valid");
        } else if (binding.etMovieName.getText().toString().matches("[a-zA-Z ]+")) {

        } else if (binding.etMovieName.getText().toString().isEmpty()) {

        } else {
            binding.tvError.setVisibility(View.VISIBLE);
            binding.tvError.setText("Please Enter title in Alphabets only");
        }

        String inputText = binding.etMovieName.getText().toString();
        String inputText2 = binding.tvSelectedClass.getText().toString();
        String inputText3 = binding.auto.getText().toString();
        String inputText4 = binding.tvDate.getText().toString();
        String inputText5 = binding.etMinMember.getText().toString();
        String inputText6 = binding.etMax.getText().toString();
        String inputText7 = binding.tvTime.getText().toString();

        if ((inputText!=null && inputText2!=null&& inputText3!=null
                && inputText4!=null && inputText5!=null && inputText6!=null && inputText7!=null)) {
            binding.btnCreateEcperince.setEnabled(true);
            binding.btnCreateEcperince.setBackgroundResource(R.drawable.button_background);

        } else {
            binding.btnCreateEcperince.setEnabled(false);
            binding.btnCreateEcperince.setBackgroundResource(R.drawable.button_bg_enable);

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
//        binding.etAddress.addTextChangedListener(tw);
        binding.tvDate.addTextChangedListener(tw);
        binding.etMinMember.addTextChangedListener(tw);
        binding.etMax.addTextChangedListener(tw);

    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                      /*  if (selectedImage != null){
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
                      /*  Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                if (picturePath != null){
                                    binding.ivUploadImage.setVisibility(View.VISIBLE);
                                    binding.rlUploadRl.setVisibility(View.GONE);
                                    binding.ivUploadImage.setImageURI(selectedImage);
//                                    binding.ivUploadImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                }else {
                                    binding.ivUploadImage.setVisibility(View.GONE);
                                    binding.rlUploadRl.setVisibility(View.VISIBLE);
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
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
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
        // moviesListModles.clear();
        moviesListAdapter = new MoviesListAdapter(getContext(), moviesListModles, "Select Movie Name", this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        binding.rvSelectClass.setLayoutManager(mLayoutManager);
        binding.rvSelectClass.setItemAnimator(new DefaultItemAnimator());
//        moviesListModles.add(new MoviesListModle("Ugram"));
//        moviesListModles.add(new MoviesListModle("Rajakumara"));
//        moviesListModles.add(new MoviesListModle("OM"));
        binding.rvSelectClass.setAdapter(moviesListAdapter);
    }

    private void selectClass() {
        if (binding.expandableLayoutSelectClass.isExpanded()) {
            binding.ivSelectClass.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_expend));
            binding.expandableLayoutSelectClass.collapse();
        } else {
            binding.ivSelectClass.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_collapse));
            binding.expandableLayoutSelectClass.expand();
        }
    }

  /*  @Override
    public void onBackPressed() {
        super.onBackPressed();
    }*/

    @Override
    public void setSelectMoviesName(String name, String categoryId) {
        catId = categoryId;
        binding.tvSelectedClass.setText(name);
        SelectedMoviveName = binding.tvSelectedClass.getText().toString();
        binding.expandableLayoutSelectClass.collapse();
        binding.ivSelectClass.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_expend));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_selected_class:
                selectClass();
                initializeNewChatClassRecycler();
                binding.tvSelectedClass.setInputType(InputType.TYPE_NULL);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                break;
            case R.id.iv_onBackPress:
              /*  FragmentManager fragManager = getFragmentManager();
                AddFragment fragment = new AddFragment();
                FragmentTransaction transaction = fragManager.beginTransaction();
                transaction.remove(fragment);
                transaction.addToBackStack(null);
                transaction.commit();*/
//                AddFragment rSum = new AddFragment();
//                  getSupportFragmentManager().beginTransaction().remove(rSum).commit();
               /* getActivity().getFragmentManager().beginTransaction().remove(AddFragment.this)
                        .commit();*/
                // onBackPressed();
                break;
            case R.id.btn_createEcperince:
                tagsList.add(binding.tagsEditText.getText().toString());
//                tagsName = tagsName.replace(",$","");
//                if (tagsName.endsWith(",")) {
//                    tagsName = tagsName.substring(0, tagsName.length() - 1);
//                }
//                tagsName = binding.tagsEditText.getText().toString().replace("[", "")
//                        .replace("]", "").replace("  ", ",  ");
                tagsName = binding.tagsEditText.getText().toString().replace(' ', ',');
                if (tagsName.endsWith(",")) {
                    tagsName = tagsName.substring(0, tagsName.length() - 1);
                }
                callApi();
                break;
        }
    }

    private void callApi() {
        Api api = ApiClient.getClient().create(Api.class);
        JsonObject body = new JsonObject();
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
        loading = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<CreateExperiencesModle> call = api.PostCreateExperience(access_token, "application/json", body);
        call.enqueue(new Callback<CreateExperiencesModle>() {
            @Override
            public void onResponse(Call<CreateExperiencesModle> call, Response<CreateExperiencesModle> response) {
                loading.cancel();
                if (response.isSuccessful()) {
                    Intent i = new Intent(getApplicationContext(), YourExperienceActivity.class);
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
            public void onFailure(Call<CreateExperiencesModle> call, Throwable t) {
                loading.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    private void callApiLocationList() {
        Api api = ApiClient.getClient().create(Api.class);
        loading = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
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
        if (autoSuggestiolocationList != null) {
            autoSuggestionAdapter = new LocationAutoSuggestionAdapter(getApplicationContext(), autoSuggestiolocationList, this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            binding.rvLocationDetails.setLayoutManager(layoutManager);
            binding.rvLocationDetails.setAdapter(autoSuggestionAdapter);
        }

    }
   /* public void selectLocation(String  address) {
        binding.etAddress.setText(address);
    }

    public void selectId(String locationID) {
        locationId = locationID;
    }*/


    @Override
    public void onTagsChanged(Collection<String> tags) {

    }

    @Override
    public void onEditingFinished() {

    }

    private void callAllCatsApi() {
        Api api = ApiClient.getClient().create(Api.class);
        loading1 = ProgressDialog.show(getContext(), "Loading .....", "wait....", false, false);
        Call<ChooseYourInterestModle> call = api.GetCategoryResponse(access_token, "All", "All", "category_subcategory");
        call.enqueue(new Callback<ChooseYourInterestModle>() {
            @Override
            public void onResponse(Call<ChooseYourInterestModle> call, Response<ChooseYourInterestModle> response) {
                loading1.cancel();
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
                loading1.cancel();
                // showToast(getApplicationContext(), t.toString());

            }
        });

    }

    @Override
    public void selectLocation(String address, int position) {
//        binding.etAddress.setText(address);
    }

    @Override
    public void selectId(String locationID, int position) {
        locationId = locationID;
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