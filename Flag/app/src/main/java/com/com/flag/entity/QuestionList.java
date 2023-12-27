package com.com.flag.entity;
import android.util.Log;

import com.com.flag.R;

import java.util.ArrayList;
import java.util.Objects;

public class QuestionList {
    public final ArrayList<QuestionNare> List = new ArrayList<>();
    public final ArrayList<String> Countries = new ArrayList<>();

    public QuestionList() {
    }

    public void GenerateQuestion(int num) {
        ArrayList<String> tempCountries = new ArrayList<>(Countries);
        int size = Countries.size();
        if (num != -1) {
            size = num;
        }
        for (int i = 0; i < size; i++) {
            QuestionNare Q = new QuestionNare();
            Q.ID = "" + (i + 1);
            ArrayList<String> Answer = new ArrayList<>();
            for (int j = 0; j < 4; j++) {
                Answer.add(RandomCountry(tempCountries));
                tempCountries.remove(Answer.get(Answer.size() - 1));
            }
            Q.AnswerA = Answer.get(0);
            Q.AnswerB = Answer.get(1);
            Q.AnswerC = Answer.get(2);
            Q.AnswerD = Answer.get(3);
            for (int j = 0; j < 4; j++) {
                tempCountries.add(Answer.get(j));
            }
            int temp = RandomQuesID(Answer);
            Q.Context = Answer.get(temp);
            Q.Answer = "" + temp;
            List.add(Q);
            tempCountries.remove(Answer.get(temp));
            if (tempCountries.size() <= 4) {
                tempCountries = new ArrayList<>(Countries);
            }
        }
    }

    private int RandomQuesID(ArrayList<String> Answer) {
        return (int) ((Math.random() * (Answer.size())));
    }

    private String RandomCountry(ArrayList<String> tempCountries) {
        int randomID = (int) ((Math.random() * (tempCountries.size())));
        return tempCountries.get(randomID);
    }

    public boolean CheckAnswer(int id, int idCheck) {
        if (idCheck == R.id.ButtonAnsA) {
            return List.get(id).Answer.compareTo("0") == 0;
        } else if (idCheck == R.id.ButtonAnsB) {
            return List.get(id).Answer.compareTo("1") == 0;
        } else if (idCheck == R.id.ButtonAnsC) {
            return List.get(id).Answer.compareTo("2") == 0;
        } else if (idCheck == R.id.ButtonAnsD) {
            return List.get(id).Answer.compareTo("3") == 0;
        }
        return false;
    }
}
