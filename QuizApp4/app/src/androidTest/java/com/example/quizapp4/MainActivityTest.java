package com.example.quizapp4;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.content.Intent.ACTION_PICK;
import static android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertEquals;

import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

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

//    @Rule
//    public GrantPermissionRule permissionRule = GrantPermissionRule.grant(READ_MEDIA_IMAGES);

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
        Intent resultIntent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        Uri uri = Uri.parse("android.resource://com.example.quizapp4/" + R.drawable.eagon);
        resultIntent.setData(uri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(MainActivity.RESULT_OK, resultIntent);
        intending(hasAction(MediaStore.ACTION_PICK_IMAGES)).respondWith(result);
        int cntBefore = getItemCount();
        onView(withId(R.id.btnPickImage)).perform(click());
        onView(withHint("Name")).inRoot(isDialog()).check(matches(isDisplayed())).perform(typeText("newDog"));
        onView(withId(android.R.id.button1)).perform(click());
        int cntAfter = getItemCount();
        assertEquals(cntBefore+1, cntAfter);
    }
}
