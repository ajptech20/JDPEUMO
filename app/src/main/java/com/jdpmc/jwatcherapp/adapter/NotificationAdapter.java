package com.jdpmc.jwatcherapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.jdpmc.jwatcherapp.Youtube_Video_player;
import com.jdpmc.jwatcherapp.database.NewNotificationsPost;
import com.jdpmc.jwatcherapp.pdf_Viewer_Activity;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    //Imageloader to load image
    private String imgpost;
    private String userImage;
    private Context context;
    private static final String check_clear = "cleared";

    //List to store all superheroes
    List<NewNotificationsPost> superHeroes;

    //Constructor of this class
    public NotificationAdapter(List<NewNotificationsPost> superHeroes, Context context){
        super();
        //Getting all superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_notifications_post_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //Getting the particular item from the list
        NewNotificationsPost superHero =  superHeroes.get(position);
        String check = (superHero.getStatus());
        if (check.equals("Live Post")){
            imgpost = (superHero.getPostimg());
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
        }else if (check.equals("Short Video")){
            imgpost = (superHero.getPostimg());
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
        }else if ((check.equals("HotImageUpload"))){
            imgpost = (superHero.getPostimg());
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
        }else if ((check.equals("HotVideoUpload"))){
            imgpost = (superHero.getPostimg());
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
        }else if ((check.equals("Image Post"))){
            imgpost = (superHero.getPostimg());
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
        }else if ((check.equals("img"))){
            imgpost = (superHero.getPostimg());
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
        }else if ((check.equals("pdf"))){
            userImage = (superHero.getUserimg());
            //Glide.with(context).load(imgpost).into(holder.postImage);
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

                }).into(holder.postImage);
        }else{
            imgpost = (superHero.getPostimg());
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
        }
        //Loading image from url
        userImage = (superHero.getUserimg());
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

        //Showing data on the views
        holder.textViewName.setText(superHero.getUsername());
        holder.textViewhotarea.setText(superHero.getReptype());
        holder.textViewIsviewd.setText(superHero.getClearstatus());
        holder.textViewDescription.setText(superHero.getComment());
        holder.texUserimage.setText(superHero.getUserimg());
        holder.textViewRepImgUrl.setText(superHero.getPostimg());
        holder.textViewState.setText(superHero.getState());
        //holder.textViewLga.setText(superHero.getLga());
        holder.textViewRstatus.setText(superHero.getStatus());
        holder.textViewArea.setText(superHero.getArea());
        //holder.textViewLikes.setText(superHero.getLikes());
        //holder.textViewComments.setText(superHero.getComment());
        holder.textViewDate.setText(superHero.getDatee());
        holder.textViewRsuri.setText(superHero.getVidrsc());
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
        public TextView textViewIsviewd;
        public TextView textViewhotarea;
        public TextView textViewId;
        public TextView texUserimage;
        public TextView textViewDescription;
        public String textViewClearStatus;

        public ImageView postImage;
        public ImageView Userimage;
        public TextView textViewRepImgUrl;

        public TextView textViewRepId;
        public TextView textViewState;
        public TextView textViewLga;
        public TextView textViewRstatus;
        public TextView textViewArea;
        public TextView textViewLikes;
        public TextView textViewComments;
        public TextView textViewDate;
        public TextView textViewRsuri;
        public LinearLayout RemoveViewdNotice;
        public NetworkImageView imageViewPreview;

        String Postsrc = "Hot Zone";

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            /*send_button = (TextView) itemView.findViewById(R.id.send_button_id);
            itemView.setOnClickListener(this);*/
            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            itemView.setOnClickListener(this);
            Userimage = (ImageView) itemView.findViewById(R.id.reporter_image);
            textViewName = (TextView) itemView.findViewById(R.id.Poster_name);
            textViewIsviewd = (TextView) itemView.findViewById(R.id.clearme);
            textViewhotarea = (TextView) itemView.findViewById(R.id.hot_postArea);
            textViewClearStatus = String.valueOf((TextView) itemView.findViewById(R.id.clearme));
            textViewRepImgUrl = (TextView) itemView.findViewById(R.id.imgurl);
            textViewState = (TextView) itemView.findViewById(R.id.post_state);
            //textViewLga = (TextView) itemView.findViewById(R.id.post_lga);
            texUserimage = (TextView) itemView.findViewById(R.id.user_img);
            textViewRsuri = (TextView) itemView.findViewById(R.id.vid_url);

            textViewRstatus = (TextView) itemView.findViewById(R.id.post_type);

            textViewArea = (TextView) itemView.findViewById(R.id.set_area);
            //textViewLikes = (TextView) itemView.findViewById(R.id.post_likes);
            //textViewComments = (TextView) itemView.findViewById(R.id.post_comments);
            textViewDescription = (TextView) itemView.findViewById(R.id.post_describ);
            textViewDate = (TextView) itemView.findViewById(R.id.post_date);
            //textViewDescription = (TextView) itemView.findViewById(R.id.post_description);

            //String clearpref = PreferenceUtils.getNoticeClearer(context.getApplicationContext());
            /*String clearme = "viewed";
            String not_viewed = textViewIsviewd.getText().toString();
            textViewClearStatus = PreferenceUtils.getNoticeClearer(context.getApplicationContext());
            String check_viewed = textViewClearStatus;
            if (Objects.equals(check_viewed, "viewed")){
                RemoveViewdNotice = (LinearLayout) itemView.findViewById(R.id.post_viewed);
                RemoveViewdNotice.setVisibility(View.VISIBLE);
            }else if (not_viewed.equals(not_viewed)){
                RemoveViewdNotice = (LinearLayout) itemView.findViewById(R.id.post_viewed);
                RemoveViewdNotice.setVisibility(View.GONE);
            }*/

            //FancyToast.makeText(context, "Did it Worked" +check_viewed, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            //RemoveViewdNotice = (LinearLayout) itemView.findViewById(R.id.post_viewed);

        }

        @Override
        public void onClick(View view) {
            RemoveViewdNotice = (LinearLayout) itemView.findViewById(R.id.post_viewed);
            String clearme = "viewed";
            String noticstatuse = textViewRstatus.getText().toString();
            switch (noticstatuse) {
                case "Live Post":
                case "Short Video":
                    postImage.setOnClickListener(v -> {
                        //RemoveViewdNotice.setVisibility(View.GONE);
                        //PreferenceUtils.saveNoticeClearer(clearme, context.getApplicationContext());
                        String username = textViewName.getText().toString();
                        String userimg = texUserimage.getText().toString();
                        String postimg = textViewRepImgUrl.getText().toString();
                        String town = textViewhotarea.getText().toString();
                        String state = textViewState.getText().toString();
                        //String lga = textViewLga.getText().toString();
                        String date = textViewDate.getText().toString();
                        String comment = textViewDescription.getText().toString();
                        String rscurl = textViewRsuri.getText().toString();
                        String source = textViewRstatus.getText().toString();
                        Intent intent = new Intent(context.getApplicationContext(), Hot_Video_post_player.class);
                        intent.putExtra("username_key", username);
                        intent.putExtra("userimg_key", userimg);
                        intent.putExtra("imgurl_key", postimg);
                        intent.putExtra("town_key", town);
                        intent.putExtra("state_key", state);
                        //intent.putExtra("lga_key", lga);
                        intent.putExtra("date_key", date);
                        intent.putExtra("comment_key", comment);
                        intent.putExtra("rscurl_key", rscurl);
                        intent.putExtra("postsrc_key", source);
                        view.getContext().startActivity(intent);
                    });
                    break;
                case "Image Post":
                case "img":
                    postImage.setOnClickListener(v -> {
                        //RemoveViewdNotice.setVisibility(View.GONE);
                        //PreferenceUtils.saveNoticeClearer(clearme, context.getApplicationContext());
                        String username = textViewName.getText().toString();
                        String userimg = texUserimage.getText().toString();
                        String postimg = textViewRepImgUrl.getText().toString();
                        String town = textViewhotarea.getText().toString();
                        String state = textViewState.getText().toString();
                        //String lga = textViewLga.getText().toString();
                        String date = textViewDate.getText().toString();
                        String comment = textViewDescription.getText().toString();
                        String rscurl = textViewRsuri.getText().toString();
                        String source = textViewRstatus.getText().toString();
                        Intent intent = new Intent(context.getApplicationContext(), Hot_Image_post_viewer.class);
                        intent.putExtra("username_key", username);
                        intent.putExtra("userimg_key", userimg);
                        intent.putExtra("imgurl_key", postimg);
                        intent.putExtra("town_key", town);
                        intent.putExtra("state_key", state);
                        //intent.putExtra("lga_key", lga);
                        intent.putExtra("date_key", date);
                        intent.putExtra("comment_key", comment);
                        intent.putExtra("rscurl_key", rscurl);
                        intent.putExtra("postsrc_key", source);
                        view.getContext().startActivity(intent);
                    });
                    break;
                case "HotImageUpload":
                    postImage.setOnClickListener(v -> {
                        //RemoveViewdNotice.setVisibility(View.GONE);
                        //PreferenceUtils.saveNoticeClearer(clearme, context.getApplicationContext());
                        String source = "Hot Zone";
                        String username = textViewName.getText().toString();
                        String userimg = texUserimage.getText().toString();
                        String postimg = textViewRepImgUrl.getText().toString();
                        String town = textViewhotarea.getText().toString();
                        String state = textViewState.getText().toString();
                        //String lga = textViewLga.getText().toString();
                        String date = textViewDate.getText().toString();
                        String comment = textViewDescription.getText().toString();
                        String rscurl = textViewRsuri.getText().toString();
                        //String source = textViewRstatus.getText().toString();
                        Intent intent = new Intent(context.getApplicationContext(), Hot_Image_post_viewer.class);
                        intent.putExtra("username_key", username);
                        intent.putExtra("userimg_key", userimg);
                        intent.putExtra("imgurl_key", postimg);
                        intent.putExtra("town_key", town);
                        intent.putExtra("state_key", state);
                        //intent.putExtra("lga_key", lga);
                        intent.putExtra("date_key", date);
                        intent.putExtra("comment_key", comment);
                        intent.putExtra("rscurl_key", rscurl);
                        intent.putExtra("postsrc_key", source);
                        view.getContext().startActivity(intent);
                    });
                    break;
                case "HotVideoUpload":
                    postImage.setOnClickListener(v -> {
                        //RemoveViewdNotice.setVisibility(View.GONE);
                        //PreferenceUtils.saveNoticeClearer(clearme, context.getApplicationContext());
                        String source = "Hot Zone";
                        String username = textViewName.getText().toString();
                        String userimg = texUserimage.getText().toString();
                        String postimg = textViewRepImgUrl.getText().toString();
                        String town = textViewhotarea.getText().toString();
                        String state = textViewState.getText().toString();
                        //String lga = textViewLga.getText().toString();
                        String date = textViewDate.getText().toString();
                        String comment = textViewDescription.getText().toString();
                        String rscurl = textViewRsuri.getText().toString();
                        Intent intent = new Intent(context.getApplicationContext(), Hot_Video_post_player.class);
                        intent.putExtra("username_key", username);
                        intent.putExtra("userimg_key", userimg);
                        intent.putExtra("imgurl_key", postimg);
                        intent.putExtra("town_key", town);
                        intent.putExtra("state_key", state);
                        //intent.putExtra("lga_key", lga);
                        intent.putExtra("date_key", date);
                        intent.putExtra("comment_key", comment);
                        intent.putExtra("rscurl_key", rscurl);
                        intent.putExtra("postsrc_key", source);
                        view.getContext().startActivity(intent);
                    });
                    break;
                case "pdf":
                    postImage.setOnClickListener(v -> {
                        //RemoveViewdNotice.setVisibility(View.GONE);
                        //PreferenceUtils.saveNoticeClearer(clearme, context.getApplicationContext());
                        String username = textViewName.getText().toString();
                        String userimg = texUserimage.getText().toString();
                        String postimg = textViewRepImgUrl.getText().toString();
                        String town = textViewhotarea.getText().toString();
                        String state = textViewState.getText().toString();
                        //String lga = textViewLga.getText().toString();
                        String date = textViewDate.getText().toString();
                        String comment = textViewDescription.getText().toString();
                        String rscurl = textViewRsuri.getText().toString();
                        String source = textViewRstatus.getText().toString();
                        Intent intent = new Intent(context.getApplicationContext(), pdf_Viewer_Activity.class);
                        intent.putExtra("srctitle_key", username);
                        intent.putExtra("description_key", comment);
                        intent.putExtra("vidurl_key", rscurl);
                        intent.putExtra("fileurl_key", postimg);
                        intent.putExtra("prevurl_key", userimg);
                        intent.putExtra("imgurl_key", userimg);
                        intent.putExtra("author_key", town);
                        intent.putExtra("type_key", state);
                        intent.putExtra("date_key", date);
                        intent.putExtra("postsrc_key", source);
                        view.getContext().startActivity(intent);
                    });
                    break;
                default:
                        String username = textViewName.getText().toString();
                        String userimg = texUserimage.getText().toString();
                        String postimg = textViewRepImgUrl.getText().toString();
                        String town = textViewhotarea.getText().toString();
                        String state = textViewState.getText().toString();
                        //String lga = textViewLga.getText().toString();
                        String date = textViewDate.getText().toString();
                        String comment = textViewDescription.getText().toString();
                        String rscurl = textViewRsuri.getText().toString();
                        String source = textViewRstatus.getText().toString();
                        Intent intent = new Intent(context.getApplicationContext(), Youtube_Video_player.class);
                        intent.putExtra("srctitle_key", username);
                        intent.putExtra("description_key", comment);
                        intent.putExtra("vidurl_key", rscurl);
                        intent.putExtra("fileurl_key", postimg);
                        intent.putExtra("prevurl_key", postimg);
                        intent.putExtra("imgurl_key", userimg);
                        intent.putExtra("author_key", town);
                        intent.putExtra("type_key", state);
                        intent.putExtra("date_key", date);
                        intent.putExtra("postsrc_key", source);
                        view.getContext().startActivity(intent);
                    break;
            }

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
    }

}
