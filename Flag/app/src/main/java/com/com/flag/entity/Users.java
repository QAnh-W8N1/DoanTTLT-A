package com.com.flag.entity;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Users {
    String name, score;

    public String getScore() {
        return score;
    }
    DatabaseReference reference;
    public Users(Context context, String tempName) {
        this.name = tempName;
        this.score = "0";
        ReadUser(context);
    }
    private void GenerateUser(Context context) {
        HashMap newScore = new HashMap();
        newScore.put("score", "0");
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(name).setValue(newScore).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(context,"Successfuly Added",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void ReadUser(Context context) {
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(name).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        Toast.makeText(context,"Successfully Read",Toast.LENGTH_SHORT).show();
                        DataSnapshot dataSnapshot = task.getResult();
                        score = String.valueOf(dataSnapshot.child("score").getValue());
                    }else {
                        GenerateUser(context);
                        Toast.makeText(context, "User Doesn't Exist", Toast.LENGTH_SHORT).show();
                    }
                }else
                    Toast.makeText(context,"Failed to read",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void UpdateUser(Context context, String tempScore) {
        HashMap newHighScore = new HashMap();
        newHighScore.put("score", tempScore);
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(name).updateChildren(newHighScore).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                    Toast.makeText(context,"Successfully Updated",Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(context,"Failed to Update",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

