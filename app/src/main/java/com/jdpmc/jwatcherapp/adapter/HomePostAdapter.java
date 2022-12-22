package com.jdpmc.jwatcherapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.jdpmc.jwatcherapp.Video_post_player;
import com.jdpmc.jwatcherapp.database.HomePosts;
import com.jdpmc.jwatcherapp.view_and_comment;

import java.util.List;
import java.util.Objects;

public class HomePostAdapter extends RecyclerView.Adapter<HomePostAdapter.ViewHolder> {
    //Imageloader to load image
    private String imgpost;
    private String userImage;
    private Context context;
    ImageView postImage;

    //List to store all superheroes
    List<HomePosts> superHeroes;

    //Constructor of this class
    public HomePostAdapter(List<HomePosts> superHeroes, Context context){
        super();
        //Getting all superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_post_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        HomePosts superHero =  superHeroes.get(position);
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

        imgpost = (superHero.getPostImg());
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
        holder.texUserimage.setText(superHero.getImageUrl());
        holder.textViewimg.setText(superHero.getPostImg());
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
        //private final ImageView send_button;
        //Views
        public TextView send_button;
        public ImageView like_button;
        public ImageView comment_button;
        public TextView send_text;
        public NetworkImageView imageView;
        public TextView textViewName;
        public TextView texUserimage;
        public TextView textViewId;
        public TextView textViewimg;
        public TextView textViewDescription;

        public ImageView postImage;
        public ImageView Userimage;



        public TextView textViewRepId;
        public TextView textViewState;
        public TextView textViewStatus;
        public TextView textViewRType;
        public TextView textViewArea;
        public TextView textViewLikes;
        public TextView textViewComments;
        public TextView textViewDate;
        public TextView textViewRsuri;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            send_button = (TextView) itemView.findViewById(R.id.send_button_id);
            itemView.setOnClickListener(this);
            like_button = (ImageView) itemView.findViewById(R.id.like);
            itemView.setOnClickListener(this);
            comment_button = (ImageView) itemView.findViewById(R.id.comment);
            itemView.setOnClickListener(this);
            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            Userimage = (ImageView) itemView.findViewById(R.id.reporter_image);
            textViewName = (TextView) itemView.findViewById(R.id.Poster_name);
            textViewDescription = (TextView) itemView.findViewById(R.id.post_decription);
            textViewimg = (TextView) itemView.findViewById(R.id.imglink);
            texUserimage = (TextView) itemView.findViewById(R.id.userimglink);
            textViewRepId = (TextView) itemView.findViewById(R.id.send_text_id);
            textViewState = (TextView) itemView.findViewById(R.id.post_state);
            textViewStatus = (TextView) itemView.findViewById(R.id.post_status);
            textViewRType = (TextView) itemView.findViewById(R.id.post_type);
            textViewArea = (TextView) itemView.findViewById(R.id.set_area);
            textViewLikes = (TextView) itemView.findViewById(R.id.post_likes);
            textViewComments = (TextView) itemView.findViewById(R.id.post_comments);
            textViewDate = (TextView) itemView.findViewById(R.id.post_date);
            textViewRsuri = (TextView) itemView.findViewById(R.id.rscurlchecker);
            //textViewDescription = (TextView) itemView.findViewById(R.id.post_description);
            send_text = (EditText) itemView.findViewById(R.id.send_text_id);

        }

        @Override
        public void onClick(View view) {
            String rsc_uri_checker = textViewRsuri.getText().toString();
            if (!Objects.equals(rsc_uri_checker, "")){
                send_button.setOnClickListener(v -> {
                    String str = send_text.getText().toString();
                    Intent intent = new Intent(context.getApplicationContext(), Video_post_player.class);
                    intent.putExtra("message_key", str);
                    view.getContext().startActivity(intent);
                });
            }else {
                send_button.setOnClickListener(v -> {
                    String username = textViewName.getText().toString();
                    String postimgurl = textViewimg.getText().toString();
                    String userimg = texUserimage.getText().toString();
                    String typeofpost = textViewRType.getText().toString();
                    String repstatus = textViewStatus.getText().toString();
                    String state = textViewState.getText().toString();
                    String date = textViewDate.getText().toString();
                    String comment = textViewDescription.getText().toString();
                    String area = textViewArea.getText().toString();
                    Intent intent = new Intent(context.getApplicationContext(), Image_Post_Viewer_Activity.class);
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
            }

            if (!Objects.equals(rsc_uri_checker, "")){
                postImage.setOnClickListener(v -> {
                    String str = send_text.getText().toString();
                    Intent intent = new Intent(context.getApplicationContext(), Video_post_player.class);
                    intent.putExtra("message_key", str);
                    view.getContext().startActivity(intent);
                });
            }else {
                postImage.setOnClickListener(v -> {
                    String username = textViewName.getText().toString();
                    String postimgurl = textViewimg.getText().toString();
                    String userimg = texUserimage.getText().toString();
                    String typeofpost = textViewRType.getText().toString();
                    String repstatus = textViewStatus.getText().toString();
                    String state = textViewState.getText().toString();
                    String date = textViewDate.getText().toString();
                    String comment = textViewDescription.getText().toString();
                    String area = textViewArea.getText().toString();
                    Intent intent = new Intent(context.getApplicationContext(), Image_Post_Viewer_Activity.class);
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
            }

            like_button.setOnClickListener(v -> {
                Toast.makeText(context.getApplicationContext(), "Like Press", Toast.LENGTH_SHORT).show();
            });
            comment_button.setOnClickListener(v -> {
                String str = send_text.getText().toString();
                Intent intent = new Intent(context.getApplicationContext(), view_and_comment.class);
                intent.putExtra("message_key", str);
                view.getContext().startActivity(intent);
                //Toast.makeText(context.getApplicationContext(), "Comment Press", Toast.LENGTH_SHORT).show();
            });
        }


        /*private void onClick(View v) {
            // get the value which input by user in EditText and convert it to string
            String str = send_text.getText().toString();
            // Create the Intent object of this class Context() to Second_activity class
            Intent intent = new Intent(context.getApplicationContext(), Video_post_player.class);
            // now by putExtra method put the value in key, value pair key is
            // message_key by this key we will receive the value, and put the string
            intent.putExtra("message_key", str);
            // start the Intent
            startActivity(intent);
        }

        private void startActivity(Intent intent) {
        }*/
    }

}
