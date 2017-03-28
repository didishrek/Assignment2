package com.griffith.assignment2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static com.griffith.assignment2.ReversiGameLogic.SIZE_BOARD;

public class Reversi extends View {

    private int square_size;
    private Coordinate touchUpCoordinate;
    private ReversiGameLogic rgc;
    private boolean size_calculated = false;

    private ShapeDrawable[][] graphic_board = new ShapeDrawable[SIZE_BOARD][SIZE_BOARD];

    private int height, width;

    public Reversi(Context context) {
        super(context);
        init(null, 0);
    }

    public Reversi(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public Reversi(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }


    public void addGameLogic(ReversiGameLogic rgc){
        this.rgc = rgc;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.width = w;
        this.height = h;
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = widthMeasureSpec;
        int h = heightMeasureSpec;
        if (w > h)
            w = h;
        else
            h = w;

        height = h;
        width = w;
        super.onMeasure(w, h);
        Log.d("REVERSI", "onMeasure");
    }

    private void init(AttributeSet attrs, int defStyle) {

        for (int i = 0 ; i < SIZE_BOARD; ++i){
            for (int j = 0 ; j < SIZE_BOARD; ++j){
                graphic_board[i][j] = new ShapeDrawable(new OvalShape());
                graphic_board[i][j].getPaint().setColor(Color.GREEN);

            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0 ; i < SIZE_BOARD; ++i){
            for (int j = 0 ; j < SIZE_BOARD; ++j) {
                graphic_board[i][j].draw(canvas);
            }
        }

        Log.d("REVERSI", "onDraw");
    }

    public void updateBoard(ReversiGameLogic.Pawn[][] board){
        square_size  = height / SIZE_BOARD;
        Log.d("REVERSI", "square_size = " + square_size);
        for (int i = 0 ; i < SIZE_BOARD; ++i){
            for (int j = 0 ; j < SIZE_BOARD; ++j){
                graphic_board[i][j].setBounds(square_size * i, square_size * j, square_size * i + square_size, square_size * j + square_size);
                if (board[i][j] == ReversiGameLogic.Pawn.WHITE)
                    graphic_board[i][j].getPaint().setColor(Color.WHITE);
                else if (board[i][j] == ReversiGameLogic.Pawn.BLACK)
                    graphic_board[i][j].getPaint().setColor(Color.BLACK);
                else
                    graphic_board[i][j].getPaint().setColor(Color.GREEN);
            }
        }
        Log.d("REVERSI", "board updated");
        invalidate();
    }

    private Coordinate getCoordinateFromClick(float x, float y){
        for (int i = 0 ; i < SIZE_BOARD; ++i) {
            for (int j = 0; j < SIZE_BOARD; ++j) {
                if ((x >= square_size * i && x <= square_size * i + square_size) && (y >= square_size * j && y <= square_size * j + square_size))
                    return new Coordinate(i, j);
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            touchUpCoordinate = getCoordinateFromClick(event.getX(), event.getY());
            return true;
        } else if(event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (touchUpCoordinate.equals(getCoordinateFromClick(event.getX(), event.getY()))){
                invalidate();
                rgc.updateTouchEvent(touchUpCoordinate);
                touchUpCoordinate = null;
            }
            return true;
        }
        return false;
    }
}
