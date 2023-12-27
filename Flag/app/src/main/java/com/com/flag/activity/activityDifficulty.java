package com.com.flag.activity;

import static com.com.flag.MainActivity.soundBackground;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.com.flag.R;

import java.util.Objects;

public class activityDifficulty extends Activity {
    Button Easy, Hard, Expert;
    String style;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* RECEIVE NAVIGATION TO NEXT ACTIVITY */
        Intent previousintent = getIntent();
        if (previousintent != null) {
            style = previousintent.getStringExtra("style");
        }
        else {
            style = "quizgame";
        }
        /* -------------------------------------------------------------------------------------- */
        setContentView(R.layout.activity_difficulty);
        /* SEND DATA PATH TO READ DIFFICULTY EASY */
        Easy = (Button)findViewById(R.id.ButtonDiffEasy);
        Easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(style, "flashcard")) {
                    intent = new Intent(activityDifficulty.this, activityFlashCard.class);
                }
                else {
                    intent = new Intent(activityDifficulty.this, activityGamePlay.class);
                }
                intent.putExtra("path", "Data_Easy");
                startActivity(intent);
            }
        });
        /* -------------------------------------------------------------------------------------- */
        /* SEND DATA PATH TO READ DIFFICULTY HARD */
        Hard = (Button)findViewById(R.id.ButtonDiffHard);
        Hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(style, "flashcard")) {
                    intent = new Intent(activityDifficulty.this, activityFlashCard.class);
                }
                else {
                    intent = new Intent(activityDifficulty.this, activityGamePlay.class);
                }
                intent.putExtra("path", "Data_Hard");
                startActivity(intent);
            }
        });
        /* -------------------------------------------------------------------------------------- */
        /* SEND DATA PATH TO READ DIFFICULTY EXPERT */
        Expert = (Button) findViewById(R.id.ButtonDiffExpert);
        Expert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Objects.equals(style, "flashcard")) {
                    intent = new Intent(activityDifficulty.this, activityFlashCard.class);
                }
                else {
                    intent = new Intent(activityDifficulty.this, activityGamePlay.class);
                }
                intent.putExtra("path", "Data_Expert");
                startActivity(intent);
            }
        });
        /* -------------------------------------------------------------------------------------- */
    }
}