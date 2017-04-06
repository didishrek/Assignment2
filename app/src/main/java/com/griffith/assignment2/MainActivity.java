package com.griffith.assignment2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Reversi graphic_board;
    private ReversiGameLogic reversiGameLogic;

    private TextView current_player;
    private TextView points_black;
    private TextView points_white;
    private TextView msg_win;
    private Button reset_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        graphic_board = (Reversi)findViewById(R.id.reversi_board);
        reset_button = (Button)findViewById(R.id.reset_button);
        current_player = (TextView)findViewById(R.id.current_player);
        points_black = (TextView)findViewById(R.id.points_black);
        points_white = (TextView)findViewById(R.id.points_white);
        msg_win = (TextView)findViewById(R.id.msg_win);
        msg_win.setText("");

        graphic_board.post(new Runnable() {
            @Override
            public void run() {
                reversiGameLogic = new ReversiGameLogic(graphic_board, current_player, points_black, points_white, msg_win);
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
