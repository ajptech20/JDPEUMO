package com.a2tocsolutions.nispsasapp;

import static com.a2tocsolutions.nispsasapp.utils.Constants.GET_HOT_URL;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a2tocsolutions.nispsasapp.adapter.HomePostAdapter;
import com.a2tocsolutions.nispsasapp.database.AppDatabase;
import com.a2tocsolutions.nispsasapp.database.HomePosts;
import com.a2tocsolutions.nispsasapp.model.HotZoneDetails;
import com.a2tocsolutions.nispsasapp.model.UseRscDetails;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.networking.generator.DataGenerator;
import com.a2tocsolutions.nispsasapp.utils.Config4;
import com.a2tocsolutions.nispsasapp.utils.PreferenceUtils;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.SettingsClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mancj.slideup.SlideUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class Activity_home extends AppCompatActivity implements RecyclerView.OnScrollChangeListener{
    private static final String live_vid = "amlive";
    private static final String live_vid2 = "amlive";
    //Creating a List of superheroes
    private List<HomePosts> listSuperHeroes;
    private List<HomePosts> listSuperHeroes2;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

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

    FloatingActionButton flGoLive, flShortVid, flImagePost;
    FloatingActionButton mMainbutton;
    //TextView addAlarmActionText, addPersonActionText;
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

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
        Glide.with(Activity_home.this)
                .load(user_image)
                .into(imageView);
        ImageView open_settings = findViewById(R.id.app_profile);
        if ((PreferenceUtils.getConfStatus(getApplicationContext())) != ""){
            ImageView campain_finance_form = findViewById(R.id.campaign_fin_monitor);
            campain_finance_form.setVisibility(View.VISIBLE);
            campain_finance_form.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = "https://nispsas.com.ng/NISPSAS/Registration/verifyCfm";
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
        verifyPsid(callid);

        String callid2 = (live_vid2);
        GetHotzones(callid2);

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
                Intent intent = new Intent(Activity_home.this, Gbv_form_Activity.class);
                //Intent intent = new Intent(getApplicationContext(), Go_New_live.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        short_video_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_home.this, ShortVideoActivity.class);
                //Intent intent = new Intent(getApplicationContext(), picture_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        hot_zone_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_home.this, hot_picture_uploader.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        view_image_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_home.this, ImagePostsActivity.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        view_live_videos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_home.this, LiveVideoActivity.class);
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
                        Intent intent = new Intent(Activity_home.this, shortvideo_uploader.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                    }
                });
        flGoLive.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Activity_home.this, Go_New_live.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                    }
                });
        flImagePost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Activity_home.this, picture_uploader.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                    }
                });

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
                        Toast.makeText(Activity_home.this, "No More Items Available", Toast.LENGTH_SHORT).show();
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
        for (int i = 0; i < array.length(); i++) {
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

    private void verifyPsid(String callid) {
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

                                String videosrc = verifyResponse.getSGvideourl();
                                String imageUrl = verifyResponse.getImage();
                                //progress.setVisibility(View.GONE);
                                showDialog(sgresponse, sgname, sgphone, sgstate, imageUrl, videosrc, sgreptype, sglga, sgcomment, sgstatuse, date);
                                //Toast.makeText(Video_post_player.this, resourceuri, Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            //progress.setVisibility(View.GONE);
                            Toast.makeText(Activity_home.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<HotZoneDetails> call, @NonNull Throwable t) {
                        //progress.setVisibility(View.GONE);
                        Toast.makeText(Activity_home.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                //progress.setVisibility(View.GONE);
                Toast.makeText(Activity_home.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog(String sgresponse, String sgname, String sgphone, String sgstate, String imageUrl, String videosrc, String sgreptype, String sglga, String sgcomment, String sgstatuse, String date) {
        //TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
        //verifiedResponse.setText(sgresponse);
        //TextView nametext = findViewById(R.id.hot_pic);
        //nametext.setText(sgname);
        //TextView statustext = findViewById(R.id.brstatus);
        //statustext.setText(sgstatuse);
        //TextView psttype = findViewById(R.id.post_type);
        //psttype.setText(sgstatuse);
        TextView statetext = findViewById(R.id.hot_state);
        statetext.setText(sgstate);
        TextView Areaofrep = findViewById(R.id.hot_lga);
        Areaofrep.setText(sglga);
        //TextView ReportType = findViewById(R.id.rep_type);
        //ReportType.setText(sgreptype);
        //TextView RepDate = findViewById(R.id.report_date);
        //RepDate.setText(date);
        //TextView ReporComment = findViewById(R.id.rep_comment);
        //ReporComment.setText(sgcomment);
        ImageView imageView = (ImageView) findViewById(R.id.hot_pic);
        Glide.with(Activity_home.this)
                .load(imageUrl)
                .into(imageView);
    }


    public Boolean verifyFields2() {
        return true;
    }

    private void GetHotzones(String callid) {
        if (verifyFields2()) {
            //progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, GET_HOT_URL);
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
                            Toast.makeText(Activity_home.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<UseRscDetails> call, @NonNull Throwable t) {
                        //progress.setVisibility(View.GONE);
                        Toast.makeText(Activity_home.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                //progress.setVisibility(View.GONE);
                Toast.makeText(Activity_home.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
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
        Glide.with(Activity_home.this)
                .load(imageUrl)
                .into(imageView);
    }

}
