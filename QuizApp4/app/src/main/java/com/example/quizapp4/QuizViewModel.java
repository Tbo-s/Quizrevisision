// QuizViewModel.java
package com.example.quizapp4;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class QuizViewModel extends ViewModel {
    private ArrayList<gallerymodel> images;
    private int answerTotal = 0;
    private int answerCorrect = 0;
    private String correctName;
    private int idCorrect;
    private int idCurrent;
    // You can also store other quiz state such as the current question index, current options, etc.

    // Getters and setters
    public ArrayList<gallerymodel> getImages() {
        return images;
    }
    public void setImages(ArrayList<gallerymodel> images) {
        this.images = images;
    }
    public int getAnswerTotal() {
        return answerTotal;
    }
    public void setAnswerTotal(int answerTotal) {
        this.answerTotal = answerTotal;
    }
    public int getAnswerCorrect() {
        return answerCorrect;
    }
    public void setAnswerCorrect(int answerCorrect) {
        this.answerCorrect = answerCorrect;
    }
    public String getCorrectName() {
        return correctName;
    }
    public void setCorrectName(String correctName) {
        this.correctName = correctName;
    }
    public int getIdCorrect() {
        return idCorrect;
    }
    public void setIdCorrect(int idCorrect) {
        this.idCorrect = idCorrect;
    }
    public int getIdCurrent() {
        return idCurrent;
    }
    public void setIdCurrent(int idCurrent) {
        this.idCurrent = idCurrent;
    }
}
