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
import com.jdpmc.jwatcherapp.Image_Post_Viewer_Activity;
import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.database.ImgPost;

import java.util.List;

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
        //holder.textViewId.setText(superHero.getId());
        holder.textViewDescription.setText(superHero.getComment());
        holder.textViewUserimg.setText(superHero.getImageUrl());
        holder.textViewPostimg.setText(superHero.getPreview());
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
            postImgViewer = (ImageView) itemView.findViewById(R.id.img_post_image);
            itemView.setOnClickListener(this);
            postImage = (ImageView) itemView.findViewById(R.id.img_post_image);
            Userimage = (ImageView) itemView.findViewById(R.id.img_reporter_image);
            textViewName = (TextView) itemView.findViewById(R.id.Poster_name);
            textViewUserimg = (TextView) itemView.findViewById(R.id.userimgurl);
            textViewRsuri = (TextView) itemView.findViewById(R.id.rscurilink);
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
        }
    }
}
