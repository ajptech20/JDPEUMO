package com.a2tocsolutions.nispsasapp.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.a2tocsolutions.nispsasapp.BuildConfig;
import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.a2tocsolutions.nispsasapp.utils.PreferenceUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.a2tocsolutions.nispsasapp.utils.Constants.BASE_URL;

public class cpccomplaint extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    @BindView(R.id.image_header)
    AppCompatImageView image_header;

    @BindView(R.id.selectImage)
    AppCompatImageView selectImage;

    @BindView(R.id.comment)
    TextInputLayout comment;

    @BindView(R.id.input_comment)
    TextInputEditText input_comment;

    @BindView(R.id.comment1)
    TextInputLayout comment1;

    @BindView(R.id.input_comment1)
    TextInputEditText input_comment1;

    @BindView(R.id.comment2)
    TextInputLayout comment2;

    @BindView(R.id.input_comment2)
    TextInputEditText input_comment2;

    @BindView(R.id.comment3)
    TextInputLayout comment3;

    @BindView(R.id.input_comment3)
    TextInputEditText input_comment3;

    @BindView(R.id.comment4)
    TextInputLayout comment4;

    @BindView(R.id.input_comment4)
    TextInputEditText input_comment4;

    @BindView(R.id.comment5)
    TextInputLayout comment5;

    @BindView(R.id.input_comment5)
    TextInputEditText input_comment5;

    @BindView(R.id.comment6)
    TextInputLayout comment6;

    @BindView(R.id.input_comment6)
    TextInputEditText input_comment6;

    @BindView(R.id.date_text)
    TextView date_text;

    @BindView(R.id.button_upload)
    MaterialButton button_upload;

    @BindView(R.id.progress)
    ProgressBar progress;

    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int REQUEST_PICK_PHOTO = 2;
    private static final int CAMERA_PIC_REQUEST = 1111;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri fileUri;
    private String mediaPath;
    private String mImageFileLocation = "";
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";
    private String postPath;
    private static final String TAG = cpccomplaint.class.getSimpleName();
    private String path;
    private static final String POST_PATH = "post_path";
    static final int REQUEST_VIDEO_CAPTURE = 3;
    private Calendar calendar;
    private int year, day, month;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_complaints);

        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setTitle("File a Complaint");
        button_upload.setOnClickListener(this);
        selectImage.setOnClickListener(this);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DATE);
        month++;

        date = year + "-" + month + "-" + day;
        date_text.setText(date);
    }

    public void setDate(View v) {
        Calendar now = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, cpccomplaint.this, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        month++;
        day = dayOfMonth;
        month = month;
        date = year + " - " + month + " - " + dayOfMonth;
        date_text.setText(date);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_upload:
                comment.setError(null);
                comment1.setError(null);
                comment2.setError(null);
                comment3.setError(null);
                comment4.setError(null);
                comment5.setError(null);
                if (postPath == null) {
                    Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
                } else if (input_comment.length() == 0) {
                    comment.setError("Your email is required!");
                } else if (input_comment1.length()== 0) {
                    comment1.setError("Title is Required!");
                }else if (input_comment2.length() == 0) {
                    comment2.setError("Complaint is required!");
                }else if (input_comment4.length() == 0) {
                    comment4.setError("Company/Individual name is required!");
                } else if (input_comment5.length()== 0) {
                    comment5.setError("Company/Individual Phone Number is Required!");
                }else{
                    String mreporteremail = Objects.requireNonNull(input_comment.getText()).toString();
                    String mtitle = Objects.requireNonNull(input_comment1.getText()).toString();
                    String mfullreport = Objects.requireNonNull(input_comment2.getText()).toString();
                    String mcomemail = Objects.requireNonNull(input_comment3.getText()).toString();
                    String mcomname = Objects.requireNonNull(input_comment4.getText()).toString();
                    String mcomphone = Objects.requireNonNull(input_comment5.getText()).toString();
                    String mtamount = Objects.requireNonNull(input_comment6.getText()).toString();
                    String mtdate = date_text.getText().toString();

                        submitDetails(mreporteremail, mtitle, mfullreport, mcomemail, mcomname, mcomphone, mtamount, mtdate);
                }
                break;
            case R.id.selectImage:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(cpccomplaint.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                    } else {
                        launchImagePicker();
                    }
                } else {
                    launchImagePicker();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                launchImagePicker();
            }else{
                Toast.makeText(cpccomplaint.this, "Permission denied, the permissions are very important for the apps usage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void launchImagePicker(){
        new MaterialDialog.Builder(this)
                .title(R.string.uploadMedia)
                .items(R.array.uploadImages)
                .itemsIds(R.array.itemIds)
                .itemsCallback((dialog, view, which, text) -> {
                    switch (which) {
                        case 0:
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO);
                            break;
                        case 1:
                            captureImage();
                            break;
                        case 2:
                            Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                            if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
                                startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
                            }
                            break;
                        case 3:
                            image_header.setImageResource(R.color.colorPrimary);
                            postPath.equals(null);
                            path.equals(null);
                            refreshActivity();
                            break;
                    }
                })
                .show();
    }

    /**
     * Launching camera app to capture image
     */
    private void captureImage() {
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            Intent callCameraApplicationIntent = new Intent();
            callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            // We give some instruction to the intent to save the image
            File photoFile = null;

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile();
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (IOException e) {
                Logger.getAnonymousLogger().info("Exception error in generating the file");
                e.printStackTrace();
            }
            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            Uri outputUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    Objects.requireNonNull(photoFile));
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Logger.getAnonymousLogger().info("Calling the camera App by intent");

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
        }
    }

    File createImageFile() throws IOException {
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app");
        Logger.getAnonymousLogger().info("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory, imageFileName + ".jpg");
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set");

        mImageFileLocation = image.getAbsolutePath();
        fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    // Get the Image from data
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    assert selectedImage != null;
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    mediaPath = cursor.getString(columnIndex);
                    // Set the Image in ImageView for Previewing the Media
                    image_header.setImageBitmap(BitmapFactory.decodeFile(mediaPath));
                    cursor.close();

                    postPath = mediaPath;
                    path = mediaPath;
                }

            }else if (requestCode == CAMERA_PIC_REQUEST){
                if (Build.VERSION.SDK_INT > 21) {
                    Glide.with(this).load(mImageFileLocation).into(image_header);
                    postPath = mImageFileLocation;
                    path = postPath;
                }else{
                    Glide.with(this).load(fileUri).into(image_header);
                    postPath = fileUri.getPath();
                    path = postPath;
                }
            } else if (requestCode == REQUEST_VIDEO_CAPTURE) {
                Uri videoUri = data.getData();
                //video_header.setVideoURI(videoUri);
                //video_header.start();
                Toast.makeText(this, "" + videoUri, Toast.LENGTH_SHORT).show();

            }
        }
        else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putString(POST_PATH, path);
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        path = savedInstanceState.getString(POST_PATH);
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(
                MultipartBody.FORM, descriptionString);
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

    private void submitDetails(String sreporteremail, String stitle, String sfullreport, String scomemail, String scomname, String scomphone, String stamount, String stdate){
        progress.setVisibility(View.VISIBLE);
        Service userService = DataGenerator.createService(Service.class, BASE_URL);

        // create part for file (photo, video, ...)
        MultipartBody.Part body = prepareFilePart("passport");
        // create a map of data to pass along
        String slongitude = PreferenceUtils.getLocationLongitude(getApplicationContext());
        String slatitude = PreferenceUtils.getLocationLatitude(getApplicationContext());
        String sphonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        String susername = PreferenceUtils.getUsername(getApplicationContext());
        //convert String to RequestBody to pass along with Image
        RequestBody reporteremail = createPartFromString(sreporteremail);
        RequestBody title = createPartFromString(stitle);
        RequestBody fullreport = createPartFromString(sfullreport);
        RequestBody comemail = createPartFromString(scomemail);
        RequestBody comname = createPartFromString(scomname);
        RequestBody comphone = createPartFromString(scomphone);
        RequestBody tamount = createPartFromString(stamount);
        RequestBody tdate = createPartFromString(stdate);
        RequestBody longitude = createPartFromString(slongitude);
        RequestBody latitude = createPartFromString(slatitude);
        RequestBody reporter = createPartFromString(sphonenumber);
        RequestBody sendername = createPartFromString(susername);

        Call<Void> call = userService.uploadComplaint(reporteremail, title, fullreport, comemail, comname, comphone, tamount, tdate, reporter, latitude, longitude, sendername, body);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    emptyInputEditText();
                    Toast.makeText(cpccomplaint.this, "Complaint Successfully Sent", Toast.LENGTH_SHORT).show();
                } else {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(cpccomplaint.this, "Error uploading Complaint", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(cpccomplaint.this, "Error uploading Coomplaint " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        input_comment.setText("");
        input_comment1.setText("");
        input_comment2.setText("");
        input_comment3.setText("");
        input_comment4.setText("");
        input_comment5.setText("");
        input_comment6.setText("");
        image_header.setImageResource(R.color.colorPrimary);
        postPath.equals(null);
        path.equals(null);
        refreshActivity();
    }

    private void refreshActivity(){
        recreate();
    }


}
