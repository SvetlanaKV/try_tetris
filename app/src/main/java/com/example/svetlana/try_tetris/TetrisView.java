package com.example.svetlana.try_tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import static com.example.svetlana.try_tetris.Game.ROWS;
import static com.example.svetlana.try_tetris.Game.COLUMNS;

import java.util.Timer;

public class TetrisView extends View {

    final static long SPEED = 500L; //скорость падения кубиков

    private Timer timer;
    private Game game;//игра
    private ScoreView scoreView;//отдельное поле для очков
    private Paint paint;
    private Rect rect;
    private int size;//размер ячейки
    private float startPoint; //начальная точка для отрисовки игрового поля
    private float startY;//координаты для движения

    //конструктор
    public TetrisView(Context context, ScoreView scoreView) {
        super(context);
        this.scoreView = scoreView;
        paint = new Paint();//создаем то, чем будем рисовать
        rect = new Rect();//создаем место, где будем рисовать
        timer = new Timer();//запускаем таймер
        game = new Game();//создаем игру
        game.dropFigure();//кидаем случайную фигуру
        //создаем задание, которое будет генерироваться каждый интервал времени
        ActionTask task = new ActionTask(ActionTypes.DOWN, this);
        //запускаем таймер, который через каждый интервал времени будет опускать фигуру вниз
        timer.schedule(task, 300L, SPEED);
    }

    //метод, делающий отрисовку
    public void onDraw(Canvas canvas) {
        canvas.getClipBounds(rect);//определяем место где будем рисовать
        size = rect.height() / (ROWS + 1) > rect.width() / COLUMNS ?
                rect.width() / COLUMNS : rect.height() / (ROWS + 1);
        canvas.drawColor(Color.DKGRAY);//рисуем фон - темносерый
        startPoint = (rect.width() - size * COLUMNS) / 2;
        //отрисовываем упавшие фигуры
        paint.setColor(Color.CYAN);
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (game.getField()[i][j] == 1) {
                    canvas.drawRect(startPoint + j * size, i * size, startPoint + (j + 1) * size,
                            (i + 1) * size, paint);
                }
            }
        }
        //отрисовываем фигуру
        paint.setColor(Color.BLUE);
        for (int i = 0; i < game.getFigure().getSizeX(); i++) {
            for (int j = 0; j < game.getFigure().getSizeY(); j++) {
                if (game.getFigure().getFigureShape()[i][j] == 1) {
                    canvas.drawRect(startPoint + (game.getVerticalPlace() + j) * size, (game.getHorizontalPlace() + i) * size,
                            startPoint + (game.getVerticalPlace() + j + 1) * size, (game.getHorizontalPlace() + i + 1) * size, paint);
                }
            }
        }
        //рисуем поле для игры
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(1);
        for (int i = 0; i < ROWS + 1; i++) {
            canvas.drawLine(startPoint, i * size, startPoint + size * COLUMNS, i * size, paint);
        }
        for (int i = 0; i < COLUMNS + 1; i++) {
            canvas.drawLine(startPoint + i * size, 0, startPoint + i * size, size * ROWS, paint);
        }
        //если игра закончилась
        if (game.isGameOver()) {
            paint.setColor(Color.RED);
            paint.setTextSize(size * 2);
            canvas.drawText("Game Over", size * 2, size * 3, paint);
        }
    }

    //определение клика на экран
    public boolean onTouchEvent(MotionEvent event) {
        if (!game.isGameOver()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startY = event.getY();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getY() - startY > size * 3) {
                    ActionTask task = new ActionTask(ActionTypes.FASTDOWN, this);
                    task.run();
                } else {
                    if ((game.getVerticalPlace()) * size + startPoint > event.getX()) {
                        ActionTask task = new ActionTask(ActionTypes.LEFT, this);
                        task.run();
                    }
                    if ((game.getVerticalPlace() + game.getFigure().getSizeY()) * size + startPoint < event.getX()) {
                        ActionTask task = new ActionTask(ActionTypes.RIGHT, this);
                        task.run();
                    }
                }
            }
        }
        return true;
    }

    public Game getGame() {
        return game;
    }

    public ScoreView getScoreView() {
        return scoreView;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}