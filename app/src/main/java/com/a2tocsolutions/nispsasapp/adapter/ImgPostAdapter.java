package com.a2tocsolutions.nispsasapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.database.ImgPost;
import com.a2tocsolutions.nispsasapp.networking.ImgPostRequest;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

public class ImgPostAdapter extends RecyclerView.Adapter<ImgPostAdapter.ViewHolder> {

    //Imageloader to load image
    private ImageLoader imageLoader;
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
        imageLoader = ImgPostRequest.getInstance(context).getImageLoader();
        imageLoader.get(superHero.getImageUrl(), ImageLoader.getImageListener(holder.imageView, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));
        imageLoader.get(superHero.getImageUrl(), ImageLoader.getImageListener(holder.imageViewPreview, R.drawable.ic_launcher_background, android.R.drawable.ic_dialog_alert));

        //Showing data on the views
        holder.imageView.setImageUrl(superHero.getImageUrl(), imageLoader);
        holder.textViewName.setText(superHero.getName());
        //holder.textViewId.setText(superHero.getId());
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
        holder.imageViewPreview.setImageUrl(superHero.getPreview(), imageLoader);
        //holder.textViewRsuri.setText(superHero.getResUri());


    }

    @Override
    public int getItemCount() {
        return superHeroes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        //Views
        public NetworkImageView imageView;
        public TextView textViewName;
        public TextView textViewId;
        public TextView textViewimg;
        public TextView textViewDescription;

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
            imageView = (NetworkImageView) itemView.findViewById(R.id.reporter_image);

            imageViewPreview = (NetworkImageView) itemView.findViewById(R.id.post_image);

            textViewName = (TextView) itemView.findViewById(R.id.Poster_name);
            //textViewId = (TextView) itemView.findViewById(R.id.textViewId);
            //textViewimg = (TextView) itemView.findViewById(R.id.textViewName2);

            //textViewRepId = (TextView) itemView.findViewById(R.id.textViewName3);
            textViewState = (TextView) itemView.findViewById(R.id.post_state);
            //textViewStatus = (TextView) itemView.findViewById(R.id.textViewName5);
            textViewRType = (TextView) itemView.findViewById(R.id.post_type);
            textViewArea = (TextView) itemView.findViewById(R.id.set_area);
            textViewLikes = (TextView) itemView.findViewById(R.id.post_likes);
            textViewComments = (TextView) itemView.findViewById(R.id.post_comments);
            textViewDate = (TextView) itemView.findViewById(R.id.post_date);
            //textViewRsuri = (TextView) itemView.findViewById(R.id.textViewName10);

            textViewDescription = (TextView) itemView.findViewById(R.id.post_description);
        }
    }
}
