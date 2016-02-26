package com.example.svetlana.try_tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import static com.example.svetlana.try_tetris.Game.ROWS;
import static com.example.svetlana.try_tetris.Game.COLUMNS;

import java.util.Timer;

public class TetrisView extends View {

    final static long SPEED = 500L; //скорость падения кубиков

    private Timer timer;
    private Game game;//игра
    private ScoreView scoreView;//отдельное поле для очков
    private NextFigureView nextFigureView;//отдельное поле для отображения следующей фигуры
    private Paint paint;
    private Rect rect;

    //конструктор
    public TetrisView(Context context, ScoreView scoreView, NextFigureView nextFigureView, Game game) {
        super(context);
        this.scoreView = scoreView;
        this.scoreView.setScore(game.getScore());
        this.nextFigureView = nextFigureView;
        paint = new Paint();//создаем то, чем будем рисовать
        rect = new Rect();//создаем место, где будем рисовать
        this.game = game;
        this.timer = new Timer ();
        nextFigureView.setNextFigure(game.getNextFigure()); //отображаем будущую фигуру
    }

    public void playGame() {
        if (game.isGameOver()) {
            game = new Game();//создаем игру
            game.dropFigure();//кидаем случайную фигуру
            timer.cancel();
            nextFigureView.setNextFigure(game.getNextFigure()); //отображаем будущую фигуру
            nextFigureView.postInvalidate();
            scoreView.setScore(0);
            scoreView.postInvalidate();
        }
        if (!game.isGameOn()) {
            game.setGameOn(true);
            //запускаем таймер, который через каждый интервал времени будет опускать фигуру вниз
            timer = new Timer();//запускаем таймер
            //создаем задание, которое будет генерироваться каждый интервал времени
            ActionTask task = new ActionTask(ActionTypes.DOWN, this);
            timer.schedule(task, 300L, SPEED);
        }

    }

    //метод, делающий отрисовку
    public void onDraw(Canvas canvas) {
        canvas.getClipBounds(rect);//определяем место где будем рисовать
        int size = rect.height() / (ROWS + 1) > rect.width() / COLUMNS ?
                rect.width() / COLUMNS : rect.height() / (ROWS + 1);
        canvas.drawColor(Color.DKGRAY);//рисуем фон - темносерый
        float startPoint = (rect.width() - size * COLUMNS) / 2;
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
            paint.setTextSize(size * 3 / 2);
            canvas.drawText("Game Over", size * 2, size * 3, paint);
        }
    }

    public Game getGame() {
        return game;
    }

    public ScoreView getScoreView() {
        return scoreView;
    }

    public NextFigureView getNextFigureView() {
        return nextFigureView;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }
}