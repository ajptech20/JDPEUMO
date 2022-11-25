package com.a2tocsolutions.nispsasapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.a2tocsolutions.nispsasapp.R;
import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class ArticleslideAdapter extends SliderViewAdapter<ArticleslideAdapter.SliderAdapterVH> {

    private Context context;
    private int mCount;

    public ArticleslideAdapter(Context context) {
        this.context = context;
    }


    public void setCount(int count) {
        this.mCount = count;
    }
    @NonNull
    @Override
    public SliderAdapterVH onCreateViewHolder(@NonNull ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slide, null);

        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderAdapterVH viewHolder, int position) {

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Toast.makeText(context, "This is item in position " + position, Toast.LENGTH_SHORT).show();

            }

        });

        switch (position) {

            case 0:

                viewHolder.textViewDescription.setText("This is slider item " + position);

                viewHolder.textViewDescription.setTextSize(16);

                viewHolder.textViewDescription.setTextColor(Color.GREEN);

                viewHolder.imageGifContainer.setVisibility(View.GONE);

                Glide.with(viewHolder.itemView)

                        .load("https://images.pexels.com/photos/218983/pexels-photo-218983.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")

                        .fitCenter()

                        .into(viewHolder.imageViewBackground);

                break;

            case 2:

                viewHolder.textViewDescription.setText("This is slider item " + position);

                viewHolder.textViewDescription.setTextSize(16);

                viewHolder.textViewDescription.setTextColor(Color.GREEN);

                viewHolder.imageGifContainer.setVisibility(View.GONE);

                Glide.with(viewHolder.itemView)

                        .load("https://images.pexels.com/photos/747964/pexels-photo-747964.jpeg?auto=compress&cs=tinysrgb&h=750&w=1260")

                        .fitCenter()

                        .into(viewHolder.imageViewBackground);

                break;

            case 4:

                viewHolder.textViewDescription.setText("This is slider item " + position);

                viewHolder.textViewDescription.setTextSize(16);

                viewHolder.textViewDescription.setTextColor(Color.GREEN);

                viewHolder.imageGifContainer.setVisibility(View.GONE);

                Glide.with(viewHolder.itemView)

                        .load("https://images.pexels.com/photos/929778/pexels-photo-929778.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260")

                        .fitCenter()

                        .into(viewHolder.imageViewBackground);

                break;
            case 5:

                viewHolder.textViewDescription.setText("This is slider item " + position);

                viewHolder.textViewDescription.setTextSize(16);

                viewHolder.textViewDescription.setTextColor(Color.GREEN);

                viewHolder.imageGifContainer.setVisibility(View.GONE);

                Glide.with(viewHolder.itemView)

                        .load(R.drawable.lagos)

                        .fitCenter()

                        .into(viewHolder.imageViewBackground);


                break;

            default:

                viewHolder.textViewDescription.setTextSize(29);

                viewHolder.textViewDescription.setTextColor(Color.GREEN);

                viewHolder.textViewDescription.setText("Ohhhh! look at this!");

                viewHolder.imageGifContainer.setVisibility(View.VISIBLE);

                Glide.with(viewHolder.itemView)

                        .load(R.drawable.crossriver)

                        .fitCenter()

                        .into(viewHolder.imageViewBackground);

                Glide.with(viewHolder.itemView)

                        .asGif()

                        .load(R.drawable.coat_of_arm)

                        .into(viewHolder.imageGifContainer);

                break;



        }



    }

    /**
     * Returns the number of items to display.
     */
    @Override
    public int getCount() {
       return mCount;
    }



    // Inner class for creating ViewHolders
    public class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        public View itemView;
        public ImageView imageViewBackground;
        public ImageView imageGifContainer;
        TextView textViewDescription;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);

            imageGifContainer = itemView.findViewById(R.id.iv_gif_container);

            textViewDescription = itemView.findViewById(R.id.tv_auto_image_slider);

            this.itemView = itemView;

            }
        }

    }



