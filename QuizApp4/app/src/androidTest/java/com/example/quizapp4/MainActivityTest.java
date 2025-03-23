package com.example.quizapp4;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Before
    public void setup() {
        Intents.init();
        ActivityScenario.launch(MainActivity.class);
    }

    @Test
    public void testQuizButton() {
        onView(withId(R.id.Quizbutton)).perform(click());
        intended(hasComponent(QuizActivity.class.getName()));
    }

}
