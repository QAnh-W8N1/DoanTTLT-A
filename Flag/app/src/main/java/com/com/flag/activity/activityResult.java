package com.com.flag.activity;

import static com.com.flag.MainActivity.soundBackground;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.com.flag.MainActivity;
import com.com.flag.R;

public class activityResult extends Activity {
    Button TryAgain, TapOut, MuteSound;
    TextView Score;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        /* RETRIEVE SCORE FROM THE CONCLUSION OF GAMEPLAY */
        Score = (TextView) findViewById(R.id.TextResultScore);
        Intent callerIntent=getIntent();
        Bundle packageFromCaller= callerIntent.getBundleExtra("MyPackage");
        assert packageFromCaller != null;
        Score.setText("" + packageFromCaller.getInt("Progress"));
        /* -------------------------------------------------------------------------------------- */
        /* FUNCTION TRY AGAIN TO PLAY THE GAME */
        TryAgain = (Button)findViewById(R.id.ButtonResultTA);
        TryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityResult.this,  activityPlayStyle.class);
                startActivity(intent);
            }
        });
        /* -------------------------------------------------------------------------------------- */
        /* FUNCTION TAP OUT TO BACK TO MAIN ACTIVITY */
        TapOut = (Button)findViewById(R.id.ButtonResultTO);
        TapOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityResult.this,  MainActivity.class);
                startActivity(intent);
            }
        });
        /* -------------------------------------------------------------------------------------- */
        /* SYSTEM MUTE */
        MuteSound = findViewById(R.id.ButtonStopMusic);
        MuteSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundBackground.Mute();
            }
        });
        /* -------------------------------------------------------------------------------------- */
    }
}