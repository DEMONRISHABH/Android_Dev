package com.example.vibratorapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationAttributes;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.w3c.dom.Text;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    Button b1;
    Vibrator v1;
    ImageView iv;
    TextToSpeech ts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1=(Button) findViewById(R.id.button);
        v1=(Vibrator) getSystemService(VIBRATOR_SERVICE);
        iv=(ImageView) findViewById(R.id.imageView);
        ts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                ts.setLanguage(Locale.ENGLISH);
                ts.setSpeechRate(1.0f);
                String s="Teri Maa Ki";
                ts.speak(s,TextToSpeech.QUEUE_FLUSH,null);
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                vibrate();
                torchon();
                iv.setImageResource(R.drawable.a);
            }
        });
    }
    private void vibrate()
    {
        if(Build.VERSION.SDK_INT>26)
        {
            v1.vibrate(10000);
        }
        else
        {
            v1.vibrate(10000);
        }
    }
    private void torchon()
    {
        CameraManager cm=(CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            String id=cm.getCameraIdList()[0];
            cm.setTorchMode(id,true);
        } catch (CameraAccessException e)
        {

        }
    }
}

