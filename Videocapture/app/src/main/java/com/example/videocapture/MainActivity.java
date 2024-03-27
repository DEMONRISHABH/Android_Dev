package com.example.videocapture;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    Button b1;
    VideoView v1;
    Uri uri;
    MediaController mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(Button) findViewById(R.id.button);
        v1=(VideoView) findViewById(R.id.videoView);
        mp=new MediaController(this);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(i1,100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100)
        {
            uri=data.getData();
            v1.setVideoURI(uri);
            v1.setMediaController(mp);
            mp.setAnchorView(v1);
            v1.start();
        }
        else
        {
            Toast.makeText(MainActivity.this, "Camera is not working", Toast.LENGTH_SHORT).show();
        }
    }
}