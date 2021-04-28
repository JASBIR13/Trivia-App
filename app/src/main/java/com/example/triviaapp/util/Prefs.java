package com.example.triviaapp.util;

import android.app.Activity;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences mPreferences;

    public Prefs(Activity activity){//it can also be context
        mPreferences = activity.getPreferences(activity.MODE_PRIVATE);
    }

    public void savedHighScore(int score){
        int currentScore = score;
        int last = mPreferences.getInt("high_score",0);

        if (currentScore>last){
            mPreferences.edit().putInt("high_score",currentScore).apply();
        }
    }

    public int getHighScore(){
        return mPreferences.getInt("high_score",0);
    }

    public void setState(int index){
        mPreferences.edit().putInt("index_state",index).apply();
    }

    public int getState(){
        return mPreferences.getInt("index_state",0);
    }

    public void saveCurrentScore(int score){
        mPreferences.edit().putInt("current_score",score).apply();
    }

    public int getCurrentScore(){
        return mPreferences.getInt("current_score",0);
    }
}
