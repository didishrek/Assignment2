package com.griffith.assignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private Reversi graphic_board;
    private ReversiGameLogic reversiGameLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graphic_board = (Reversi)findViewById(R.id.reversi_board);

        graphic_board.post(new Runnable() {
            @Override
            public void run() {
                reversiGameLogic = new ReversiGameLogic(graphic_board);
                graphic_board.addGameLogic(reversiGameLogic);
            }
        });
    }
}
