package com.griffith.assignment2;


/**
 * Created by 42900 on 28/03/2017 for Assignment2.
 */

public class ReversiGameLogic {
    public static int SIZE_BOARD = 8;

    public enum Pawn{
        EMPTY,
        WHITE,
        BLACK
    }
    private Pawn current_player = Pawn.WHITE;
    private Reversi graphic_board;
    private Pawn[][] board = new Pawn[SIZE_BOARD][SIZE_BOARD];

    public ReversiGameLogic(Reversi graphic_board) {
        this.graphic_board = graphic_board;
        for (int i = 0 ; i < SIZE_BOARD; ++i){
            for (int j = 0 ; j < SIZE_BOARD; ++j){
                board[i][j] = Pawn.EMPTY;
            }
        }
        board[3][3] = Pawn.WHITE;
        board[4][3] = Pawn.BLACK;
        board[3][4] = Pawn.BLACK;
        board[4][4] = Pawn.WHITE;

        this.graphic_board.updateBoard(this.board);
    }

    public void updateTouchEvent(Coordinate coordinate){
        if (board[coordinate.getX()][coordinate.getY()] == Pawn.EMPTY) {
            board[coordinate.getX()][coordinate.getY()] = current_player;
            nextPlayer();
        }
        this.graphic_board.updateBoard(this.board);
    }

    private void nextPlayer(){
        if (current_player == Pawn.WHITE)
            current_player = Pawn.BLACK;
        else
            current_player = Pawn.WHITE;
    }
}
