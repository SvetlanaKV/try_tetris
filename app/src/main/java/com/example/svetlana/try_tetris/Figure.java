package com.example.svetlana.try_tetris;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Figure {

    private int sizeX;
    private int sizeY;
    private int[][] figureShape;

    Figure(int figureType) {
        switch (figureType) {
            //просто квадрат-ячейка
            case 0:
                this.sizeX = 1;
                this.sizeY = 1;
                this.figureShape = new int[][]{{1}, {1}};
                break;
            //фигура Q
            case 1:
                this.sizeX = 2;
                this.sizeY = 2;
                this.figureShape = new int[][]{{1, 1}, {1, 1}};
                break;
            //фигура Z
            case 2:
                this.sizeX = 2;
                this.sizeY = 3;
                this.figureShape = new int[][]{{1, 1, 0}, {0, 1, 1}};
                break;
            //фигура S
            case 3:
                this.sizeX = 2;
                this.sizeY = 3;
                this.figureShape = new int[][]{{0, 1, 1}, {1, 1, 0}};
                break;
            //фигура T
            case 4:
                this.sizeX = 2;
                this.sizeY = 3;
                this.figureShape = new int[][]{{1, 1, 1}, {0, 1, 0}};
                break;
            //фигура I
            case 5:
                this.sizeX = 4;
                this.sizeY = 1;
                this.figureShape = new int[][]{{1}, {1}, {1}, {1}};
                break;
            //фигура J
            case 6:
                this.sizeX = 3;
                this.sizeY = 2;
                this.figureShape = new int[][]{{0, 1}, {0, 1}, {1, 1}};
                break;
            //фигура L
            case 7:
                this.sizeX = 3;
                this.sizeY = 2;
                this.figureShape = new int[][]{{1, 0}, {1, 0}, {1, 1}};
                break;
        }
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public int[][] getFigureShape() {
        return figureShape;
    }

    //проверка возможности переместить фигуру влево
    boolean checkLeft(int horizontalPlace, int verticalPlace, int[][] field) {
        int max = 0;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                int current = figureShape[i][j] + field[horizontalPlace + i][verticalPlace - 1 + j];
                if (current > max) {
                    max = current;
                }
            }
        }
        if (max > 1) {
            return false;
        } else
        return true;
    }

    //проверка возможности переместить фигуру вправо
    boolean checkRight(int horizontalPlace, int verticalPlace, int[][] field) {
        int max = 0;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                int current = figureShape[i][j] + field[horizontalPlace + i][verticalPlace + 1 + j];
                if (current > max) {
                    max = current;
                }
            }
        }
        if (max > 1) {
            return false;
        }
        return true;
    }

    //проверка возможности переместить фигуру вниз
    boolean checkDown(int horizontalPlace, int verticalPlace, int[][] field) {
        int max = 0;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                int current = figureShape[i][j] + field[horizontalPlace + 1 + i][verticalPlace + j];
                if (current > max) {
                    max = current;
                }
            }
        }
        if (max > 1) {
            return false;
        }
        return true;
    }

    //проверка возможности создать фигуру
    boolean checkCreate (int horizontalPlace, int verticalPlace, int[][] field) {
        int max = 0;
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                int current = figureShape[i][j] + field[horizontalPlace + i][verticalPlace + j];
                if (current > max) {
                    max = current;
                }
            }
        }
        if (max > 1) {
            return false;
        }
        return true;
    }

    //нарисовать фигуру
    void drawFigure(Canvas canvas, Paint paint, int horizontalPlace, int verticalPlace, int size) {
        paint.setColor(Color.BLUE);
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (figureShape[i][j] == 1) {
                    canvas.drawRect((verticalPlace + j) * size, (horizontalPlace + i) * size, (verticalPlace + j + 1) * size,
                            (horizontalPlace + i + 1) * size, paint);
                }
            }
        }
    }

}