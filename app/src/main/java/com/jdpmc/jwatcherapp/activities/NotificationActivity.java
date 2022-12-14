package com.jdpmc.jwatcherapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jdpmc.jwatcherapp.R;

import java.util.Locale;

import butterknife.ButterKnife;

public class NotificationActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private static final String TAG = "NotificationActivity";
    public static final  String TITLES = "title";
    public static final String SENDA="sender";
    public static final  String BODYS = "body";
    TextToSpeech t1;
    private String mtitle, msenda, mbody;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_marker_view);

        ButterKnife.bind(this);

        t1 = new TextToSpeech(this, this);

        ButterKnife.bind(this);

        setTitle("SAFETY TIPS FOR TODAY");

        Intent intent = getIntent();
        if(intent.getExtras() != null && intent.getExtras().containsKey("icon")) {

            mtitle = intent.getExtras().getString("title");
            mbody = intent.getExtras().getString("body");
            msenda = intent.getExtras().getString("sender");

        }else {
            mtitle = intent.getExtras().getString(TITLES);
            mbody = intent.getExtras().getString(BODYS);
            msenda= intent.getExtras().getString(SENDA);

        }
            TextView a1 = findViewById(R.id.tvContent);
            TextView a2 = findViewById(R.id.tvContent1);
            TextView a3 = findViewById(R.id.tvContent2);

            a1.setText(msenda);
            a2.setText(mtitle);
            a3.setText(mbody);

            showDialog(mtitle, mbody);



    }




    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            t1.setLanguage(Locale.US);
            Log.i(TAG, msenda + ". " + mtitle + ". " + mbody );

            String toSpeak = msenda + ". " + mtitle + ". " + mbody;
            t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH, null);

        }


    }

    @Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
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


    private void showDialog(String title, String body) {


     /*     String toSpeaka = "Now I Read me " + sender;
        t1.speak(toSpeaka, TextToSpeech.QUEUE_FLUSH, null);
      boolean wrapInScrollView = true;
        MaterialDialog dialog = new MaterialDialog.Builder(this)

                .title("Verified Response")
                .customView(R.layout.driver_res_view, wrapInScrollView)
                .positiveText("OK")
                .onPositive((dialog1, which) -> {
                    dialog1.dismiss();
                })
                .show();
        View view = dialog.getCustomView();

        if (view != null) {
            ImageView imageView = (ImageView) view.findViewById(R.id.image);
            TextView verifiedResponse = view.findViewById(R.id.verifiedResponse);
            imageView.setVisibility(View.GONE);

            verifiedResponse.setText(title);
            verifiedResponse.setText(body);
            verifiedResponse.setText(sender);


        String Speaka = "Now I Reading " + body;
        t1.speak(Speaka, TextToSpeech.QUEUE_FLUSH, null); }*/


}





}
