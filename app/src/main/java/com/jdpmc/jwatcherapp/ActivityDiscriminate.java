package com.jdpmc.jwatcherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
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

import static com.jdpmc.jwatcherapp.utils.Constants.BASE_URL;
public class ActivityDiscriminate extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.image_header2)
    AppCompatImageView image_header;

    @BindView(R.id.selectImage2)
    AppCompatImageView selectImage;

    @BindView(R.id.comment2)
    TextInputLayout comment;

    @BindView(R.id.input_comment2)
    TextInputEditText input_comment;

    @BindView(R.id.button_upload3)
    MaterialButton button_upload;

    @BindView(R.id.progress4)
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
    private static final String TAG = ActivityDiscriminate.class.getSimpleName();
    private String path;
    private static final String POST_PATH = "post_path";
    private String m_comment;
    private Spinner covidtyp;
    static final int REQUEST_VIDEO_CAPTURE = 3;
    private int Hold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discriminate);
        final MediaPlayer mp =  MediaPlayer.create(getApplicationContext(), R.raw.clicktoselect);
        final MediaPlayer mp2 = MediaPlayer.create(getApplicationContext(), R.raw.inaccesablebuilding);
        final MediaPlayer mp3 = MediaPlayer.create(getApplicationContext(), R.raw.domesticabuse);
        final MediaPlayer mp4 = MediaPlayer.create(getApplicationContext(), R.raw.sgbv);
        final MediaPlayer mp5 = MediaPlayer.create(getApplicationContext(), R.raw.equaloppurtunites);
        final MediaPlayer mp6 = MediaPlayer.create(getApplicationContext(), R.raw.others);
        final MediaPlayer mp9 = MediaPlayer.create(getApplicationContext(), R.raw.comment);

        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        covidtyp = (Spinner) findViewById(R.id.covidtype2);
        String text = covidtyp.getSelectedItem().toString();

        if(covidtyp.getSelectedItem().equals("Inaccessible Building")){
            mp2.start();
        }
        if(text == "Domestic Abuse"){
            mp3.start();
        }
        if(text == "SGBV"){
            mp4.start();
        }
        if(text == "Equal oppurtunities issuing"){
            mp5.start();
        }
        if(text == "Others"){
            mp6.start();
        }
        if (comment == null) {
            mp9.start();
        }
        covidtyp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (parentView.getItemAtPosition(position).equals("Select...")) {

                }else{
                    String item = parentView.getItemAtPosition(position).toString();

                    if (parentView.getItemAtPosition(position).equals("Inaccessible Building")){
                        mp2.start();
                    }
                    if (parentView.getItemAtPosition(position).equals("Domestic Abuse")){
                        mp3.start();
                    }
                    if (parentView.getItemAtPosition(position).equals("SGBV")){
                        mp4.start();
                    }
                    if (parentView.getItemAtPosition(position).equals("Equal oppurtunities issuing")){
                        mp5.start();
                    }
                    if (parentView.getItemAtPosition(position).equals("Others")){
                        mp6.start();
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });


        button_upload.setOnClickListener(this);
        selectImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final MediaPlayer mp7 = MediaPlayer.create(getApplicationContext(), R.raw.imageupload);
        switch (v.getId()) {

            case R.id.button_upload3:
                if (postPath == null) {
                    Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
                } else {

                    String comment = Objects.requireNonNull(input_comment.getText()).toString();
                    if (comment.isEmpty() || comment == null) {
                        submitDetails("comment");

                    } else {
                        submitDetails(comment);

                    }

                }
                break;
            case R.id.selectImage2:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(ActivityDiscriminate.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                    } else {
                        launchImagePicker();
                        mp7.start();
                    }
                } else {
                    launchImagePicker();
                    mp7.start();
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
                Toast.makeText(ActivityDiscriminate.this, "Permission denied, the permissions are very important for the apps usage", Toast.LENGTH_SHORT).show();
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

    private void submitDetails(String mcomment){
        progress.setVisibility(View.VISIBLE);
        Service userService = DataGenerator.createService(Service.class, BASE_URL);

        // create part for file (photo, video, ...)
        MultipartBody.Part body = prepareFilePart("passport");
        // create a map of data to pass along
        String mtype = covidtyp.getSelectedItem().toString();
        String mlongitude = PreferenceUtils.getLocationLongitude(getApplicationContext());
        String mlatitude = PreferenceUtils.getLocationLatitude(getApplicationContext());
        String mphonenumber = PreferenceUtils.getPhoneNumber(getApplicationContext());
        //convert String to RequestBody to pass along with Image
        RequestBody comment = createPartFromString(mcomment);
        RequestBody longitude = createPartFromString(mlongitude);
        RequestBody latitude = createPartFromString(mlatitude);
        RequestBody reporter = createPartFromString(mphonenumber);
        RequestBody type = createPartFromString(mtype);

        Call<Void> call = userService.uploadComments(comment, body, reporter, latitude, longitude, type);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                final MediaPlayer mp8 = MediaPlayer.create(getApplicationContext(), R.raw.dicriminationreport);
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    emptyInputEditText();
                    Toast.makeText(ActivityDiscriminate.this, "Discrimination Reported successfully", Toast.LENGTH_SHORT).show();
               mp8.start();
                } else {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(ActivityDiscriminate.this, "error Reporting Discrimination...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(ActivityDiscriminate.this, "Error Reporting Discrimination..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        input_comment.setText("");
        image_header.setImageResource(R.color.colorPrimary);
        postPath.equals(null);
        path.equals(null);
        refreshActivity();
    }

    private void refreshActivity(){
        recreate();
    }
}