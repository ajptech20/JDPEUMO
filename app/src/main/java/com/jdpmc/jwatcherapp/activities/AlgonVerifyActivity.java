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
import com.jdpmc.jwatcherapp.activities.algon.VerifyAmbulance;
import com.jdpmc.jwatcherapp.activities.algon.VerifyArtisanDomestic;
import com.jdpmc.jwatcherapp.activities.algon.VerifyChild;
import com.jdpmc.jwatcherapp.activities.algon.VerifyDrainage;
import com.jdpmc.jwatcherapp.activities.algon.VerifyFamers;
import com.jdpmc.jwatcherapp.activities.algon.VerifyHospital;
import com.jdpmc.jwatcherapp.activities.algon.VerifyHouse;
import com.jdpmc.jwatcherapp.activities.algon.VerifyMedical;
import com.jdpmc.jwatcherapp.activities.algon.VerifySecurity;
import com.jdpmc.jwatcherapp.activities.algon.VerifyTrader;
import com.jdpmc.jwatcherapp.activities.algon.VerifyUnion;
import com.jdpmc.jwatcherapp.adapter.VerifyAdapter;
import com.jdpmc.jwatcherapp.model.Verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlgonVerifyActivity extends AppCompatActivity  implements VerifyAdapter.ItemClickListener{
    private List<Verify> verifyList;
    private VerifyAdapter adapter;
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("Verify");
        verifyList = new ArrayList<>();
        adapter = new VerifyAdapter(this, verifyList, this);

        recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(adapter);

        prepareVerify();
    }

    private void prepareVerify() {
        int[] covers = new int[]{
                R.drawable.labor,
                R.drawable.doctor,
                R.drawable.brt,
                R.drawable.vigilante,
                R.drawable.trader,
                R.drawable.farmer,
                R.drawable.child,
                R.drawable.house,
                R.drawable.hospital,
                R.drawable.ambulance,
                R.drawable.drainage,
        };

        Verify a = new Verify(1, covers[0], " Verify Artisans/Domestic Staff");
        verifyList.add(a);

        a = new Verify(2, covers[1], "Verify Medical Personnel");
        verifyList.add(a);

        a = new Verify(3, covers[2], "Verify Union Members");
        verifyList.add(a);

        a = new Verify(4, covers[3], "Verify Security Personnel");
        verifyList.add(a);

        a = new Verify(5, covers[4], "Verify Traders & Merchants");
        verifyList.add(a);

        a = new Verify(6, covers[5], "Verify Farmers");
        verifyList.add(a);

        a = new Verify(7, covers[6], "Verify Child");
        verifyList.add(a);

        a = new Verify(8, covers[7], "Verify Houses");
        verifyList.add(a);

        a = new Verify(9, covers[8], "Verify Hospital/Pharmacy");
        verifyList.add(a);

        a = new Verify(10, covers[9], "Verify Ambulance");
        verifyList.add(a);

        a = new Verify(11, covers[10], "Verify Drainage Staff ");
        verifyList.add(a);


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
            Intent intent = new Intent(this, VerifyArtisanDomestic.class);
            startActivity(intent);
        } else if (itemId == 2) {
            Intent intent = new Intent(this, VerifyMedical.class);
            startActivity(intent);
        } else if (itemId == 3) {
            Intent intent = new Intent(this, VerifyUnion.class);
            startActivity(intent);
        } else if (itemId == 4) {
            Intent intent = new Intent(this, VerifySecurity.class);
            startActivity(intent);
        } else if (itemId == 5) {
            Intent intent = new Intent(this, VerifyTrader.class);
            startActivity(intent);
        } else if (itemId == 6) {
            Intent intent = new Intent(this, VerifyFamers.class);
            startActivity(intent);
        } else if (itemId == 7) {
            Intent intent = new Intent(this, VerifyChild.class);
            startActivity(intent);
        } else if (itemId == 8) {
            Intent intent = new Intent(this, VerifyHouse.class);
            startActivity(intent);
        } else if (itemId == 9) {
            Intent intent = new Intent(this, VerifyHospital.class);
            startActivity(intent);
        } else if (itemId == 10) {
            Intent intent = new Intent(this, VerifyAmbulance.class);
            startActivity(intent);
        } else if (itemId == 11) {
            Intent intent = new Intent(this, VerifyDrainage.class);
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
                AlgonVerifyActivity.this.client = client;

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
