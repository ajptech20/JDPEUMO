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
import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.Youtube_Video_player;
import com.jdpmc.jwatcherapp.database.UseFulResource;
import com.jdpmc.jwatcherapp.model.LikesResponse;
import com.jdpmc.jwatcherapp.networking.api.Service;
import com.jdpmc.jwatcherapp.networking.generator.DataGenerator;
import com.jdpmc.jwatcherapp.pdf_Viewer_Activity;
import com.jdpmc.jwatcherapp.use_resource_Viewer_Activity;
import com.jdpmc.jwatcherapp.utils.FancyToast;
import com.jdpmc.jwatcherapp.utils.PreferenceUtils;
import com.jdpmc.jwatcherapp.view_and_comment;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;

public class UseRcsAdapter extends RecyclerView.Adapter<UseRcsAdapter.ViewHolder> {
    //Imageloader to load image
    private String imgpost;
    private String userImage;
    private Context context;
    ImageView postImage;

    //List to store all superheroes
    List<UseFulResource> superHeroes;

    //Constructor of this class
    public UseRcsAdapter(List<UseFulResource> superHeroes, Context context){
        super();
        //Getting all superheroes
        this.superHeroes = superHeroes;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resource_post_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Getting the particular item from the list
        UseFulResource superHero =  superHeroes.get(position);
        //Loading image from url
        /*userImage = (superHero.getPrevieImg());
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

                }).into(holder.Userimage);*/

        imgpost = (superHero.getImgFileurl());
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

                }).into(holder.PrevieImg);

        //Showing data on the views
        holder.textViewTitle.setText(superHero.getTitle());
        holder.textViewRepId.setText(superHero.getId());
        holder.textViewDescription.setText(superHero.getDescrib());
        holder.texVidurl.setText(superHero.getVidurl());
        holder.textViewFileurl.setText(superHero.getFileUrl());
        holder.textViewPreviewimg.setText(superHero.getPrevieImg());
        holder.textViewImgFileUrl.setText(superHero.getImgFileurl());
        holder.textViewAuthor.setText(superHero.getAuthor());
        holder.textViewLikes.setText(superHero.getLikes());
        holder.textViewComments.setText(superHero.getComments());
        holder.textViewRepUuid.setText(superHero.getRepuuid());
        holder.textViewType.setText(superHero.getType());


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
        public TextView textViewTitle;
        public TextView texVidurl;
        public TextView textViewId;
        public TextView textViewFileurl;
        public TextView textViewDescription;

        public ImageView PrevieImg;
        public ImageView Userimage;
        public TextView textViewRepUuid;



        public TextView textViewRepId;
        public TextView textViewPreviewimg;
        public TextView textViewImgFileUrl;
        public TextView textViewAuthor;
        public TextView textViewType;
        public TextView textViewLikes;
        public TextView textViewComments;
        public TextView textViewDate;
        public TextView textViewRsuri;
        String Postsrc = "Resource";
        String isadmin = "Admin";
        public ProgressBar Progview;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            /*send_button = (TextView) itemView.findViewById(R.id.rsc_title);
            itemView.setOnClickListener(this);*/
            like_button = (ImageView) itemView.findViewById(R.id.like);
            itemView.setOnClickListener(this);
            comment_button = (ImageView) itemView.findViewById(R.id.comment);
            itemView.setOnClickListener(this);
            PrevieImg = (ImageView) itemView.findViewById(R.id.rscprev_image);
            textViewRepUuid = (TextView) itemView.findViewById(R.id.send_text_uuid);
            textViewTitle = (TextView) itemView.findViewById(R.id.rsc_title);
            textViewDescription = (TextView) itemView.findViewById(R.id.rsc_decription);
            textViewDescription = (TextView) itemView.findViewById(R.id.rsc_describe);
            textViewFileurl = (TextView) itemView.findViewById(R.id.filelink);
            Progview = (ProgressBar) itemView.findViewById(R.id.prgress);
            texVidurl = (TextView) itemView.findViewById(R.id.vidurllink);
            textViewRepId = (TextView) itemView.findViewById(R.id.send_text_id);
            textViewPreviewimg = (TextView) itemView.findViewById(R.id.preview_url);
            textViewImgFileUrl = (TextView) itemView.findViewById(R.id.imgfile_url);
            textViewAuthor = (TextView) itemView.findViewById(R.id.author);
            textViewType = (TextView) itemView.findViewById(R.id.rsc_type);
            textViewLikes = (TextView) itemView.findViewById(R.id.post_likes);
            textViewComments = (TextView) itemView.findViewById(R.id.post_comments);
            textViewDate = (TextView) itemView.findViewById(R.id.post_date);
            //textViewRsuri = (TextView) itemView.findViewById(R.id.rscurlchecker);
            //textViewDescription = (TextView) itemView.findViewById(R.id.post_description);
            send_text = (EditText) itemView.findViewById(R.id.send_text_id);

        }

        @Override
        public void onClick(View view) {
            String typechk = textViewType.getText().toString();
            if (Objects.equals(typechk, "pdf")){
                PrevieImg.setOnClickListener(v -> {
                    String title = textViewTitle.getText().toString();
                    String filurl = textViewFileurl.getText().toString();
                    String vidurl = texVidurl.getText().toString();
                    String author = textViewAuthor.getText().toString();
                    String imgfileurl = textViewImgFileUrl.getText().toString();
                    String prevurl = textViewPreviewimg.getText().toString();
                    String date = textViewDate.getText().toString();
                    String describ = textViewDescription.getText().toString();
                    String type = textViewType.getText().toString();
                    Intent intent = new Intent(context.getApplicationContext(), pdf_Viewer_Activity.class);
                    intent.putExtra("srctitle_key", title);
                    intent.putExtra("description_key", describ);
                    intent.putExtra("vidurl_key", vidurl);
                    intent.putExtra("fileurl_key", filurl);
                    intent.putExtra("prevurl_key", prevurl);
                    intent.putExtra("imgurl_key", imgfileurl);
                    intent.putExtra("author_key", author);
                    intent.putExtra("type_key", type);
                    intent.putExtra("date_key", date);
                    view.getContext().startActivity(intent);
                });
            }else if ((Objects.equals(typechk, "img"))){
                PrevieImg.setOnClickListener(v -> {
                    String title = textViewTitle.getText().toString();
                    String filurl = textViewFileurl.getText().toString();
                    String vidurl = texVidurl.getText().toString();
                    String author = textViewAuthor.getText().toString();
                    String imgfileurl = textViewImgFileUrl.getText().toString();
                    String prevurl = textViewPreviewimg.getText().toString();
                    String date = textViewDate.getText().toString();
                    String describ = textViewDescription.getText().toString();
                    String type = textViewType.getText().toString();
                    Intent intent = new Intent(context.getApplicationContext(), use_resource_Viewer_Activity.class);
                    intent.putExtra("srctitle_key", title);
                    intent.putExtra("description_key", describ);
                    intent.putExtra("vidurl_key", vidurl);
                    intent.putExtra("fileurl_key", filurl);
                    intent.putExtra("prevurl_key", prevurl);
                    intent.putExtra("imgurl_key", imgfileurl);
                    intent.putExtra("author_key", author);
                    intent.putExtra("type_key", type);
                    intent.putExtra("date_key", date);
                    view.getContext().startActivity(intent);
                });
            }else{
                String title = textViewTitle.getText().toString();
                String filurl = textViewFileurl.getText().toString();
                String vidurl = texVidurl.getText().toString();
                String author = textViewAuthor.getText().toString();
                String imgfileurl = textViewImgFileUrl.getText().toString();
                String prevurl = textViewPreviewimg.getText().toString();
                String date = textViewDate.getText().toString();
                String describ = textViewDescription.getText().toString();
                String type = textViewType.getText().toString();
                Intent intent = new Intent(context.getApplicationContext(), Youtube_Video_player.class);
                intent.putExtra("srctitle_key", title);
                intent.putExtra("description_key", describ);
                intent.putExtra("vidurl_key", vidurl);
                intent.putExtra("fileurl_key", filurl);
                intent.putExtra("prevurl_key", prevurl);
                intent.putExtra("imgurl_key", imgfileurl);
                intent.putExtra("author_key", author);
                intent.putExtra("type_key", type);
                intent.putExtra("date_key", date);
                view.getContext().startActivity(intent);
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
                String userimg = textViewPreviewimg.getText().toString();
                String comm_counts =textViewComments.getText().toString();
                String username = isadmin;
                String postimgurl = textViewPreviewimg.getText().toString();
                //String typeofpost = textViewRType.getText().toString();
                String typeofpost = Postsrc;
                String repstatus = textViewTitle.getText().toString();
                String date = textViewDate.getText().toString();
                String comment = textViewDescription.getText().toString();
                Intent intent = new Intent(context.getApplicationContext(), view_and_comment.class);
                intent.putExtra("message_key", str);
                intent.putExtra("commenter_userimg_key", userimg);
                intent.putExtra("comment_count_key", comm_counts);
                intent.putExtra("username_key", username);
                intent.putExtra("imgurl_key", postimgurl);
                intent.putExtra("userimg_key", userimg);
                intent.putExtra("reptype_key", typeofpost);
                intent.putExtra("date_key", date);
                intent.putExtra("comment_key", comment);
                intent.putExtra("repstatus_key", repstatus);
                intent.putExtra("rep_type", Postsrc);
                view.getContext().startActivity(intent);
                //Toast.makeText(context.getApplicationContext(), "Comment Press", Toast.LENGTH_SHORT).show();
            });
        }

        private void LikeMypost(String postpid, String liks_count, String rpuuid) {
            Progview.setVisibility(View.VISIBLE);
            try {
                Service service = DataGenerator.createService(Service.class, USER_LIKE_BASE_URL);
                String userphone = PreferenceUtils.getPhoneNumber(context.getApplicationContext());
                retrofit2.Call<LikesResponse> call = service.RsclikeApost(postpid, userphone, rpuuid);
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
