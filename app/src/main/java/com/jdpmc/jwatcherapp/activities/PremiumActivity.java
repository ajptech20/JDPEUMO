package com.jdpmc.jwatcherapp.activities;

import android.app.Activity;
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

import butterknife.ButterKnife;

import com.jdpmc.jwatcherapp.R;
import com.jdpmc.jwatcherapp.adapter.ComplaintAdapter;
import com.jdpmc.jwatcherapp.model.Complaint;
import java.util.ArrayList;
import java.util.List;

public class PremiumActivity<Private> extends AppCompatActivity implements ComplaintAdapter.ItemClickListener {
    public static final String CUSTOM_TAB_PACKAGE_NAME = "com.android.chrome";
    private static final String Smokcomm = "https://nispsas.com.ng/Fasm";
    private static final String Viptas = "https://nispsas.com.ng/VIPTAS";
    private ComplaintAdapter adapter;
    /* access modifiers changed from: private */
    public CustomTabsClient client;
    private List<Complaint> complaintList;
    private CustomTabsServiceConnection connection;
    private CustomTabsSession customTabsSession;
    /* access modifiers changed from: private */
    public boolean mayLaunchWhenReady = false;
    RecyclerView recyclerview;
    /* access modifiers changed from: private */
    public boolean warmupWhenReady = false;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.verify);
        ButterKnife.bind((Activity) this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setTitle("Premium Portal");
        ArrayList arrayList = new ArrayList();
        this.complaintList = arrayList;
        this.adapter = new ComplaintAdapter(this, arrayList, this);
        this.recyclerview.setLayoutManager(new GridLayoutManager(this, 3));
        this.recyclerview.setItemAnimator(new DefaultItemAnimator());
        this.recyclerview.setHasFixedSize(true);
        this.recyclerview.setAdapter(this.adapter);
        prepareComplaint();
    }

    private void prepareComplaint() {
        int[] iArr = {R.drawable.fasm, R.drawable.vip};
        this.complaintList.add(new Complaint(1, iArr[0], "Fire Alert System Monitoring"));
        this.complaintList.add(new Complaint(2, iArr[1], "VIPTAS"));
        this.adapter.notifyDataSetChanged();
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        }
        onBackPressed();
        return true;
    }

    public void onItemClickListener(int i) {
        if (i == 1) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            builder.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
            builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_arrow_back_black_24dp));
            builder.build().launchUrl(this, Uri.parse(Smokcomm));
        } else if (i == 2) {
            CustomTabsIntent.Builder builder2 = new CustomTabsIntent.Builder();
            builder2.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
            builder2.setExitAnimations(this, R.anim.slide_in_left, R.anim.slide_out_right);
            builder2.setToolbarColor(getResources().getColor(R.color.colorPrimary));
            builder2.setCloseButtonIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_arrow_back_black_24dp));
            builder2.build().launchUrl(this, Uri.parse(Viptas));
        }
    }

    public void warmup() {
        CustomTabsClient customTabsClient = this.client;
        if (customTabsClient != null) {
            this.warmupWhenReady = false;
            customTabsClient.warmup(0);
            return;
        }
        this.warmupWhenReady = true;
    }

    public void mayLaunch(String str) {
        CustomTabsSession session = getSession();
        if (this.client != null) {
            this.mayLaunchWhenReady = false;
            session.mayLaunchUrl(Uri.parse(str), (Bundle) null, (List<Bundle>) null);
            return;
        }
        this.mayLaunchWhenReady = true;
    }

    private CustomTabsSession getSession() {
        CustomTabsClient customTabsClient = this.client;
        if (customTabsClient == null) {
            this.customTabsSession = null;
        } else if (this.customTabsSession == null) {
            this.customTabsSession = customTabsClient.newSession(new CustomTabsCallback() {
                public void onNavigationEvent(int i, Bundle bundle) {
                    Log.w("CustomTabs", "onNavigationEvent: Code = " + i);
                }
            });
        }
        return this.customTabsSession;
    }


}
