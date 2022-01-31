package com.example.funquiz;

import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.funquiz.data.Repository;
import com.example.funquiz.databinding.ActivityMainBinding;
import com.example.funquiz.model.Question;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private  ActivityMainBinding binding;
    private int currentQuestionIndex= 0 ;
    private List<Question> questionList;
    private int currentQuestionIndexPo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);


        questionList = new Repository().getQuestions(questionArrayList -> {
            binding.questionTextView.setText((CharSequence) questionArrayList.get(currentQuestionIndex).getAnswer());
           // binding.outOfText.setText("Question: " + currentQuestionIndex + "/" + questionArrayList.size());

        }
        );


        binding.buttonNext.setOnClickListener(view -> {
            currentQuestionIndex = (currentQuestionIndex +1) %  questionList.size();

            updateQuestion();
        });

        binding.buttonPrev.setOnClickListener(view -> {
            currentQuestionIndex = (currentQuestionIndex -1)% questionList.size();

            updateQuestion();
        });

        binding.buttonTrue.setOnClickListener(view -> {
         checkAnswer(true);
         updateQuestion();
        });
        binding.buttonFalseNew.setOnClickListener(view -> {
         checkAnswer(false);
         updateQuestion();

        });

    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean answer = questionList.get(currentQuestionIndex).isAnswerTrue();
        int snackMessageId =0;
        if(userChooseCorrect ==answer){
            snackMessageId = R.string.correct_answer;
            fadeAnimation();
        }
        else{
            snackMessageId = R.string.incorrect_answer;
            shakeAnimation();
        }
        Snackbar.make(binding.cardView,snackMessageId,Snackbar.LENGTH_SHORT).show();
    }

    public  void  updateCounter(ArrayList<Question> questionArrayList){
      // binding.outOfText.setText("Question: "+currentQuestionIndex+"/"+questionArrayList.size());
        binding.outOfText.setText(String.format("Question: %d/%d", currentQuestionIndex, questionArrayList.size()));

    }



    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getAnswer();
        binding.questionTextView.setText(question);
        updateCounter((ArrayList<Question>) questionList);
    }
    private void shakeAnimation(){
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this,
                R.anim.shake_animation);
        binding.cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    private void fadeAnimation(){
        AlphaAnimation alphaAnimation = new AlphaAnimation(2.0f,0.0f);
        alphaAnimation.setDuration(200);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);

        binding.cardView.setAnimation(alphaAnimation);

        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.questionTextView.setTextColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
             binding.questionTextView.setTextColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}