package com.song.christopher.guesstheplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

// Activity at the end of the quiz
public class FinishGameActivity extends AppCompatActivity {
    // Fields related to user's performance on the quiz
    private int finalScore;
    private int numQuestions;

    // Child views in the corresponding XML layout file
    private TextView finalScoreDisplay;
    private Button newGameButton;
    private ImageView finishImage;
    private int finishImageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_game);

        // Obtain and display data regarding the user's performance on the quiz
        Intent intent = getIntent();
        finalScore = intent.getIntExtra("finalScore", 0);
        numQuestions = intent.getIntExtra("numQuestions", 0);
        finalScoreDisplay = findViewById(R.id.final_score);
        finalScoreDisplay.setText(String.valueOf(finalScore) + "/" + String.valueOf(numQuestions));

        // Button to start a new quiz
        newGameButton = findViewById(R.id.new_game_button);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        // Show a randomly generated NBA-related image
        finishImage = findViewById(R.id.finish_image);
        Random randomNumGenerator = new Random();
        int randomInt = randomNumGenerator.nextInt(10) + 1;

        finishImageID = getResources().getIdentifier(
                "end_" + randomInt,
                "drawable",
                getPackageName()
        );

        finishImage.setImageResource(finishImageID);

    }
}