package com.griffith.assignment2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import static com.griffith.assignment2.ReversiGameLogic.SIZE_BOARD;

public class Reversi extends View {

    private int square_size;
    private Location touchUpLocation;
    private ReversiGameLogic rgc;

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
        //Log.d("REVERSI", "onMeasure");
    }

    private void init(AttributeSet attrs, int defStyle) {
        for (int i = 0 ; i < SIZE_BOARD; ++i){
            for (int j = 0 ; j < SIZE_BOARD; ++j){
                graphic_board[i][j] = new ShapeDrawable(new OvalShape());
                graphic_board[i][j].getPaint().setColor(Color.GRAY);

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

        //Log.d("REVERSI", "onDraw");
    }

    public void updateBoard(ReversiGameLogic.Pawn[][] board){
        int margin = 2;
        if (square_size > 5000 || square_size == 0)
            square_size  = height / SIZE_BOARD;
        for (int i = 0 ; i < SIZE_BOARD; ++i){
            for (int j = 0 ; j < SIZE_BOARD; ++j){
                graphic_board[i][j].setBounds(square_size * i + margin, square_size * j + margin, square_size * i + square_size - margin, square_size * j + square_size - margin);
                if (board[i][j] == ReversiGameLogic.Pawn.WHITE)
                    graphic_board[i][j].getPaint().setColor(Color.WHITE);
                else if (board[i][j] == ReversiGameLogic.Pawn.BLACK)
                    graphic_board[i][j].getPaint().setColor(Color.BLACK);
                else
                    graphic_board[i][j].getPaint().setColor(Color.GRAY);
            }
        }
        invalidate();
    }

    private Location getLocationFromClick(float x, float y){
        for (int i = 0 ; i < SIZE_BOARD; ++i) {
            for (int j = 0; j < SIZE_BOARD; ++j) {
                if ((x >= square_size * i && x <= square_size * i + square_size) && (y >= square_size * j && y <= square_size * j + square_size))
                    return new Location(i, j);
            }
        }
        return null;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            touchUpLocation = getLocationFromClick(event.getX(), event.getY());
            return true;
        } else if(event.getActionMasked() == MotionEvent.ACTION_UP) {
            if (touchUpLocation.equals(getLocationFromClick(event.getX(), event.getY()))){
                rgc.updateTouchEvent(touchUpLocation);
            }
            return true;
        }
        return false;
    }
}
