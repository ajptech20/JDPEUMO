package com.jdpmc.jwatcherapp;

import static com.jdpmc.jwatcherapp.utils.Constants.NOTIFICATION_BASE_URL;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jdpmc.jwatcherapp.adapter.NotificationAdapter;
import com.jdpmc.jwatcherapp.database.NewNotificationsPost;
import com.jdpmc.jwatcherapp.model.VideoReportDetails;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.FancyToast;
import com.jdpmc.jwatcherapp.utils.NotificationConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.M)
public class NotificationActivity extends AppCompatActivity implements RecyclerView.OnScrollChangeListener{

    private static final String TAG = "myTag";
    private List<NewNotificationsPost> listSuperHeroes;
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;
    private static final String live_vid = "e8f747a5-05da-4fc0-abb6-a20cd8b688fb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        TextView txtMarquee;
        txtMarquee = (TextView) findViewById(R.id.rep_comment);
        txtMarquee.setSelected(true);
        Intent intent = getIntent();
        String repuuid = intent.getStringExtra("title");
        String callid = (live_vid);
        getclickedtraynotice(callid);
        ImageView close = findViewById(R.id.stop_close_live3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.notificationrecyclerView);
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
        adapter = new NotificationAdapter(listSuperHeroes, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);
        handleIntent(getIntent());

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String body = (String) getIntent().getExtras().get("body");
                Object value = getIntent().getExtras().get(key);
                Log.d("MainActivity: ", "Key: " + key + " Value: " + value + "Body:" + body);
            }
        }

    }

    private void handleIntent(Intent intent) {
        /*String user_id = intent.getStringExtra("uuid");
        if(user_id != null);
        Toast.makeText(NotificationActivity.this, "Rep Id" + user_id, Toast.LENGTH_SHORT).show();*/
        String sgcomment = intent.getStringExtra("comment");
        String sgname = intent.getStringExtra("username");
        String sgreptype = intent.getStringExtra("reptype");
        String userimg = intent.getStringExtra("userimg");
        String imageUrl = intent.getStringExtra("post_img");
        String videosrc = intent.getStringExtra("vid_url");
        String sgstate = intent.getStringExtra("state");
        String date = intent.getStringExtra("date");
        String sgreparea = intent.getStringExtra("area");
        String sgstatuse = intent.getStringExtra("repstatus");

        TextView ReporComment = findViewById(R.id.rep_comment);
        ReporComment.setText(sgcomment);

        TextView nametext = findViewById(R.id.poster_name);
        nametext.setText(sgname);
        TextView postsrc = findViewById(R.id.post_source);
        postsrc.setText(sgreptype);
        ImageView imageView = (ImageView) findViewById(R.id.reporter_image);
        Glide.with(NotificationActivity.this)
                .load(userimg)
                .into(imageView);

        ImageView postimageView = (ImageView) findViewById(R.id.post_image_view);
        if (Objects.equals(sgstatuse, "Live Post")){
            Glide.with(NotificationActivity.this).load(imageUrl)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .thumbnail(0.05f)
                .fitCenter()
                .transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.img)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(postimageView);
        }else if (Objects.equals(sgstatuse, "Short Video")){
            Glide.with(NotificationActivity.this).load(imageUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .thumbnail(0.05f)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.img)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(postimageView);
        }else if (Objects.equals(sgstatuse, "Image Post")){
            Glide.with(NotificationActivity.this).load(imageUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .thumbnail(0.05f)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.img)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(postimageView);
        }else if(Objects.equals(sgstatuse, "HotImageUpload")){
            Glide.with(NotificationActivity.this).load(imageUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .thumbnail(0.05f)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.img)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(postimageView);
        }else if(Objects.equals(sgstatuse, "HotVideoUpload")){
            Glide.with(NotificationActivity.this).load(imageUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .thumbnail(0.05f)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.img)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(postimageView);
        }else if (Objects.equals(sgstatuse, "img")){
            Glide.with(NotificationActivity.this).load(imageUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .thumbnail(0.05f)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.img)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(postimageView);
        }else if (Objects.equals(sgstatuse, "pdf")){
            Glide.with(NotificationActivity.this).load(userimg)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .thumbnail(0.05f)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.img)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(postimageView);
        }else{
            Glide.with(NotificationActivity.this).load(imageUrl)
                    .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                    .thumbnail(0.05f)
                    .fitCenter()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.img)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    }).into(postimageView);
        }

        ImageView openpostimageView = (ImageView) findViewById(R.id.post_image_view);
        if (Objects.equals(sgstatuse, "Live Post")){
            openpostimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationActivity.this, Video_post_player.class);
                    intent.putExtra("username_key", sgname);
                    intent.putExtra("vidurl_key", videosrc);
                    intent.putExtra("userimg_key", userimg);
                    intent.putExtra("reptype_key", sgreptype);
                    intent.putExtra("state_key", sgstate);
                    intent.putExtra("date_key", date);
                    intent.putExtra("comment_key", sgcomment);
                    intent.putExtra("area_key", sgreparea);
                    intent.putExtra("repstatus_key", sgstatuse);
                    v.getContext().startActivity(intent);
                }
            });
        }else if (Objects.equals(sgstatuse, "Short Video")){
            openpostimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationActivity.this, Video_post_player.class);
                    intent.putExtra("username_key", sgname);
                    intent.putExtra("vidurl_key", videosrc);
                    intent.putExtra("userimg_key", userimg);
                    intent.putExtra("reptype_key", sgreptype);
                    intent.putExtra("state_key", sgstate);
                    intent.putExtra("date_key", date);
                    intent.putExtra("comment_key", sgcomment);
                    intent.putExtra("area_key", sgreparea);
                    intent.putExtra("repstatus_key", sgstatuse);
                    v.getContext().startActivity(intent);
                }
            });
        }else if (Objects.equals(sgstatuse, "HotVideoUpload")){
            String source = "Hot Zone";
            openpostimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationActivity.this, Hot_Video_post_player.class);
                    intent.putExtra("username_key", sgname);
                    intent.putExtra("rscurl_key", videosrc);
                    intent.putExtra("userimg_key", userimg);
                    intent.putExtra("reptype_key", sgreptype);
                    intent.putExtra("state_key", sgstate);
                    intent.putExtra("date_key", date);
                    intent.putExtra("comment_key", sgcomment);
                    intent.putExtra("town_key", sgreparea);
                    intent.putExtra("postsrc_key", source);
                    v.getContext().startActivity(intent);
                }
            });
        }else if (Objects.equals(sgstatuse, "HotImageUpload")){
            String source = "Hot Zone";
            openpostimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationActivity.this, Hot_Image_post_viewer.class);
                    intent.putExtra("username_key", sgname);
                    intent.putExtra("imgurl_key", imageUrl);
                    intent.putExtra("userimg_key", userimg);
                    intent.putExtra("reptype_key", sgreptype);
                    intent.putExtra("state_key", sgstate);
                    intent.putExtra("date_key", date);
                    intent.putExtra("comment_key", sgcomment);
                    intent.putExtra("town_key", sgreparea);
                    intent.putExtra("postsrc_key", source);
                    v.getContext().startActivity(intent);
                }
            });
        }
        else if (Objects.equals(sgstatuse, "Image Post")){
            openpostimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationActivity.this, Image_Post_Viewer_Activity.class);
                    intent.putExtra("username_key", sgname);
                    intent.putExtra("vidurl_key", videosrc);
                    intent.putExtra("userimg_key", userimg);
                    intent.putExtra("imgurl_key", imageUrl);
                    intent.putExtra("reptype_key", sgreptype);
                    intent.putExtra("state_key", sgstate);
                    intent.putExtra("date_key", date);
                    intent.putExtra("comment_key", sgcomment);
                    intent.putExtra("area_key", sgreparea);
                    intent.putExtra("repstatus_key", sgstatuse);
                    v.getContext().startActivity(intent);
                }
            });
        }else if (Objects.equals(sgstatuse, "img")){
            openpostimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationActivity.this, Image_Post_Viewer_Activity.class);
                    intent.putExtra("username_key", sgname);
                    intent.putExtra("vidurl_key", videosrc);
                    intent.putExtra("userimg_key", userimg);
                    intent.putExtra("imgurl_key", imageUrl);
                    intent.putExtra("reptype_key", sgreptype);
                    intent.putExtra("state_key", sgstate);
                    intent.putExtra("date_key", date);
                    intent.putExtra("comment_key", sgcomment);
                    intent.putExtra("area_key", sgreparea);
                    intent.putExtra("repstatus_key", sgstatuse);
                    v.getContext().startActivity(intent);
                }
            });
        }else if (Objects.equals(sgstatuse, "pdf")){
            openpostimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationActivity.this, pdf_Viewer_Activity.class);
                    intent.putExtra("srctitle_key", sgname);
                    intent.putExtra("description_key", sgcomment);
                    intent.putExtra("vidurl_key", videosrc);
                    intent.putExtra("fileurl_key", imageUrl);
                    intent.putExtra("prevurl_key", userimg);
                    intent.putExtra("imgurl_key", userimg);
                    intent.putExtra("author_key", sgreptype);
                    intent.putExtra("type_key", sgreptype);
                    intent.putExtra("date_key", date);
                    intent.putExtra("postsrc_key", sgreparea);
                    v.getContext().startActivity(intent);


                }
            });
        }else{
            openpostimageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationActivity.this, Youtube_Video_player.class);
                    intent.putExtra("srctitle_key", sgname);
                    intent.putExtra("description_key", sgcomment);
                    intent.putExtra("vidurl_key", videosrc);
                    intent.putExtra("fileurl_key", imageUrl);
                    intent.putExtra("prevurl_key", imageUrl);
                    intent.putExtra("imgurl_key", userimg);
                    intent.putExtra("author_key", sgreptype);
                    intent.putExtra("type_key", sgreptype);
                    intent.putExtra("date_key", date);
                    intent.putExtra("postsrc_key", sgreparea);
                    v.getContext().startActivity(intent);
                }
            });
        }

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.hotprogressBar);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(NotificationConfig.DATA_URL + String.valueOf(requestCount),
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
                        FancyToast.makeText(getApplicationContext(), "No Internet Connection", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
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
            NewNotificationsPost superHero = new NewNotificationsPost();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object
                superHero.setUserimg(json.getString(NotificationConfig.TAG_USER_IMAGE));
                superHero.setPostimg(json.getString(NotificationConfig.TAG_POST_IMG));
                superHero.setTitle(json.getString(NotificationConfig.TAG_TITLE));
                superHero.setComment(json.getString(NotificationConfig.TAG_COMMENT));
                superHero.setUuid(json.getString(NotificationConfig.TAG_UUID));
                superHero.setUsername(json.getString(NotificationConfig.TAG_USER_NAME));
                superHero.setReptype(json.getString(NotificationConfig.TAG_REP_TYPE));
                superHero.setState(json.getString(NotificationConfig.TAG_STATE));
                superHero.setArea(json.getString(NotificationConfig.TAG_REP_AREA));
                superHero.setStatus(json.getString(NotificationConfig.TAG_STATUS));
                superHero.setVidrsc(json.getString(NotificationConfig.TAG_VID));
                superHero.setDatee(json.getString(NotificationConfig.TAG_DATE));
                superHero.setClearstatus(json.getString(NotificationConfig.TAG_CLEAR_STATUS));
                superHero.setDate(json.getString(NotificationConfig.TAG_DATE_IN));

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

    private void getclickedtraynotice(String callid) {
        if (verifyFields()) {
            //progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, NOTIFICATION_BASE_URL);
                retrofit2.Call<VideoReportDetails> call = service.getanotification(callid);

                call.enqueue(new retrofit2.Callback<VideoReportDetails>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<VideoReportDetails> call, @NonNull retrofit2.Response<VideoReportDetails> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                VideoReportDetails verifyResponse = response.body();
                                String sgresponse = verifyResponse.getResponse();
                                String sgname = verifyResponse.getSGname();
                                String sgphone = verifyResponse.getSGphone();
                                String sgstate = verifyResponse.getSGstate();
                                String date = verifyResponse.getSGdate();

                                String sgreptype = verifyResponse.getSgreptype();
                                String sgreparea = verifyResponse.getSgreparea();
                                String sgcomment = verifyResponse.getSGcomment();
                                String sgstatuse = verifyResponse.getSgstatuse();

                                String videosrc = verifyResponse.getSGvideourl();
                                String imageUrl = verifyResponse.getImage();
                                String userimg = verifyResponse.getUserImage();
                                //progress.setVisibility(View.GONE);
                                //showDialog(sgresponse, sgname, sgphone, sgstate, imageUrl, videosrc, sgreptype, sgreparea, sgcomment, sgstatuse, date, userimg);
                                String resourceuri = (videosrc);
                            }
                        } else {
                            //progress.setVisibility(View.GONE);
                            Toast.makeText(NotificationActivity.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<VideoReportDetails> call, @NonNull Throwable t) {
                        //progress.setVisibility(View.GONE);
                        Toast.makeText(NotificationActivity.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                //progress.setVisibility(View.GONE);
                Toast.makeText(NotificationActivity.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog(String sgresponse, String sgname, String sgphone, String sgstate, String imageUrl, String videosrc, String sgreptype, String sgreparea, String sgcomment, String sgstatuse, String date, String userimg) {
        //TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
        //verifiedResponse.setText(sgresponse);
        /*TextView nametext = findViewById(R.id.poster_name);
        nametext.setText(sgname);
        TextView statustext = findViewById(R.id.brstatus);
        statustext.setText(sgstatuse);
        TextView psttype = findViewById(R.id.post_type);
        psttype.setText(sgstatuse);
        TextView statetext = findViewById(R.id.rep_state);
        statetext.setText(sgstate);
        TextView Areaofrep = findViewById(R.id.vidarea);
        Areaofrep.setText(sgreparea);
        TextView ReportType = findViewById(R.id.rep_type);
        ReportType.setText(sgreptype);
        TextView RepDate = findViewById(R.id.report_date);
        RepDate.setText(date); */


    }
}