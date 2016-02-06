package com.example.svetlana.try_tetris;

import java.util.Random;

//класс, описывающий состояние игры
public class Game {

    //константы для поля
    final static int ROWS = 20;
    final static int COLUMNS = 12;

    private int[][] field = new int[ROWS + 1][COLUMNS + 1]; //игоровое поле
    private int verticalPlace = COLUMNS / 2;//текущее положение
    private int horizontalPlace = 0;//текущее положение
    private Figure figure; //текущая активная фигура
    private int score; //очки
    private boolean gameOver = false; //окончание игры

    //метод выброса новой фигуры
    void dropFigure() {
        Random random = new Random();
        figure = new Figure(random.nextInt(7));
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

    public void setVerticalPlace(int verticalPlace) {
        this.verticalPlace = verticalPlace;
    }

    public void setHorizontalPlace(int horizontalPlace) {
        this.horizontalPlace = horizontalPlace;
    }

    public int[][] getField() {
        return field;
    }
}
