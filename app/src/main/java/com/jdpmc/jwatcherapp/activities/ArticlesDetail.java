package com.jdpmc.jwatcherapp.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jdpmc.jwatcherapp.R;
import com.bumptech.glide.Glide;

import java.lang.reflect.Method;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesDetail extends AppCompatActivity {
    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.image)
    ImageView image;

    @BindView(R.id.fullText)
    TextView fullText;

    private WebView mWebView;
    private boolean mIsPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_detail);

        ButterKnife.bind(this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());

        WebSettings ws = webView.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);

        mIsPaused = true;
        resumeBrowser();
        String image_url = "http://104.131.77.176/NISPSAS/Postpics/";

        Intent intent = getIntent();
        if (intent.hasExtra("articletype")) {
            String picname = intent.getExtras().getString("picname");
            String posttitle = intent.getExtras().getString("posttitle");
            String videourl = intent.getExtras().getString("videourl");
            String postnotes = intent.getExtras().getString("postnotes");

            title.setText(posttitle);
            Glide.with(this)
                    .load(image_url + picname)
                    .into(image);
            if (videourl != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    webView.loadData(videourl, "text/html",
                            "utf-8");
                } else {
                    webView.loadData(videourl, "text/html",
                            "utf-8");
                }
                webView.loadUrl(videourl);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fullText.setText(Html.fromHtml(postnotes, Html.FROM_HTML_MODE_COMPACT));
            } else {
                fullText.setText(Html.fromHtml(postnotes));
            }
        }
        setTitle("Article Detail");

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
    protected void onPause()
    {
        pauseBrowser();
        super.onPause();
    }

    @Override
    protected void onResume()
    {
        resumeBrowser();
        super.onResume();
    }

    private void pauseBrowser()
    {
        if (!mIsPaused)
        {
            // pause flash and javascript etc
            callHiddenWebViewMethod(webView, "onPause");
            webView.pauseTimers();
            mIsPaused = true;
        }
    }

    private void resumeBrowser()
    {
        if (mIsPaused)
        {
            // resume flash and javascript etc
            callHiddenWebViewMethod(webView, "onResume");
            webView.resumeTimers();
            mIsPaused = false;
        }
    }

    private void callHiddenWebViewMethod(final WebView wv, final String name)
    {
        try
        {
            final Method method = WebView.class.getMethod(name);
            method.invoke(webView);
        } catch (final Exception e)
        {}
    }

}
