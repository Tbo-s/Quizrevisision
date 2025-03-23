package com.example.quizapp4;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> rule = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void release() {
        Intents.release();
    }

    @Test
    public void testQuizButton() {
        onView(withId(R.id.Quizbutton)).perform(click());
        intended(hasComponent(QuizActivity.class.getName()));
    }

    @Test
    public void testDeleteItem() {
        int cntBefore = getItemCount();
        onView(withId(R.id.rycyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, Dog_recyclerviewadapter.DogRecyclerViewAction.clickChildViewWithId(R.id.buttondelete)));
        int cntAfter = getItemCount();
        assertEquals(cntBefore-1, cntAfter);
    }

    public int getItemCount() {
        AtomicInteger cnt = new AtomicInteger();
        rule.getScenario().onActivity(a -> {
            RecyclerView rv = a.findViewById(R.id.rycyclerview);
            cnt.set(Objects.requireNonNull(rv.getAdapter()).getItemCount());
        });
        return cnt.get();
    }

    @Test
    public void testAddItem() {

    }
}
