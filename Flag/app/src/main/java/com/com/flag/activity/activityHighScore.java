package com.com.flag.activity;

import static com.com.flag.MainActivity.player;
import static com.com.flag.MainActivity.soundBackground;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.com.flag.MainActivity;
import com.com.flag.R;

public class activityHighScore extends Activity {
    TextView Txt1;
    Button PlayGame, Exit, MuteSound;
    int HighScore;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);
        /* RETRIEVE HIGH SCORE FROM THE USER DATABASE */
        HighScore = Integer.parseInt(player.getScore());
        Txt1 = (TextView)findViewById(R.id.TextHSScore);
        Txt1.setText(""+ HighScore);
        /* -------------------------------------------------------------------------------------- */
        /* FUNCTION PLAY GAME */
        PlayGame = (Button)findViewById(R.id.ButtonHSPG);
        PlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityHighScore.this,  activityPlayStyle.class);
                startActivity(intent);
            }
        });
        /* -------------------------------------------------------------------------------------- */
        /* FUNCTION EXIT */
        Exit= (Button)findViewById(R.id.ButtonHSExit);
        Exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activityHighScore.this,  MainActivity.class);
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