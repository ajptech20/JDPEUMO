package com.jdpmc.jwatcherapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class pdf_Viewer_Activity extends Activity {
    PDFView pdfView;
    String pdfurl = "https://j-watcher.org/Apps/UsefulFiles/643fd79efe27a5a38945b01003237b81.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView txtMarquee;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdfrsc_viewer);
        txtMarquee = (TextView) findViewById(R.id.pdf_describ);
        txtMarquee.setSelected(true);
        Intent intent = getIntent();
        String title = intent.getStringExtra("srctitle_key");
        String description = intent.getStringExtra("description_key");
        String fileurl = intent.getStringExtra("fileurl_key");
        String previewimg = intent.getStringExtra("prevurl_key");
        String imgfileurl = intent.getStringExtra("imgurl_key");
        String authour = intent.getStringExtra("author_key");
        String date = intent.getStringExtra("date_key");
        String type = intent.getStringExtra("type_key");
        String vidurl = intent.getStringExtra("vidurl_key");
        pdfView = findViewById(R.id.idPDFView);
        new RetrievePDFfromUrl().execute(fileurl);
        TextView titletext = findViewById(R.id.pdf_title);
        titletext.setText(title);
        TextView RepSource = findViewById(R.id.usersc_author);
        RepSource.setText(authour);
        TextView ReporComment = findViewById(R.id.pdf_describ);
        ReporComment.setText(description);
        ImageView imageView = (ImageView) findViewById(R.id.rsc_image);
        Glide.with(pdf_Viewer_Activity.this)
                .load(previewimg)
                .into(imageView);

        ImageView close = findViewById(R.id.stop_close_live3);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    class RetrievePDFfromUrl extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;
            startProgress();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new Handler().postDelayed(pdf_Viewer_Activity.this::endProgress, 3000);
                        }
                    });
                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();

        }

    }
    private void startProgress(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout prog = findViewById(R.id.preload_pdf);
                prog.setVisibility(View.VISIBLE);
            }
        });
    }

    private void endProgress() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RelativeLayout prog = findViewById(R.id.preload_pdf);
                prog.setVisibility(View.GONE);
            }
        });
    }
}
