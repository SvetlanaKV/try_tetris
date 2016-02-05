package com.example.svetlana.try_tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class TetrisView extends View {

    private Context context;
    private Paint paint; //чем рисуем
    private Rect rect;
    private Timer timer;
    private long speed = 500L; //скорость падения кубиков
    private Figure figure; //текущая фигура
    private int horizontalPlace; //позиция отрисовки фигуры
    private int verticalPlace;//позиция отрисовки фигуры
    private int score; //очки
    private boolean gameOver = false; //окончание игры
    //константы для отрисовки поля
    private final int rows = 20;
    private final int columns = 12;
    private final int columnsForScore = 4;
    private int size;
    int[][] field = new int[rows + 1][columns + 1]; //поле
    //координаты для движения
    float startY;

    //возможные движения
    private enum actionTypes {
        LEFT, RIGHT, ROTATE, DOWN, FASTDOWN
    }

    //конструктор
    public TetrisView(Context context) {
        super(context);
        this.context = context;
        paint = new Paint();//создаем то, чем будем рисовать
        rect = new Rect();//создаем место, где будем рисовать
        timer = new Timer();//запускаем таймер
        //устанавливаем начальную точку
        verticalPlace = columns / 2;
        horizontalPlace = 0;
        score = 0;
        //генерируем случайную фигуру
        Random random = new Random();
        figure = new Figure(random.nextInt(7));
        //создаем задание, которое будет генерироваться каждый интервал времени
        ActitionTask task = new ActitionTask(actionTypes.DOWN);
        //запускаем таймер, который через каждый интервал времени будет опускать фигуру вниз
        timer.schedule(task, 300L, speed);
    }

    //метод, делающий отрисовку
    public void onDraw(Canvas canvas) {
        canvas.getClipBounds(rect);//определяем место где будем рисовать
        size = rect.height() / (rows + 1) > rect.width() / (columns + columnsForScore) ?
                rect.width() / (columns + columnsForScore) : rect.height() / (rows + 1);
        //size=30;
        canvas.drawColor(Color.DKGRAY);//рисуем фон - темносерый
        //отрисовываем упавшие фигуры
        paint.setColor(Color.CYAN);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (field[i][j] == 1) {
                    canvas.drawRect(j * size, i * size, (j + 1) * size,
                            (i + 1) * size, paint);
                }
            }
        }
        //выводим текущие очки
        paint.setColor(Color.GREEN);
        paint.setTextSize(size);
        canvas.drawText("Score", size * (columns + 1), size, paint);
        canvas.drawText(Integer.toString(score), size * (columns + 1), size * 3, paint);
        //отрисовываем фигуру
        figure.drawFigure(canvas, paint, horizontalPlace, verticalPlace, size);
        //рисуем поле для игры
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(1);
        for (int i = 0; i < rows + 1; i++) {
            canvas.drawLine(0, i * size, size * columns, i * size, paint);
        }
        for (int i = 0; i < columns + 1; i++) {
            canvas.drawLine(i * size, 0, i * size, size * rows, paint);
        }
        //если игра закончилась
        if (gameOver) {
            paint.setColor(Color.RED);
            paint.setTextSize(size * 2);
            canvas.drawText("Game Over", size * 2, size * 3, paint);
        }
    }

    //Задание, которое определяет, что происходит с фигурой
    class ActitionTask extends TimerTask {
        actionTypes action;

        public ActitionTask(actionTypes action) {
            this.action = action;
        }

        @Override
        public void run() {
            switch (action) {
                case LEFT:
                    //двигаем фигуру налево
                    if (verticalPlace > 0 && figure.checkLeft(horizontalPlace, verticalPlace, field)) {
                        verticalPlace--;
                    }
                    break;
                case RIGHT:
                    //двигаем фигуру направо
                    if (verticalPlace < columns - figure.getSizeY() && figure.checkRight(horizontalPlace, verticalPlace, field)) {
                        verticalPlace++;
                    }
                    break;
                case ROTATE:
                    //переворачиваем фигуру
                    break;
                case DOWN:
                    //фигура просто падает вниз
                    if (horizontalPlace == (rows - figure.getSizeX()) || !figure.checkDown(horizontalPlace, verticalPlace, field)) {
                        for (int i = 0; i < figure.getSizeX(); i++) {
                            for (int j = 0; j < figure.getSizeY(); j++) {
                                if (figure.getFigureShape()[i][j] == 1) {
                                    field[horizontalPlace + i][verticalPlace + j] = 1;
                                }
                            }
                        }
                        updateField();
                        horizontalPlace = 0;
                        verticalPlace = columns / 2;
                        Random random = new Random();
                        figure = new Figure(random.nextInt(7));
                        timer.cancel();
                        timer = new Timer();
                        ActitionTask task = new ActitionTask(actionTypes.DOWN);
                        timer.schedule(task, 300L, speed);
                        if (!figure.checkCreate(horizontalPlace, verticalPlace, field)) {
                            gameOver = true;
                            timer.cancel();
                        }
                        updateField();
                    } else {
                        horizontalPlace++;
                    }
                    break;
                case FASTDOWN: {
                    //фигура ускоряется
                    timer.cancel();
                    timer = new Timer();
                    ActitionTask task = new ActitionTask(actionTypes.DOWN);
                    timer.schedule(task, 0, speed / 30);
                    break;
                }
            }
            //обновляем графику
            postInvalidate();
        }
    }

    //метод делает апдейт поля: проверяет наличие заполненных строк и дохождение до потолка
    void updateField() {
        //последняя колонка =сумма ячеек строки
        int countDeletedRows = 0;
        for (int i = rows - 1; i >= 0; i--) {
            field[i][columns] = 0;
            for (int j = 0; j < columns; j++) {
                field[i][columns] += field[i][j];
            }
            //проверяем заполненную строку
            if (field[i][columns] == columns) {
                //score += 100;
                //удаляем строку
                countDeletedRows++;
                for (int k = i; k > 0; k--) {
                    System.arraycopy(field[k - 1], 0, field[k], 0, columns + 1);
                }
                i++;
            }
        }
        //начисляем очки по правилам
        switch (countDeletedRows) {
            case 1:
                score += 100;
                break;
            case 2:
                score += 300;
                break;
            case 3:
                score += 700;
                break;
            case 4:
                score += 1500;
                break;
        }
        //последняя строка = сумма ячеек столбца
        for (int i = 0; i < columns; i++) {
            field[rows][i] = 0;
            for (int j = 0; j < rows; j++) {
                field[rows][i] += field[j][i];
            }
            if (field[rows][i] == rows) {
                timer.cancel();
                gameOver = true;
            }
        }
    }

    //определение клика на экран

    public boolean onTouchEvent(MotionEvent event) {
        if (!gameOver) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                startY = event.getY();
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (event.getY() - startY > size * 4) {
                    ActitionTask task = new ActitionTask(actionTypes.FASTDOWN);
                    task.run();
                } else {
                    if ((verticalPlace + figure.getSizeY()) * size > event.getX()) {
                        ActitionTask task = new ActitionTask(actionTypes.LEFT);
                        task.run();
                    }
                    if ((verticalPlace + figure.getSizeY()) * size < event.getX()) {
                        ActitionTask task = new ActitionTask(actionTypes.RIGHT);
                        task.run();
                    }
                }
            }
        }
        return true;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Figure getFigure() {
        return figure;
    }

    public int getHorizontalPlace() {
        return horizontalPlace;
    }

    public int getVerticalPlace() {
        return verticalPlace;
    }

    public void setVerticalPlace(int verticalPlace) {
        this.verticalPlace = verticalPlace;
    }
}
