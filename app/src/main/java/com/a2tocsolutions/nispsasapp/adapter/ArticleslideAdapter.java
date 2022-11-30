package com.a2tocsolutions.nispsasapp.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.RecyclerView;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.database.ArticleEntry;
import com.bumptech.glide.Glide;

import java.util.List;

public class ArticleslideAdapter extends RecyclerView.Adapter<ArticleslideAdapter.ArticleViewHolder> {

    // Member variable to handle item clicks
    private List<ArticleEntry> mArticleEntries;
    private Context mContext;

    public ArticleslideAdapter(Context context) {
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
        String image_url = "https://nispsas.com.ng/NISPSAS/Postpics/";
        // Determine the values of the wanted data
        ArticleEntry articleEntry = mArticleEntries.get(position);
        String name = articleEntry.getPosttitle();
        String images = articleEntry.getPicname();

        //Set values
        holder.articleSnippet.setText(name);
        Glide.with(mContext)
                .load(image_url + images)
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

        TextView articleSnippet;
        ImageView image;

        public ArticleViewHolder(View itemView) {
            super(itemView);

            articleSnippet = itemView.findViewById(R.id.articleSnippet);
            image = itemView.findViewById(R.id.image);

            itemView.setOnClickListener(v -> {

                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    ArticleEntry clickedDataItem = mArticleEntries.get(pos);
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    String link = "https://nispsas.com.ng/blog/Safetyblog/ViewPost/" + clickedDataItem.getPicname();
                    builder.setStartAnimations(mContext, R.anim.slide_in_right, R.anim.slide_out_left);
                    builder.setExitAnimations(mContext, R.anim.slide_in_left, R.anim.slide_out_right);
                    builder.setToolbarColor(mContext.getResources().getColor(R.color.colorPrimary));
                    builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                            mContext.getResources(), R.drawable.ic_arrow_back_black_24dp));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl(mContext, Uri.parse(link));
                }

            });
        }

    }
}
