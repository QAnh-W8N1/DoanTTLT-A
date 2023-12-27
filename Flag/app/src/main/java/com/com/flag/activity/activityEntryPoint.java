package com.com.flag.activity;

import static com.com.flag.MainActivity.soundBackground;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.com.flag.R;

public class activityEntryPoint extends Activity {
    Button PlayGame, HighScore, Intro, MuteSound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrypoint);

        /* FUNCTION PLAY GAME */
        PlayGame = findViewById(R.id.ButtonEntryPG);
        PlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityEntryPoint.this, activityPlayStyle.class);
                startActivity(intent);
            }
        });
        /* -------------------------------------------------------------------------------------- */
        /* FUNCTION HIGH SCORE */
        HighScore = findViewById(R.id.ButtonEntryHS);
        HighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityEntryPoint.this, activityHighScore.class);
                startActivity(intent);
            }
        });
        /* -------------------------------------------------------------------------------------- */
        /* FUNCTION INTRODUCTION */
        Intro = findViewById(R.id.ButtonEntryIns);
        Intro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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