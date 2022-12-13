package com.a2tocsolutions.nispsasapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.a2tocsolutions.nispsasapp.utils.FancyToast;
import com.a2tocsolutions.nispsasapp.utils.PreferenceUtils;
import com.bumptech.glide.Glide;
import com.ikhiloyaimokhai.nigeriastatesandlgas.Nigeria;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.BindView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDataUpdate extends AppCompatActivity {
    /*@BindView(R.id.phone_user)
    EditText input_user_phone;*/
    @BindView(R.id.image_header)
    AppCompatImageView image_header;

    private Spinner mStateSpinner, mLgaSpinner;
    private String mState, mLga;
    private String postPath;

    private ProgressDialog progressDialog;

    private String mediaPath;
    private String mImageFileLocation = "";

    private String path;
    private List<String> states;
    private static final int SPINNER_HEIGHT = 500;
    //private static final int GalleryPick = 1;

    private ImageView mImageView;
    private Button img_upload;
    private Button pic_activity;
    private ExecutorService mExecutor;

    public static Intent createIntent(Activity activity, Uri uri) {
        Intent intent = new Intent(activity, UserDataUpdate.class);
        intent.setData(uri);
        return intent;
    }

    ImageView croped_image;

    ImageView croped_image2;

    ImageView user_image_cycle;

    ActivityResultLauncher<String> mGetContent;

    String prevStarted = "yes";
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedpreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        if (!sharedpreferences.getBoolean(prevStarted, false)) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putBoolean(prevStarted, Boolean.TRUE);
            editor.apply();
        } else {
            //moveToSecondary();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_data_update_activity);

        mImageView = findViewById(R.id.set_profile_image);
        mExecutor = Executors.newSingleThreadExecutor();

        TextView input_username = findViewById(R.id.name_user);
        input_username.setText(PreferenceUtils.getUsername(getApplicationContext()));
        TextView username = findViewById(R.id.user_name);
        username.setText(PreferenceUtils.getUsername(getApplicationContext()));

        TextView userphone = findViewById(R.id.user_phone);
        userphone.setText(PreferenceUtils.getPhoneNumber(getApplicationContext()));

        TextView input_homeTown = findViewById(R.id.town_user);
        input_homeTown.setText(PreferenceUtils.getTown(getApplicationContext()));
        TextView homeTown = findViewById(R.id.user_town);
        homeTown.setText(PreferenceUtils.getTown(getApplicationContext()));

        TextView userState = findViewById(R.id.user_state);
        userState.setText(PreferenceUtils.getState(getApplicationContext()));

        TextView userLga = findViewById(R.id.user_lga);
        userLga.setText(PreferenceUtils.getLga(getApplicationContext()));

        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
        ImageView imageView = (ImageView) findViewById(R.id.user_image);
        Glide.with(UserDataUpdate.this)
                .load(user_image)
                .into(imageView);

        String user_image_banner = (PreferenceUtils.getUserImage(getApplicationContext()));
        ImageView imageViewBanner = (ImageView) findViewById(R.id.set_profile_image);
        Glide.with(UserDataUpdate.this)
                .load(user_image_banner)
                .into(imageViewBanner);

        progressDialog = new ProgressDialog(UserDataUpdate.this);
        progressDialog.setMessage("Uploading Image Pleas Wait");
        progressDialog.setCancelable(false);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        //final Uri uri = getIntent().getData();
        //mExecutor.submit(new LoadScaledImageTask(this, uri, mImageView, calcImageSize()));

        //postPath = String.valueOf((uri));
        //Toast.makeText(UserDataUpdate.this, "state: " + uri, Toast.LENGTH_LONG).show();

        mStateSpinner = findViewById(R.id.stateSpinner);
        mLgaSpinner = findViewById(R.id.lgaSpinner);
        resizeSpinner(mStateSpinner, SPINNER_HEIGHT);
        resizeSpinner(mLgaSpinner, SPINNER_HEIGHT);
        Button submit = findViewById(R.id.profileSubmit);

        ImageView select_image = findViewById(R.id.set_profile_image);

        croped_image=findViewById(R.id.set_profile_image);
        user_image_cycle=findViewById(R.id.user_image);
        //img_upload=findViewById(R.id.btn_upload_image);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = PreferenceUtils.getPhoneNumber(getApplicationContext());
                String lga = mLgaSpinner.getSelectedItem().toString();
                String state = mStateSpinner.getSelectedItem().toString();
                AutoCompleteTextView polling = (AutoCompleteTextView) findViewById(R.id.autocomplete_Pollingunit);
                String pollingunit = polling.getText().toString();
                submitDetails();
            }
        });

        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dexter.withActivity(UserDataUpdate.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                CropImage.activity()
                                        .setGuidelines(CropImageView.Guidelines.ON)
                                        .setAspectRatio(1, 1)
                                        .start(UserDataUpdate.this);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {
                                if (response.isPermanentlyDenied()){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(UserDataUpdate.this);
                                    builder.setTitle("Permission Required")
                                            .setMessage("Permission to Access Your Device Storage to Pick Image Please go to settings to enable Permission")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    Intent intent = new Intent();
                                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                    startActivityForResult(intent, 51);

                                                }
                                            })
                                            .setNegativeButton("Cancel", null)
                                            .show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        })
                        .check();
            }
        });

        /*select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //mGetContent.launch("image/*");

                Intent intent = new Intent(getApplicationContext(), ImageCroperActivity.class);
                startActivity(intent);
            }
        });*/

        /*mGetContent=registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                Intent intent = new Intent(UserDataUpdate.this,CropperActivity.class);
                intent.putExtra("DATA",result.toString());
                startActivityForResult(intent, 101);
            }
        });*/

        states = Nigeria.getStates();
        //call to method that'll set up state and lga spinner
        setupSpinners();



        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete_wurd);
        AutoCompleteTextView polling = (AutoCompleteTextView) findViewById(R.id.autocomplete_Pollingunit);
// Get the string array
        String[] countries = getResources().getStringArray(R.array.WURDS);
        String[] pollingUnit = getResources().getStringArray(R.array.PollingUnit);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countries);
        textView.setAdapter(adapter);

        ArrayAdapter<String> adapter2 =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pollingUnit);
        polling.setAdapter(adapter2);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                croped_image.setImageURI(resultUri);
                user_image_cycle.setImageURI(resultUri);
                //img_upload.setVisibility(View.VISIBLE);
                File imageFileUp = new File(resultUri.getPath());
                postPath = String.valueOf(new File(resultUri.getPath()));
                PreferenceUtils.saveUserImage(postPath, getApplicationContext());
                //Toast.makeText(UserDataUpdate.this, "File Url:" + imageFileUp, Toast.LENGTH_SHORT).show();
                /*img_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File imageFile = new File(resultUri.getPath());
                        progressDialog.show();
                        AndroidNetworking.upload("https://nispsas.com.ng/NISPSAS/Mobile/RegisterpolinunitUpload")
                                .addMultipartFile("passport", imageFile)
                                .addMultipartParameter("officerphone", String.valueOf("08026265324"))
                                .setPriority(Priority.HIGH)
                                .build()
                                .setUploadProgressListener(new UploadProgressListener() {
                                    @Override
                                    public void onProgress(long bytesUploaded, long totalBytes) {
                                        float progress = (float) bytesUploaded / totalBytes *100;
                                        progressDialog.setProgress((int) progress);
                                    }
                                })
                                .getAsString(new StringRequestListener() {
                                    @Override
                                    public void onResponse(String response) {
                                        Log.i("myTag", response);
                                        try {
                                            progressDialog.dismiss();
                                            JSONObject jsonObject = new JSONObject(response);
                                            int status = jsonObject.getInt("status");
                                            String message = jsonObject.getString("message");
                                            if (status == 0){
                                                Toast.makeText(UserDataUpdate.this, "Unable to Upload Image:" + message, Toast.LENGTH_SHORT).show();
                                            }else{
                                                Toast.makeText(UserDataUpdate.this, message, Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Toast.makeText(UserDataUpdate.this, "Passing Error", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {
                                        progressDialog.dismiss();
                                        anError.printStackTrace();
                                        Toast.makeText(UserDataUpdate.this, "Error Uploading Image", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });*/
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }





    public void setupSpinners() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        //populates the quantity spinner ArrayList

        ArrayAdapter<String> statesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, states);

        // Specify dropdown layout style - simple list view with 1 item per line
        statesAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        // Apply the adapter to the spinner
        statesAdapter.notifyDataSetChanged();
        mStateSpinner.setAdapter(statesAdapter);

        // Set the integer mSelected to the constant values
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mState = (String) parent.getItemAtPosition(position);
                setUpStatesSpinner(position);
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Unknown
            }
        });
    }


    /**
     * method to set up the state spinner
     *
     * @param position current position of the spinner
     */
    private void setUpStatesSpinner(int position) {
        List<String> list = new ArrayList<>(Nigeria.getLgasByState(states.get(position)));
        setUpLgaSpinner(list);
    }


    /**
     * Method to set up the local government areas corresponding to selected states
     *
     * @param lgas represents the local government areas of the selected state
     */
    private void setUpLgaSpinner(List<String> lgas) {

        ArrayAdapter lgaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, lgas);
        lgaAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        lgaAdapter.notifyDataSetChanged();
        mLgaSpinner.setAdapter(lgaAdapter);

        mLgaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                mLga = (String) parent.getItemAtPosition(position);
                //Toast.makeText(UserDataUpdate.this, "state: " + mState + " lga: " + mLga, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }
    /*public void moveToSecondary(){
        // use an intent to travel from one activity to another.
        Intent intent = new Intent(this, seesay.class);
        startActivity(intent);
    }*/

    private void resizeSpinner(Spinner spinner, int height) {
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            //Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner);

            //set popupWindow height to height
            popupWindow.setHeight(height);

        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName) {

        File file = new File(postPath);

        // create RequestBody instance from file
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("*/*"),
                        file
                );

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }



    private void submitDetails() {
        ProgressBar progress = findViewById(R.id.electionProgress);
        progress.setVisibility(View.VISIBLE);
        Service userService = DataGenerator.createService(Service.class, "https://nispsas.com.ng/");

        // create part for file (photo, video, ...)
        MultipartBody.Part body = prepareFilePart("passport");
        EditText input_user_name = findViewById(R.id.name_user);
        EditText input_user_town = findViewById(R.id.town_user);
        // create a map of data to pass along
        String userlga = mLgaSpinner.getSelectedItem().toString();
        String userstate = mStateSpinner.getSelectedItem().toString();
        String mphonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        String username = input_user_name.getText().toString().trim();
        //String userphone = input_user_phone.getText().toString().trim();
        String usertown = input_user_town.getText().toString().trim();
        PreferenceUtils.saveHome(usertown, getApplicationContext());
        PreferenceUtils.saveState(userstate, getApplicationContext());
        PreferenceUtils.saveLga(userlga, getApplicationContext());
        //convert String to RequestBody to pass along with Image
        RequestBody lga = createPartFromString(userlga);
        RequestBody state = createPartFromString(userstate);
        RequestBody officerphone = createPartFromString(mphonenumber);
        RequestBody officername = createPartFromString(username);
        RequestBody town = createPartFromString(usertown);



        Call<Void> call = userService.updateUserdata(officerphone, body, state, lga, officername, town);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    emptyInputEditText();
                    FancyToast.makeText(getApplicationContext(), "Profile Updated successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                } else {
                    progress.setVisibility(View.GONE);
                    FancyToast.makeText(getApplicationContext(), "Error Uploading Profile.....", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
                FancyToast.makeText(getApplicationContext(), "No internet connection...", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        });
    }








    /*private void logUserIn(String officerphone, String state, String lga, String polliunit) {

        ProgressBar progress = findViewById(R.id.electionProgress);
        MultipartBody.Part fileBody = prepareFilePart("passport");

        FancyToast.makeText(getApplicationContext(), String.valueOf(fileBody), FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

        progress.setVisibility(View.VISIBLE);
        String usertoken =   PreferenceUtils.getUserkey(getApplicationContext());
        try {
            Service service = DataGenerator.createService(Service.class, "https://nispsas.com.ng/");
            // create part for file (photo, video, ...)
            //MultipartBody.Part fileBody = prepareFilePart("passport");

            Call<LoginResponse> call = service.pollinUnit(officerphone, state,lga,polliunit);

            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            LoginResponse loginResponse = response.body();

                            progress.setVisibility(View.GONE);
                            emptyInputEditText();
                            PreferenceUtils.saveState(state, getApplicationContext());
                            PreferenceUtils.saveLga(lga, getApplicationContext());
                            PreferenceUtils.savePollingUnit(polliunit, getApplicationContext());
                            FancyToast.makeText(getApplicationContext(), "Your Data has been Updated Successfully", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            /*Intent intent = new Intent(UserDataUpdate.this, seesay.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        progress.setVisibility(View.GONE);
                        FancyToast.makeText(getApplicationContext(), "error sign up", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    progress.setVisibility(View.GONE);
                    FancyToast.makeText(getApplicationContext(), "I am not Connected to the Internet !", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                }
            });
        } catch (Exception e) {
            progress.setVisibility(View.GONE);
            FancyToast.makeText(getApplicationContext(), "error sign up", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
        }
    }*/


    private void emptyInputEditText() {
        mStateSpinner = findViewById(R.id.stateSpinner);
        mLgaSpinner = findViewById(R.id.lgaSpinner);
        AutoCompleteTextView polling = (AutoCompleteTextView) findViewById(R.id.autocomplete_Pollingunit);
        polling.setText("");
    }

}
