package com.example.svetlana.try_tetris;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

//класс, описывающий свайпы
public class OnSwipeTouchListener implements OnTouchListener {

    private final GestureDetector gestureDetector;
    private TetrisView tetrisView;

    public OnSwipeTouchListener(Context context, TetrisView tetrisView) {
        this.tetrisView = tetrisView;
        this.gestureDetector = new GestureDetector(context, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends SimpleOnGestureListener {

        //константы для определения величины свайпа
        private static final int SWIPE_THRESHOLD = 50;
        private static final int SWIPE_VELOCITY_THRESHOLD = 50;

        @Override
        public boolean onDown(MotionEvent event) {
            /*
            //более быстрое управление, если реагировать на down, но тогда свайпы вверх/вниз будут реагировать
            //только если сделать их на уровне фигуры
            if (tetrisView.getGame().isGameOn() && !tetrisView.getGame().isGameOver()) {
                if (event.getX() < tetrisView.getFigureStart()) {
                    onLeft();
                    return false;
                } else if (event.getX() > tetrisView.getFigureFinish()) {
                    onRight();
                    return false;
                }
            }
            */
            return true;
        }

        //точечное нажатие на экран (работает медленнее, если убирать из down
        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            if (tetrisView.getGame().isGameOn() && !tetrisView.getGame().isGameOver()) {
                if (event.getX() < tetrisView.getFigureStart()) {
                    onLeft();
                } else if (event.getX() > tetrisView.getFigureFinish()) {
                    onRight();
                }
            }
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            if (tetrisView.getGame().isGameOn() && !tetrisView.getGame().isGameOver()) {
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
                                onRight();
                            } else {
                                onLeft();
                            }
                        }
                        result = true;
                    } else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeBottom();
                        } else {
                            onSwipeTop();
                        }
                    }
                    result = true;

                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
            return result;
        }
    }

    public void onRight() {
        ActionTask task = new ActionTask(ActionTypes.RIGHT, tetrisView);
        task.run();
    }

    public void onLeft() {
        ActionTask task = new ActionTask(ActionTypes.LEFT, tetrisView);
        task.run();
    }

    public void onSwipeTop() {
        ActionTask task = new ActionTask(ActionTypes.UP, tetrisView);
        task.run();
    }

    public void onSwipeBottom() {
        ActionTask task = new ActionTask(ActionTypes.FASTDOWN, tetrisView);
        task.run();
    }
}
