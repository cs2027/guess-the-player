package com.song.christopher.guesstheplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// Activity for the quiz itself
public class GameActivity extends AppCompatActivity {
    // Data related to the current quiz state
    private List<PlayerStats> finalPlayers = new ArrayList<>();
    private int currentPlayerIndex;
    private PlayerStats currentPlayer;
    private int currentScore;
    private int questionNumber;
    private int numQuestions;

    // Child views in the corresponding XML layout file
    private TextView currentScoreDisplay;
    private TextView questionNumberDisplay;
    private TextView statlinePoints;
    private TextView statlineAssists;
    private TextView statlineRebounds;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private Button submitButton;
    private Button hintOneButton;
    private Button hintTwoButton;
    private Button giveUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Obtain data about the current quiz state from the intent passed in
        Intent intent = getIntent();
        finalPlayers = intent.getParcelableArrayListExtra("finalPlayersList");
        currentPlayerIndex = intent.getIntExtra("currentPlayerIndex", 0);
        currentPlayer = finalPlayers.get(currentPlayerIndex);
        currentScore = intent.getIntExtra("currentScore", 0);
        questionNumber = intent.getIntExtra("questionNumber", 1);
        numQuestions = intent.getIntExtra("numQuestions", 0);

        // Initialize the child views in the corresponding XML layout file
        currentScoreDisplay = findViewById(R.id.current_score);
        questionNumberDisplay = findViewById(R.id.question_number);
        statlinePoints = findViewById(R.id.statline_points);
        statlineAssists = findViewById(R.id.statline_assists);
        statlineRebounds = findViewById(R.id.statline_rebounds);
        firstNameInput = findViewById(R.id.first_name_input);
        lastNameInput = findViewById(R.id.last_name_input);
        submitButton = findViewById(R.id.submit_button);
        hintOneButton = findViewById(R.id.hint_one_button);
        hintTwoButton = findViewById(R.id.hint_two_button);
        giveUpButton = findViewById(R.id.give_up_button);

        // Display the current quiz state (the user's score, question number, current player's statline)
        currentScoreDisplay.setText("Current Score: " + currentScore);
        questionNumberDisplay.setText("Question #" + questionNumber);
        statlinePoints.setText("Points: " + currentPlayer.getPoints());
        statlineAssists.setText("Assists: " + currentPlayer.getAssists());
        statlineRebounds.setText("Rebounds: " + currentPlayer.getRebounds());

        // Go here once the user submits a quiz answer
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (firstNameInput.getText().toString().replaceAll("[^a-zA-Z]", "").toLowerCase().
                        equals(currentPlayer.getFirstName().replaceAll("[^a-zA-Z]", "").toLowerCase()) &&
                lastNameInput.getText().toString().replaceAll("[^a-zA-Z]", "").toLowerCase()
                        .equals(currentPlayer.getLastName().replaceAll("[^a-zA-Z]", "").toLowerCase())) {
                    alertMessage("Correct!");

                    /* If the answer is correct, update the quiz state & pass along an intent to the 'ImageActivity',
                     * which displays an image related to the current player */
                    currentPlayerIndex++;
                    currentScore++;
                    questionNumber++;

                    if (questionNumber < numQuestions) {
                        currentPlayer = finalPlayers.get(currentPlayerIndex);
                    }

                    Context context = getApplicationContext();
                    Intent intent = new Intent(context, ImageActivity.class);
                    intent.putParcelableArrayListExtra("finalPlayersList", (ArrayList<? extends Parcelable>) finalPlayers);
                    intent.putExtra("currentPlayerIndex", currentPlayerIndex);
                    intent.putExtra("currentScore", currentScore);
                    intent.putExtra("questionNumber", questionNumber);
                    intent.putExtra("numQuestions", numQuestions);

                    startActivity(intent);
                } else {
                    alertMessage("Sorry, that's not correct. Maybe try a hint?");
                }

                // Clear the input fields for the current quiz question
                firstNameInput.setText("");
                lastNameInput.setText("");
            }
        });

        // Display a hint about the current player (height & position)
        hintOneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String position = "";

                if (currentPlayer.getPosition().equals("G")) {
                    position = "guard";
                } else if (currentPlayer.getPosition().equals("F")) {
                    position = "forward";
                } else if (currentPlayer.getPosition().equals("C")) {
                    position = "center";
                } else if(currentPlayer.getPosition().equals("F-C") || currentPlayer.getPosition().equals("C-F")) {
                    position = "forward/center";
                } else if (currentPlayer.getPosition().equals("F-G") || currentPlayer.getPosition().equals("G-F")) {
                    position = "guard/forward";
                }

                alertMessage("This player is " + currentPlayer.getHeightFeet() + " ft. " +
                        currentPlayer.getHeightInches() + " in. tall and plays as a " + position + ".");
            }
        });

        // Display a hint about the current player (team)
        hintTwoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertMessage("This player plays for the " + currentPlayer.getTeamName() + ".");
            }
        });

        // Go here if the user gives up on a question
        giveUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
                builder.setMessage("Are you sure you want to give up?");
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertMessage("The correct answer was " + currentPlayer.getFirstName() +
                                " " + currentPlayer.getLastName() + ".");

                        // Update the state of the quiz and show an image related to the current player (via an Intent to a new activity)
                        currentPlayerIndex++;
                        questionNumber++;

                        if (questionNumber < numQuestions) {
                            currentPlayer = finalPlayers.get(currentPlayerIndex);
                        }

                        Context context = getApplicationContext();
                        Intent intent = new Intent(context, ImageActivity.class);
                        intent.putParcelableArrayListExtra("finalPlayersList", (ArrayList<? extends Parcelable>) finalPlayers);
                        intent.putExtra("currentPlayerIndex", currentPlayerIndex);
                        intent.putExtra("currentScore", currentScore);
                        intent.putExtra("questionNumber", questionNumber);
                        intent.putExtra("numQuestions", numQuestions);

                        startActivity(intent);
                    }
                });

                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


    }

    // Creates and displays an alert with a given message
    public void alertMessage(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
        builder.setMessage(s);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}