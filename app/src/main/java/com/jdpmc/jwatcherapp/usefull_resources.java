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
import com.jdpmc.jwatcherapp.adapter.UseRcsAdapter;
import com.jdpmc.jwatcherapp.database.UseFulResource;
import com.jdpmc.jwatcherapp.model.VideoReportDetails;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.ResourceConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class usefull_resources extends AppCompatActivity implements RecyclerView.OnScrollChangeListener {
    //Creating a List of superheroes
    private List<UseFulResource> listSuperHeroes;
    //Creating Views
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;

    String Postsrc = "Use Full Resource";

    //Volley Request Queue
    private RequestQueue requestQueue;

    //The request counter to send ?page=1, ?page=2  requests
    private int requestCount = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.usefull_resources_acivity);
        Intent intent = getIntent();
        String str = intent.getStringExtra("message_key");
        String title = intent.getStringExtra("srctitle_key");
        String description = intent.getStringExtra("description_key");
        String vidurl = intent.getStringExtra("vidurl_key");
        String fileurl = intent.getStringExtra("fileurl_key");
        String previewimg = intent.getStringExtra("prevurl_key");
        String imgfileurl = intent.getStringExtra("imgurl_key");
        String authour = intent.getStringExtra("author_key");
        String date = intent.getStringExtra("date_key");
        String type = intent.getStringExtra("type_key");
        String sourcec = Postsrc;

        String rsctitle = (title);
        String rscdescrip = (description);
        String rscvidurl = (vidurl);
        String rscfileurl = (fileurl);
        String rscimgpreview = (previewimg);
        String rscimgfileurl = (imgfileurl);
        String rscauthor = (authour);
        String rscdate = (date);
        String source = (sourcec);

        //verifyPsid(callid);
        TextView rscTitle = findViewById(R.id.usersc_titletext);
        rscTitle.setText(rsctitle);
        TextView rscDate = findViewById(R.id.rsc_datetxt);
        rscDate.setText(rscdate);
        TextView rscDesc = findViewById(R.id.rsc_description);
        rscDesc.setText(rscdescrip);
        ImageView imageView = (ImageView) findViewById(R.id.rsc_imgpreview);
        Glide.with(usefull_resources.this).load(rscimgpreview)
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

        RelativeLayout view_resour_details = findViewById(R.id.view_usersc);
        view_resour_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("img".equals(type)) {
                    Intent intent = new Intent(usefull_resources.this, use_resource_Viewer_Activity.class);
                    //intent.putExtra("message_key", str);
                    intent.putExtra("srctitle_key", title);
                    intent.putExtra("description_key", description);
                    intent.putExtra("vidurl_key", vidurl);
                    intent.putExtra("fileurl_key", fileurl);
                    intent.putExtra("prevurl_key", previewimg);
                    intent.putExtra("imgurl_key", imgfileurl);
                    intent.putExtra("author_key", authour);
                    intent.putExtra("type_key", type);
                    intent.putExtra("date_key", date);
                    intent.putExtra("postsrc_key", source);
                    v.getContext().startActivity(intent);
                } else if ("pdf".equals(type)){
                    Intent intent = new Intent(usefull_resources.this, pdf_Viewer_Activity.class);
                    //intent.putExtra("message_key", str);
                    intent.putExtra("srctitle_key", title);
                    intent.putExtra("description_key", description);
                    intent.putExtra("vidurl_key", vidurl);
                    intent.putExtra("fileurl_key", fileurl);
                    intent.putExtra("prevurl_key", previewimg);
                    intent.putExtra("imgurl_key", imgfileurl);
                    intent.putExtra("author_key", authour);
                    intent.putExtra("type_key", type);
                    intent.putExtra("date_key", date);
                    intent.putExtra("postsrc_key", source);
                    v.getContext().startActivity(intent);
                }else{
                    Intent intent = new Intent(usefull_resources.this, Youtube_Video_player.class);
                    //intent.putExtra("message_key", str);
                    intent.putExtra("srctitle_key", title);
                    intent.putExtra("description_key", description);
                    intent.putExtra("vidurl_key", vidurl);
                    intent.putExtra("fileurl_key", fileurl);
                    intent.putExtra("prevurl_key", previewimg);
                    intent.putExtra("imgurl_key", imgfileurl);
                    intent.putExtra("author_key", authour);
                    intent.putExtra("type_key", type);
                    intent.putExtra("date_key", date);
                    intent.putExtra("postsrc_key", source);
                    v.getContext().startActivity(intent);
                }
            }
        });

        //Initializing Views
        recyclerView = (RecyclerView) findViewById(R.id.usefullrecyclerView);
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
        adapter = new UseRcsAdapter(listSuperHeroes, this);

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
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(ResourceConfig.DATA_URL + String.valueOf(requestCount),
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
                        Toast.makeText(usefull_resources.this, "No More Items Available", Toast.LENGTH_SHORT).show();
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
            UseFulResource superHero = new UseFulResource();
            JSONObject json = null;
            try {
                //Getting json
                json = array.getJSONObject(i);

                //Adding data to the superhero object
                superHero.setTitle(json.getString(ResourceConfig.TAG_TITLE));
                superHero.setDescrib(json.getString(ResourceConfig.TAG_DESCRIB));
                superHero.setId(json.getString(ResourceConfig.TAG_Id));
                superHero.setRepuuid(json.getString(ResourceConfig.TAG_UUId));
                superHero.setLikes(json.getString(ResourceConfig.TAG_LIKES));
                superHero.setComments(json.getString(ResourceConfig.TAG_COMMENTS));
                superHero.setVidUrl(json.getString(ResourceConfig.TAG_VIDURL));
                superHero.setFileUrl(json.getString(ResourceConfig.TAG_FILEURL));
                superHero.setPrevieImg(json.getString(ResourceConfig.TAG_PREVIEWIMG));
                superHero.setImgFileurl(json.getString(ResourceConfig.TAG_IMGFILE));
                superHero.setAuthour(json.getString(ResourceConfig.TAG_AUTHOUR));
                superHero.setType(json.getString(ResourceConfig.TAG_TYPE));
                superHero.setDate(json.getString(ResourceConfig.TAG_DATE));

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
                            Toast.makeText(usefull_resources.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull retrofit2.Call<VideoReportDetails> call, @NonNull Throwable t) {
                        //progress.setVisibility(View.GONE);
                        Toast.makeText(usefull_resources.this, "I am not Connected to the Internet !", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                //progress.setVisibility(View.GONE);
                Toast.makeText(usefull_resources.this, "Invalid PSID Service Code !", Toast.LENGTH_SHORT).show();
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
