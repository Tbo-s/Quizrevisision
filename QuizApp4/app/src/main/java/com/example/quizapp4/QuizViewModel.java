package com.example.quizapp4;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class QuizViewModel extends ViewModel {
    private ArrayList<gallerymodel> images;
    private int answerTotal = 0;
    private int answerCorrect = 0;

    // New fields to preserve the current question state
    private gallerymodel currentQuestion;
    private ArrayList<String> currentOptions;
    private int correctOptionIndex = -1;

    // Getters and setters for images and score
    public ArrayList<gallerymodel> getImages() { return images; }
    public void setImages(ArrayList<gallerymodel> images) { this.images = images; }
    public int getAnswerTotal() { return answerTotal; }
    public void setAnswerTotal(int answerTotal) { this.answerTotal = answerTotal; }
    public int getAnswerCorrect() { return answerCorrect; }
    public void setAnswerCorrect(int answerCorrect) { this.answerCorrect = answerCorrect; }

    // Getters and setters for the current question
    public gallerymodel getCurrentQuestion() { return currentQuestion; }
    public void setCurrentQuestion(gallerymodel currentQuestion) { this.currentQuestion = currentQuestion; }
    public ArrayList<String> getCurrentOptions() { return currentOptions; }
    public void setCurrentOptions(ArrayList<String> currentOptions) { this.currentOptions = currentOptions; }
    public int getCorrectOptionIndex() { return correctOptionIndex; }
    public void setCorrectOptionIndex(int correctOptionIndex) { this.correctOptionIndex = correctOptionIndex; }
}
