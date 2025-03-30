package com.example.quizapp4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    private FragmentScenario<QuizFragment> fragmentScenario;


    @Before
    public void setup() {
        fragmentScenario = FragmentScenario.launchInContainer(QuizFragment.class);
    }

    @Test
    public void testWrongAnswerScore() {
        // get the score before
        int beforeCorrect = getScoreCorrect();
        int beforeTotal = getScoreTotal();
        // get the correct answer
        int correctIndex = gettCorrectOption();
        // answer a wrong option
        if (correctIndex == 1) {
            onView(withId(R.id.buttonQuizOption3)).perform(click());
        } else {
            onView(withId(R.id.buttonQuizOption2)).perform(click());
        }
        onView(withId(R.id.buttonQuizSubmit)).perform(click());
        // get number of correct answers after
        int afterCorrect = getScoreCorrect();
        int afterTotal = getScoreTotal();
        // compare
        assertEquals(beforeTotal+1, afterTotal);
        assertEquals(beforeCorrect, afterCorrect);
    }

    @Test
    public void testCorrectAnswerScore() {
        // get the score before
        int beforeCorrect = getScoreCorrect();
        int beforeTotal = getScoreTotal();
        // get the correct answer
        int correctIndex = gettCorrectOption();
        // answer a wrong option
        if (correctIndex == 1) {
            onView(withId(R.id.buttonQuizOption2)).perform(click());
        } else if (correctIndex == 2){
            onView(withId(R.id.buttonQuizOption3)).perform(click());
        } else {
            onView(withId(R.id.buttonQuizOption1)).perform(click());
        }
        onView(withId(R.id.buttonQuizSubmit)).perform(click());
        // get number of correct answers after
        int afterCorrect = getScoreCorrect();
        int afterTotal = getScoreTotal();
        // compare
        assertEquals(beforeTotal+1, afterTotal);
        assertEquals(beforeCorrect+1, afterCorrect);
    }

    private int getScoreCorrect() {
        AtomicInteger cnt = new AtomicInteger();
        fragmentScenario.onFragment(f -> cnt.set(f.getViewModel().getAnswerCorrect()));
        return cnt.get();
    }

    private int getScoreTotal() {
        AtomicInteger cnt = new AtomicInteger();
        fragmentScenario.onFragment(f -> cnt.set(f.getViewModel().getAnswerTotal()));
        return cnt.get();
    }

    private int gettCorrectOption() {
        AtomicInteger cnt = new AtomicInteger();
        fragmentScenario.onFragment(f -> cnt.set(f.getViewModel().getCorrectOptionIndex()));
        return cnt.get();
    }

}
