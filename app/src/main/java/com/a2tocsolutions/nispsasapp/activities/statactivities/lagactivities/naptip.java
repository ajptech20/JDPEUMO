package com.a2tocsolutions.nispsasapp.activities.statactivities.lagactivities;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.a2tocsolutions.nispsasapp.networking.api.Service;
import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.adapter.ComplaintAdapter;
import com.a2tocsolutions.nispsasapp.model.Complaint;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class naptip extends AppCompatActivity  implements ComplaintAdapter.ItemClickListener{
    private List<Complaint> complaintList;
    private ComplaintAdapter adapter;
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    private CustomTabsSession customTabsSession;
    private CustomTabsClient client;
    private CustomTabsServiceConnection connection;
    private boolean warmupWhenReady = false;
    private boolean mayLaunchWhenReady = false;

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify);

        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setTitle("NAPTIP Reports");
        complaintList = new ArrayList<>();
        adapter = new ComplaintAdapter(this, complaintList, this);

        recyclerview.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(adapter);

        prepareComplaint();
    }

    private void prepareComplaint() {
        int[] covers = new int[]{
                R.drawable.child,
                R.drawable.factory,
                R.drawable.trafficker,
                R.drawable.abuse,
                R.drawable.regis
        };

        Complaint a = new Complaint(1, covers[0], "Report Child Labor");
        complaintList.add(a);

        a = new Complaint(2, covers[1], "Report Baby Factory");
        complaintList.add(a);

        a = new Complaint(3, covers[2], "Report Human Trafficker");
        complaintList.add(a);

        a = new Complaint(4, covers[3], "Report Sexual Violence");
        complaintList.add(a);

        a = new Complaint(5, covers[4], "Report Other Case");
        complaintList.add(a);


        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClickListener(int itemId) {
        if (itemId == 1) {
            Intent intent = new Intent(this, Stollenchild.class);
            startActivity(intent);
        } else if (itemId == 2) {
            Intent intent = new Intent(this, Childrenfactory.class);
            startActivity(intent);
        } else if (itemId == 3) {
            Intent intent = new Intent(this, Childtraffic.class);
            startActivity(intent);
        } else if (itemId == 4) {
            Intent intent = new Intent(this, Abuseabroad.class);
            startActivity(intent);
        } else if (itemId == 5) {
                Intent intent = new Intent(this, Othernaptip.class);
                startActivity(intent);
        }
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
                naptip.this.client = client;

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
        boolean ok = CustomTabsClient.bindCustomTabsService(this, CUSTOM_TAB_PACKAGE_NAME, connection);
        if (!ok) {
            connection = null;
        }
    }

    public void unbindCustomTabsService() {
        if (connection == null) {
            return;
        }
        unbindService(connection);
        client = null;
        customTabsSession = null;
    }
}