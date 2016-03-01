package com.example.svetlana.try_tetris;

import java.io.Serializable;
import java.util.Random;

//класс, описывающий состояние игры
public class Game implements Serializable {

    //константы для поля
    final static int ROWS = 20;
    final static int COLUMNS = 10;

    private long speed; //скорость падения кубиков
    private int[][] field = new int[ROWS + 1][COLUMNS + 1]; //игоровое поле
    private int verticalPlace = COLUMNS / 2;//текущее положение
    private int horizontalPlace = 0;//текущее положение
    private Figure figure; //текущая активная фигура
    private Figure nextFigure; //будущая фигура
    private int score; //очки
    private int scoreToLevel;
    private int level;
    private boolean gameOn = false; //признак действия игры
    private boolean gameOver = false; //окончание игры

    Game() {
        Random random = new Random();
        scoreToLevel = 0;
        speed = 500L;
        level = 1;
        nextFigure = new Figure(random.nextInt(7));
    }

    //метод выброса новой фигуры
    void dropFigure() {
        figure = nextFigure;
        Random random = new Random();
        nextFigure = new Figure(random.nextInt(7));
        horizontalPlace = 0;
        verticalPlace = COLUMNS / 2;
    }

    //метод изменения поля в случае, если фигура упала
    void rememberFallenFigure(Figure figure) {
        for (int i = 0; i < figure.getSizeX(); i++) {
            for (int j = 0; j < figure.getSizeY(); j++) {
                if (figure.getFigureShape()[i][j] == 1) {
                    field[horizontalPlace + i][verticalPlace + j] = 1;
                }
            }
        }
        updateField();
    }

    //метод делает апдейт поля: проверяет наличие заполненных строк и дохождение до потолка
    void updateField() {
        //последняя колонка =сумма ячеек строки
        int countDeletedRows = 0;
        for (int i = ROWS - 1; i >= 0; i--) {
            field[i][COLUMNS] = 0;
            for (int j = 0; j < COLUMNS; j++) {
                field[i][COLUMNS] += field[i][j];
            }
            //проверяем заполненную строку
            if (field[i][COLUMNS] == COLUMNS) {
                //удаляем строку
                countDeletedRows++;
                for (int k = i; k > 0; k--) {
                    System.arraycopy(field[k - 1], 0, field[k], 0, COLUMNS + 1);
                }
                i++;
            }
        }
        //начисляем очки по правилам
        switch (countDeletedRows) {
            case 1:
                score += 100;
                scoreToLevel += 100;
                break;
            case 2:
                score += 300;
                scoreToLevel += 300;
                break;
            case 3:
                score += 700;
                scoreToLevel += 700;
                break;
            case 4:
                score += 1500;
                scoreToLevel += 1500;
                break;
        }
        if (scoreToLevel >= 1500) {
            speed = Math.round(650 * Math.pow(level, 1 / 10) / Math.log(level + 2));
            level++;
            scoreToLevel -= 1500;
        }
        //последняя строка = сумма ячеек столбца
        for (int i = 0; i < COLUMNS; i++) {
            field[ROWS][i] = 0;
            for (int j = 0; j < ROWS; j++) {
                field[ROWS][i] += field[j][i];
            }
            if (field[ROWS][i] == ROWS) {
                gameOver = true;
            }
        }
    }

    //проверка возможности переместить фигуру влево
    boolean checkLeft() {
        for (int i = 0; i < figure.getSizeX(); i++) {
            for (int j = 0; j < figure.getSizeY(); j++) {
                if (figure.getFigureShape()[i][j] + field[horizontalPlace + i][verticalPlace - 1 + j] > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    //проверка возможности переместить фигуру вправо
    boolean checkRight() {
        for (int i = 0; i < figure.getSizeX(); i++) {
            for (int j = 0; j < figure.getSizeY(); j++) {
                if (figure.getFigureShape()[i][j] + field[horizontalPlace + i][verticalPlace + 1 + j] > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    //проверка возможности переместить фигуру вниз
    boolean checkDown() {
        for (int i = 0; i < figure.getSizeX(); i++) {
            for (int j = 0; j < figure.getSizeY(); j++) {
                if (figure.getFigureShape()[i][j] + field[horizontalPlace + 1 + i][verticalPlace + j] > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    //проверка возможности создать фигуру
    boolean checkCreate() {
        for (int i = 0; i < figure.getSizeX(); i++) {
            for (int j = 0; j < figure.getSizeY(); j++) {
                if (figure.getFigureShape()[i][j] + field[horizontalPlace + i][verticalPlace + j] > 1) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public int getScore() {
        return score;
    }

    public int getVerticalPlace() {
        return verticalPlace;
    }

    public int getHorizontalPlace() {
        return horizontalPlace;
    }

    public Figure getFigure() {
        return figure;
    }

    public Figure getNextFigure() {
        return nextFigure;
    }

    public void setVerticalPlace(int verticalPlace) {
        this.verticalPlace = verticalPlace;
    }

    public void setHorizontalPlace(int horizontalPlace) {
        this.horizontalPlace = horizontalPlace;
    }

    public int[][] getField() {
        return field;
    }

    public void setGameOn(boolean gameOn) {
        this.gameOn = gameOn;
    }

    public boolean isGameOn() {
        return gameOn;
    }

    public long getSpeed() {
        return speed;
    }

    public int getLevel() {
        return level;
    }
}
