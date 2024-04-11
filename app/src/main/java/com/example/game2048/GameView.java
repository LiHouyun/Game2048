package com.example.game2048;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

public class GameView extends GridLayout {

    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initGameView();
    }

    private void initGameView() {

        setColumnCount(4);
        addCard(getCardWidth(), getCardWidth());

        setOnTouchListener(new View.OnTouchListener() {
            private float startX, startY;
            private float offsetX, offsetY;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;

                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                // Left
                                swipeLeft();
                                printCard();
                            } else if (offsetX > 5) {
                                // Right
                                swipeRight();
                            }
                        } else {
                            if (offsetY < -5) {
                                // Up
                                swipeUp();
                            } else if (offsetY > 5) {
                                // Down
                                swipeDown();
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }


    private int getCardWidth() {
        // 声明屏幕对象
        DisplayMetrics displayMetrics;
        displayMetrics = getResources().getDisplayMetrics();
        int cardWidth;
        cardWidth = displayMetrics.widthPixels;
        return (cardWidth - 10) / 4;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        System.out.println("onSizeChanged");
        super.onSizeChanged(w, h, oldw, oldh);
/*
        int cardWidth = (Math.min(w, h) - 10) / 4;
        addCard(cardWidth, cardWidth);*/
        startGame();

    }

    private void startGame() {
        MainActivity.getMainActivity().clearScore();

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                cardsMap[x][y].setNum(0);

            }
        }

//        cardsMap[0][0].setNum(2);
//        cardsMap[0][1].setNum(0);
//        cardsMap[0][2].setNum(4);
//        cardsMap[0][3].setNum(2);

        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
        addRandomNum();
    }

    private void addCard(int cardWidth, int cardHeight) {
        System.out.println("addCard");
        Card c;

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                c = new Card(getContext());
                c.setNum(0);
                addView(c, cardWidth, cardHeight);

                cardsMap[x][y] = c;

            }
        }

    }

    private void addRandomNum() {

        emptyPoints.clear();

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (cardsMap[x][y].getNum() == 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }
        Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size())); // Math.random() 是 0~1 之间的随机数
        cardsMap[p.x][p.y].setNum(Math.random() > 0.2 ? 2 : 4);
    }

    private void swipeLeft() {
        boolean merge = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                for (int y1 = y + 1; y1 < 4; y1++) {
                    if (cardsMap[x][y1].getNum() != 0) {

                        if (cardsMap[x][y].getNum() == 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y--;

                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            if ((y + 1 == y1) || (y + 1 < y1 && y + 2 == y1 && cardsMap[x][y + 1].getNum() == 0) || (y + 2 < y1 && cardsMap[x][y + 2].getNum() == 0 && cardsMap[x][y + 1].getNum() == 0)) {
                                // y 与 y1 相邻 或 y 与 y1 之间不能有非零值
                                cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                                cardsMap[x][y1].setNum(0);

                                MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                                merge = true;
                            }
                        }
                        break;
                    }
                }
            }
        }

        if (merge == true) {
            addRandomNum();
            checkComplete();
        }
        System.out.println("Left");
    }

    private void swipeRight() {
        boolean merge = false;
        for (int x = 0; x < 4; x++) {
            for (int y = 3; y >= 0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() != 0) {

                        if (cardsMap[x][y].getNum() == 0) {
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);
                            y++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            if ((y - 1 == y1) || (y - 1 > y1 && y - 2 == y1 && cardsMap[x][y - 1].getNum() == 0) || (y - 2 > y1 && cardsMap[x][y - 2].getNum() == 0 && cardsMap[x][y - 1].getNum() == 0)) {
                                cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                                cardsMap[x][y1].setNum(0);
                                MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                                merge = true;
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (merge == true) {
            addRandomNum();
            checkComplete();
        }

        System.out.println("Right");
    }

    private void swipeUp() {
        boolean merge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {

                for (int x1 = x + 1; x1 < 4; x1++) {
                    if (cardsMap[x1][y].getNum() != 0) {

                        if (cardsMap[x][y].getNum() == 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x--;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            if ((x + 1 == x1) || (x + 1 < x1 && x + 2 == x1 && cardsMap[x + 1][y].getNum() == 0) || (x + 2 < x1 && cardsMap[x + 2][y].getNum() == 0 && cardsMap[x + 1][y].getNum() == 0)) {
                                cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                                cardsMap[x1][y].setNum(0);
                                MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                                merge = true;
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (merge == true) {
            addRandomNum();
            checkComplete();
        }

        System.out.println("Up");
    }

    private void swipeDown() {
        boolean merge = false;
        for (int y = 0; y < 4; y++) {
            for (int x = 3; x >= 0; x--) {

                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() != 0) {

                        if (cardsMap[x][y].getNum() == 0) {
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);
                            x++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            if ((x - 1 == x1) || (x - 1 > x1 && x - 2 == x1 && cardsMap[x - 1][y].getNum() == 0) || (x - 2 > x1 && cardsMap[x - 2][y].getNum() == 0 && cardsMap[x - 1][y].getNum() == 0)) {
                                cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                                cardsMap[x1][y].setNum(0);
                                MainActivity.getMainActivity().addScore(cardsMap[x][y].getNum());
                            }
                            merge = true;
                        }
                        break;
                    }
                }
            }
        }
        if (merge == true) {
            addRandomNum();
            checkComplete();
        }

        System.out.println("Down");
    }

    private void checkComplete() {
        boolean complete = true;
        ALL:
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                if (cardsMap[x][y].getNum() == 0
                        || (y - 1 >= 0 && cardsMap[x][y - 1].getNum() == cardsMap[x][y].getNum())
                        || (x - 1 >= 0 && cardsMap[x - 1][y].getNum() == cardsMap[x][y].getNum())
                        || (x + 1 <= 3 && cardsMap[x + 1][y].getNum() == cardsMap[x][y].getNum())
                        || (y + 1 <= 3 && cardsMap[x][y + 1].getNum() == cardsMap[x][y].getNum())) {
                    // 此位置为空，此位置的相邻位置有同一值，不结束
                    complete = false;
                    break ALL;
                }
            }
        }

        if (complete) {
            new AlertDialog.Builder(getContext()).setTitle("游戏结束").setPositiveButton("重来", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
        }
    }

    private Card[][] cardsMap = new Card[4][4];
    private List<Point> emptyPoints = new ArrayList<Point>();


    private void printCard() {

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {

                System.out.println("x=");
                System.out.println(x);
                System.out.println("y=");
                System.out.println(y);
                System.out.println(cardsMap[x][y].getNum());
            }
        }
    }
}