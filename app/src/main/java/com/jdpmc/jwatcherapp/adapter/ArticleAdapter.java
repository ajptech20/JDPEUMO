package com.jdpmc.jwatcherapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jdpmc.jwatcherapp.Hot_Image_post_viewer;
import com.jdpmc.jwatcherapp.Hot_Video_post_player;
import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.database.ArticleEntry;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    // Member variable to handle item clicks
    private List<ArticleEntry> mArticleEntries;
    private Context mContext;

    public ArticleAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.article_items, parent, false);

        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        // Determine the values of the wanted data
        ArticleEntry articleEntry = mArticleEntries.get(position);
        String postcomment = articleEntry.getPostcomment();
        String username = articleEntry.getUsername();
        String postarea = articleEntry.getPostarea();
        String state = articleEntry.getPoststate();
        String posttype = articleEntry.getPosttype();
        String liks = articleEntry.getLikscount();
        String postdate = articleEntry.getPostdate();
        String commentscount = articleEntry.getCommentcount();
        String images = articleEntry.getPreview();
        String userimages = articleEntry.getUserimage();
        String post_status = articleEntry.getPoststatus();
        String vidrscurl = articleEntry.getVidrscurl();

        //Set values
        holder.Postcomment.setText(postcomment);
        holder.PostArea.setText(postarea);
        holder.Username.setText(username);
        holder.PostDate.setText(postdate);
        holder.PostState.setText(state);
        holder.PostType.setText(posttype);
        holder.Comments.setText(commentscount);
        holder.userimage.setText(userimages);
        holder.previewimg.setText(images);
        holder.vidurl.setText(vidrscurl);
        holder.Postsatus.setText(post_status);
        holder.Likes.setText(liks);

        Glide.with(mContext)
                .load(images)
                .into(holder.image);
    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getItemCount() {
        if (mArticleEntries == null) {
            return 0;
        }
        return mArticleEntries.size();
    }

    public List<ArticleEntry> getClassifier() {
        return mArticleEntries;
    }


    public void setTasks(List<ArticleEntry> articleEntries) {
        mArticleEntries = articleEntries;
        notifyDataSetChanged();
    }

    // Inner class for creating ViewHolders
    public class ArticleViewHolder extends RecyclerView.ViewHolder {

        TextView Postcomment;
        TextView PostArea;
        TextView Username;
        TextView PostDate;
        TextView PostState;
        TextView PostType;
        TextView Comments;
        TextView userimage;
        TextView vidurl;
        TextView Likes;
        TextView Postsatus;
        TextView previewimg;
        ImageView image;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            Postcomment = itemView.findViewById(R.id.articleSnippet);
            PostArea = itemView.findViewById(R.id.post_area);
            Username = itemView.findViewById(R.id.user_name);
            PostDate = itemView.findViewById(R.id.post_date);
            PostState = itemView.findViewById(R.id.post_state);
            PostType = itemView.findViewById(R.id.post_type);
            Comments = itemView.findViewById(R.id.post_comments);
            Likes = itemView.findViewById(R.id.post_likes);
            userimage = itemView.findViewById(R.id.user_imageurl);
            vidurl = itemView.findViewById(R.id.vid_rscurl);
            Postsatus = itemView.findViewById(R.id.post_statusflag);
            previewimg = itemView.findViewById(R.id.post_img);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(v -> {
                String rsc_uri_checker = vidurl.getText().toString();
                String username = Username.getText().toString();
                String userimg = userimage.getText().toString();
                String postimg = previewimg.getText().toString();
                String posttype = PostType.getText().toString();
                String state = PostState.getText().toString();
                String postarea = PostArea.getText().toString();
                String date = PostDate.getText().toString();
                String comment = Postcomment.getText().toString();
                String rscurl = vidurl.getText().toString();
                String status = Postsatus.getText().toString();
                Intent intent;
                if (!rsc_uri_checker.equals("")){
                    intent = new Intent(mContext.getApplicationContext(), Hot_Video_post_player.class);
                    //Toast.makeText(mContext,"Post Clicked" + rsc_uri_checker, Toast.LENGTH_SHORT).show();
                }else{
                    intent = new Intent(mContext.getApplicationContext(), Hot_Image_post_viewer.class);
                }
                intent.putExtra("username_key", username);
                intent.putExtra("userimg_key", userimg);
                intent.putExtra("imgurl_key", postimg);
                intent.putExtra("town_key", posttype);
                intent.putExtra("state_key", state);
                intent.putExtra("lga_key", postarea);
                intent.putExtra("date_key", date);
                intent.putExtra("comment_key", comment);
                intent.putExtra("rscurl_key", rscurl);
                intent.putExtra("postsrc_key", status);
                v.getContext().startActivity(intent);

            });
        }

    }
}
