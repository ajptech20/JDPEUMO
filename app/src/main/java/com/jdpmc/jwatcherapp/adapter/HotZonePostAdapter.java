package com.jdpmc.jwatcherapp.adapter;

import static com.jdpmc.jwatcherapp.utils.Constants.USER_LIKE_BASE_URL;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.jdpmc.jwatcherapp.Hot_Image_post_viewer;
import com.jdpmc.jwatcherapp.Hot_Video_post_player;
import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.database.HotZonePost;
import com.jdpmc.jwatcherapp.model.LikesResponse;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.FancyToast;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.jdpmc.jwatcherapp.view_and_comment;

import java.util.List;

import retrofit2.Call;

public class HotZonePostAdapter extends RecyclerView.Adapter<HotZonePostAdapter.ViewHolder> {
    //Imageloader to load image
    private String imgpost;
    private String userImage;
    private Context context;

    //List to store all superheroes
    List<HotZonePost> superHeroes;

    //Constructor of this class
    public HotZonePostAdapter(List<HotZonePost> superHeroes, Context context){
        super();
        //Getting all superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hot_zones_post_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Getting the particular item from the list
        HotZonePost superHero =  superHeroes.get(position);

        //Loading image from url
        userImage = (superHero.getImageUrl());
        Glide.with(context).load(userImage)
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

                }).into(holder.Userimage);

        imgpost = (superHero.getPreview());
        //Glide.with(context).load(imgpost).into(holder.postImage);
        Glide.with(context).load(imgpost)
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

                }).into(holder.postImage);

        //Showing data on the views
        holder.textViewName.setText(superHero.getName());
        holder.textViewhotarea.setText(superHero.getHotarea());
        holder.textViewRepId.setText(superHero.getId());
        holder.textViewRepUuid.setText(superHero.getRepuuid());
        holder.textViewDescription.setText(superHero.getComment());
        holder.texUserimage.setText(superHero.getImageUrl());
        holder.textViewRepImgUrl.setText(superHero.getPreview());
        holder.textViewState.setText(superHero.getState());
        holder.textViewLga.setText(superHero.getLga());
        //holder.textViewRType.setText(superHero.getRepType());
        holder.textViewArea.setText(superHero.getArea());
        holder.textViewLikes.setText(superHero.getLikes());
        holder.textViewComments.setText(superHero.getComments());
        holder.textViewDate.setText(superHero.getDate());
        holder.textViewRsuri.setText(superHero.getResUri());
    }

    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private final ImageView send_button;
        //Views
        public ImageView send_button;
        public NetworkImageView imageView;
        public TextView textViewName;
        public TextView textViewhotarea;
        public TextView textViewId;
        public TextView texUserimage;
        public TextView textViewDescription;
        public TextView send_text;

        public ImageView postImage;
        public ImageView Userimage;
        public TextView textViewRepImgUrl;

        public ImageView like_button;
        public ImageView comment_button;

        public TextView textViewRepId;
        public TextView textViewRepUuid;
        public TextView textViewState;
        public TextView textViewLga;
        public TextView textViewRType;
        public TextView textViewArea;
        public TextView textViewLikes;
        public TextView textViewComments;
        public TextView textViewDate;
        public TextView textViewRsuri;
        public NetworkImageView imageViewPreview;
        public ProgressBar Progview;

        String Postsrc = "Hot Zone";

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            /*send_button = (TextView) itemView.findViewById(R.id.send_button_id);
            itemView.setOnClickListener(this);*/
            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            itemView.setOnClickListener(this);
            like_button = (ImageView) itemView.findViewById(R.id.like);
            itemView.setOnClickListener(this);
            comment_button = (ImageView) itemView.findViewById(R.id.comment);
            itemView.setOnClickListener(this);
            Userimage = (ImageView) itemView.findViewById(R.id.reporter_image);
            textViewName = (TextView) itemView.findViewById(R.id.Poster_name);
            textViewhotarea = (TextView) itemView.findViewById(R.id.hot_postArea);
            Progview = (ProgressBar) itemView.findViewById(R.id.prgress);
            textViewRepId = (TextView) itemView.findViewById(R.id.send_text_id);
            textViewRepUuid = (TextView) itemView.findViewById(R.id.send_text_uuid);
            textViewRepImgUrl = (TextView) itemView.findViewById(R.id.imgurl);
            textViewState = (TextView) itemView.findViewById(R.id.post_state);
            textViewLga = (TextView) itemView.findViewById(R.id.post_lga);
            texUserimage = (TextView) itemView.findViewById(R.id.user_img);
            textViewRsuri = (TextView) itemView.findViewById(R.id.vid_url);

            //textViewRType = (TextView) itemView.findViewById(R.id.post_type);

            textViewArea = (TextView) itemView.findViewById(R.id.set_area);
            textViewLikes = (TextView) itemView.findViewById(R.id.post_likes);
            textViewComments = (TextView) itemView.findViewById(R.id.post_comments);
            textViewDescription = (TextView) itemView.findViewById(R.id.post_describ);
            textViewDate = (TextView) itemView.findViewById(R.id.post_date);
            send_text = (EditText) itemView.findViewById(R.id.send_text_id);

        }

        @Override
        public void onClick(View view) {

            String rsc_uri_checker = textViewRsuri.getText().toString();
            if (!rsc_uri_checker.equals("")){
                postImage.setOnClickListener(v -> {
                    String username = textViewName.getText().toString();
                    String userimg = texUserimage.getText().toString();
                    String postimg = textViewRepImgUrl.getText().toString();
                    String town = textViewhotarea.getText().toString();
                    String state = textViewState.getText().toString();
                    String lga = textViewLga.getText().toString();
                    String date = textViewDate.getText().toString();
                    String comment = textViewDescription.getText().toString();
                    String rscurl = textViewRsuri.getText().toString();
                    String source = Postsrc;
                    Intent intent = new Intent(context.getApplicationContext(), Hot_Video_post_player.class);
                    intent.putExtra("username_key", username);
                    intent.putExtra("userimg_key", userimg);
                    intent.putExtra("imgurl_key", postimg);
                    intent.putExtra("town_key", town);
                    intent.putExtra("state_key", state);
                    intent.putExtra("lga_key", lga);
                    intent.putExtra("date_key", date);
                    intent.putExtra("comment_key", comment);
                    intent.putExtra("rscurl_key", rscurl);
                    intent.putExtra("postsrc_key", source);
                    view.getContext().startActivity(intent);
                });
            }else {
                postImage.setOnClickListener(v -> {
                    String username = textViewName.getText().toString();
                    String userimg = texUserimage.getText().toString();
                    String postimg = textViewRepImgUrl.getText().toString();
                    String town = textViewhotarea.getText().toString();
                    String state = textViewState.getText().toString();
                    String lga = textViewLga.getText().toString();
                    String date = textViewDate.getText().toString();
                    String comment = textViewDescription.getText().toString();
                    String rscurl = textViewRsuri.getText().toString();
                    String source = Postsrc;
                    Intent intent = new Intent(context.getApplicationContext(), Hot_Image_post_viewer.class);
                    intent.putExtra("username_key", username);
                    intent.putExtra("userimg_key", userimg);
                    intent.putExtra("imgurl_key", postimg);
                    intent.putExtra("town_key", town);
                    intent.putExtra("state_key", state);
                    intent.putExtra("lga_key", lga);
                    intent.putExtra("date_key", date);
                    intent.putExtra("comment_key", comment);
                    intent.putExtra("rscurl_key", rscurl);
                    intent.putExtra("postsrc_key", source);
                    view.getContext().startActivity(intent);
                });
            }

            like_button.setOnClickListener(v -> {
                //textViewLikes.setText("10");
                String postpid = send_text.getText().toString();
                String liks_count = textViewLikes.getText().toString();
                String rpuuid = textViewRepUuid.getText().toString();
                LikeMypost(postpid, liks_count, rpuuid);
                //Toast.makeText(context.getApplicationContext(), "Like Press" +postpid +rpuuid, Toast.LENGTH_SHORT).show();
            });
            comment_button.setOnClickListener(v -> {
                String str = send_text.getText().toString();
                String userimg = texUserimage.getText().toString();
                String comm_counts =textViewComments.getText().toString();
                String username = textViewName.getText().toString();
                String postimgurl = textViewRepImgUrl.getText().toString();
                //String typeofpost = textViewRType.getText().toString();
                String typeofpost = Postsrc;
                String repstatus = Postsrc;
                String state = textViewState.getText().toString();
                String date = textViewDate.getText().toString();
                String comment = textViewDescription.getText().toString();
                String area = textViewArea.getText().toString();
                Intent intent = new Intent(context.getApplicationContext(), view_and_comment.class);
                intent.putExtra("message_key", str);
                intent.putExtra("commenter_userimg_key", userimg);
                intent.putExtra("comment_count_key", comm_counts);
                intent.putExtra("username_key", username);
                intent.putExtra("imgurl_key", postimgurl);
                intent.putExtra("userimg_key", userimg);
                intent.putExtra("reptype_key", typeofpost);
                intent.putExtra("state_key", state);
                intent.putExtra("date_key", date);
                intent.putExtra("comment_key", comment);
                intent.putExtra("area_key", area);
                intent.putExtra("repstatus_key", repstatus);
                intent.putExtra("rep_type", repstatus);
                view.getContext().startActivity(intent);
                //Toast.makeText(context.getApplicationContext(), "Comment Press", Toast.LENGTH_SHORT).show();
            });

            /*postImage.setOnClickListener(v -> {
                String username = textViewName.getText().toString();
                String userimg = texUserimage.getText().toString();
                String postimg = textViewRepImgUrl.getText().toString();
                String town = textViewhotarea.getText().toString();
                String state = textViewState.getText().toString();
                String lga = textViewLga.getText().toString();
                String date = textViewDate.getText().toString();
                String comment = textViewDescription.getText().toString();
                String rscurl = textViewRsuri.getText().toString();
                Intent intent = new Intent(context.getApplicationContext(), Hot_Video_post_player.class);
                intent.putExtra("username_key", username);
                intent.putExtra("userimg_key", userimg);
                intent.putExtra("postimg_key", postimg);
                intent.putExtra("town_key", town);
                intent.putExtra("state_key", state);
                intent.putExtra("lga_key", lga);
                intent.putExtra("date_key", date);
                intent.putExtra("comment_key", comment);
                intent.putExtra("rscurl_key", rscurl);
                view.getContext().startActivity(intent);
            });*/
        }

        private void LikeMypost(String postpid, String liks_count, String rpuuid) {
            Progview.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, USER_LIKE_BASE_URL);
                String userphone = PreferenceUtils.getPhoneNumber(context.getApplicationContext());
                retrofit2.Call<LikesResponse> call = service.HotlikeApost(postpid, userphone, rpuuid);
                //Call<LikesResponse> call = service.likeApost(postpid, userphone, rpuuid);
                call.enqueue(new retrofit2.Callback<LikesResponse>() {
                    @Override
                    public void onResponse(@NonNull retrofit2.Call<LikesResponse> call, @NonNull retrofit2.Response<LikesResponse> response) {
                        if (response.isSuccessful()) {
                            Progview.setVisibility(View.GONE);
                            //FancyToast.makeText(context.getApplicationContext(), "Like Taken", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                            if (response.body() != null) {
                                LikesResponse likeResponse = response.body();
                                String isliked = likeResponse.getLiked();
                                String isunliked = likeResponse.getUnliked();
                                String likecount = likeResponse.getLikeCount();
                                String isErr = likeResponse.getLikeErr();
                                if (isliked != null){
                                    String ilike = likeResponse.getLiked();
                                    textViewLikes.setText(likecount);
                                    //liks_count.setVisibility(View.GONE);
                                    FancyToast.makeText(context.getApplicationContext(),ilike, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                }else{
                                    String unlik = likeResponse.getUnliked();
                                    textViewLikes.setText(likecount);
                                    FancyToast.makeText(context.getApplicationContext(),unlik, FancyToast.LENGTH_LONG, FancyToast.SUCCESS, false).show();
                                }
                            }else {
                                FancyToast.makeText(context.getApplicationContext(), "Like Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<LikesResponse> call, @NonNull Throwable t) {
                        Progview.setVisibility(View.GONE);
                        FancyToast.makeText(context.getApplicationContext(), "No Internet", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            } catch (Exception e) {
                FancyToast.makeText(context.getApplicationContext(), "Like Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

            }
        }
    }

}
