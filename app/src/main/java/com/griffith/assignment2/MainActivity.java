package com.griffith.assignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Reversi graphic_board;
    private ReversiGameLogic reversiGameLogic;

    private Button reset_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graphic_board = (Reversi)findViewById(R.id.reversi_board);
        reset_button = (Button)findViewById(R.id.reset_button);

        graphic_board.post(new Runnable() {
            @Override
            public void run() {
                reversiGameLogic = new ReversiGameLogic(graphic_board);
                graphic_board.addGameLogic(reversiGameLogic);
            }
        });

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reversiGameLogic.resetGame();
            }
        });
    }
}
