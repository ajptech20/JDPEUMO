package com.jdpmc.jwatcherapp.activities;

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

import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.activities.institutions.CorrectionActivity;
import com.jdpmc.jwatcherapp.activities.institutions.EduActivity;
import com.jdpmc.jwatcherapp.activities.institutions.FfsActivity;
import com.jdpmc.jwatcherapp.activities.institutions.ImmigrationActivity;
import com.jdpmc.jwatcherapp.activities.institutions.MotActivity;
import com.jdpmc.jwatcherapp.activities.institutions.NimcActivity;
import com.jdpmc.jwatcherapp.activities.institutions.NscdcActivity;
import com.jdpmc.jwatcherapp.activities.institutions.PoliceActivity;
import com.jdpmc.jwatcherapp.activities.institutions.VerifyHajj;
import com.jdpmc.jwatcherapp.activities.statactivities.lagactivities.naptip;
import com.jdpmc.jwatcherapp.adapter.ComplaintAdapter;
import com.jdpmc.jwatcherapp.model.Complaint;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InstitutionsActivity extends AppCompatActivity  implements ComplaintAdapter.ItemClickListener{
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


        setTitle("Other Institutions");
        complaintList = new ArrayList<>();
        adapter = new ComplaintAdapter(this, complaintList, this);

        recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(adapter);

        prepareComplaint();
    }

    private void prepareComplaint() {
        int[] covers = new int[]{
                R.drawable.coat_of_arm,
                R.drawable.coat_of_arm,
                R.drawable.nigeria_police,
                R.drawable.nscdc,
                R.drawable.fire_service,
                R.drawable.prison_service,
                R.drawable.immigrations,
                R.drawable.nimc,
                R.drawable.naptip,
                R.drawable.nahcon,
                R.drawable.ndic,

        };

        Complaint a = new Complaint(1, covers[0], "MOT");
        complaintList.add(a);

        a = new Complaint(2, covers[1], "Ministry of Education");
        complaintList.add(a);

        a = new Complaint(3, covers[2], "POLICE FORCE");
        complaintList.add(a);

        a = new Complaint(4, covers[3], "NSCDC");
        complaintList.add(a);

        a = new Complaint(5, covers[4], "FED FIRE SERVICE");
        complaintList.add(a);

        a = new Complaint(6, covers[5], "CORRECTIONAL SERVICE");
        complaintList.add(a);

        a = new Complaint(7, covers[6], "IMMIGRATION SERVICE");
        complaintList.add(a);

        a = new Complaint(8, covers[7], "NIMC");
        complaintList.add(a);

        a = new Complaint(9, covers[8], "NAPTIP Complaints");
        complaintList.add(a);

        a = new Complaint(10, covers[9], "NAHCON Verification");
        complaintList.add(a);

        a = new Complaint(11, covers[10], "NDIC Verification");
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
            Intent intent = new Intent(this, MotActivity.class);
            startActivity(intent);
        } else if (itemId == 2) {
            Intent intent = new Intent(this, EduActivity.class);
            startActivity(intent);
        } else if (itemId == 3) {
            Intent intent = new Intent(this, PoliceActivity.class);
            startActivity(intent);
        }else if (itemId == 4) {
            Intent intent = new Intent(this, NscdcActivity.class);
            startActivity(intent);
        }else if (itemId == 5) {
            Intent intent = new Intent(this, FfsActivity.class);
            startActivity(intent);
        }else if (itemId == 6) {
            Intent intent = new Intent(this, CorrectionActivity.class);
            startActivity(intent);
        }else if (itemId == 7) {
            Intent intent = new Intent(this, ImmigrationActivity.class);
            startActivity(intent);
        }else if (itemId == 8) {
            Intent intent = new Intent(this, NimcActivity.class);
            startActivity(intent);
        }else if (itemId == 9) {
            Intent intent = new Intent(this, naptip.class);
            startActivity(intent);
        }else if (itemId == 10) {
            Intent intent = new Intent(this, VerifyHajj.class);
            startActivity(intent);
        }else if (itemId == 11) {
            Intent intent = new Intent(this, VerifyNdic.class);
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
                InstitutionsActivity.this.client = client;

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
