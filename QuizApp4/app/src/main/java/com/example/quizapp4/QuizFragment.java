package com.example.quizapp4;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class QuizFragment extends Fragment {

    // UI elements
    private ImageView imageView;
    private RadioGroup radioGroup;
    private RadioButton option1, option2, option3;
    private Button buttonSubmit, buttonNext, buttonEnd;
    private TextView textResult, textScore;

    // ViewModel to persist quiz state
    private QuizViewModel viewModel;
    private ArrayList<gallerymodel> images;
    private Random rand = new Random();
    private String correctName;
    private int idCorrect, idCurrent;

    public QuizFragment() {
        // Required empty public constructor
    }

    public QuizViewModel getViewModel() {
        return viewModel;
    }

    public String getCorrectName() {
        return correctName;
    }

    public int getIdCorrect() {
        return idCorrect;
    }

    public int getIdCurrent() {
        return idCurrent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout. Android will automatically load the appropriate layout
        // from res/layout (portrait) or res/layout-land (landscape) if available.
        return inflater.inflate(R.layout.quiz_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Bind UI elements
        imageView = view.findViewById(R.id.imageViewQuiz);
        radioGroup = view.findViewById(R.id.radioQuizAnswers);
        option1 = view.findViewById(R.id.buttonQuizOption1);
        option2 = view.findViewById(R.id.buttonQuizOption2);
        option3 = view.findViewById(R.id.buttonQuizOption3);
        buttonSubmit = view.findViewById(R.id.buttonQuizSubmit);
        buttonNext = view.findViewById(R.id.buttonQuizNext);
        buttonEnd = view.findViewById(R.id.buttonQuizEnd);
        textResult = view.findViewById(R.id.textQuizResult);
        textScore = view.findViewById(R.id.textQuizScore);

        // Get the ViewModel from the hosting Activity
        viewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        // Initialize images if not yet set
        if (viewModel.getImages() == null) {
            ArrayList<gallerymodel> imgList = null;
            if (getArguments() != null) {
                imgList = (ArrayList<gallerymodel>) getArguments().getSerializable("galleryList");
            }
            // If there are fewer than 3 images, use default items
            if (imgList == null || imgList.size() < 3) {
                imgList = new ArrayList<>();
                imgList.add(new gallerymodel("Aegon", R.drawable.eagon));
                imgList.add(new gallerymodel("Hank", R.drawable.hank));
                imgList.add(new gallerymodel("Lia", R.drawable.lia));
            }
            viewModel.setImages(imgList);
        }
        images = viewModel.getImages();

        // Listener for radio group changes
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> idCurrent = checkedId);

        // Set listener for the Submit button
        buttonSubmit.setOnClickListener(v -> {
            updateScore();
            showAfterAnswer();
            // Optionally disable the Submit button to prevent multiple submissions
            buttonSubmit.setEnabled(false);
        });

        // Set listener for the Next button
        buttonNext.setOnClickListener(v -> {
            generateQuestion();
            hideBeforeAnswer();
            // Re-enable the Submit button for the new question
            buttonSubmit.setEnabled(true);
        });

        // Add the End Quiz button listener here.
        buttonEnd.setOnClickListener(v -> {
            // End the quiz by finishing the current activity.
            requireActivity().finish();
            // Alternatively, you could start another activity:

        });

        // Set initial score display
        textScore.setText(getString(R.string.quiz_score, viewModel.getAnswerCorrect(),
                viewModel.getAnswerTotal(), getString(R.string.quiz_score_init)));

        // Restore the current question if it exists; otherwise, generate a new one.
        if (viewModel.getCurrentQuestion() == null) {
            generateQuestion();
        } else {
            restoreQuestion();
        }
    }

    /**
     * Generates a new quiz question and saves its state in the ViewModel.
     */
    private void generateQuestion() {
        int size = images.size();
        if (size < 3) return; // Safety check

        // Select the correct item and two distractors randomly.
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

        // Prepare and shuffle options.
        ArrayList<String> options = new ArrayList<>();
        options.add(correctName);
        options.add(distractor1);
        options.add(distractor2);
        Collections.shuffle(options);

        // Set the options on the radio buttons.
        option1.setText(options.get(0));
        option2.setText(options.get(1));
        option3.setText(options.get(2));

        // Determine which option is correct.
        int correctOptionIndex = options.indexOf(correctName);
        if (correctOptionIndex == 0) {
            idCorrect = option1.getId();
        } else if (correctOptionIndex == 1) {
            idCorrect = option2.getId();
        } else {
            idCorrect = option3.getId();
        }

        // Display the corresponding image.
        Uri uri = correctItem.getImageUri();
        if (uri != null) {
            imageView.setImageURI(uri);
        } else {
            imageView.setImageResource(correctItem.getImageResource());
        }
        radioGroup.clearCheck();

        // Save the current question in the ViewModel.
        viewModel.setCurrentQuestion(correctItem);
        viewModel.setCurrentOptions(options);
        viewModel.setCorrectOptionIndex(correctOptionIndex);
    }

    /**
     * Restores the current question state from the ViewModel.
     */
    private void restoreQuestion() {
        gallerymodel current = viewModel.getCurrentQuestion();
        if (current == null) return;
        correctName = current.getNameOfDog();
        ArrayList<String> options = viewModel.getCurrentOptions();
        if (options == null || options.size() < 3) return;

        option1.setText(options.get(0));
        option2.setText(options.get(1));
        option3.setText(options.get(2));

        int correctOptionIndex = viewModel.getCorrectOptionIndex();
        if (correctOptionIndex == 0) {
            idCorrect = option1.getId();
        } else if (correctOptionIndex == 1) {
            idCorrect = option2.getId();
        } else {
            idCorrect = option3.getId();
        }

        Uri uri = current.getImageUri();
        if (uri != null) {
            imageView.setImageURI(uri);
        } else {
            imageView.setImageResource(current.getImageResource());
        }
        radioGroup.clearCheck();
    }

    /**
     * Checks if the selected answer is correct.
     */
    private boolean wasCorrect() {
        return idCorrect == idCurrent;
    }

    /**
     * Updates the score and displays a result.
     */
    private void updateScore() {
        viewModel.setAnswerTotal(viewModel.getAnswerTotal() + 1);
        if (wasCorrect()) {
            textResult.setText(R.string.correct);
            viewModel.setAnswerCorrect(viewModel.getAnswerCorrect() + 1);
        } else {
            textResult.setText(getString(R.string.incorrect, correctName));
        }
        double percentage = viewModel.getAnswerCorrect() * 100.0 / viewModel.getAnswerTotal();
        String formattedPercentage = String.format(Locale.getDefault(), "%.1f %%", percentage);
        textScore.setText(getString(R.string.quiz_score, viewModel.getAnswerCorrect(), viewModel.getAnswerTotal(), formattedPercentage));
    }

    /**
     * Hides elements that should not be visible before answering.
     */
    private void hideBeforeAnswer() {
        textResult.setVisibility(View.INVISIBLE);
        buttonEnd.setVisibility(View.INVISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        buttonSubmit.setVisibility(View.VISIBLE);
    }

    /**
     * Reveals elements after an answer has been given.
     */
    private void showAfterAnswer() {
        textResult.setVisibility(View.VISIBLE);
        buttonEnd.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        buttonSubmit.setVisibility(View.INVISIBLE);
    }
}
