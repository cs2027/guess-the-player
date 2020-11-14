package com.song.christopher.guesstheplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// Activity to show an image related to the current player (player's team logo/colors & player name)
public class ImageActivity extends AppCompatActivity {
    // Variables to hold the current state of the quiz
    private List<PlayerStats> finalPlayers = new ArrayList<>();
    private int currentPlayerIndex;
    private int pastPlayerIndex;
    private PlayerStats pastPlayer;
    private int currentScore;
    private int questionNumber;
    private int numQuestions;

    // Child views in the corresponding XML layout file
    private LinearLayout topSectionLayout;
    private ImageView teamImage;
    private LinearLayout bottomSectionLayout;
    private TextView firstNameDisplay;
    private TextView lastNameDisplay;
    private Button continueButton;

    // Variables related to the current player's team
    private int teamImageID;
    private int topColorID;
    private String topColorStr;
    private int bottomColorID;
    private String bottomColorStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        // Obtain data regarding the current state of the quiz
        Intent intent = getIntent();
        finalPlayers = intent.getParcelableArrayListExtra("finalPlayersList");
        currentPlayerIndex = intent.getIntExtra("currentPlayerIndex", 0);
        pastPlayerIndex = currentPlayerIndex - 1;
        pastPlayer = finalPlayers.get(pastPlayerIndex);
        currentScore = intent.getIntExtra("currentScore", 0);
        questionNumber = intent.getIntExtra("questionNumber", 1);
        numQuestions = intent.getIntExtra("numQuestions", 0);

        // Initialize the child views of the corresponding XML layout file
        topSectionLayout = findViewById(R.id.layout_top_section);
        teamImage = findViewById(R.id.past_player_team_image);
        bottomSectionLayout = findViewById(R.id.layout_bottom_section);
        firstNameDisplay = findViewById(R.id.player_first_name);
        lastNameDisplay = findViewById(R.id.player_last_name);
        continueButton = findViewById(R.id.continue_button);

        // Obtain an image of the current player's team logo, as well as his team colors
        teamImageID = getResources().getIdentifier(
                "logo_" + pastPlayer.getTeamID(),
                "drawable",
                getPackageName()
        );

        topColorID = getResources().getIdentifier(
                "top_" + pastPlayer.getTeamID(),
                "color",
                getPackageName()
        );

        topColorStr = getResources().getString(topColorID);

        bottomColorID = getResources().getIdentifier(
                "bottom_" + pastPlayer.getTeamID(),
                "color",
                getPackageName()
        );

        bottomColorStr = getResources().getString(bottomColorID);

        // Display the team logo & colors, along with the player's name
        teamImage.setImageResource(teamImageID);
        topSectionLayout.setBackgroundColor(Color.parseColor(topColorStr));
        bottomSectionLayout.setBackgroundColor(Color.parseColor(bottomColorStr));

        firstNameDisplay.setText(pastPlayer.getFirstName());
        lastNameDisplay.setText(pastPlayer.getLastName());

        // Go here if we're not yet done with the quiz
        if (questionNumber <= numQuestions) {
            continueButton.setText("Next Question");

            // Return to the main quiz activity, passing in data regarding the current state of the quiz via an Intent
            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = getApplicationContext();
                    Intent intent = new Intent(context, GameActivity.class);
                    intent.putParcelableArrayListExtra("finalPlayersList", (ArrayList<? extends Parcelable>) finalPlayers);
                    intent.putExtra("currentPlayerIndex", currentPlayerIndex);
                    intent.putExtra("currentScore", currentScore);
                    intent.putExtra("questionNumber", questionNumber);
                    intent.putExtra("numQuestions", numQuestions);

                    startActivity(intent);
                }
            });
        } else {
            continueButton.setText("Finish Quiz");

            // If we're done with the quiz, go to the 'FinishGameActivity'
            continueButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = getApplicationContext();
                    Intent intent = new Intent(context, FinishGameActivity.class);
                    intent.putExtra("finalScore", currentScore);
                    intent.putExtra("numQuestions", numQuestions);

                    startActivity(intent);
                }
            });
        }
    }
}