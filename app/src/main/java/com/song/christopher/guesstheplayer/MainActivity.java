package com.song.christopher.guesstheplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

// Initial landing screen when user opens the app
public class MainActivity extends AppCompatActivity {
    // Child views in the corresponding XML layout file
    private Button startGameButton;
    private ImageView startImage;
    private int startImageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Allow the user to begin the game once he/she clicks the start button
        startGameButton = findViewById(R.id.start_game_button);
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, GameSetupActivity.class);
                context.startActivity(intent);
            }
        });

        // Add a randomly selected image to be displayed when the game loads
        startImage = findViewById(R.id.start_image);
        Random randomNumGenerator = new Random();
        int randomInt = randomNumGenerator.nextInt(10) + 1;

        startImageID = getResources().getIdentifier(
                "start_" + randomInt,
                "drawable",
                getPackageName()
        );

        startImage.setImageResource(startImageID);
    }
}