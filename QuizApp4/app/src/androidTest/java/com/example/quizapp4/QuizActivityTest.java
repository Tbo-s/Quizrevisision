package com.example.quizapp4;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import android.os.Bundle;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
// You may also import other matchers as needed

@RunWith(AndroidJUnit4.class)
public class QuizActivityTest {

    @Rule
    public ActivityScenarioRule<QuizActivity> activityScenarioRule =
            new ActivityScenarioRule<>(QuizActivity.class);

    @Test
    public void scoreUpdatesCorrectlyForRightAnswer() {
        // For testing, run some code on the activity to inject a known quiz question.
        activityScenarioRule.getScenario().onActivity(activity -> {
            // Suppose we set a known quiz question in the QuizViewModel.
            QuizViewModel viewModel = new QuizViewModel();
            // For example, set the question so that the correct answer is "Hank"
            gallerymodel testQuestion = new gallerymodel("Hank", R.drawable.hank);
            ArrayList<String> options = new ArrayList<>();
            options.add("Hank");
            options.add("Aegon");
            options.add("Lia");
            // Save these in the view model.
            viewModel.setCurrentQuestion(testQuestion);
            viewModel.setCurrentOptions(options);
            viewModel.setCorrectOptionIndex(0);
            viewModel.setAnswerCorrect(0);
            viewModel.setAnswerTotal(0);
            // You might have to set the ViewModel instance in the QuizActivity or inject it via a test dependency.
        });

        // Simulate selecting the correct answer ("Hank")
        onView(withText("Hank")).perform(click());
        // Click the Submit button
        onView(withId(R.id.buttonQuizSubmit)).perform(click());
        // Verify that the score TextView shows the updated score.
        onView(withId(R.id.textQuizScore)).check(matches(withText("Score: 1/1 (100.0 %)")));
    }

    @Test
    public void scoreUpdatesCorrectlyForWrongAnswer() {
        // Similar to the above test, inject a known question with correct answer "Hank"
        activityScenarioRule.getScenario().onActivity(activity -> {
            QuizViewModel viewModel = new QuizViewModel();
            gallerymodel testQuestion = new gallerymodel("Hank", R.drawable.hank);
            ArrayList<String> options = new ArrayList<>();
            options.add("Hank");
            options.add("Aegon");
            options.add("Lia");
            viewModel.setCurrentQuestion(testQuestion);
            viewModel.setCurrentOptions(options);
            viewModel.setCorrectOptionIndex(0);
            viewModel.setAnswerCorrect(0);
            viewModel.setAnswerTotal(0);
        });
        // Simulate selecting a wrong answer ("Aegon")
        onView(withText("Aegon")).perform(click());
        // Click the Submit button
        onView(withId(R.id.buttonQuizSubmit)).perform(click());
        // Verify that the score TextView shows the updated score.
        onView(withId(R.id.textQuizScore)).check(matches(withText("Score: 0/1 (0.0 %)")));
    }
}
