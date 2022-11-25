package com.a2tocsolutions.nispsasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.a2tocsolutions.nispsasapp.activities.BanksActivity;
import com.a2tocsolutions.nispsasapp.activities.UploadMediaActivity;

public class CameraAndPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_and_page);
        setTitle("Upload Video");
        ImageView camera = findViewById(R.id.imagepicker);
        ImageView video = findViewById(R.id.videopicker);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadMediaActivity.class);
                getApplicationContext().startActivity(intent);
            }
        });

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://nispsas.com.ng/Videos/index.php"));
                startActivity(browserIntent);
            }
        });
    }
}
