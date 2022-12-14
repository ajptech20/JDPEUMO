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
import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.Video_post_player;
import com.jdpmc.jwatcherapp.database.LivePost;

import java.util.List;

public class LiveVideoPostAdapter extends RecyclerView.Adapter<LiveVideoPostAdapter.ViewHolder> {
    //Imageloader to load image
    private String imgpost;
    private String userImage;
    private Context context;

    //List to store all superheroes
    List<LivePost> superHeroes;

    //Constructor of this class
    public LiveVideoPostAdapter(List<LivePost> superHeroes, Context context){
        super();
        //Getting all superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.live_vid_post_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Getting the particular item from the list
        LivePost superHero =  superHeroes.get(position);

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
        //holder.textViewimg.setText(superHero.getImageUrl());
        //holder.textViewRepId.setText(superHero.getRepId());
        holder.textViewState.setText(superHero.getState());
        //holder.textViewStatus.setText(superHero.getStatus());
        holder.textViewRType.setText(superHero.getRepType());
        //holder.textViewArea.setText(superHero.getArea());
        holder.textViewLikes.setText(superHero.getLikes());
        holder.textViewComments.setText(superHero.getComments());
        holder.textViewDate.setText(superHero.getDate());
        //holder.textViewRsuri.setText(superHero.getResUri());
    }

    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private final ImageView send_button;
        //Views
        public ImageView send_button;
        public TextView send_text;
        public NetworkImageView imageView;
        public TextView textViewName;
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
        public NetworkImageView imageViewPreview;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            send_button = (ImageView) itemView.findViewById(R.id.send_button_id);
            itemView.setOnClickListener(this);
            postImage = (ImageView) itemView.findViewById(R.id.live_post_image);
            Userimage = (ImageView) itemView.findViewById(R.id.live_reporter_image);
            textViewName = (TextView) itemView.findViewById(R.id.Poster_name);
            //textViewId = (TextView) itemView.findViewById(R.id.textViewId);
            //textViewimg = (TextView) itemView.findViewById(R.id.textViewName2);
            textViewRepId = (TextView) itemView.findViewById(R.id.send_text_id);
            textViewState = (TextView) itemView.findViewById(R.id.post_state);
            //textViewStatus = (TextView) itemView.findViewById(R.id.textViewName5);
            textViewRType = (TextView) itemView.findViewById(R.id.post_type);
            textViewArea = (TextView) itemView.findViewById(R.id.set_area);
            textViewLikes = (TextView) itemView.findViewById(R.id.post_likes);
            textViewComments = (TextView) itemView.findViewById(R.id.post_comments);
            textViewDate = (TextView) itemView.findViewById(R.id.post_date);
            //textViewRsuri = (TextView) itemView.findViewById(R.id.textViewName10);
            textViewDescription = (TextView) itemView.findViewById(R.id.post_description);
            send_text = (EditText) itemView.findViewById(R.id.send_text_id);

        }

        @Override
        public void onClick(View view) {
            send_button.setOnClickListener(v -> {
                String str = send_text.getText().toString();
                Intent intent = new Intent(context.getApplicationContext(), Video_post_player.class);
                intent.putExtra("message_key", str);
                view.getContext().startActivity(intent);
            });
        }
    }

}
