package com.example.quizrevision;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class QuizFragment extends Fragment {

    private QuizViewModel quizViewModel;
    private ImageView quizImageView;
    private Button answerButton1, answerButton2, answerButton3;
    private Button quizScoreText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate de juiste layout (portrait of landscape via res/layout en res/layout-land)
        View view = inflater.inflate(R.layout.fragment_quiz, container, false);
        quizImageView = view.findViewById(R.id.quizImageView);
        answerButton1 = view.findViewById(R.id.answerButton1);
        answerButton2 = view.findViewById(R.id.answerButton2);
        answerButton3 = view.findViewById(R.id.answerButton3);
        quizScoreText = view.findViewById(R.id.quizScoreText);

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
        if (quizViewModel.getCurrentItem() == null) {
            quizViewModel.generateNewQuestion();
        }
        updateUI();

        View.OnClickListener onClickListener = v -> {
            Button clicked = (Button) v;
            String answer = clicked.getText().toString();
            quizViewModel.submitAnswer(answer);
            String result = answer.equals(quizViewModel.getCurrentItem().name)
                    ? "Correct!" : "Fout! Juist: " + quizViewModel.getCurrentItem().name;
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            quizViewModel.generateNewQuestion();
            updateUI();
        };

        answerButton1.setOnClickListener(onClickListener);
        answerButton2.setOnClickListener(onClickListener);
        answerButton3.setOnClickListener(onClickListener);

        return view;
    }

    private void updateUI() {
        GalleryItem currentItem = quizViewModel.getCurrentItem();
        if (currentItem != null) {
            quizImageView.setImageURI(Uri.parse(currentItem.uri));
        }
        if (quizViewModel.getCurrentAlternatives() != null && quizViewModel.getCurrentAlternatives().size() >= 3) {
            answerButton1.setText(quizViewModel.getCurrentAlternatives().get(0));
            answerButton2.setText(quizViewModel.getCurrentAlternatives().get(1));
            answerButton3.setText(quizViewModel.getCurrentAlternatives().get(2));
        }
        quizScoreText.setText("Score: " + quizViewModel.getScore() + "/" + quizViewModel.getAttempts());
    }
}
