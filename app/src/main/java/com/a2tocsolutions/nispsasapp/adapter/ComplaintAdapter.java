package com.a2tocsolutions.nispsasapp.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.recyclerview.widget.RecyclerView;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.model.Complaint;
import com.bumptech.glide.Glide;

import java.util.List;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.MyViewHolder> {

    private Context mContext;
    private List<Complaint> complaintList;
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    private CustomTabsSession customTabsSession;
    private CustomTabsClient client;
    private CustomTabsServiceConnection connection;
    private boolean warmupWhenReady = false;
    private boolean mayLaunchWhenReady = false;
    final private ComplaintAdapter.ItemClickListener mItemClickListener;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title;
        public TextView sub;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            sub = view.findViewById(R.id.subtitle);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int elementId = complaintList.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }


    public ComplaintAdapter(Context mContext, List<Complaint> complains, ItemClickListener listener) {
        this.mContext = mContext;
        this.complaintList = complains;
        mItemClickListener = listener;
        bindCustomTabsService();
    }

    public void warmup() {
        if (client != null) {
            warmupWhenReady = false;
            client.warmup(0);
        }
        else {
            warmupWhenReady = true;
        }
    }

    public void mayLaunch(String url) {
        CustomTabsSession session = getSession();
        if (client != null) {
            mayLaunchWhenReady = false;
            session.mayLaunchUrl(Uri.parse(url), null, null);
        }
        else {
            mayLaunchWhenReady = true;
        }
    }

    private CustomTabsSession getSession() {
        if (client == null) {
            customTabsSession = null;
        } else if (customTabsSession == null) {
            customTabsSession = client.newSession(new CustomTabsCallback() {
                @Override
                public void onNavigationEvent(int navigationEvent, Bundle extras) {
                    Log.w("CustomTabs", "onNavigationEvent: Code = " + navigationEvent);
                }
            });
        }
        return customTabsSession;
    }

    private void bindCustomTabsService() {
        if (client != null) {
            return;
        }
        connection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName name, CustomTabsClient client) {
                ComplaintAdapter.this.client = client;

                if (warmupWhenReady) {
                    warmup();
                }

                if (mayLaunchWhenReady) {

                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                client = null;
            }
        };
        boolean ok = CustomTabsClient.bindCustomTabsService(mContext, CUSTOM_TAB_PACKAGE_NAME, connection);
        if (!ok) {
            connection = null;
        }
    }

    public void unbindCustomTabsService() {
        if (connection == null) {
            return;
        }
        mContext.unbindService(connection);
        client = null;
        customTabsSession = null;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.state_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Complaint complaint = complaintList.get(position);
        holder.title.setText(complaint.getName());
        holder.sub.setText(complaint.getSub());
        Glide.with(mContext).load(complaint.getThumbnail()).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return complaintList.size();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }
}
