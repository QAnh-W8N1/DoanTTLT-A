package com.com.flag.activity;

import static com.com.flag.MainActivity.soundBackground;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.com.flag.R;
import com.com.flag.entity.TextSpeech;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class activityFlashCard extends Activity implements View.OnClickListener, View.OnLongClickListener {
    private final int[] ButtonArr = {R.id.ButtonFCardCountry, R.id.ButtonStopMusic, R.id.ButtonNext};
    private String path = "";
    private int id = 0;
    private final ArrayList<String> Countries = new ArrayList<>();
    public TextSpeech textSpeech = new TextSpeech();
    Button MuteSound;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        /* READ FILE NAMES FROM CLOUD STORAGE INTO A LIST */
        StorageReference listRef = FirebaseStorage.getInstance().getReference().child(path);
        listRef.listAll()
                .addOnSuccessListener(listResult -> {
                    for (StorageReference item : listResult.getItems()) {
                        Countries.add((item.getName().split("\\."))[0]);
                    }
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    setContentView(R.layout.activity_flashcard);
                    /* DISPLAY CONTROLLER */
                    Display(id);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(activityFlashCard.this, "Error _ Retrieving Data", Toast.LENGTH_SHORT).show());
        /* -------------------------------------------------------------------------------------- */
    }
    /* SET UP ON LONG CLICK READ THE CONTEXT OUT LOUD */
    @Override
    public boolean onLongClick(View v) {
        int idCheck = v.getId();
        if (idCheck == R.id.ButtonFCardCountry) {
            textSpeech.ToSpeech(Countries.get(id));
        }
        return false;
    }
    /* -------------------------------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        int idCheck = v.getId();
        if (idCheck == R.id.ButtonStopMusic) {
            soundBackground.Mute();
        }
        else if (idCheck == R.id.ButtonNext) {
            id++;
            if (id >= Countries.size()) {
                id = 0 ;
            }
            Display(id);
        }
    }
    @SuppressLint({"SetTextI18n", "DiscouragedApi"})
    private void Display(int id) {
        /* READ IMAGE FROM CLOUD STORAGE BASED ON GIVEN FILE NAME INTO A BITMAP */
        StorageReference storeref;
        storeref = FirebaseStorage.getInstance().getReference(path + "/" + Countries.get(id) + ".png");
        try {
            File tempfile = File.createTempFile("tempfile", ".png");
            storeref.getFile(tempfile).addOnSuccessListener(taskSnapshot -> {
                Bitmap bitmap = BitmapFactory.decodeFile(tempfile.getAbsolutePath());
                ((ImageView) findViewById(R.id.ImgFCardFlag)).setImageBitmap(bitmap);
            }).addOnFailureListener(e -> Toast.makeText(activityFlashCard.this, "Error _ Flash Cards", Toast.LENGTH_SHORT).show());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* -------------------------------------------------------------------------------------- */
        ((Button) findViewById(R.id.ButtonFCardCountry)).setText(Countries.get(id));
        for (int button : ButtonArr) {
            View v = findViewById(button);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }
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
