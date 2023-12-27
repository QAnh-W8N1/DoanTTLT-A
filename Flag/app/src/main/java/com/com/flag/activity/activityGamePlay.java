package com.com.flag.activity;
import com.com.flag.R;
import com.com.flag.entity.*;

import static com.com.flag.MainActivity.soundBackground;
import static com.com.flag.MainActivity.player;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;


public class activityGamePlay extends Activity implements View.OnClickListener, View.OnLongClickListener {
    private boolean AnswerCheck = false;
    private final int[] ButtonArr = {R.id.ButtonAnsA, R.id.ButtonAnsB, R.id.ButtonAnsC, R.id.ButtonAnsD,
            R.id.ButtonStopMusic, R.id.ButtonHelp1, R.id.ButtonHelp2, R.id.ButtonHelp3, R.id.ButtonNext};
    private final int quiz = 20, survival = 1000;
    private int position = 1, id = 0, num = quiz;
    private int Progress = 0, HighScore = 0;
    private String path = "";
    private ProgressBar CountdownPB;
    private CountDownTimer CountdownTimer;
    public SoundEffect soundEffect = new SoundEffect();
    public QuestionList questionList = new QuestionList();
    public Support support = new Support();
    public TextSpeech textSpeech = new TextSpeech();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* SET UP SOUND EFFECTS FUNCTION */
        soundEffect.GenerateSound(this);
        /* -------------------------------------------------------------------------------------- */
        /* SET UP TEXT TO SPEECH FUNCTION */
        textSpeech.GenerateTextSpeech(this);
        /* -------------------------------------------------------------------------------------- */
        /* RECEIVE FILE PATH INDICATES DIFFICULTY OF THE DATASET */
        Intent intent = getIntent();
        if (intent != null)
        {
            path = intent.getStringExtra("path");
        }
        /* -------------------------------------------------------------------------------------- */
        /* LOAD HIGH SCORE BASED ON USER NAME */
        LoadHighScore();
        /* -------------------------------------------------------------------------------------- */
        /* READ FILE NAMES FROM CLOUD STORAGE INTO A LIST */
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child(path);
        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        questionList.Countries.add((item.getName().split("\\."))[0]);
                    }
                    soundBackground.Pause();
                    if (soundBackground.systemSound) {
                        setVolumeControlStream(AudioManager.STREAM_MUSIC);
                        soundEffect.Effect.play(soundEffect.soundStartGame, 1, 1, 1, 0, 1);
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    soundBackground.Play();
                    if (Objects.equals(path, "Data_Survival")) {
                        num = survival;
                        questionList.GenerateQuestion(num);
                    }
                    else {
                        questionList.GenerateQuestion(-1);
                    }
                    GameplayLayout1();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(activityGamePlay.this, "Error _ Retrieving Data", Toast.LENGTH_SHORT).show());
        /* -------------------------------------------------------------------------------------- */
    }
    /* SET UP ON LONG CLICK READ THE CONTEXT OUT LOUD */
    @Override
    public boolean onLongClick(View v) {
        int idCheck = v.getId();
        if (position % 2 == 0) {
            if (idCheck == R.id.Layout2_TextQuestion)
                textSpeech.ToSpeech(questionList.List.get(id).Context);
        }
        else {
            if (idCheck == R.id.ButtonAnsA) {
                textSpeech.ToSpeech(questionList.List.get(id).AnswerA);
            } else if (idCheck == R.id.ButtonAnsB) {
                textSpeech.ToSpeech(questionList.List.get(id).AnswerB);
            } else if (idCheck == R.id.ButtonAnsC) {
                textSpeech.ToSpeech(questionList.List.get(id).AnswerC);
            } else if (idCheck == R.id.ButtonAnsD) {
                textSpeech.ToSpeech(questionList.List.get(id).AnswerD);
            }
        }
        return false;
    }
    /* -------------------------------------------------------------------------------------------*/
    /* SET UP ON CLICK AS THE MAIN CONTROLLER, NAVIGATE USER THROUGHOUT THIS GAMEPLAY ACTIVITY */
    @Override
    public void onClick(View v) {
        int idCheck = v.getId();
        /* MUTE SYSTEM */
        if (idCheck == R.id.ButtonStopMusic) {
            soundBackground.Mute();
        }
        /* -------------------------------------------------------------------------------------- */
        /* USE SUPPORT FEATURES */
        else if (idCheck == R.id.ButtonHelp1) {
            support.getHelp1();
            NextGameplay(position);
        }
        else if (idCheck == R.id.ButtonHelp2) {
            DisableAnswerButton(support.getHelp2(questionList, id), -1);
        }
        else if (idCheck == R.id.ButtonHelp3) {
            DisableAnswerButton(support.getHelp3(), -1);
        }
        /* -------------------------------------------------------------------------------------- */
        /* NEXT QUESTION */
        else if (idCheck == R.id.ButtonNext) {
            position++;
            questionList.List.remove(id);
            NextGameplay(position);
        }
        /* -------------------------------------------------------------------------------------- */
        /* HANDLING CHOSEN ANSWER */
        else {
            if (!AnswerCheck) {
                if (CheckAnswer(idCheck))
                    Progress = Progress + 1;
                else {
                    if (num == survival)
                        position = num;
                }
            }
            /* ---------------------------------------------------------------------------------- */
            /* HANDLING ON COMPLETE OF THE CURRENT GAME */
            if (position >= num) {
                /* SOUND EFFECT FOR ENDING THE GAME */
                if (soundBackground.systemSound) {
                    setVolumeControlStream(AudioManager.STREAM_MUSIC);
                    if (Progress >= quiz)
                        soundEffect.Effect.play(soundEffect.soundGoodEG, 1, 1, 0, 0, 1);
                    else
                        soundEffect.Effect.play(soundEffect.soundBadEG, 1, 1, 0, 0, 1);
                }
                /* -------------------------------------------------------------------------------------- */
                /* SENDING SCORE TO ACTIVITY RESULT FOR DISPLAY */
                Intent intent = new Intent(activityGamePlay.this, activityResult.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Progress", Progress);
                intent.putExtra("MyPackage", bundle);
                startActivity(intent);
                /* -------------------------------------------------------------------------------------- */
                /* CHECK HIGH SCORE FOR A POSSIBLE UPDATE */
                if (Progress > HighScore) {
                    HighScore = Progress;
                    SaveHighScore();
                }
                finish();
            }
            /* ---------------------------------------------------------------------------------- */
        }
    }
    /* -------------------------------------------------------------------------------------- */
    /* FUNCTION HELPS HANDLING WHEN AN ANSWER IS LOCKED IN */
    private boolean CheckAnswer(int idCheck) {
        /* SET FLAG */
        AnswerCheck = true;
        /* STOP COUNTDOWN */
        if (CountdownTimer != null)
            CountdownTimer.cancel();
        /* -------------------------------------------------------------------------------------- */
        /* DISABLE BUTTONS */
        DisableAnswerButton(new int[]{R.id.ButtonAnsA, R.id.ButtonAnsB, R.id.ButtonAnsC, R.id.ButtonAnsD}, idCheck);
        DisableHelpButton();
        /* -------------------------------------------------------------------------------------- */
        /* USE METHOD OF QUESTIONLIST TO CHECK THE CHOSEN ANSWER */
        boolean correct = questionList.CheckAnswer(id, idCheck);
        /* -------------------------------------------------------------------------------------- */
        /* SOUND EFFECT FOR ENDING THE GAME */
        if (soundBackground.systemSound) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            if (correct)
                soundEffect.Effect.play(soundEffect.soundRightAns, 1, 1, 0, 0, (float)1.5);
            else
                soundEffect.Effect.play(soundEffect.soundWrongAns, 1, 1, 0, 0, (float)1.5);
        }
        /* -------------------------------------------------------------------------------------- */
        return correct;
    }
    /* FUNCTION HELPS HANDLING WHEN GO TO NEXT QUESTION */
    private void NextGameplay(int pos)
    {
        /* STOP COUNTDOWN */
        if (CountdownTimer != null)
            CountdownTimer.cancel();
        /* -------------------------------------------------------------------------------------- */
        /* SET FLAG */
        AnswerCheck = false;
        if (pos % 2 == 0)
            GameplayLayout2();
        else
            GameplayLayout1();
    }
    /* FUNCTIONS HELP SETTING UP FOR NEW GAMEPLAY */
    private void GameplayLayout1(){
        setContentView(R.layout.activity_gamelayout_1);
        /* SET UP LAYOUT */
        DisplayLayout1(questionList.List);
        /* SET UP SUPPORT */
        EnableHelpButton();
        /* SET UP COUNTDOWN */
        StartCountDown();
        /* SET UP CONTROLLER */
        for (int id : ButtonArr) {
            View v = findViewById(id);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }
    }
    private void GameplayLayout2(){
        setContentView(R.layout.activity_gamelayout_2);
        /* SET UP LAYOUT */
        DisplayLayout2(questionList.List);
        /* SET UP SUPPORT */
        EnableHelpButton();
        /* SET UP COUNTDOWN */
        StartCountDown();
        /* SET UP CONTROLLER */
        for (int id : ButtonArr) {
            View v = findViewById(id);
            v.setOnClickListener(this);
        }
        View z = findViewById(R.id.Layout2_TextQuestion);
        z.setOnLongClickListener(this);
    }
    /* ------------------------------------------------------------------------------------------ */

    @SuppressLint({"SetTextI18n", "DiscouragedApi"})
    private void DisplayLayout1(ArrayList<QuestionNare> L) {
        id = Utility.RandomExamID(L.size());
        TextView Result;
        Result = findViewById(R.id.TextResult);
        ((TextView) findViewById(R.id.TextQuesID)).setText("QUESTION " + position);
        /* READ IMAGE FROM CLOUD STORAGE BASED ON GIVEN FILE NAME INTO A BITMAP */
        StorageReference storeref;
        storeref = FirebaseStorage.getInstance().getReference(path + "/" + L.get(id).Context + ".png");
        try {
            File tempfile = File.createTempFile("tempfile", ".png");
            storeref.getFile(tempfile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(tempfile.getAbsolutePath());
                ((ImageView) findViewById(R.id.Layout1_ImgQuestion)).setImageBitmap(bitmap);
            }).addOnFailureListener(e -> Toast.makeText(activityGamePlay.this, "Error _ Game Layout 1", Toast.LENGTH_SHORT).show());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        /* -------------------------------------------------------------------------------------- */
        /* READ COUNTRY NAMES INTO BUTTONS CONTEXT AS ANSWERS */
        ((Button) findViewById(R.id.ButtonAnsA)).setText(L.get(id).AnswerA);
        ((Button) findViewById(R.id.ButtonAnsB)).setText(L.get(id).AnswerB);
        ((Button) findViewById(R.id.ButtonAnsC)).setText(L.get(id).AnswerC);
        ((Button) findViewById(R.id.ButtonAnsD)).setText(L.get(id).AnswerD);
        /* -------------------------------------------------------------------------------------- */
        Result.setText("" + Progress);
    }
    @SuppressLint({"SetTextI18n", "DiscouragedApi"})
    private void DisplayLayout2(ArrayList<QuestionNare> L) {
        id = Utility.RandomExamID(L.size());
        TextView Result;
        Result = findViewById(R.id.TextResult);
        ((TextView) findViewById(R.id.TextQuesID)).setText("QUESTION " + position);
        /* READ COUNTRY NAME INTO BUTTON CONTEXT AS QUESTION */
        ((Button) findViewById(R.id.Layout2_TextQuestion)).setText(L.get(id).Context);
        /* -------------------------------------------------------------------------------------- */
        /* READ IMAGE FROM CLOUD STORAGE BASED ON GIVEN FILE NAME INTO A BITMAP */
        int index = 0;
        StorageReference storeref;
        for (String context : new String[]{L.get(id).AnswerA, L.get(id).AnswerB, L.get(id).AnswerC, L.get(id).AnswerD}) {
            storeref = FirebaseStorage.getInstance().getReference(path + "/" + context + ".png");
            try {
                File tempfile = File.createTempFile("tempfile", ".png");
                int finalIndex = index;
                storeref.getFile(tempfile).addOnSuccessListener(taskSnapshot -> {
                    Bitmap bitmap = BitmapFactory.decodeFile(tempfile.getAbsolutePath());
                    ((ImageButton) findViewById(ButtonArr[finalIndex])).setImageBitmap(bitmap);
                }).addOnFailureListener(e -> Toast.makeText(activityGamePlay.this, "Error _ Game Layout 2", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
            }
            index++;
        }
        /* -------------------------------------------------------------------------------------- */
        Result.setText("" + Progress);
    }
    private void LoadHighScore(){

        HighScore = Integer.parseInt(player.getScore());
    }
    private void SaveHighScore(){

        player.UpdateUser(this, ""+HighScore);
    }

    @SuppressLint("DiscouragedApi")
    private void DisableAnswerButton(int[] arr, int idCheck) {
        for (int j : arr) {
            if (position % 2 == 0) {
                if (j == idCheck && idCheck != Utility.AnswerIDtoButtonID(questionList.List.get(id).Answer)) {
                    ((ImageButton) findViewById(j)).buildDrawingCache();
                    ((ImageButton) findViewById(j)).setImageBitmap(Utility.BlurBitmap(findViewById(j).getDrawingCache(), this));
                    ((ImageButton) findViewById(j)).setEnabled(false);
                }
                else if (j == Utility.AnswerIDtoButtonID(questionList.List.get(id).Answer) && AnswerCheck) {
                    ((ImageButton) findViewById(j)).setEnabled(false);
                }
                else {
                    if (j != Utility.AnswerIDtoButtonID(questionList.List.get(id).Answer)) {
                        ((ImageButton) findViewById(j)).setImageBitmap(null);
                        ((ImageButton) findViewById(j)).setEnabled(false);
                    }
                }
            }
            else {
                if (j == idCheck && idCheck != Utility.AnswerIDtoButtonID(questionList.List.get(id).Answer)) {
                    ((Button) findViewById(j)).setTextColor(Color.parseColor("#800000"));
                    ((Button) findViewById(j)).setEnabled(false);
                }
                else if (j == Utility.AnswerIDtoButtonID(questionList.List.get(id).Answer) && AnswerCheck) {
                    ((Button) findViewById(j)).setEnabled(false);
                }
                else {
                    if (j != Utility.AnswerIDtoButtonID(questionList.List.get(id).Answer)) {
                        ((Button) findViewById(j)).setText("");
                        ((Button) findViewById(j)).setEnabled(false);
                    }
                }
            }
        }
    }
    private void DisableHelpButton() {
        for (int i : new int[]{R.id.ButtonHelp1, R.id.ButtonHelp2, R.id.ButtonHelp3}) {
            ((Button) findViewById(i)).setEnabled(false);
        }
    }
    private void EnableHelpButton() {
        ((Button) findViewById(R.id.ButtonHelp1)).setEnabled(support.Help1);
        ((Button) findViewById(R.id.ButtonHelp2)).setEnabled(support.Help2);
        ((Button) findViewById(R.id.ButtonHelp3)).setEnabled(support.Help3);
    }
    private void StartCountDown() {
        CountdownPB = findViewById(R.id.ProgressbarCountdown);
        CountdownTimer = new CountDownTimer(30000,1000) {
            int countdown = 30;
            @Override
            public void onTick(long millisUntilFinished) {
                CountdownPB.setProgress(countdown);
                countdown--;
            }
            @Override
            public void onFinish() {
                CountdownPB.setProgress(0);
                CheckAnswer(-1);
            }
        }.start();
    }
}
