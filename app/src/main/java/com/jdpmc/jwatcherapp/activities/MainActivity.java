package com.jdpmc.jwatcherapp.activities;

import static com.jdpmc.jwatcherapp.utils.Constants.GET_HOT_URL;
import static com.jdpmc.jwatcherapp.utils.Constants.GET_USEFUL_URL;
import static com.jdpmc.jwatcherapp.utils.Constants.RECENT_POST_URL;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.jdpmc.jwatcherapp.AppSettings;
import com.jdpmc.jwatcherapp.Gbv_form_Activity;
import com.jdpmc.jwatcherapp.Go_New_live;
import com.jdpmc.jwatcherapp.ImagePostsActivity;
import com.jdpmc.jwatcherapp.LiveVideoActivity;
import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.ShortVideoActivity;
import com.jdpmc.jwatcherapp.adapter.ArticleAdapter;
import com.jdpmc.jwatcherapp.adapter.HomePostAdapter;
import com.jdpmc.jwatcherapp.database.AppDatabase;
import com.jdpmc.jwatcherapp.database.ArticleEntry;
import com.jdpmc.jwatcherapp.database.HomePosts;
import com.jdpmc.jwatcherapp.hot_picture_uploader;
import com.jdpmc.jwatcherapp.model.Article;
import com.jdpmc.jwatcherapp.model.ArticleResponse;
import com.jdpmc.jwatcherapp.model.HotZoneDetails;
import com.jdpmc.jwatcherapp.model.UseRscDetails;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.picture_uploader;
import com.jdpmc.jwatcherapp.service.NispsasLockService;
import com.jdpmc.jwatcherapp.shortvideo_uploader;
import com.jdpmc.jwatcherapp.utils.AppExecutors;
import com.jdpmc.jwatcherapp.utils.Config4;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.jdpmc.jwatcherapp.view_hot_zone;
import com.jdpmc.jwatcherapp.viewmodel.ArticleViewModel;
import com.mancj.slideup.SlideUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity implements RecyclerView.OnScrollChangeListener{
    private static final String TAG = "MainActivity";
    private static final int RC_APP_UPDATE = 0;
    private AppUpdateManager mAppUpdateManager;
    private static final String live_vid = "hotzone";
    private static final String live_vid2 = "hotzone";
    private ArticleAdapter articleAdapter;
    //Creating a List of superheroes
    private List<HomePosts> listSuperHeroes;
    private List<HomePosts> listSuperHeroes2;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private int current_position = 0;
    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;

    private AppDatabase mDb;
    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private String longitude, latitude, phonenumber, reporter;

    private SlideUp slideUp;
    private SlideUp slideUpnewLive;
    private View dim;
    private View slideView;
    private View liveVid;
    private String Unconfirmed;
    private String Confirmed;
    public TextView send_text;
    public TextView hot_state_text;
    public TextView hot_lga_text;
    public TextView hot_town_text;
    public TextView hot_imag_banner;
    public TextView hot_comment;
    public TextView hot_username;
    public TextView hot_rscurl;
    String id = UUID.randomUUID().toString();
    FloatingActionButton flGoLive, flShortVid, flImagePost;
    FloatingActionButton mMainbutton;
    //TextView addAlarmActionText, addPersonActionText;
    Boolean isAllFabsVisible;

    @BindView(R.id.articleRecyclerview)
    RecyclerView articleRecyclerview;

    final int duration = 6000;
    final int pixelsToMove = 1080;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Runnable SCROLLING_RUNNABLE = new Runnable() {

        @Override
        public void run() {
            if (current_position == 0) {
                articleRecyclerview.smoothScrollToPosition(0);
                current_position = 1;
            } else if (current_position == 1) {
                articleRecyclerview.smoothScrollToPosition(1);
                current_position = 2;
            } else if (current_position == 2) {
                articleRecyclerview.smoothScrollToPosition(2);
                current_position = 0;
            }

            articleRecyclerview.smoothScrollBy(pixelsToMove, 0);
            mHandler.postDelayed(this, duration);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {Manifest.permission.CALL_PHONE,Manifest.permission.RECORD_AUDIO,Manifest.permission.CAMERA,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAPTURE_AUDIO_OUTPUT,Manifest.permission.MODIFY_AUDIO_SETTINGS}, 1);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mMainbutton = findViewById(R.id.floating_action_button);
        flGoLive = findViewById(R.id.go_live_stream);
        flShortVid = findViewById(R.id.post_new_short);
        flImagePost = findViewById(R.id.post_new_image);

        /*addAlarmActionText = findViewById(R.id.add_alarm_action_text);
        addPersonActionText = findViewById(R.id.add_person_action_text);*/

        flGoLive.setVisibility(View.GONE);
        flShortVid.setVisibility(View.GONE);
        flImagePost.setVisibility(View.GONE);
        isAllFabsVisible = false;

        ImageView gbv_plain_form_rep = findViewById(R.id.gbv_plain_form);
        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
        ImageView imageView = (ImageView) findViewById(R.id.app_profile);
        Glide.with(MainActivity.this)
                .load(user_image)
                .into(imageView);
        ImageView open_settings = findViewById(R.id.app_profile);
        if ((PreferenceUtils.getConfStatus(getApplicationContext())) != ""){
            ImageView campain_finance_form = findViewById(R.id.campaign_fin_monitor);
            campain_finance_form.setVisibility(View.VISIBLE);
            campain_finance_form.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://j-watcher.org/Apps/Mobile/campainform";
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    customTabsIntent.launchUrl(getApplicationContext(), Uri.parse(url));
                }
            });
        }else{
            ImageView camp_icon = findViewById(R.id.campaign_fin_monitor);
            camp_icon.setVisibility(View.GONE);
        }
        ImageView hot_zone_rep = findViewById(R.id.hot_zone_area);
        ImageView view_image_post = findViewById(R.id.image_posts);
        ImageView open_appsettings = findViewById(R.id.app_settings);
        ImageView view_live_videos = findViewById(R.id.view_live_posts);
        ImageView short_video_post = findViewById(R.id.short_videos);
        slideView = findViewById(R.id.slideView);
        liveVid = findViewById(R.id.new_stream);
        dim = findViewById(R.id.dim);
        slideUp = new SlideUp(slideView);
        slideUpnewLive = new SlideUp(liveVid);
        slideUp.hideImmediately();
        slideUpnewLive.hideImmediately();

        String callid = (live_vid);
        getHotZone(callid);

        String callid2 = (live_vid2);
        GetUsefullRsc(callid2);

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        //int numberOfColumns = 2;
        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        //Initializing our superheroes list
        listSuperHeroes = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        //Calling method to get data to fetch data
        getData();

        //Adding an scroll change listener to recyclerview
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(this);
        }

        //initializing our adapter
        adapter = new HomePostAdapter(listSuperHeroes, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

        send_text = (EditText) findViewById(R.id.send_text_id);
        hot_state_text = (EditText) findViewById(R.id.hot_state_txt);
        hot_lga_text = (EditText) findViewById(R.id.hot_lga_txt);
        hot_town_text = (EditText) findViewById(R.id.hot_town_txt);
        hot_imag_banner = (EditText) findViewById(R.id.hot_imag_txt);
        hot_comment = (EditText) findViewById(R.id.hot_commenttxt);
        hot_username = (EditText) findViewById(R.id.hot_usernamettxt);
        hot_rscurl = (EditText) findViewById(R.id.hot_rscurlttxt);

        ImageView New_postbam = findViewById(R.id.new_post);
        New_postbam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.animateIn();
            }
        });

        ImageView New_Stream = findViewById(R.id.go_live_stream);
        New_Stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUpnewLive.animateIn();
            }
        });

        slideUp.setSlideListener(new SlideUp.SlideListener() {
            @Override
            public void onSlideDown(float v) {
                dim.setAlpha(1 - (v / 100));
            }

            @Override
            public void onVisibilityChanged(int i) {
                if (i == View.GONE) {
                    //fab.show();
                }

            }
        });

        slideUpnewLive.setSlideListener(new SlideUp.SlideListener() {
            @Override
            public void onSlideDown(float v) {
                dim.setAlpha(1 - (v / 100));
            }

            @Override
            public void onVisibilityChanged(int i) {
                if (i == View.GONE) {
                    //fab.show();
                }

            }
        });

        gbv_plain_form_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Gbv_form_Activity.class);
                //Intent intent = new Intent(getApplicationContext(), Go_New_live.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        short_video_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShortVideoActivity.class);
                //Intent intent = new Intent(getApplicationContext(), picture_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        hot_zone_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, hot_picture_uploader.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        view_image_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ImagePostsActivity.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        view_live_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LiveVideoActivity.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        open_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AppSettings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });
        open_appsettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AppSettings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        mMainbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!isAllFabsVisible) {

                            flGoLive.show();
                            flShortVid.show();
                            flImagePost.show();
                            //addAlarmActionText.setVisibility(View.VISIBLE);
                            //addPersonActionText.setVisibility(View.VISIBLE);

                            //mAddFab.extend();
                            isAllFabsVisible = true;
                        } else {
                            flGoLive.hide();
                            flShortVid.hide();
                            flImagePost.hide();
                            //addAlarmActionText.setVisibility(View.GONE);
                            //addPersonActionText.setVisibility(View.GONE);
                            //mAddFab.shrink();
                            isAllFabsVisible = false;
                        }
                    }
                });

        flShortVid.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, shortvideo_uploader.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                    }
                });
        flGoLive.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, Go_New_live.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                    }
                });
        flImagePost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, picture_uploader.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                    }
                });
        startService(new Intent(getApplicationContext(), NispsasLockService.class));

        AppUpdateManager mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)){

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.FLEXIBLE, MainActivity.this, RC_APP_UPDATE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }


            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e(TAG, "checkForAppUpdateAvailability: something else");
            }
        });

        RecyclerView autoScroll = (RecyclerView) findViewById(R.id.articleRecyclerview);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        autoScroll.setLayoutManager(layoutManager);

        autoScroll.setHasFixedSize(true);
        articleAdapter = new ArticleAdapter(MainActivity.this);
        autoScroll.setAdapter(articleAdapter);


        articleRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
                if(lastItem == layoutManager.getItemCount()){
                    mHandler.removeCallbacks(SCROLLING_RUNNABLE);
                    Handler postHandler = new Handler();
                    postHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            articleRecyclerview.setAdapter(null);
                            articleRecyclerview.setAdapter(articleAdapter);
                            mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);
                        }
                    }, 2000);
                }
            }
        });
        mHandler.postDelayed(SCROLLING_RUNNABLE, 2000);

        mDb = AppDatabase.getInstance(getApplicationContext());
        fetchArticle();
        ArticleViewModel viewModel = ViewModelProviders.of(this).get(ArticleViewModel.class);
        viewModel.getArticle().observe(this, articleEntries -> {
            if (articleEntries != null) {
                articleAdapter.setTasks(articleEntries);
            }
        });

    }

    InstallStateUpdatedListener installStateUpdatedListener = new
            InstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(InstallState state) {
                    if (state.installStatus() == InstallStatus.DOWNLOADED){
                        popupSnackbarForCompleteUpdate();
                    } else if (state.installStatus() == InstallStatus.INSTALLED){
                        if (mAppUpdateManager != null){
                            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                        }

                    } else {
                        Log.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
                    }
                }
            };

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.tvContent),
                        "New Update is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    //Request to get json from server we are passing an integer here
    //This integer will used to specify the page number for the request ?page = requestcount
    //This method would return a JsonArrayRequest that will be added to the request queue
    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config4.DATA_URL + String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Calling method parseData to parse the json response
                        parseData(response);
                        //Hiding the progressbar
                        progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        //If an error occurs that means end of the list has reached
                        Toast.makeText(MainActivity.this, "No More Items Available", Toast.LENGTH_SHORT).show();
                    }
                });

        //Returning the request
        return jsonArrayRequest;
    }

    //This method will get data from the web api
    private void getData() {
        //Adding the method to the queue by calling the method getDataFromServer
        requestQueue.add(getDataFromServer(requestCount));
        //Incrementing the request counter
        requestCount++;
    }

    //This method will parse json data
    private void parseData(JSONArray array) {
        for (int i=array.length()-1;i>=0;i--) {
            //Creating the superhero object
            HomePosts superHero = new HomePosts();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object
                superHero.setImageUrl(json.getString(Config4.TAG_IMAGE_URL));
                superHero.setName(json.getString(Config4.TAG_NAME));
                superHero.setId(json.getString(Config4.TAG_Id));
                superHero.setComment(json.getString(Config4.TAG_PUBLISHER));
                superHero.setRepId(json.getString(Config4.TAG_REPID));
                superHero.setState(json.getString(Config4.TAG_STATE));
                superHero.setStatus(json.getString(Config4.TAG_STATUS));
                superHero.setRepType(json.getString(Config4.TAG_REPTYP));
                superHero.setArea(json.getString(Config4.TAG_AREA));
                superHero.setLikes(json.getString(Config4.TAG_LIKES));
                superHero.setComments(json.getString(Config4.TAG_COMMENTS));
                superHero.setPostImg(json.getString(Config4.TAG_POSIIMG));
                superHero.setResUri(json.getString(Config4.TAG_RSURI));
                superHero.setDate(json.getString(Config4.TAG_DATE));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Adding the superhero object to the list
            listSuperHeroes.add(superHero);
        }

        //Notifying the adapter that data has been added or changed
        adapter.notifyDataSetChanged();
    }

    //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                return true;
        }
        return false;
    }

    //Overriden method to detect scrolling
    @Override
    public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //Ifscrolled at last then
        if (isLastItemDisplaying(recyclerView)) {
            //Calling the method getdata again
            getData();
        }
    }

    public Boolean verifyFields() {
        return true;
    }

    private void getHotZone(String callid) {
        if (verifyFields()) {
            //progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, GET_HOT_URL);
                retrofit2.Call<HotZoneDetails> call = service.gethotzones(callid);

                call.enqueue(new retrofit2.Callback<HotZoneDetails>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<HotZoneDetails> call, @NonNull retrofit2.Response<HotZoneDetails> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                HotZoneDetails verifyResponse = response.body();
                                String sgresponse = verifyResponse.getResponse();
                                String sgname = verifyResponse.getSGname();
                                String sgphone = verifyResponse.getSGphone();
                                String sgstate = verifyResponse.getSGstate();
                                String date = verifyResponse.getSGdate();

                                String sgreptype = verifyResponse.getSgreptype();
                                String sglga = verifyResponse.getLga();
                                String sgcomment = verifyResponse.getSGcomment();
                                String sgstatuse = verifyResponse.getSgstatuse();
                                String hot_id = verifyResponse.getId();
                                String hot_area = verifyResponse.getArea();

                                String videosrc = verifyResponse.getSGvideourl();
                                String imageUrl = verifyResponse.getImage();
                                String UserimageUrl = verifyResponse.getUserimage();
                                //progress.setVisibility(View.GONE);
                                showDialog(sgresponse, sgname, sgphone, sgstate, imageUrl, videosrc, sgreptype, sglga, sgcomment, sgstatuse, date, hot_id, hot_area, UserimageUrl);
                                //Toast.makeText(Video_post_player.this, resourceuri, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //progress.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<HotZoneDetails> call, @NonNull Throwable t) {
                        //progress.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                //progress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog(String sgresponse, String sgname, String sgphone, String sgstate, String imageUrl, String videosrc, String sgreptype, String sglga, String sgcomment, String sgstatuse, String date, String hot_id, String hot_area, String UserimageUrl) {
        //TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
        //verifiedResponse.setText(sgresponse);
        TextView nametext = findViewById(R.id.hotuser_name);
        nametext.setText(sgname);
        //TextView statustext = findViewById(R.id.brstatus);
        //statustext.setText(sgstatuse);
        //TextView psttype = findViewById(R.id.post_type);
        //psttype.setText(sgstatuse);
        TextView VidUrl = findViewById(R.id.video_url);
        VidUrl.setText(videosrc);
        TextView statetext = findViewById(R.id.hot_state);
        statetext.setText(sgstate);
        TextView Areaofrep = findViewById(R.id.hot_lga);
        Areaofrep.setText(sglga);

        EditText hotid = findViewById(R.id.send_text_id);
        hotid.setText(hot_id);

        TextView hotviewstate = findViewById(R.id.hot_state_txt);
        hotviewstate.setText(sgstate);
        TextView hotviewlga = findViewById(R.id.hot_lga_txt);
        hotviewlga.setText(sglga);
        TextView hotviewtown = findViewById(R.id.hot_town_txt);
        hotviewtown.setText(hot_area);
        TextView hotvieimg = findViewById(R.id.hot_imag_txt);
        hotvieimg.setText(imageUrl);
        TextView hotviewcomment = findViewById(R.id.hot_commenttxt);
        hotviewcomment.setText(sgcomment);
        ImageView imageView = (ImageView) findViewById(R.id.hot_pic);
        Glide.with(MainActivity.this)
                .load(imageUrl)
                .into(imageView);

        CardView open_hot_zones = findViewById(R.id.hot_zone);
        open_hot_zones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = send_text.getText().toString();
                String state = hot_state_text.getText().toString();
                String lga = hot_lga_text.getText().toString();
                String town = hot_town_text.getText().toString();
                String imgurl = hot_imag_banner.getText().toString();
                String comment = hot_comment.getText().toString();
                String username = hot_username.getText().toString();
                String rscurl = hot_rscurl.getText().toString();
                //Intent intent = new Intent(MainActivity.getApplicationContext(), view_and_comment.class);
                Intent intent = new Intent(MainActivity.this, view_hot_zone.class);
                intent.putExtra("message_key", str);
                intent.putExtra("state_key", state);
                intent.putExtra("lga_key", lga);
                intent.putExtra("town_key", town);
                intent.putExtra("imgurl_key", imgurl);
                intent.putExtra("comment_key", comment);
                intent.putExtra("username_key", sgname);
                intent.putExtra("rscurl_key", videosrc);
                intent.putExtra("userimg_key", UserimageUrl);
                intent.putExtra("date_key", date);
                v.getContext().startActivity(intent);

                //Toast.makeText(MainActivity.this, sgname, Toast.LENGTH_SHORT).show();
            }
        });
    }


    public Boolean verifyFields2() {
        return true;
    }

    private void GetUsefullRsc(String callid) {
        if (verifyFields2()) {
            //progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, GET_USEFUL_URL);
                retrofit2.Call<UseRscDetails> call = service.getuseresoutce(callid);

                call.enqueue(new retrofit2.Callback<UseRscDetails>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<UseRscDetails> call, @NonNull retrofit2.Response<UseRscDetails> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                UseRscDetails verifyResponse = response.body();
                                String sgresponse = verifyResponse.getResponse();
                                String sgname = verifyResponse.getSGname();
                                String sgphone = verifyResponse.getSGphone();
                                String sgstate = verifyResponse.getSGstate();
                                String date = verifyResponse.getSGdate();

                                String sgreptype = verifyResponse.getSgreptype();
                                String sglga = verifyResponse.getLga();
                                String sgcomment = verifyResponse.getSGcomment();
                                String sgstatuse = verifyResponse.getSgstatuse();

                                String videosrc = verifyResponse.getSGvideourl();
                                String imageUrl = verifyResponse.getImage();
                                //progress.setVisibility(View.GONE);
                                showDialog2(sgresponse, sgname, sgphone, sgstate, imageUrl, videosrc, sgreptype, sglga, sgcomment, sgstatuse, date);
                                //Toast.makeText(Video_post_player.this, resourceuri, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //progress.setVisibility(View.GONE);
                            Toast.makeText(MainActivity.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<UseRscDetails> call, @NonNull Throwable t) {
                        //progress.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "No Useful Resources Posted", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                //progress.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog2(String sgresponse, String sgname, String sgphone, String sgstate, String imageUrl, String videosrc, String sgreptype, String sglga, String sgcomment, String sgstatuse, String date) {
        //TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
        //verifiedResponse.setText(sgresponse);
        //TextView nametext = findViewById(R.id.hot_pic);
        //nametext.setText(sgname);
        //TextView statustext = findViewById(R.id.brstatus);
        //statustext.setText(sgstatuse);
        //TextView psttype = findViewById(R.id.post_type);
        //psttype.setText(sgstatuse);
        TextView statetext = findViewById(R.id.usersc_state);
        statetext.setText(sgstate);
        TextView Areaofrep = findViewById(R.id.usersc_type);
        Areaofrep.setText(sglga);
        //TextView ReportType = findViewById(R.id.rep_type);
        //ReportType.setText(sgreptype);
        //TextView RepDate = findViewById(R.id.report_date);
        //RepDate.setText(date);
        //TextView ReporComment = findViewById(R.id.rep_comment);
        //ReporComment.setText(sgcomment);
        ImageView imageView = (ImageView) findViewById(R.id.usersc_image);
        Glide.with(MainActivity.this)
                .load(imageUrl)
                .into(imageView);
    }


    private void fetchArticle() {
        try{
            Service service = DataGenerator.createService(Service.class, RECENT_POST_URL);
            Call<ArticleResponse> call = service.recentPosts("All");

            call.enqueue(new Callback<ArticleResponse>() {
                @Override
                public void onResponse(@NonNull Call<ArticleResponse> call, @NonNull retrofit2.Response<ArticleResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body()!= null) {
                            AppExecutors.getInstance().diskIO().execute(() -> mDb.nispsasDao().deleteAll());
                            List<Article> articleResponseList = response.body().getArticles();
                            if (articleResponseList != null) {
                                for (Article article : articleResponseList) {
                                    int id = article.getId();
                                    //String articleid = article.getArticleid();
                                    String user_pic_name = article.getUserimage();
                                    String urser_name = article.getUsername();
                                    String post_comment = article.getPostcomment();
                                    String post_state = article.getPoststate();
                                    String post_status = article.getPoststatus();

                                    String post_type = article.getPosttype();
                                    String post_area = article.getPostarea();
                                    String post_likes = article.getLikscount();
                                    String post_comments = article.getCommentcount();
                                    String post_image = article.getPreview();
                                    String post_vidurl = article.getVidrscurl();
                                    String post_date = article.getPostdate();
                                    Log.v(TAG, "https://nispsas.com.ng/NISPSAS/Postpics/"+article.getUserimage());

                                    ArticleEntry articleEntry = new ArticleEntry(id, user_pic_name, urser_name, post_comment, post_state, post_status, post_type
                                    ,post_area, post_likes, post_comments, post_image, post_vidurl, post_date);
                                    AppExecutors.getInstance().diskIO().execute(() ->mDb.nispsasDao().insertArticle(articleEntry));
                                }
                            }
                        }
                    } else {

                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArticleResponse> call, @NonNull Throwable t) {

                }
            });
        } catch (Exception e) {

        }
    }



}