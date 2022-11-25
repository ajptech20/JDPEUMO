package com.a2tocsolutions.nispsasapp.activities.institutions;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.adapter.VerifyAdapter;
import com.a2tocsolutions.nispsasapp.model.Verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyNscdcActivity extends AppCompatActivity  implements VerifyAdapter.ItemClickListener{
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

        setTitle("NSCDC ID Verification");
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

                R.drawable.security_personnel,
                R.drawable.vigilante,
                R.drawable.hunter,
                R.drawable.passenger_manifest,


        };

        Verify a = new Verify(1, covers[0], "Verify Security Personnel ID");
        verifyList.add(a);

        a = new Verify(2, covers[1], "Verify Vigilante");
        verifyList.add(a);

        a = new Verify(3, covers[2], "Verify Hunter");
        verifyList.add(a);

        a = new Verify(4, covers[3], "Passenger Manifest");
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
            Intent intent = new Intent(this, FaceCodeActivity.class);
            startActivity(intent);
        } else if (itemId == 2) {
            Intent intent = new Intent(this, FaceCodeActivity.class);
            startActivity(intent);
        } else if (itemId == 3) {
            Intent intent = new Intent(this, FaceCodeActivity.class);
            startActivity(intent);
        } else if (itemId == 4) {
            Toast.makeText(getApplicationContext(), "Not Available Yet", Toast.LENGTH_SHORT).show();
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
                VerifyNscdcActivity.this.client = client;

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
