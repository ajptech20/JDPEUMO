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
import com.jdpmc.jwatcherapp.Image_Post_Viewer_Activity;
import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.database.ImgPost;
import com.jdpmc.jwatcherapp.model.LikesResponse;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.utils.FancyToast;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.jdpmc.jwatcherapp.view_and_comment;

import java.util.List;

import retrofit2.Call;

public class ImgPostAdapter extends RecyclerView.Adapter<ImgPostAdapter.ViewHolder> {

    private String imgpost;
    private String userImage;
    private Context context;

    //List to store all superheroes
    List<ImgPost> superHeroes;

    //Constructor of this class
    public ImgPostAdapter(List<ImgPost> superHeroes, Context context){
        super();
        //Getting all superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.image_post_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        ImgPost superHero =  superHeroes.get(position);
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
        holder.textViewRepId.setText(superHero.getId());
        holder.textViewDescription.setText(superHero.getComment());
        holder.textViewUserimg.setText(superHero.getImageUrl());
        holder.textViewPostimg.setText(superHero.getPreview());
        holder.textViewRepUuid.setText(superHero.getRepuuid());
        holder.textViewState.setText(superHero.getState());
        holder.textViewStatus.setText(superHero.getStatus());
        holder.textViewRType.setText(superHero.getRepType());
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
        //Views
        public ImageView postImgViewer;
        public TextView send_text;
        public NetworkImageView imageView;
        public TextView textViewName;
        public TextView textViewUserimg;
        public TextView textViewPostimg;
        public TextView textViewDescription;

        public ImageView postImage;
        public ImageView Userimage;

        public ImageView like_button;
        public ImageView comment_button;

        public TextView textViewRepId;
        public TextView textViewRepUuid;
        public TextView textViewState;
        public TextView textViewStatus;
        public TextView textViewRType;
        public TextView textViewArea;
        public TextView textViewLikes;
        public TextView textViewComments;
        public TextView textViewDate;
        public TextView textViewRsuri;
        public ProgressBar Progview;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            postImgViewer = (ImageView) itemView.findViewById(R.id.img_post_image);
            itemView.setOnClickListener(this);
            like_button = (ImageView) itemView.findViewById(R.id.like);
            itemView.setOnClickListener(this);
            comment_button = (ImageView) itemView.findViewById(R.id.comment);
            itemView.setOnClickListener(this);
            postImage = (ImageView) itemView.findViewById(R.id.img_post_image);
            Userimage = (ImageView) itemView.findViewById(R.id.img_reporter_image);
            textViewRepUuid = (TextView) itemView.findViewById(R.id.send_text_uuid);
            textViewRepId = (TextView) itemView.findViewById(R.id.send_text_id);
            textViewName = (TextView) itemView.findViewById(R.id.Poster_name);
            textViewUserimg = (TextView) itemView.findViewById(R.id.userimgurl);
            textViewRsuri = (TextView) itemView.findViewById(R.id.rscurilink);
            Progview = (ProgressBar) itemView.findViewById(R.id.prgress);
            textViewPostimg = (TextView) itemView.findViewById(R.id.postimgurl);
            send_text = (EditText) itemView.findViewById(R.id.send_text_id);
            textViewState = (TextView) itemView.findViewById(R.id.post_state);
            textViewStatus = (TextView) itemView.findViewById(R.id.post_status);
            textViewRType = (TextView) itemView.findViewById(R.id.post_type);
            textViewArea = (TextView) itemView.findViewById(R.id.set_area);
            textViewLikes = (TextView) itemView.findViewById(R.id.post_likes);
            textViewComments = (TextView) itemView.findViewById(R.id.post_comments);
            textViewDate = (TextView) itemView.findViewById(R.id.post_date);
            textViewDescription = (TextView) itemView.findViewById(R.id.post_description);


        }

        @Override
        public void onClick(View view) {
            postImgViewer.setOnClickListener(v -> {
                String username = textViewName.getText().toString();
                String postimgurl = textViewPostimg.getText().toString();
                String userimg = textViewUserimg.getText().toString();
                String typeofpost = textViewRType.getText().toString();
                String repstatus = textViewStatus.getText().toString();
                String state = textViewState.getText().toString();
                String date = textViewDate.getText().toString();
                String comment = textViewDescription.getText().toString();
                String area = textViewArea.getText().toString();
                String str = send_text.getText().toString();
                Intent intent = new Intent(context.getApplicationContext(), Image_Post_Viewer_Activity.class);
                intent.putExtra("message_key", str);
                intent.putExtra("username_key", username);
                intent.putExtra("imgurl_key", postimgurl);
                intent.putExtra("userimg_key", userimg);
                intent.putExtra("reptype_key", typeofpost);
                intent.putExtra("state_key", state);
                intent.putExtra("date_key", date);
                intent.putExtra("comment_key", comment);
                intent.putExtra("area_key", area);
                intent.putExtra("repstatus_key", repstatus);
                view.getContext().startActivity(intent);
            });

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
                String userimg = textViewUserimg.getText().toString();
                String comm_counts =textViewComments.getText().toString();
                String username = textViewName.getText().toString();
                String postimgurl = textViewPostimg.getText().toString();
                String typeofpost = textViewRType.getText().toString();
                String repstatus = textViewStatus.getText().toString();
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
                view.getContext().startActivity(intent);
                //Toast.makeText(context.getApplicationContext(), "Comment Press", Toast.LENGTH_SHORT).show();
            });
        }

        private void LikeMypost(String postpid, String liks_count, String rpuuid) {
            Progview.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, USER_LIKE_BASE_URL);
                String userphone = PreferenceUtils.getPhoneNumber(context.getApplicationContext());
                retrofit2.Call<LikesResponse> call = service.likeApost(postpid, userphone, rpuuid);
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
                                Progview.setVisibility(View.GONE);
                                FancyToast.makeText(context.getApplicationContext(), "Like Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<LikesResponse> call, @NonNull Throwable t) {
                        //progression.setVisibility(View.GONE);
                        FancyToast.makeText(context.getApplicationContext(), "No Internet", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
                    }
                });
            } catch (Exception e) {
                FancyToast.makeText(context.getApplicationContext(), "Like Failed", FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();

            }
        }

    }
}
