package com.jdpmc.jwatcherapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jdpmc.jwatcherapp.R;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreActivity extends AppCompatActivity {

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_fragment);
        ButterKnife.bind(this);

        setupNavContent(navigationView);
    }

    private void setupNavContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectItem(menuItem);
                    return true;
                });
    }




    public void selectItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {

            case R.id.agency:
                Intent inten2 = new Intent(this, AgencyList.class);
                startActivity(inten2);
                break;
            case R.id.about:
                String uri = "https://nispsas.com.ng/about/";
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(uri));
                break;
            case R.id.termsandcontitions:
                String ure = "http://tocnetng.com.ng/pdf/Correctected%20terms%20and%20condition%20(1).pdf";
                CustomTabsIntent.Builder builde = new CustomTabsIntent.Builder();
                CustomTabsIntent customTabsIntens = builde.build();
                customTabsIntens.launchUrl(this, Uri.parse(ure));
                break;
            case R.id.contact:
                boolean wrapInScrollView = true;
                MaterialDialog dialog = new MaterialDialog.Builder(Objects.requireNonNull(this))
                        .title("Contact Us")
                        .customView(R.layout.contact_view, wrapInScrollView)
                        .positiveText("OK")
                        .onPositive((dialog1, which) -> {
                            dialog1.dismiss();
                        })
                        .show();
                View view = dialog.getCustomView();

                if (view != null) {
                    TextView phoneNumber = view.findViewById(R.id.phoneNumber);
                    TextView email = view.findViewById(R.id.email);
                    TextView website = view.findViewById(R.id.website);

                    phoneNumber.setText("08000NISPSAS");
                    email.setText("iosdeveloper@nispsas.com.ng");
                    website.setText("www.nispsas.com.ng");
                }

                break;
            default:
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
        }

        menuItem.setChecked(true);
    }

}
