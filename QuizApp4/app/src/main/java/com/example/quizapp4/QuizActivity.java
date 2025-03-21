package com.example.quizapp4;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {

    // Instance of our ViewModel
    private QuizViewModel viewModel;

    // Random number generator for quiz questions
    Random rand = new Random();
    String correctName;
    int idCorrect;
    int idCurrent;

    // UI elements
    TextView textScore;
    TextView textResult;
    Button buttonSubmit;
    Button buttonEnd;
    Button buttonNext;
    RadioButton buttonOpt1;
    RadioButton buttonOpt2;
    RadioButton buttonOpt3;
    ArrayList<RadioButton> optButtons;
    RadioGroup radioGroup;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);

        // Adjust for system insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve the ViewModel instance
        viewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        // If this is the first time (or if the images are not yet set), initialize the list:
        if (viewModel.getImages() == null) {
            ArrayList<gallerymodel> images = getGalleryListFromIntent();
            if (images.size() < 3) {
                images = new ArrayList<>();
                images.add(new gallerymodel("Aegon", R.drawable.eagon));
                images.add(new gallerymodel("Hank", R.drawable.hank));
                images.add(new gallerymodel("Lia", R.drawable.lia));
            }
            viewModel.setImages(images);
        }

        // Bind UI elements
        textScore = findViewById(R.id.textQuizScore);
        textResult = findViewById(R.id.textQuizResult);
        buttonEnd = findViewById(R.id.buttonQuizEnd);
        buttonNext = findViewById(R.id.buttonQuizNext);
        buttonSubmit = findViewById(R.id.buttonQuizSubmit);
        buttonOpt1 = findViewById(R.id.buttonQuizOption1);
        buttonOpt2 = findViewById(R.id.buttonQuizOption2);
        buttonOpt3 = findViewById(R.id.buttonQuizOption3);
        image = findViewById(R.id.imageView);
        radioGroup = findViewById(R.id.radioQuizAnsweres);

        // Add the RadioButtons to a list for easier handling
        optButtons = new ArrayList<>();
        optButtons.add(buttonOpt1);
        optButtons.add(buttonOpt2);
        optButtons.add(buttonOpt3);

        // Listener for changes in the RadioGroup
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> idCurrent = checkedId);

        // Set listeners for quiz buttons
        buttonSubmit.setOnClickListener(v -> {
            showAfterAnswer();
            updateScore();
        });

        buttonNext.setOnClickListener(v -> {
            generateQuestion();
            hideBeforeAnswer();
        });

        buttonEnd.setOnClickListener(v -> {
            Intent intent = new Intent(QuizActivity.this, MainActivity.class);
            intent.putExtra("galleryList", viewModel.getImages());
            startActivity(intent);
            finish();
        });

        // Set the initial score display
        textScore.setText(getString(R.string.quiz_score, viewModel.getAnswerCorrect(), viewModel.getAnswerTotal(), getString(R.string.quiz_score_init)));

        // Start the quiz by hiding post-answer elements and generating the first question
        hideBeforeAnswer();
        generateQuestion();
    }

    /**
     * Safely retrieves the gallery list from the Intent extra.
     * It checks if the extra exists and if every element is an instance of gallerymodel.
     */
    private ArrayList<gallerymodel> getGalleryListFromIntent() {
        Serializable extra = getIntent().getSerializableExtra("galleryList");
        ArrayList<gallerymodel> images = new ArrayList<>();
        if (extra instanceof ArrayList<?>) {
            for (Object o : (ArrayList<?>) extra) {
                if (o instanceof gallerymodel) {
                    images.add((gallerymodel) o);
                }
            }
        }
        return images;
    }

    // Generates a new quiz question based on the images stored in the ViewModel
    protected void generateQuestion() {
        ArrayList<gallerymodel> images = viewModel.getImages();
        int size = images.size();
        if (size < 3) return; // Safety check

        // Randomly select the correct item and two distractors
        int correctIndex = rand.nextInt(size);
        gallerymodel correctItem = images.get(correctIndex);
        correctName = correctItem.getNameOfDog();

        int distractor1Index, distractor2Index;
        do {
            distractor1Index = rand.nextInt(size);
        } while (distractor1Index == correctIndex);
        do {
            distractor2Index = rand.nextInt(size);
        } while (distractor2Index == correctIndex || distractor2Index == distractor1Index);

        String distractor1 = images.get(distractor1Index).getNameOfDog();
        String distractor2 = images.get(distractor2Index).getNameOfDog();

        // Create a list of options and shuffle them
        ArrayList<String> options = new ArrayList<>();
        options.add(correctName);
        options.add(distractor1);
        options.add(distractor2);
        Collections.shuffle(options);

        // Set the text for each RadioButton
        buttonOpt1.setText(options.get(0));
        buttonOpt2.setText(options.get(1));
        buttonOpt3.setText(options.get(2));

        // Identify which RadioButton holds the correct answer
        int correctOptionIndex = options.indexOf(correctName);
        idCorrect = optButtons.get(correctOptionIndex).getId();

        // Display the corresponding image
        Uri uri = correctItem.getImageUri();
        if (uri != null) {
            image.setImageURI(uri);
        } else {
            image.setImageResource(correctItem.getImageResource());
        }

        // Clear any previous selection
        radioGroup.clearCheck();
    }

    // Checks if the selected answer is correct
    protected boolean wasCorrect() {
        return idCorrect == idCurrent;
    }

    // Updates the quiz score and shows the result
    protected void updateScore() {
        viewModel.setAnswerTotal(viewModel.getAnswerTotal() + 1);
        if (wasCorrect()) {
            textResult.setText(R.string.correct);
            viewModel.setAnswerCorrect(viewModel.getAnswerCorrect() + 1);
        } else {
            textResult.setText(getString(R.string.incorrect, correctName));
        }
        String percentage = String.format(Locale.getDefault(), "%.1f %%", viewModel.getAnswerCorrect() * 100.0 / viewModel.getAnswerTotal());

        textScore.setText(getString(R.string.quiz_score, viewModel.getAnswerCorrect(), viewModel.getAnswerTotal(), percentage));
    }

    // Hides UI elements that should not be visible before answering
    protected void hideBeforeAnswer() {
        textResult.setVisibility(View.INVISIBLE);
        buttonEnd.setVisibility(View.INVISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        buttonSubmit.setVisibility(View.VISIBLE);
    }

    // Reveals UI elements after an answer has been given
    protected void showAfterAnswer() {
        textResult.setVisibility(View.VISIBLE);
        buttonEnd.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        buttonSubmit.setVisibility(View.INVISIBLE);
    }
}
