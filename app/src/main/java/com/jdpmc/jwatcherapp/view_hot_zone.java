package com.jdpmc.jwatcherapp;

import static com.jdpmc.jwatcherapp.utils.Constants.VERIFY_BASE_URL;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import com.jdpmc.jwatcherapp.adapter.HotZonePostAdapter;
import com.jdpmc.jwatcherapp.database.HotZonePost;
import com.jdpmc.jwatcherapp.model.VideoReportDetails;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.HotConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RequiresApi(api = Build.VERSION_CODES.M)
public class view_hot_zone extends AppCompatActivity implements RecyclerView.OnScrollChangeListener {
    //Creating a List of superheroes
    private List<HotZonePost> listSuperHeroes;
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    String Postsrc = "Hot Zone";

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotzone_view_activity);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        String state = intent.getStringExtra("state_key");
        String lga = intent.getStringExtra("lga_key");
        String town = intent.getStringExtra("town_key");
        String imgurl = intent.getStringExtra("imgurl_key");
        String comment = intent.getStringExtra("comment_key");
        String username = intent.getStringExtra("username_key");
        String rscurl = intent.getStringExtra("rscurl_key");
        String userimg = intent.getStringExtra("userimg_key");
        String date = intent.getStringExtra("date_key");
        String source = Postsrc;

        String statedata = (state);
        String imageurl = (imgurl);
        String towndata = (town);
        String commentdata = (comment);
        String usernamedata = (username);
        String rscurldata = (rscurl);
        String lgadata = (lga);

        //verifyPsid(callid);
        TextView hotstate = findViewById(R.id.hot_statetext);
        hotstate.setText(state);
        TextView hotlga = findViewById(R.id.hot_lgatxt);
        hotlga.setText(lga);
        TextView hottown = findViewById(R.id.hot_area);
        hottown.setText(town);
        TextView hotcoment = findViewById(R.id.hot_comment);
        hotcoment.setText(comment);
        ImageView imageView = (ImageView) findViewById(R.id.hot_imgview);
        Glide.with(view_hot_zone.this).load(imgurl)
            .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
            .thumbnail(0.05f)
            .centerCrop()
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

            }).into(imageView);

        RelativeLayout view_hot_zone_vid = findViewById(R.id.view_hot);
        if(!Objects.equals(rscurldata, "")){
            view_hot_zone_vid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view_hot_zone.this, Hot_Video_post_player.class);
                    //intent.putExtra("message_key", str);
                    intent.putExtra("state_key", statedata);
                    intent.putExtra("lga_key", lgadata);
                    intent.putExtra("town_key", towndata);
                    intent.putExtra("imgurl_key", imageurl);
                    intent.putExtra("comment_key", commentdata);
                    intent.putExtra("username_key", usernamedata);
                    intent.putExtra("rscurl_key", rscurldata);
                    intent.putExtra("userimg_key", userimg);
                    intent.putExtra("date_key", date);
                    intent.putExtra("postsrc_key", source);
                    v.getContext().startActivity(intent);
                }
            });
        }else{
            view_hot_zone_vid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(view_hot_zone.this, Hot_Image_post_viewer.class);
                    //intent.putExtra("message_key", str);
                    intent.putExtra("state_key", statedata);
                    intent.putExtra("lga_key", lgadata);
                    intent.putExtra("town_key", towndata);
                    intent.putExtra("imgurl_key", imageurl);
                    intent.putExtra("comment_key", commentdata);
                    intent.putExtra("username_key", usernamedata);
                    intent.putExtra("rscurl_key", rscurldata);
                    intent.putExtra("userimg_key", userimg);
                    intent.putExtra("date_key", date);
                    intent.putExtra("postsrc_key", source);
                    v.getContext().startActivity(intent);
                }
            });
        }

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.hotrecyclerView);
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
        adapter = new HotZonePostAdapter(listSuperHeroes, this);

        //Adding adapter to recyclerview
        recyclerView.setAdapter(adapter);

        //Toast.makeText(view_hot_zone.this, hcoment, Toast.LENGTH_SHORT).show();

        ImageView close = findViewById(R.id.stop_close_live3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private JsonArrayRequest getDataFromServer(int requestCount) {
        //Initializing ProgressBar
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.hotprogressBar);

        //Displaying Progressbar
        progressBar.setVisibility(View.VISIBLE);
        setProgressBarIndeterminateVisibility(true);

        //JsonArrayRequest of volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(HotConfig.DATA_URL + String.valueOf(requestCount),
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
                        Toast.makeText(view_hot_zone.this, "No More Items Available", Toast.LENGTH_SHORT).show();
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
            HotZonePost superHero = new HotZonePost();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object
                superHero.setImageUrl(json.getString(HotConfig.TAG_IMAGE_URL));
                superHero.setName(json.getString(HotConfig.TAG_NAME));
                superHero.setId(json.getString(HotConfig.TAG_Id));
                superHero.setComment(json.getString(HotConfig.TAG_PUBLISHER));
                superHero.setRepId(json.getString(HotConfig.TAG_REPID));
                superHero.setState(json.getString(HotConfig.TAG_STATE));
                superHero.setHotarea(json.getString(HotConfig.TAG_HOTAREA));
                superHero.setLga(json.getString(HotConfig.TAG_LGA));
                superHero.setRepType(json.getString(HotConfig.TAG_REPTYP));
                superHero.setArea(json.getString(HotConfig.TAG_AREA));
                superHero.setLikes(json.getString(HotConfig.TAG_LIKES));
                superHero.setComments(json.getString(HotConfig.TAG_COMMENTS));
                superHero.setPreview(json.getString(HotConfig.TAG_PREVIEW));
                superHero.setResUri(json.getString(HotConfig.TAG_RSURI));
                superHero.setDate(json.getString(HotConfig.TAG_DATE));

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




    // validation on all fields
    public Boolean verifyFields() {
        return true;
    }

    private void verifyPsid(String callid) {
        if (verifyFields()) {
            //progress.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, VERIFY_BASE_URL);
                retrofit2.Call<VideoReportDetails> call = service.getlivevideos(callid);

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
                                //progress.setVisibility(View.GONE);
                                showDialog(sgresponse, sgname, sgphone, sgstate, imageUrl, videosrc, sgreptype, sgreparea, sgcomment, sgstatuse, date);
                                String resourceuri = (videosrc);
                            }
                        } else {
                            //progress.setVisibility(View.GONE);
                            Toast.makeText(view_hot_zone.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<VideoReportDetails> call, @NonNull Throwable t) {
                        //progress.setVisibility(View.GONE);
                        Toast.makeText(view_hot_zone.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                //progress.setVisibility(View.GONE);
                Toast.makeText(view_hot_zone.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDialog(String sgresponse, String sgname, String sgphone, String sgstate, String imageUrl, String videosrc, String sgreptype, String sgreparea, String sgcomment, String sgstatuse, String date) {
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
        RepDate.setText(date);
        TextView ReporComment = findViewById(R.id.rep_comment);
        ReporComment.setText(sgcomment);
        ImageView imageView = (ImageView) findViewById(R.id.reporter_image);
        Glide.with(view_and_comment.this)
                .load(imageUrl)
                .into(imageView);*/
    }

}
