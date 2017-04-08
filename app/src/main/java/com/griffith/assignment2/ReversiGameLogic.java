package com.griffith.assignment2;


import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class ReversiGameLogic {
    public static int SIZE_BOARD = 8;

    public enum Pawn{
        EMPTY,
        WHITE,
        BLACK
    }

    private TextView tw_current_player;
    private TextView tw_points_black;
    private TextView tw_points_white;
    private TextView msg_win;

    private int black;
    private int white;

    private Pawn current_player = Pawn.BLACK;
    private Reversi graphic_board;
    private Pawn[][] board = new Pawn[SIZE_BOARD][SIZE_BOARD];
    private ArrayList<Coordinate> limitChangeColor = new ArrayList<>();;

    public ReversiGameLogic(Reversi graphic_board, TextView current_player, TextView points_black, TextView points_white, TextView msg_win) {
        this.tw_current_player = current_player;
        this.tw_points_black = points_black;
        this.tw_points_white = points_white;
        this.graphic_board = graphic_board;
        this.msg_win = msg_win;
        resetGame();
    }

    public void resetGame(){
        msg_win.setText("");
        current_player = Pawn.BLACK;
        this.tw_current_player.setText(R.string.black);
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
        updateCountPoints();
    }

    private void updateCountPoints(){
        black = 0;
        white = 0;
        for (int i = 0 ; i < SIZE_BOARD; ++i) {
            for (int j = 0; j < SIZE_BOARD; ++j) {
                if (board[i][j] == Pawn.BLACK){
                    black++;
                } else if (board[i][j] == Pawn.WHITE){
                    white++;
                }
            }
        }
        this.tw_points_black.setText(String.valueOf(black));
        this.tw_points_white.setText(String.valueOf(white));
    }

    public void updateTouchEvent(Coordinate coordinate){
        Boolean finished = false;
        if (board[coordinate.getX()][coordinate.getY()] == Pawn.EMPTY) {
            if (playOn(coordinate))
                return;
            for (Coordinate c : limitChangeColor){
                changeColorOnLine(coordinate, c);
            }

            board[coordinate.getX()][coordinate.getY()] = current_player;
            nextPlayer();
            updateCountPoints();
            if (!canPlay()){
                nextPlayer();
                if (!canPlay()){
                    gameFinished();
                }
            }
        }
        this.graphic_board.updateBoard(this.board);
    }

    private void gameFinished(){
        if( black > white){
            for (int i = 0 ; i < SIZE_BOARD; ++i){
                for (int j = 0 ; j < SIZE_BOARD; ++j){
                    board[i][j] = Pawn.BLACK;
                }
                msg_win.setText(R.string.black_win);
            }
        } else if (white > black){
            for (int i = 0 ; i < SIZE_BOARD; ++i){
                for (int j = 0 ; j < SIZE_BOARD; ++j){
                    board[i][j] = Pawn.WHITE;
                }
            }
            msg_win.setText(R.string.white_win);

        } else {
            msg_win.setText(R.string.loose);
        }
    }

    private boolean canPlay(){
        for (int i = 0 ; i < SIZE_BOARD; ++i) {
            for (int j = 0; j < SIZE_BOARD; ++j) {
                if (board[i][j] == Pawn.EMPTY){
                    if (!playOn(new Coordinate(i, j)))
                        return true;
                }
            }
        }
        return false;
    }

    private boolean checkOutOfBound(int x, int y){
        return (x >= 0 && x < SIZE_BOARD) && (y >= 0 && y < SIZE_BOARD);
    }

    private boolean isNotEmptyAndNotSameColor(Pawn p){
        return (p != Pawn.EMPTY && p != current_player);
    }

    private boolean checkLine(Coordinate origin, Coordinate target){
        int count = 0;
        Coordinate stop = null;
        if (origin.getX() == target.getX()){ //vertical
            for (int i = origin.getY() + 1 ; i <= target.getY(); i++){
                if (!checkOutOfBound(origin.getX(), i)) break;
                if(isNotEmptyAndNotSameColor(board[origin.getX()][i]))
                    count++;
                else if (board[origin.getX()][i] != Pawn.EMPTY){
                    stop = new Coordinate(origin.getX(), i);
                    break;
                }
            }
            for (int i = origin.getY() - 1 ; i >= target.getY(); i--){
                if (!checkOutOfBound(origin.getX(), i)) break;
                if(isNotEmptyAndNotSameColor(board[origin.getX()][i]))
                    count++;
                else if (board[origin.getX()][i] != Pawn.EMPTY){
                    stop = new Coordinate(origin.getX(), i);
                    break;
                }
            }
        } else if (origin.getY() == target.getY()){ //horizontal
            for (int i = origin.getX() + 1; i <= target.getX(); i++){
                if (!checkOutOfBound(i, origin.getY())) break;
                if (isNotEmptyAndNotSameColor(board[i][origin.getY()]))
                    count++;
                else if (board[i][origin.getY()] != Pawn.EMPTY){
                    stop = new Coordinate(i, origin.getY());
                    break;
                }
            }
            for (int i = origin.getX() - 1; i >= target.getX(); i--){
                if (!checkOutOfBound(i, origin.getY())) break;
                if (isNotEmptyAndNotSameColor(board[i][origin.getY()]))
                    count++;
                else if (board[i][origin.getY()] != Pawn.EMPTY){
                    stop = new Coordinate(i, origin.getY());
                    break;
                }
            }
        } else if (areAlignDiagonal(origin, target)){ //diagonal
            if (origin.getX() < target.getX() && origin.getY() < target.getY()){
                for (int i = 1; origin.getX() + i <= target.getX() && origin.getY() + i <= target.getY(); ++i){
                    if (!checkOutOfBound(origin.getX() + i, origin.getY() + i)) break;
                    if (isNotEmptyAndNotSameColor(board[origin.getX() + i][origin.getY() + i]))
                        count++;
                    else if (board[origin.getX() + i][origin.getY() + i] != Pawn.EMPTY){
                        stop = new Coordinate(origin.getX() + i, origin.getY() + i);
                        break;
                    }
                }
            } else if (origin.getX() > target.getX() && origin.getY() < target.getY()){
                for (int i = 1; origin.getX() - i >= target.getX() && origin.getY() + i <= target.getY(); ++i){
                    if (!checkOutOfBound(origin.getX() - i, origin.getY() + i)) break;
                    if (isNotEmptyAndNotSameColor(board[origin.getX() - i][origin.getY() + i]))
                        count++;
                    else if (board[origin.getX() - i][origin.getY() + i] != Pawn.EMPTY){
                        stop = new Coordinate(origin.getX() - i, origin.getY() + i);
                        break;
                    }
                }
            } else if (origin.getX() > target.getX() && origin.getY() > target.getY()){
                for (int i = 1; origin.getX() - i >= target.getX() && origin.getY() - i >= target.getY(); ++i){
                    if (!checkOutOfBound(origin.getX() - i, origin.getY() - i)) break;
                    if (isNotEmptyAndNotSameColor(board[origin.getX() - i][origin.getY() - i]))
                        count++;
                    else if (board[origin.getX() - i][origin.getY() - i] != Pawn.EMPTY){
                        stop = new Coordinate(origin.getX() - i, origin.getY() - i);
                        break;
                    }
                }
            } else if (origin.getX() < target.getX() && origin.getY() > target.getY()){
                for (int i = 1; origin.getX() - i <= target.getX() && origin.getY() + i >= target.getY(); --i){
                    if (!checkOutOfBound(origin.getX() - i, origin.getY() + i)) break;
                    if (isNotEmptyAndNotSameColor(board[origin.getX() - i][origin.getY() + i]))
                        count++;
                    else if (board[origin.getX() - i][origin.getY() + i] != Pawn.EMPTY){
                        stop = new Coordinate(origin.getX() - i, origin.getY() + i);
                        break;
                    }
                }
            }
        }
        if (target.equals(stop) && count > 0){
            limitChangeColor.add(target);
            return true;
        }
        return false;
    }

    private boolean playOn(Coordinate coordinate){
        if( !limitChangeColor.isEmpty())
            limitChangeColor.clear();

        for (int i = 0 ; i < SIZE_BOARD; ++i) {
            for (int j = 0; j < SIZE_BOARD; ++j) {
                if (board[i][j] != Pawn.EMPTY){
                    checkLine(coordinate, new Coordinate(i, j));
                }
            }
        }
        //Log.d("LIMIT", limitChangeColor.toString());
        return limitChangeColor.isEmpty();
    }

    private Boolean areAlignDiagonal(Coordinate origin, Coordinate target){
        int x = origin.getX() - target.getX();
        int y = origin.getY() - target.getY();
        if (x < 0)
            x *= -1;
        if (y < 0)
            y *= -1;
        return(x == y);
    }

    private void changeColorOnLine(Coordinate origin, Coordinate target){
        if (origin.getX() == target.getX()){ //vertical
            for (int i = origin.getY(); i <= target.getY(); i++){
                board[origin.getX()][i] = current_player;
            }
            for (int i = origin.getY(); i >= target.getY(); i--){
                board[origin.getX()][i] = current_player;
            }
        } else if (origin.getY() == target.getY()){ //horizontal
            for (int i = origin.getX(); i <= target.getX(); i++){
                board[i][origin.getY()] = current_player;
            }
            for (int i = origin.getX(); i >= target.getX(); i--){
                board[i][origin.getY()] = current_player;
            }
        } else if (areAlignDiagonal(origin, target)){ //diagonal
            if (origin.getX() < target.getX() && origin.getY() < target.getY()){
                for (int i = 0; origin.getX() + i <= target.getX() && origin.getY() + i <= target.getY(); ++i){
                    board[origin.getX() + i][origin.getY() + i] = current_player;
                }
            } else if (origin.getX() > target.getX() && origin.getY() < target.getY()){
                for (int i = 0; origin.getX() - i >= target.getX() && origin.getY() + i <= target.getY(); ++i){
                    board[origin.getX() - i][origin.getY() + i] = current_player;
                }
            } else if (origin.getX() > target.getX() && origin.getY() > target.getY()){
                for (int i = 0; origin.getX() - i >= target.getX() && origin.getY() - i >= target.getY(); ++i){
                    board[origin.getX() - i][origin.getY() - i] = current_player;
                }
            } else if (origin.getX() < target.getX() && origin.getY() > target.getY()){
                for (int i = 0; origin.getX() - i <= target.getX() && origin.getY() + i >= target.getY(); --i){
                    board[origin.getX() - i][origin.getY() + i] = current_player;
                }
            }
        }
    }

    private void nextPlayer(){
        if (current_player == Pawn.WHITE)
            current_player = Pawn.BLACK;
        else
            current_player = Pawn.WHITE;
        this.tw_current_player.setText(current_player == Pawn.WHITE ? R.string.white : R.string.black);
    }

    @Override
    public String toString() {
        return "ReversiGameLogic{" +
                "current_player=" + current_player +
                ", graphic_board=" + graphic_board +
                ", board=" + Arrays.toString(board) +
                '}';
    }
}
