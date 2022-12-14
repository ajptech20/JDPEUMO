package com.jdpmc.jwatcherapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.recyclerview.widget.RecyclerView;

import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.activities.CommerceActivity;
import com.jdpmc.jwatcherapp.activities.ComplaintActivity;
import com.jdpmc.jwatcherapp.activities.InstitutionsActivity;
import com.jdpmc.jwatcherapp.model.MoreData;
import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MoreDataAdapter extends RecyclerView.Adapter<MoreDataAdapter.MyViewHolder> {

    private Context mContext;
    private List<MoreData> moreDataList;
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    private CustomTabsSession customTabsSession;
    private CustomTabsClient client;
    private CustomTabsServiceConnection connection;
    private boolean warmupWhenReady = false;
    private boolean mayLaunchWhenReady = false;

    public interface ItemClickListener {
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView subtitle;
        public CircleImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            subtitle = (TextView) view.findViewById(R.id.subtitle);
            thumbnail = (CircleImageView) view.findViewById(R.id.thumbnail);

            view.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION){
                    MoreData clickedDataItem = moreDataList.get(pos);
                    int id = clickedDataItem.getId();

                    if (id == 1) {
                      //  Intent verifyOntent = new Intent(mContext, AlgonActivity.class);
                      //  mContext.startActivity(verifyOntent);
                    } else if (id == 2) {
                        Intent verifyOntent = new Intent(mContext, ComplaintActivity.class);
                        mContext.startActivity(verifyOntent);
                    } else if (id == 3) {
                        Intent verifyOntent = new Intent(mContext, CommerceActivity.class);
                        mContext.startActivity(verifyOntent);
                    } else if (id == 4) {
                       Intent verifyOntent = new Intent(mContext, InstitutionsActivity.class);
                        mContext.startActivity(verifyOntent);
                    }
                }
            });
        }
    }


    public MoreDataAdapter(Context mContext, List<MoreData> moreData) {
        this.mContext = mContext;
        this.moreDataList = moreData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.other_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        MoreData moreData = moreDataList.get(position);
        holder.title.setText(moreData.getName());
        holder.subtitle.setText(moreData.getSub());

        // loading album cover using Glide library
        Glide.with(mContext).load(moreData.getThumbnail()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return moreDataList.size();
    }
}
