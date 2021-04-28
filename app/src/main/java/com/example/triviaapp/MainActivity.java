package com.example.triviaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.triviaapp.data.AnswerListAsyncResponse;
import com.example.triviaapp.data.QuestionBank;
import com.example.triviaapp.model.Question;
import com.example.triviaapp.util.Prefs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String MESSAGE_ID = "abc";
    private TextView questionText;
    private TextView questionCounter;
    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton previousButton;
    private int currentQuestionIndex = 0;
    private List<Question> mQuestionList;
    private TextView currentScore;
    private int score=0;
    private Prefs mPrefs;
    private TextView highScore;
    private int savedScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPrefs = new Prefs(MainActivity.this);

        nextButton = findViewById(R.id.nextButton);
        previousButton = findViewById(R.id.previousButton);
        trueButton = findViewById(R.id.trueButton);
        falseButton = findViewById(R.id.falseButton);
        questionCounter = findViewById(R.id.counterText);
        questionText = findViewById(R.id.questionText);
        currentScore = findViewById(R.id.currentScore);
        highScore = findViewById(R.id.highScore);

        mQuestionList = new QuestionBank(this).getQuestion(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionText.setText(questionArrayList.get(currentQuestionIndex).getAnswer());
                questionCounter.setText((currentQuestionIndex+1)+" /"+ questionArrayList.size());
                Log.d("JSON" , "onCreate : " + questionArrayList);
            }
        });
        highScore.setText("High Score : "+String.valueOf(mPrefs.getHighScore()));
        currentScore.setText("Current Score : "+String.valueOf(mPrefs.getCurrentScore()));
        score = mPrefs.getCurrentScore();

        nextButton.setOnClickListener(this);
        previousButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        currentQuestionIndex = mPrefs.getState();




    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.previousButton:
                currentQuestionIndex = (mQuestionList.size() + (currentQuestionIndex-1))% mQuestionList.size();
                questionText.setText(mQuestionList.get(currentQuestionIndex).getAnswer());
                questionCounter.setText((currentQuestionIndex+1)+" /"+ mQuestionList.size());
                break;
            case R.id.nextButton:
                currentQuestionIndex = (currentQuestionIndex + 1) % mQuestionList.size();
                questionText.setText(mQuestionList.get(currentQuestionIndex).getAnswer());
                questionCounter.setText((currentQuestionIndex+1)+" /"+ mQuestionList.size());
                break;
            case R.id.trueButton:

                questionText.setText(mQuestionList.get(currentQuestionIndex).getAnswer());
                checkAnswer(true);
                mPrefs.savedHighScore(score);

                break;
            case R.id.falseButton:

                questionText.setText(mQuestionList.get(currentQuestionIndex).getAnswer());
                checkAnswer(false);
                mPrefs.savedHighScore(score);

                break;
        }
    }

    private void checkAnswer(boolean b) {
        boolean check = mQuestionList.get(currentQuestionIndex).isAnswerTrue();
        if(check == b){
            score+=100;
            fadeView();
            currentScore.setText("Current Score : "+score);
            if(score>mPrefs.getHighScore()){
                highScore.setText("High Score : "+score);
            }
        }
        else {
            shakeAnimation();
            if (score>0){
                score=0;
                currentScore.setText("Current Score : "+score);
            }
        }
    }

    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);
        CardView cardView = findViewById(R.id.cardView);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
                previousButton.setClickable(false);
                nextButton.setClickable(false);
                trueButton.setClickable(false);
                falseButton.setClickable(false);
                questionText.setTextColor(getResources().getColor(R.color.primary_light));
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
                previousButton.setClickable(true);
                nextButton.setClickable(true);
                trueButton.setClickable(true);
                falseButton.setClickable(true);
                questionText.setTextColor(getResources().getColor(R.color.secondary_text));
                currentQuestionIndex=0;
                score = 0;
                questionText.setText(mQuestionList.get(currentQuestionIndex).getAnswer());
                questionCounter.setText((currentQuestionIndex+1)+" /"+ mQuestionList.size());
                currentScore.setText("Current Score : "+score);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void  fadeView(){
        CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
                previousButton.setClickable(false);
                nextButton.setClickable(false);
                trueButton.setClickable(false);
                falseButton.setClickable(false);
                questionText.setTextColor(getResources().getColor(R.color.primary_dark));
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(getResources().getColor(R.color.primary_light));
                previousButton.setClickable(true);
                nextButton.setClickable(true);
                trueButton.setClickable(true);
                falseButton.setClickable(true);
                questionText.setTextColor(getResources().getColor(R.color.secondary_text));
                currentQuestionIndex = (currentQuestionIndex + 1) % mQuestionList.size();
                questionText.setText(mQuestionList.get(currentQuestionIndex).getAnswer());
                questionCounter.setText((currentQuestionIndex+1)+" /"+ mQuestionList.size());
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPrefs.savedHighScore(score);
        mPrefs.saveCurrentScore(score);
        mPrefs.setState(currentQuestionIndex);
    }
}
