package com.jdpmc.jwatcherapp.activities.institutions;

import android.Manifest;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.jdpmc.jwatcherapp.BuildConfig;
import com.jdpmc.jwatcherapp.R;
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

public class EdureportActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.image_header)
    AppCompatImageView image_header;

    @BindView(R.id.selectImage)
    AppCompatImageView selectImage;

    @BindView(R.id.comment)
    TextInputLayout comment;

    @BindView(R.id.input_comment)
    TextInputEditText input_comment;

    @BindView(R.id.comdepart)
    TextInputLayout comdepart;

    @BindView(R.id.input_depart)
    TextInputEditText input_depart;

    @BindView(R.id.autoCompleteTextView)
    AutoCompleteTextView autoCompleteTextView;

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
    private static final String TAG = EdureportActivity.class.getSimpleName();
    private String path;
    private static final String POST_PATH = "post_path";
    private String m_comment;
    private Spinner covidtyp;
    static final int REQUEST_VIDEO_CAPTURE = 3;
    String[] schoollist = {"Abubakar Tafawa Balewa University, Bauchi",
            "Akanu Ibiam Federal Polytechnic, Unwana, Ebonyi State","Auchi Polytechnic, Auchi, Edo State","Federal Polytechnic Ado Ekiti, Ekiti State",
            "Federal Polytechnic Bali, Taraba State", "Ahmadu Bello University, Zaria",   "Federal Polytechnic Bauchi, State",   "Federal Polytechnic Bida, P.M.B 55, Niger State.",
            "Bayero University, Kano",  "Federal Government College, Buni-Yadi",  "Federal Polytechnic Damaturu", "Federal Polytechnic Ede, Osun State",
            "Federal Polytechnic Ekowe, Bayelsa State", "Federal Polytechnic Idah",  "Federal University Gashua, Yobe", "Federal Government Girls College, Potiskum",  "Federal Polytechnic Ilaro, Ogun State",
            "Federal Polytechnic Ile-Oluji, Ondo State",   "Federal Polytechnic Kaura Namoda, P.M.B, 1012, Zamfara State",   "Federal University of Petroleum Resources, Effurun",
            "Federal Government College, Ganye",    "Federal Polytechnic Mubi", "Federal Polytechnic Nasarawa, P.M.B. 01 Nasarawa State",
            "Federal University of Technology, Akure",    "Federal Polytechnic Nekede, Owerri, Imo State",  "Federal Polytechnic Offa, State",   "Federal Polytechnic Oko, Anambra State",   "Federal University of Technology, Minna",
            "Federal Polytechnic of Oil and Gas Bonny, Rivers State",   "Federal Polytechnic Ukana, Akwa Ibom State",   "Federal University of Technology, Owerri",
            "Hussaini Adamu Federal Polytechnic,Kazaure Jigawa State", "Kaduna Polytechnic, Kaduna", "Federal Government Girls College, Bauchi",   "Federal University, Dutse, Jigawa State",
            "Waziri Umaru Federal Polytechnic, Birnin Kebbi", "Yaba College of Technology, Yaba, Lagos State",  "Federal University, Dutsin-Ma, Katsina", "Nigerian Army Institute of Technology and Environmental Studies (NAITES) Makurditate",
            "Federal School of Dental Technology and Therapy, Enugu, Enugu State",    "Federal Government College, Wukari ",  "Air Force Institute of Technology Nigerian Air Force",
            "Federal University, Kashere",  "Federal University, Lafia,  ",  "Federal College of Education (Technical), Asaba",
            "Federal University, Lokoja",    "Federal Science And Technical College, Lassa  ",   "Federal College of Education, Kano ", "Federal College of Education (Special), Oyo",
            "Federal University, Ndifu-Alike,   ",  "Federal College of Education, Abeokuta",     "Federal Science and Technical College Orozo, Abuja ",
            "Federal College of Education, Eha-Amufu  ",     "Federal College of Education (Technical), Gombe  ", "Federal University, Otuoke, Bayelsa  ",     "Federal College of Education, Kontagora, Niger State",
            "Federal Science and Technical School -Jalingo ",   "Federal College of Education, Okene  ", "Federal College of Education (Technical) OMoku, Rivers State.", "Federal University, Oye-Ekiti, ",
            "Federal Government Girls College, Jalingo ",    "Federal College of Education (Tech), Potiskum Yobe State  ",
            "Alvan Ikoku College of Education, Owerri Owerri, Imo State.",  "Federal College of Education (Technical), Akoka, Lagos State. ",
            "Federal College of Education (Technical), Bichi, Kano State ", "Federal University, Wukari, Taraba State ",  "Federal College of Education (Technical), Gusau, Zamfara State", "Federal Science And Technical College, Michika ",
            "Federal College of Education,Katsina State ",  "Federal College of Education, Obudu, Cross River State", "Adeyemi College of Education,Ondo, Ondo State ", "Federal College of Education, Pankshin, Plateau State",
            "Federal University, Birnin Kebbi ", "Federal College of Education, Yola , Adamawa State",    "Federal College of Education, . Zaria, Kaduna State.","Federal Government College, Azare",
            "Federal University, Gusau", "Michael Okpara University of Agricultural Umudike", "Federal Government College, Maiduguri",
            "Modibbo Adama University of Technology, Yola", "National Open University of Nigeria, Lagos",  "Federal Government Girls College, Monguno",
            "Nigerian Defence Academy Kaduna",   "Nigeria Police Academy Wudil", "Federal Government College, Billiri", "Federal Government College, Bajoga",
            "Nnamdi Azikiwe University, Awka","Federal Government Girls College, Yola", "Obafemi Awolowo University,Ile-Ife", "Federal Government College, Jos",
            "University of Abuja, Gwagwalada",   "Federal Government College, Keffi", "Federal University of Agriculture, Abeokuta", "University of Agriculture, Makurdi",
            "Federal Government Girls College, Rubochi",    "University of Benin","University of Calabar", "University of Ibadan","University of Ilorin",
            "University of Jos",   "Federal Government Girls College, Garki", "Government Science Technical College Abuja", "University of Lagos",
            "Federal Govt. Boys College Abuja",  "Regina Pacis Girls Secondary Shool Abuja",
            "Federal Government College, Vandeikya",  "University of Maiduguri", "Federal Government College, Ugwolawo",  "Federal Government College Minna,Tungan Goro",
            "Federal Government College, Suleja",  "University of Nigeria, Nsukka", "University of Port-Harcourt", "University of Uyo", "Federal Government Girls College, Kabba",   "Usumanu Danfodiyo University",
            "Nigerian Maritime University Okerenkoko, Delta State",   "Nigerian Maritime University Okerenkoko",
            "Air Force Institute of Technology, Kaduna", "Federal Government Girls College, Omu Aran", "Nigerian Army University Biu",  "Federal Government College, Ilorin",
            "Federal Government Girls College, Bida", "Federal Government Girls College, Bwari", "Federal Government Girls College, Abaji", "Federal Government Girls College, Langtang",
            "Federal Government Girls College, Keana", "Federal Science And Technical College, Doma", "Federal Government College Sokoto",
            "Federal Science College, Sokoto", "Federal Government College, Kano", "Federal Government College, Birnin Yauri", "FEDERAL GOVERNMENT COLLEGE, DAURA, KATSINA",
            "Federal Government College Kiyawa Jigawa state"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cirp_view);

        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Creating the instance of ArrayAdapter containing list of fruit names
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, schoollist);
        autoCompleteTextView.setThreshold(1);//will start working from first character
        autoCompleteTextView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView




        covidtyp = (Spinner) findViewById(R.id.cirptype);
        covidtyp.setOnItemSelectedListener(new ItemSelectedListener());
        setTitle("CIRP REPORTS");
        button_upload.setOnClickListener(this);
        selectImage.setOnClickListener(this);
    }


    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener{
        //get first string in the array
        String firstItem = String.valueOf(covidtyp.getSelectedItem());
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id){
            firstItem.equals(String.valueOf(covidtyp.getSelectedItem()));
        }
        @Override
        public void onNothingSelected(AdapterView<?> arg){

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_upload:
                if (postPath == null) {
                    Toast.makeText(this, "please select an image", Toast.LENGTH_SHORT).show();
                } else {

                    String comment = Objects.requireNonNull(input_comment.getText()).toString();
                    String school = Objects.requireNonNull(autoCompleteTextView.getText()).toString();
                    String department = Objects.requireNonNull(input_depart.getText()).toString();
                    if (comment.isEmpty() || comment == null) {
                        submitDetails("comment", "department", "school");
                    } else {
                        submitDetails(comment, department, school);
                    }

                }
                break;
            case R.id.selectImage:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if ((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)) {
                        ActivityCompat.requestPermissions(EdureportActivity.this, new String[] { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
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
                Toast.makeText(EdureportActivity.this, "Permission denied, the permissions are very important for the apps usage", Toast.LENGTH_SHORT).show();
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

    private void submitDetails(String mcomment, String mdepartment, String mschool){
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
        RequestBody department = createPartFromString(mdepartment);
        RequestBody longitude = createPartFromString(mlongitude);
        RequestBody latitude = createPartFromString(mlatitude);
        RequestBody reporter = createPartFromString(mphonenumber);
        RequestBody type = createPartFromString(mtype);
        RequestBody school = createPartFromString(mschool);

        Call<Void> call = userService.cirpComment(comment, body, reporter, latitude, longitude, type, department, school);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    progress.setVisibility(View.GONE);
                    emptyInputEditText();
                    Toast.makeText(EdureportActivity.this, "Reported successfully", Toast.LENGTH_SHORT).show();
                } else {
                    progress.setVisibility(View.GONE);
                    Toast.makeText(EdureportActivity.this, "error Submitting Report ...", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                progress.setVisibility(View.GONE);
                Toast.makeText(EdureportActivity.this, "Error Submitting Report..." + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * This method is to empty all input edit text
     */
    private void emptyInputEditText() {
        input_comment.setText("");
        input_depart.setText("");
        autoCompleteTextView.setText("");
        image_header.setImageResource(R.color.colorPrimary);
        postPath.equals(null);
        path.equals(null);
        refreshActivity();
    }

    private void refreshActivity(){
        recreate();
    }

}
