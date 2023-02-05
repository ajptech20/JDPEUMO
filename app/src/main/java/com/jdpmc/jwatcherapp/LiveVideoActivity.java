package com.jdpmc.jwatcherapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jdpmc.jwatcherapp.adapter.LiveVideoPostAdapter;
import com.jdpmc.jwatcherapp.database.LivePost;
import com.jdpmc.jwatcherapp.utils.Config3;
import com.jdpmc.jwatcherapp.utils.FancyToast;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.mancj.slideup.SlideUp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class LiveVideoActivity extends AppCompatActivity implements RecyclerView.OnScrollChangeListener {

    //Creating a List of superheroes
    private List<LivePost> listSuperHeroes;

    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;


    private SlideUp slideUp;
    private SlideUp slideUpnewLive;
    private View dim;
    private View slideView;
    private View liveVid;
    private String Unconfirmed;
    private String Confirmed;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;

    FloatingActionButton flGoLive, flShortVid, flImagePost;
    FloatingActionButton mMainbutton;
    //TextView addAlarmActionText, addPersonActionText;
    Boolean isAllFabsVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_video_activity);

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

        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
        ImageView imageView = (ImageView) findViewById(R.id.app_profile);
        Glide.with(LiveVideoActivity.this)
                .load(user_image)
                .into(imageView);
        ImageView open_settings = findViewById(R.id.app_profile);
        ImageView open_appsettings = findViewById(R.id.app_settings);
        ImageView gbv_plain_form_rep = findViewById(R.id.gbv_plain_form);
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
        ImageView close = findViewById(R.id.back_home);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageView hot_zone_rep = findViewById(R.id.hot_zone_area);
        slideView = findViewById(R.id.slideView);
        liveVid = findViewById(R.id.new_stream);
        dim = findViewById(R.id.dim);
        slideUp = new SlideUp(slideView);
        slideUpnewLive = new SlideUp(liveVid);
        slideUp.hideImmediately();
        slideUpnewLive.hideImmediately();
        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
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
        adapter = new LiveVideoPostAdapter(listSuperHeroes, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

        ImageView go_home = findViewById(R.id.app_home1);
        go_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView New_postbam = findViewById(R.id.new_post);
        New_postbam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slideUp.animateIn();
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
                Intent intent = new Intent(LiveVideoActivity.this, Gbv_form_Activity.class);
                //Intent intent = new Intent(getApplicationContext(), Go_New_live.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        hot_zone_rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveVideoActivity.this, hot_picture_uploader.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                //finish();
            }
        });

        ImageView view_image_post = findViewById(R.id.image_posts);
        view_image_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveVideoActivity.this, ImagePostsActivity.class);
                //Intent intent = new Intent(getApplicationContext(), shortvideo_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
            }
        });

        ImageView short_video_post = findViewById(R.id.short_videos);
        short_video_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LiveVideoActivity.this, ShortVideoActivity.class);
                //Intent intent = new Intent(getApplicationContext(), picture_uploader.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                finish();
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
                        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
                        if (!user_image.equals("")){
                            Intent intent = new Intent(LiveVideoActivity.this, shortvideo_uploader.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }else {
                            FancyToast.makeText(getApplicationContext(), "Kindly Update Your profile Image before you can create a  post", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            Intent intent = new Intent(LiveVideoActivity.this, UserDataUpdate.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }
                    }
                });
        flGoLive.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
                        if (!user_image.equals("")){
                            Intent intent = new Intent(LiveVideoActivity.this, Go_New_live.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }else {
                            FancyToast.makeText(getApplicationContext(), "Kindly Update Your profile Image before you can create a  post", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            Intent intent = new Intent(LiveVideoActivity.this, UserDataUpdate.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }
                    }
                });
        flImagePost.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String user_image = (PreferenceUtils.getUserImage(getApplicationContext()));
                        if (!user_image.equals("")){
                            Intent intent = new Intent(LiveVideoActivity.this, picture_uploader.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }else {
                            FancyToast.makeText(getApplicationContext(), "Kindly Update Your profile Image before you can create a  post", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            Intent intent = new Intent(LiveVideoActivity.this, UserDataUpdate.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                        }
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
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config3.DATA_URL + String.valueOf(requestCount),
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
                        Toast.makeText(LiveVideoActivity.this, "No More Items Available", Toast.LENGTH_SHORT).show();
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
            LivePost superHero = new LivePost();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object
                superHero.setImageUrl(json.getString(Config3.TAG_IMAGE_URL));
                superHero.setName(json.getString(Config3.TAG_NAME));
                superHero.setId(json.getString(Config3.TAG_Id));
                superHero.setRepuuid(json.getString(Config3.TAG_UUId));
                superHero.setComment(json.getString(Config3.TAG_PUBLISHER));
                superHero.setRepId(json.getString(Config3.TAG_REPID));
                superHero.setState(json.getString(Config3.TAG_STATE));
                superHero.setStatus(json.getString(Config3.TAG_STATUS));
                superHero.setRepType(json.getString(Config3.TAG_REPTYP));
                superHero.setArea(json.getString(Config3.TAG_AREA));
                superHero.setLikes(json.getString(Config3.TAG_LIKES));
                superHero.setComments(json.getString(Config3.TAG_COMMENTS));
                superHero.setPreview(json.getString(Config3.TAG_PREVIEW));
                superHero.setResUri(json.getString(Config3.TAG_RSURI));
                superHero.setDate(json.getString(Config3.TAG_DATE));
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
}
