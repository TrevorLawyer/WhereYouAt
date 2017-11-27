package edu.uco.tlawyer.whereyouat;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import static android.os.SystemClock.sleep;


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        MediaPlayer voice = MediaPlayer.create(SplashActivity.this,R.raw.where);
        voice.start();
        sleep(3000);
        finish();
    }
}
