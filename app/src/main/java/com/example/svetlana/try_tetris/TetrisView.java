package com.example.svetlana.try_tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class TetrisView extends View {

    //для конструктора
    Context context;
    //то, что рисует
    Paint paint;
    //границы рисования
    Rect rect;
    //таймер
    Timer timer;
    //скорость падения кубиков
    long speed = 400L;
    //константы для отрисовки поля
    final int rows = 20;
    final int columns = 12;
    final int size = 30;
    //эксперимент
    int level;
    //позиция отрисовки фигуры
    int horizontalPlace;
    int verticalPlace;

    //возможные движения
    enum actionTypes {
        LEFT, RIGHT, ROTATE, DOWN, FASTDOWN
    }

    //конструктор
    public TetrisView(Context context) {
        super(context);
        this.context = context;
        //создаем то, чем будем рисовать
        paint = new Paint();
        //создаем место, где будем рисовать
        rect = new Rect(0, 0, 200, 120);
        //запускаем таймер
        timer = new Timer();
        //устанавливаем начальную точку
        verticalPlace = columns / 2;
        horizontalPlace = 0;
        //создаем задание, которое будет генерироваться каждый интервал времени
        ActitionTask task = new ActitionTask(actionTypes.DOWN);
        //запускаем таймер, который через каждый интервал времени будет опускать фигуру вниз
        timer.schedule(task, 300L, speed);
    }

    //метод, делающий отрисовку
    public void onDraw(Canvas canvas) {
        //определяем место где будем рисовать
        canvas.getClipBounds(rect);
        //рисуем фон - темносерый
        canvas.drawColor(Color.DKGRAY);
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(1);
        //рисуем поле для игры
        for (int i = 0; i < rows + 1; i++) {
            canvas.drawLine(0, i * size, size * columns, i * size, paint);
        }
        for (int i = 0; i < columns + 1; i++) {
            canvas.drawLine(i * size, 0, i * size, size * rows, paint);
        }
        //отрисовываем фигуру
        paint.setColor(Color.BLUE);
        canvas.drawRect(verticalPlace * size, horizontalPlace * size, (verticalPlace + 1) * size,
                (horizontalPlace + 1) * size, paint);
    }

    //Задание, которое определяет, что происходит с фигурой
    public class ActitionTask extends TimerTask {
        actionTypes action;

        public ActitionTask(actionTypes action) {
            this.action = action;
        }

        @Override
        public void run() {
            switch (action) {
                case LEFT:
                    //двигаем фигуру налево
                    if (verticalPlace > 0) {
                        verticalPlace--;
                    }
                    break;
                case RIGHT:
                    //двигаем фигуру направо
                    if (verticalPlace < columns - 1) {
                        verticalPlace++;
                    }
                    break;
                case ROTATE:
                    //переворачиваем фигуру
                    break;
                case DOWN:
                    //фигура просто падает вниз
                    horizontalPlace++;
                    if (horizontalPlace == rows) {
                        horizontalPlace = 0;
                    }
                    break;
                case FASTDOWN: {
                    //фигура ускоряется и сразу оказывается внизу
                    horizontalPlace = rows;
                    //сохраняем внизу и выдаем новую фигуру
                    horizontalPlace = 0;
                    break;
                }
            }
            //обновляем графику
            postInvalidate();
        }
    }

    //определение клика на экран
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float x = event.getX();
            float y = event.getY();
            if (verticalPlace * size > x) {
                ActitionTask task = new ActitionTask(actionTypes.LEFT);
                task.run();
            }
            if (verticalPlace * size < x) {
                ActitionTask task = new ActitionTask(actionTypes.RIGHT);
                task.run();
            }
            if (y >= rows * size) {
                ActitionTask task = new ActitionTask(actionTypes.FASTDOWN);
                task.run();
            }
        }
        return true;
    }
}
