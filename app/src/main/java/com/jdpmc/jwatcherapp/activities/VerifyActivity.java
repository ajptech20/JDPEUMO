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
import com.jdpmc.jwatcherapp.activities.algon.VerifyArtisanDomestic;
import com.jdpmc.jwatcherapp.activities.algon.VerifyChild;
import com.jdpmc.jwatcherapp.activities.algon.VerifyMedical;
import com.jdpmc.jwatcherapp.adapter.VerifyAdapter;
import com.jdpmc.jwatcherapp.model.Verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyActivity extends AppCompatActivity  implements VerifyAdapter.ItemClickListener{
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
                R.drawable.national_id,
                R.drawable.fire_extinguisher_v,
                R.drawable.bus,
                R.drawable.security_personnel,
                R.drawable.vigilante,
                R.drawable.hunter,
                R.drawable.international_passport,
                R.drawable.criminal_history,
                R.drawable.park,
                R.drawable.labor,
                R.drawable.passenger_manifest,
                R.drawable.garage,
                R.drawable.electronic_address,
                R.drawable.vio,
                R.drawable.child,
                R.drawable.nurse,
        };

        Verify a = new Verify(1, covers[0], "National ID");
        verifyList.add(a);

        a = new Verify(2, covers[1], "Verify Fire Extinguisher");
        verifyList.add(a);

        a = new Verify(3, covers[2], "Verify Transporter ID");
        verifyList.add(a);

        a = new Verify(4, covers[3], "Verify Security Personnel ID");
        verifyList.add(a);

        a = new Verify(5, covers[4], "Verify Vigilante");
        verifyList.add(a);

        a = new Verify(6, covers[5], "Verify Hunter");
        verifyList.add(a);

        a = new Verify(7, covers[6], "Verify Int. Passport");
        verifyList.add(a);

        a = new Verify(8, covers[7], "Verify Conviction Status");
        verifyList.add(a);

        a = new Verify(9, covers[8], "Locate Motor Park");
        verifyList.add(a);

        a = new Verify(10, covers[9], "Artisans/Domestic Staff");
        verifyList.add(a);

        a = new Verify(11, covers[10], "Passenger Manifest");
        verifyList.add(a);

        a = new Verify(12, covers[11], "Agric SAT-C");
        verifyList.add(a);

        a = new Verify(13, covers[12], "Public Electronic Address");
        verifyList.add(a);

        a = new Verify(14, covers[13], "Vio Verification");
        verifyList.add(a);

        a = new Verify(15, covers[14], "Verify Child");
        verifyList.add(a);

        a = new Verify(16, covers[15], "Verify Medical Personnel");
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
            Intent intent = new Intent(this, VerifyNational.class);
            startActivity(intent);
        } else if (itemId == 2) {
            Intent intent = new Intent(this, VerifyExtinguisher.class);
            startActivity(intent);
        } else if (itemId == 3) {
            Intent intent = new Intent(this, VerifyTransporter.class);
            startActivity(intent);
        } else if (itemId == 4) {
            Intent intent = new Intent(this, VerifyPSID.class);
            startActivity(intent);
        } else if (itemId == 5) {
            Intent intent = new Intent(this, VerifySecurityCode.class);
            startActivity(intent);
        } else if (itemId == 6) {
            Intent intent = new Intent(this, VerifyHunter.class);
            startActivity(intent);
        } else if (itemId == 7) {

        } else if (itemId == 8) {

        } else if (itemId == 9) {

        } else if (itemId == 10) {
            Intent intent = new Intent(this, VerifyArtisanDomestic.class);
            startActivity(intent);
        } else if (itemId == 11) {

        } else if (itemId == 12) {
            Intent intent = new Intent(this, AgricSat.class);
            startActivity(intent);
        } else if (itemId == 13) {
            Intent intent = new Intent(this, VerifyAddress.class);
            startActivity(intent);
        } else if (itemId == 14) {
            Intent intent = new Intent(this, VerifyPlatenumber.class);
            startActivity(intent);
        } else if (itemId == 15) {
            Intent intent = new Intent(this, VerifyChild.class);
            startActivity(intent);
        }else if (itemId == 16) {
            Intent intent = new Intent(this, VerifyMedical.class);
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
                VerifyActivity.this.client = client;

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
