package com.a2tocsolutions.nispsasapp.activities;

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
import com.a2tocsolutions.nispsasapp.activities.statactivities.Abia;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Adamawa;
import com.a2tocsolutions.nispsasapp.activities.statactivities.AkwaIbom;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Anambra;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Bauchi;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Bayelsa;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Benue;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Borno;
import com.a2tocsolutions.nispsasapp.activities.statactivities.CrossRiver;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Delta;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Ebonyi;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Edo;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Ekiti;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Enugu;
import com.a2tocsolutions.nispsasapp.activities.statactivities.FCT;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Gombe;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Imo;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Jigawa;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Kaduna;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Kano;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Katsina;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Kebbi;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Kogi;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Kwara;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Lagos;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Nasarawa;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Niger;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Ogun;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Ondo;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Osun;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Oyo;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Plateau;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Rivers;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Sokoto;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Taraba;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Yobe;
import com.a2tocsolutions.nispsasapp.activities.statactivities.Zamfara;
import com.a2tocsolutions.nispsasapp.adapter.ComplaintAdapter;
import com.a2tocsolutions.nispsasapp.model.Complaint;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StateComplaint extends AppCompatActivity  implements ComplaintAdapter.ItemClickListener{
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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        setTitle("State Complaints");
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
                R.drawable.abia,
                R.drawable.adamawa,
                R.drawable.akwaibom,
                R.drawable.anambra,
                R.drawable.bauchi,
                R.drawable.bayelsa,
                R.drawable.benue,
                R.drawable.borno,
                R.drawable.crossriver,
                R.drawable.delta,
                R.drawable.ebony,
                R.drawable.enugu,
                R.drawable.edo,
                R.drawable.ekiti,
                R.drawable.fct,
                R.drawable.gombe,
                R.drawable.imo,
                R.drawable.jigawa,
                R.drawable.kaduna,
                R.drawable.kano,
                R.drawable.katsina,
                R.drawable.kebbi,
                R.drawable.kogi,
                R.drawable.kwara,
                R.drawable.lagos,
                R.drawable.nasarawa,
                R.drawable.niger,
                R.drawable.ogun,
                R.drawable.ondo,
                R.drawable.osun,
                R.drawable.oyo,
                R.drawable.plateau,
                R.drawable.rivers,
                R.drawable.sokoto,
                R.drawable.taraba,
                R.drawable.yobe,
                R.drawable.zamfara
        };

        Complaint a = new Complaint(1, covers[0], "Abia State");
        complaintList.add(a);

        a = new Complaint(2, covers[1], "Adamawa State");
        complaintList.add(a);

        a = new Complaint(3, covers[2], "Akwa-Ibom State");
        complaintList.add(a);

        a = new Complaint(4, covers[3], "Anambra State");
        complaintList.add(a);

        a = new Complaint(5, covers[4], "Bauchi State");
        complaintList.add(a);

        a = new Complaint(6, covers[5], "Bayelsa State");
        complaintList.add(a);

        a = new Complaint(7, covers[6], "Benue State");
        complaintList.add(a);

        a = new Complaint(8, covers[7], "Borno State");
        complaintList.add(a);

        a = new Complaint(9, covers[8], "Cross River State");
        complaintList.add(a);

        a = new Complaint(10, covers[9], "Delta State");
        complaintList.add(a);

        a = new Complaint(11, covers[10], "Ebonyi State");
        complaintList.add(a);

        a = new Complaint(12, covers[11], "Enugu State");
        complaintList.add(a);

        a = new Complaint(13, covers[12], "Edo State");
        complaintList.add(a);

        a = new Complaint(14, covers[13], "Ekiti State");
        complaintList.add(a);

        a = new Complaint(15, covers[14], "FCT");
        complaintList.add(a);

        a = new Complaint(16, covers[15], "Gombe State");
        complaintList.add(a);

        a = new Complaint(17, covers[16], "Imo State");
        complaintList.add(a);

        a = new Complaint(18, covers[17], "Jigawa State");
        complaintList.add(a);

        a = new Complaint(19, covers[18], "Kaduna State");
        complaintList.add(a);

        a = new Complaint(20, covers[19], "Kano State");
        complaintList.add(a);

        a = new Complaint(21, covers[20], "Katsina State");
        complaintList.add(a);

        a = new Complaint(22, covers[21], "Kebbi State");
        complaintList.add(a);

        a = new Complaint(23, covers[22], "Kogi State");
        complaintList.add(a);

        a = new Complaint(24, covers[23], "Kwara State");
        complaintList.add(a);

        a = new Complaint(25, covers[24], "Lagos State");
        complaintList.add(a);

        a = new Complaint(26, covers[25], "Nasarawa State");
        complaintList.add(a);

        a = new Complaint(27, covers[26], "Niger State");
        complaintList.add(a);

        a = new Complaint(28, covers[27], "Ogun State");
        complaintList.add(a);

        a = new Complaint(29, covers[28], "Ondo State");
        complaintList.add(a);

        a = new Complaint(30, covers[29], "Osun State");
        complaintList.add(a);

        a = new Complaint(31, covers[30], "Oyo State");
        complaintList.add(a);

        a = new Complaint(32, covers[31], "Plateau State");
        complaintList.add(a);

        a = new Complaint(33, covers[32], "Rivers State");
        complaintList.add(a);

        a = new Complaint(34, covers[33], "Sokoto State");
        complaintList.add(a);

        a = new Complaint(35, covers[34], "Taraba State");
        complaintList.add(a);

        a = new Complaint(36, covers[35], "Yobe State");
        complaintList.add(a);

        a = new Complaint(37, covers[36], "Zamfara State");
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
            Intent intent = new Intent(this, Abia.class);
            startActivity(intent);
        } else if (itemId == 2) {
            Intent intent = new Intent(this, Adamawa.class);
            startActivity(intent);
        } else if (itemId == 3) {
            Intent intent = new Intent(this, AkwaIbom.class);
            startActivity(intent);
        } else if (itemId == 4) {
            Intent intent = new Intent(this, Anambra.class);
            startActivity(intent);
        } else if (itemId == 5) {
            Intent intent = new Intent(this, Bauchi.class);
            startActivity(intent);
        } else if (itemId == 6) {
            Intent intent = new Intent(this, Bayelsa.class);
            startActivity(intent);
        } else if (itemId == 7) {
            Intent intent = new Intent(this, Benue.class);
            startActivity(intent);
        } else if (itemId == 8) {
            Intent intent = new Intent(this, Borno.class);
            startActivity(intent);
        } else if (itemId == 9) {
            Intent intent = new Intent(this, CrossRiver.class);
            startActivity(intent);
        } else if (itemId == 10) {
            Intent intent = new Intent(this, Delta.class);
            startActivity(intent);
        } else if (itemId == 11) {
            Intent intent = new Intent(this, Ebonyi.class);
            startActivity(intent);
        } else if (itemId == 12) {
            Intent intent = new Intent(this, Enugu.class);
            startActivity(intent);
        } else if (itemId == 13) {
            Intent intent = new Intent(this, Edo.class);
            startActivity(intent);
        } else if (itemId == 14) {
            Intent intent = new Intent(this, Ekiti.class);
            startActivity(intent);
        } else if (itemId == 15) {
            Intent intent = new Intent(this, FCT.class);
            startActivity(intent);
        } else if (itemId == 16) {
            Intent intent = new Intent(this, Gombe.class);
            startActivity(intent);
        } else if (itemId == 17) {
            Intent intent = new Intent(this, Imo.class);
            startActivity(intent);
        } else if (itemId == 18) {
            Intent intent = new Intent(this, Jigawa.class);
            startActivity(intent);
        } else if (itemId == 19) {
            Intent intent = new Intent(this, Kaduna.class);
            startActivity(intent);
        } else if (itemId == 20) {
            Intent intent = new Intent(this, Kano.class);
            startActivity(intent);
        } else if (itemId == 21) {
            Intent intent = new Intent(this, Katsina.class);
            startActivity(intent);
        } else if (itemId == 22) {
            Intent intent = new Intent(this, Kebbi.class);
            startActivity(intent);
        } else if (itemId == 23) {
            Intent intent = new Intent(this, Kogi.class);
            startActivity(intent);
        } else if (itemId == 24) {
            Intent intent = new Intent(this, Kwara.class);
            startActivity(intent);
        } else if (itemId == 25) {
            Intent intent = new Intent(this, Lagos.class);
            startActivity(intent);
        } else if (itemId == 26) {
            Intent intent = new Intent(this, Nasarawa.class);
            startActivity(intent);
        } else if (itemId == 27) {
            Intent intent = new Intent(this, Niger.class);
            startActivity(intent);
        } else if (itemId == 28) {
            Intent intent = new Intent(this, Ogun.class);
            startActivity(intent);
        } else if (itemId == 29) {
            Intent intent = new Intent(this, Ondo.class);
            startActivity(intent);
        } else if (itemId == 30) {
            Intent intent = new Intent(this, Osun.class);
            startActivity(intent);
        } else if (itemId == 31) {
            Intent intent = new Intent(this, Oyo.class);
            startActivity(intent);
        } else if (itemId == 32) {
            Intent intent = new Intent(this, Plateau.class);
            startActivity(intent);
        } else if (itemId == 33) {
            Intent intent = new Intent(this, Rivers.class);
            startActivity(intent);
        } else if (itemId == 34) {
            Intent intent = new Intent(this, Sokoto.class);
            startActivity(intent);
        } else if (itemId == 35) {
            Intent intent = new Intent(this, Taraba.class);
            startActivity(intent);
        } else if (itemId == 36) {
            Intent intent = new Intent(this, Yobe.class);
            startActivity(intent);
        } else if (itemId == 37) {
            Intent intent = new Intent(this, Zamfara.class);
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
                StateComplaint.this.client = client;

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
