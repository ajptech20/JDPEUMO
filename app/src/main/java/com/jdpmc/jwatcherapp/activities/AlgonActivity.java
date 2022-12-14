package com.jdpmc.jwatcherapp.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.adapter.ComplaintAdapter;
import com.jdpmc.jwatcherapp.model.Complaint;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jdpmc.jwatcherapp.utils.Constants.ALL_REGISTRATION;

public class AlgonActivity extends AppCompatActivity  implements ComplaintAdapter.ItemClickListener{
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


        setTitle("ALGON Content");
        complaintList = new ArrayList<>();
        adapter = new ComplaintAdapter(this, complaintList, this);

        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(adapter);

        prepareComplaint();
    }

    private void prepareComplaint() {
        int[] covers = new int[]{
                R.drawable.check,
                R.drawable.regis,

        };

        Complaint a = new Complaint(1, covers[0], "Verifications");
        complaintList.add(a);

        a = new Complaint(2, covers[1], "Registrations");
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
            Intent intent = new Intent(this, AlgonVerifyActivity.class);
            startActivity(intent);
        } else if (itemId == 2) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
            builder.setToolbarColor(this.getResources().getColor(R.color.colorPrimary));
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(
                    getApplicationContext().getResources(), R.drawable.ic_arrow_back_black_24dp));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(ALL_REGISTRATION));
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
                AlgonActivity.this.client = client;

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
