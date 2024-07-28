package com.example.memorygame;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorygame.Card;
import com.example.memorygame.CardAdapter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CardAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private TextView timerTextView;
    private CountDownTimer countDownTimer;
    private MaterialButton startButton;

    private List<Card> cards;
    private CardAdapter adapter;

    private boolean isTimerRunning = false;
    private int matchedPairs = 0;
    private  boolean isGameRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        timerTextView = findViewById(R.id.timerTextView);
        startButton = findViewById(R.id.start);


        // Initialize cards
        initializeCards();

        // Set up RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        adapter = new CardAdapter(cards, this);
        recyclerView.setAdapter(adapter);

        if(!isTimerRunning){
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startTimer(30000); // 30 seconds

                }


            });
        }else {
            startButton.setOnClickListener(null);
        }


        // Start timer
    }

    private void initializeCards() {
        cards = new ArrayList<>();
        // Add pairs of cards (for simplicity, assuming 8 pairs)
        for (int i = 0; i < 8; i++) {
            cards.add(new Card(i));
            cards.add(new Card(i));

        }
        // Shuffle the cards
        Collections.shuffle(cards);
    }

    private void startTimer(long millisInFuture) {
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {

            public void onTick(long millisUntilFinished) {
                timerTextView.setText("Time remaining: " + millisUntilFinished / 1000);
            }

            public void onFinish() {
                timerTextView.setText("Time's up!");
                isTimerRunning = false;
                // Implement logic to end the game
                endGame();
            }
        }.start();

        isTimerRunning = true;
    }

    private void endGame() {
        // Display game over message or handle game ending logic
        // For simplicity, we reset the activity here



//        recreate(); // Reset the activity to start a new game
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    @Override
    public void onItemClick(int position) {
        if (!isTimerRunning) {
            return; // Game is not running, ignore clicks
        }

        Card clickedCard = cards.get(position);

        if (!clickedCard.isMatched() && clickedCard.isVisible()) {
            // Flip the card
            clickedCard.setVisible(false);
            adapter.notifyItemChanged(position);

            // Check for a match with another visible card
            checkForMatch(clickedCard, position);
        }
    }

    private void checkForMatch(Card clickedCard, int position) {
        for (int i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            if (!card.isMatched() && card.isVisible() && card.getId() == clickedCard.getId() && i != position) {
                // Found a match
                card.setMatched(true);
                clickedCard.setMatched(true);

                // Increase matched pairs count
                matchedPairs++;

                // Check if all pairs are matched
                if (matchedPairs == 8) { // Assuming there are 8 pairs
                    // Game over, player wins
                    timerTextView.setText("You win!");
                    isTimerRunning = false;
                    endGame();
                    return;
                }
            }
        }

        // No match found, flip back after a short delay
        recyclerView.postDelayed(() -> {
            clickedCard.setVisible(true);
            adapter.notifyItemChanged(position);
        }, 1000); // Adjust delay as needed
    }
}
