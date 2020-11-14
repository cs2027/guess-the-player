package com.song.christopher.guesstheplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/* Allows a user to input the parameters regarding the number of questions,
 * as well as the types of players he/she would like to be quizzed on */
public class GameSetupActivity extends AppCompatActivity {
    // Various lists to hold relevant player IDs and player objects
    private List<Integer> allPlayerIDs = new ArrayList<>();
    private List<Player> allPlayers = new ArrayList<>();
    private List<Integer> selectedPlayerIDs = new ArrayList<>();
    private List<PlayerStats> selectedPlayers = new ArrayList<>();
    private List<PlayerStats> shuffledPlayers = new ArrayList<>();
    private List<PlayerStats> finalPlayers = new ArrayList<>();
    private RequestQueue requestQueue; // Used to query API data
    private boolean allPlayersDone = false; // Prevents a quiz from being loaded before the original query for all player data finishes

    // Child views in corresponding XML layout file
    private EditText minPointsField;
    private EditText maxPointsField;
    private EditText minAssistsField;
    private EditText maxAssistsField;
    private EditText minRebsField;
    private EditText maxRebsField;
    private EditText numQuestionsField;
    private Button loadQuizButton;

    // User inputs regarding quiz parameters
    private int minPoints;
    private int maxPoints;
    private int minAssists;
    private int maxAssists;
    private int minRebs;
    private int maxRebs;
    private int numQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        // Initializes child views in corresponding XML layout file
        minPointsField = findViewById(R.id.min_points);
        maxPointsField = findViewById(R.id.max_points);
        minAssistsField = findViewById(R.id.min_assists);
        maxAssistsField = findViewById(R.id.max_assists);
        minRebsField = findViewById(R.id.min_rebounds);
        maxRebsField = findViewById(R.id.min_rebounds);
        numQuestionsField = findViewById(R.id.num_questions);
        loadQuizButton = findViewById(R.id.load_quiz_button);

        // Gets data about all players from the API
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        getAllPlayers();

        // blank --> java.lang.NumberFormatException


        // Go here once the user enters his/her desired quiz parameters
        loadQuizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Obtain the parameters inputted by the user
                    minPoints = Integer.parseInt(minPointsField.getText().toString());
                    minPoints = Integer.parseInt(minPointsField.getText().toString());
                    maxPoints = Integer.parseInt(maxPointsField.getText().toString());
                    minAssists = Integer.parseInt(minAssistsField.getText().toString());
                    maxAssists = Integer.parseInt(maxAssistsField.getText().toString());
                    minRebs = Integer.parseInt(minRebsField.getText().toString());
                    maxRebs = Integer.parseInt(maxRebsField.getText().toString());
                    numQuestions = Integer.parseInt(numQuestionsField.getText().toString());

                    // Get the players that fit the user's parameters if we have gotten our first list of all players
                    getSelectedPlayers(allPlayersDone);
                } catch (NumberFormatException e) {
                    alertMessage("Sorry - at least one of you inputs was invalid.");
                }
            }
        });
    }

    // Obtains a 'Player' object by his ID number
    public Player findPlayerByID(int id) {
        Player playerByID = null;

        for (Player player : allPlayers) {
            if (player.getId() == id) {
                playerByID = player;
            }
        }

        return playerByID;
    }

    // Method that gets players that fit specific statistical criteria
    public void getSelectedPlayers(boolean allPlayersDone) {
        // Must wait for list of all players to load first
        if (allPlayersDone == false) {
            alertMessage("You must wait for the list of all players to load before narrowing down the list.");
        } else {
            // Setting up the URL to query for API data
            String url = "https://www.balldontlie.io/api/v1/season_averages?season=2019";
            for (int id : allPlayerIDs) {
                url += "&player_ids[]=" + id;
            }

            // Request a JSONObject encapsulating our list of all players (we will narrow down this list soon...)
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                // Store the data returned from the API
                                // Also store the number of players matching the user's specified parameters
                                JSONArray allData = response.getJSONArray("data");
                                int count = 0;

                                // Loop over the list of all players and do the following...
                                for (int i = 0; i < allData.length(); i++) {
                                    // Obtain the current player's ID number, as well as his statistical averages (pts/asts/rebs)
                                    JSONObject currentPlayer = allData.getJSONObject(i);
                                    int currentPlayerID = currentPlayer.getInt("player_id");
                                    double numPoints = (Double) currentPlayer.get("pts");
                                    double numAssists = (Double) currentPlayer.get("ast");
                                    double numRebs = (Double) currentPlayer.get("reb");

                                    // See if the player fits the user's parameters
                                    boolean pointComparison = numPoints >= minPoints && numPoints <= maxPoints;
                                    boolean assistComparison = numAssists >= minAssists && numAssists <= maxAssists;
                                    boolean rebComparison = numRebs >= minRebs && numRebs <= maxRebs;

                                    // Go here if the current player fits the users parameters
                                    if (pointComparison || assistComparison || rebComparison) {
                                        // Update the number of successfully added players
                                        // Add the current player's ID to our ID list
                                        count++;
                                        selectedPlayerIDs.add(currentPlayerID);

                                        // Obtain all data about the current player
                                        Player player = findPlayerByID(currentPlayerID);
                                        String firstName = player.getFirstName();
                                        String lastName = player.getLastName();
                                        String position = player.getPosition();
                                        int heightFeet = player.getHeightFeet();
                                        int heightInches = player.getHeightInches();
                                        int weight = player.getWeight();
                                        int teamID = player.getTeamID();
                                        String teamName = player.getTeamName();
                                        double points = numPoints;
                                        double assists = numAssists;
                                        double rebounds = numRebs;

                                        // Add the current player to our list of 'successful' players
                                        selectedPlayers.add(new PlayerStats(
                                                currentPlayerID,
                                                firstName,
                                                lastName,
                                                position,
                                                heightFeet,
                                                heightInches,
                                                weight,
                                                teamID,
                                                teamName,
                                                points,
                                                assists,
                                                rebounds));
                                    }
                                }

                                // Make sure we have enough players to make a full quiz
                                if (count < numQuestions) {
                                    alertMessage("There are not enough players meeting the criteria you have specified. Please adjust your parameters.");
                                } else {
                                    // If we do have enough players, shuffle the players randomly and get the number of players specified by the user
                                    shuffledPlayers = selectedPlayers;
                                    Collections.shuffle(shuffledPlayers);

                                    for (int i = 0; i < numQuestions; i++) {
                                        finalPlayers.add(shuffledPlayers.get(i));
                                    }

                                    AlertDialog.Builder builder = new AlertDialog.Builder(GameSetupActivity.this);
                                    builder.setMessage("Please click 'Start Quiz' to begin your quiz. Good luck!");
                                    builder.setPositiveButton("Start Quiz", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();

                                            // Start the quiz with our aforementioned list of shuffled players
                                            Context context = getApplicationContext();
                                            Intent intent = new Intent(context, GameActivity.class);
                                            intent.putParcelableArrayListExtra("finalPlayersList", (ArrayList<? extends Parcelable>) finalPlayers);
                                            intent.putExtra("currentPlayerIndex", 0);
                                            intent.putExtra("currentScore", 0);
                                            intent.putExtra("questionNumber", 1);
                                            intent.putExtra("numQuestions", numQuestions);
                                            startActivity(intent);
                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                }
                            } catch (JSONException e) {
                                Log.e("GTPAppSelected", "JSON Error");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("GTPAppSelected", "Error obtaining player data");
                        }
                    });

            requestQueue.add(request);
        }
    }

    // Method to get list of all players (original pool of players)
    public void getAllPlayers() {
        Random rand = new Random();
        int startPage = rand.nextInt(267) + 1;

        for (int i = startPage; i < startPage + 14; i++) {
            String url = "https://www.balldontlie.io/api/v1/stats?seasons[]=2019&per_page=100&page=" + i;

            // Make a JSONObject request to query for a list of players
            JsonObjectRequest request = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONArray allData = response.getJSONArray("data");

                                // Loop over the returned data as follows...
                                for (int j = 0; j < allData.length(); j++) {
                                    // Obtain the current player, his team, and ID number
                                    JSONObject currentDataObj = allData.getJSONObject(j);
                                    JSONObject currentPlayer = currentDataObj.getJSONObject("player");
                                    JSONObject currentTeam = currentDataObj.getJSONObject("team");
                                    int currentPlayerID = currentPlayer.getInt("id");

                                    // Go here if we have not yet added the current player...
                                    if (! (allPlayerIDs.contains(currentPlayerID))) {
                                        /* Obtain data about the current player,
                                         * keeping track of whether any of his data fields are null/empty */
                                        boolean anyNullFields = false;
                                        String firstName;
                                        String lastName;
                                        String position;
                                        int heightFeet;
                                        int heightInches;
                                        int weight;
                                        int teamID;
                                        String teamName;

                                        if (! (currentPlayer.isNull("first_name"))) {
                                            firstName = currentPlayer.getString("first_name");
                                        } else {
                                            firstName = "";
                                            anyNullFields = true;
                                        }

                                        if (! (currentPlayer.isNull("last_name"))) {
                                            lastName = currentPlayer.getString("last_name");
                                        } else {
                                            lastName = "";
                                            anyNullFields = true;
                                        }

                                        if (! (currentPlayer.isNull("position"))) {
                                            position = currentPlayer.getString("position");
                                        } else {
                                            position = "";
                                            anyNullFields = true;
                                        }

                                        if (! (currentPlayer.isNull("height_feet"))) {
                                            heightFeet = currentPlayer.getInt("height_feet");
                                        } else {
                                            heightFeet = 0;
                                            anyNullFields = true;
                                        }

                                        if (! (currentPlayer.isNull("height_inches"))) {
                                            heightInches = currentPlayer.getInt("height_inches");
                                        } else {
                                            heightInches = 0;
                                            anyNullFields = true;
                                        }

                                        if (! (currentPlayer.isNull("weight_pounds"))) {
                                            weight = currentPlayer.getInt("weight_pounds");
                                        } else {
                                            weight = 0;
                                            anyNullFields = true;
                                        }

                                        if (! (currentPlayer.isNull("team_id"))) {
                                            teamID = currentPlayer.getInt("team_id");
                                        } else {
                                            teamID = 0;
                                            anyNullFields = true;
                                        }

                                        if (! (currentTeam.isNull("full_name"))) {
                                            teamName = currentTeam.getString("full_name");
                                        } else {
                                            teamName = "";
                                            anyNullFields = true;
                                        }

                                        // Only if none of the player's data fields are null, add the player and his ID to our lists
                                        if (! anyNullFields) {
                                            allPlayerIDs.add(currentPlayerID);

                                            allPlayers.add(new Player(
                                                    currentPlayerID,
                                                    firstName,
                                                    lastName,
                                                    position,
                                                    heightFeet,
                                                    heightInches,
                                                    weight,
                                                    teamID,
                                                    teamName));
                                        }
                                    }
                                }

                                // Let the activity know that we are done querying for all players
                                allPlayersDone = true;
                            } catch (JSONException e) {
                                Log.e("GTPAppAll", "JSON Error");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e("GTPAppAll", "Error obtaining player data");
                        }
                    });

            requestQueue.add(request);
        }
    }

    // Creates and displays an alert with a given message
    public void alertMessage(String s) {
        AlertDialog.Builder builder = new AlertDialog.Builder(GameSetupActivity.this);
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