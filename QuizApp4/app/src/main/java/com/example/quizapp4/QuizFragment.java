package com.example.quizapp4;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorEventListener2;
import android.hardware.SensorManager;
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
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Random;

public class QuizFragment extends Fragment {

    private ImageView imageView;
    private RadioGroup radioGroup;
    private RadioButton option1, option2, option3;
    private Button buttonSubmit, buttonNext, buttonEnd;
    private TextView textResult, textScore;

    private QuizViewModel viewModel;
    private ArrayList<gallerymodel> images;
    private Random rand = new Random();
    private String correctName;
    private int idCorrect, idCurrent;

    // Sensor shake detection
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private SensorEventListener shakeListener;

    // Batching
    private Button buttonFlush;
    private TextView textFlushLog;
    private SensorEventListener2 batchListener;
    private int batchedEventCount = 0;


    public QuizFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.quiz_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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

        viewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);

        if (viewModel.getImages() == null) {
            ArrayList<gallerymodel> imgList = null;
            if (getArguments() != null) {
                imgList = (ArrayList<gallerymodel>) getArguments().getSerializable("galleryList");
            }
            if (imgList == null || imgList.size() < 3) {
                imgList = new ArrayList<>();
                imgList.add(new gallerymodel("Aegon", R.drawable.eagon));
                imgList.add(new gallerymodel("Hank", R.drawable.hank));
                imgList.add(new gallerymodel("Lia", R.drawable.lia));
            }
            viewModel.setImages(imgList);
        }
        images = viewModel.getImages();

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> idCurrent = checkedId);

        buttonSubmit.setOnClickListener(v -> {
            updateScore();
            showAfterAnswer();
            buttonSubmit.setEnabled(false);
        });

        buttonNext.setOnClickListener(v -> {
            generateQuestion();
            hideBeforeAnswer();
            buttonSubmit.setEnabled(true);
        });

        buttonEnd.setOnClickListener(v -> requireActivity().finish());

        textScore.setText(getString(R.string.quiz_score, viewModel.getAnswerCorrect(),
                viewModel.getAnswerTotal(), getString(R.string.quiz_score_init)));

        if (viewModel.getCurrentQuestion() == null) {
            generateQuestion();
        } else {
            restoreQuestion();
        }

        // --- Shake detection setup ---
        sensorManager = (SensorManager) requireActivity().getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        shakeListener = new SensorEventListener() {
            private static final float SHAKE_THRESHOLD_GRAVITY = 5F;
            private static final int SHAKE_SLOP_TIME_MS = 500;
            private long lastShakeTime = 0;

            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                float gX = x / SensorManager.GRAVITY_EARTH;
                float gY = y / SensorManager.GRAVITY_EARTH;
                float gZ = z / SensorManager.GRAVITY_EARTH;

                float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);

                if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                    long now = System.currentTimeMillis();
                    if (lastShakeTime + SHAKE_SLOP_TIME_MS > now) return;
                    lastShakeTime = now;

                    // Skip to next question without answering
                    generateQuestion();
                    hideBeforeAnswer();
                    buttonSubmit.setEnabled(true);

                    Toast.makeText(getContext(), "Shake detected! Skipped to next question.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };
        buttonFlush = view.findViewById(R.id.buttonFlush);
        textFlushLog = view.findViewById(R.id.textFlushLog);

        Sensor accelerometerBatch = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        batchListener = new SensorEventListener2() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                batchedEventCount++;
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}

            @Override
            public void onFlushCompleted(Sensor sensor) {
                requireActivity().runOnUiThread(() -> {
                    textFlushLog.setText("Batched Events: " + batchedEventCount);
                    Toast.makeText(getContext(), "Flush complete!", Toast.LENGTH_SHORT).show();
                    batchedEventCount = 0; // reset for next round
                });
            }
        };

// Register the sensor with batching (5s latency)
        sensorManager.registerListener(
                batchListener,
                accelerometerBatch,
                SensorManager.SENSOR_DELAY_NORMAL,
                5_000_000 // 5 seconds in microseconds
        );

// Flush button manually triggers delivery of batched events
        buttonFlush.setOnClickListener(v -> {
            sensorManager.flush(batchListener);
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager != null && accelerometer != null && shakeListener != null) {
            sensorManager.registerListener(shakeListener, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager != null && shakeListener != null) {
            sensorManager.unregisterListener(shakeListener);
        }
    }

    private void generateQuestion() {
        int size = images.size();
        if (size < 3) return;

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

        ArrayList<String> options = new ArrayList<>();
        options.add(correctName);
        options.add(distractor1);
        options.add(distractor2);
        Collections.shuffle(options);

        option1.setText(options.get(0));
        option2.setText(options.get(1));
        option3.setText(options.get(2));

        int correctOptionIndex = options.indexOf(correctName);
        if (correctOptionIndex == 0) {
            idCorrect = option1.getId();
        } else if (correctOptionIndex == 1) {
            idCorrect = option2.getId();
        } else {
            idCorrect = option3.getId();
        }

        Uri uri = correctItem.getImageUri();
        if (uri != null) {
            imageView.setImageURI(uri);
        } else {
            imageView.setImageResource(correctItem.getImageResource());
        }

        radioGroup.clearCheck();
        viewModel.setCurrentQuestion(correctItem);
        viewModel.setCurrentOptions(options);
        viewModel.setCorrectOptionIndex(correctOptionIndex);
    }

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

    private boolean wasCorrect() {
        return idCorrect == idCurrent;
    }

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

    private void hideBeforeAnswer() {
        textResult.setVisibility(View.INVISIBLE);
        buttonEnd.setVisibility(View.INVISIBLE);
        buttonNext.setVisibility(View.INVISIBLE);
        buttonSubmit.setVisibility(View.VISIBLE);
    }

    private void showAfterAnswer() {
        textResult.setVisibility(View.VISIBLE);
        buttonEnd.setVisibility(View.VISIBLE);
        buttonNext.setVisibility(View.VISIBLE);
        buttonSubmit.setVisibility(View.INVISIBLE);
    }
}
