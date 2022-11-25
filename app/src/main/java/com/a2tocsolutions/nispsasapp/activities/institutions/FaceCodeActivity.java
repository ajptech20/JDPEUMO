package com.a2tocsolutions.nispsasapp.activities.institutions;

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

import com.a2tocsolutions.nispsasapp.R;
import com.a2tocsolutions.nispsasapp.activities.VerifySecurityCode;
import com.a2tocsolutions.nispsasapp.activities.VerifySecurityFace;
import com.a2tocsolutions.nispsasapp.adapter.VerifyAdapter;
import com.a2tocsolutions.nispsasapp.model.Verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FaceCodeActivity extends AppCompatActivity  implements VerifyAdapter.ItemClickListener{
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

        setTitle("Use Face or Service Code");
        verifyList = new ArrayList<>();
        adapter = new VerifyAdapter(this, verifyList, this);

        recyclerview.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);
        recyclerview.setAdapter(adapter);

        prepareVerify();
    }

    private void prepareVerify() {
        int[] covers = new int[]{

                R.drawable.facescan,
                R.drawable.qrscan,


        };

        Verify a = new Verify(1, covers[0], "Verify Using Face Verification");
        verifyList.add(a);

        a = new Verify(2, covers[1], "Verify By Service Code");
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
            Intent intent = new Intent(this, VerifySecurityFace.class);
            startActivity(intent);
        } else if (itemId == 2) {
            Intent intent = new Intent(this, VerifySecurityCode.class);
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
                FaceCodeActivity.this.client = client;

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
