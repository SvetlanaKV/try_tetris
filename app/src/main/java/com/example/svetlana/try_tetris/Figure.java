package com.example.svetlana.try_tetris;

import java.io.Serializable;

import static com.example.svetlana.try_tetris.Game.ROWS;
import static com.example.svetlana.try_tetris.Game.COLUMNS;

public class Figure implements Serializable {

    //константы максимального размера фигуры при сбросе
    final static int MAX_X = 4;
    final static int MAX_Y = 3;
    private int sizeX;
    private int sizeY;
    private int[][] figureShape;

    Figure(int figureType) {
        switch (figureType) {
            //фигура Q
            case 0:
                this.sizeX = 2;
                this.sizeY = 2;
                this.figureShape = new int[][]{{1, 1}, {1, 1}};
                break;
            //фигура Z
            case 1:
                this.sizeX = 2;
                this.sizeY = 3;
                this.figureShape = new int[][]{{1, 1, 0}, {0, 1, 1}};
                break;
            //фигура S
            case 2:
                this.sizeX = 2;
                this.sizeY = 3;
                this.figureShape = new int[][]{{0, 1, 1}, {1, 1, 0}};
                break;
            //фигура T
            case 3:
                this.sizeX = 2;
                this.sizeY = 3;
                this.figureShape = new int[][]{{1, 1, 1}, {0, 1, 0}};
                break;
            //фигура I
            case 4:
                this.sizeX = 4;
                this.sizeY = 1;
                this.figureShape = new int[][]{{1}, {1}, {1}, {1}};
                break;
            //фигура J
            case 5:
                this.sizeX = 3;
                this.sizeY = 2;
                this.figureShape = new int[][]{{0, 1}, {0, 1}, {1, 1}};
                break;
            //фигура L
            case 6:
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

    //поворот фигуры
    public int rotate(int horizontalPlace, int verticalPlace, int[][] field, boolean gameOver, boolean gameOn) {
        if (gameOver || !gameOn) {
            return verticalPlace;
        }
        //создаем новую фигуру
        int newSizeX = sizeY;
        int newSizeY = sizeX;
        int[][] newFigureShape = new int[newSizeX][newSizeY];
        for (int i = 0; i < newSizeX; i++) {
            for (int j = 0; j < newSizeY; j++) {
                newFigureShape[i][j] = figureShape[newSizeY - j - 1][i];
            }
        }
        //если нет места поворота, то сдвигаемся влево
        int newVerticalPlace = verticalPlace;
        if (horizontalPlace + newSizeX > ROWS) {
            return verticalPlace;
        }
        if (verticalPlace + newSizeY > COLUMNS) {
            newVerticalPlace = COLUMNS - newSizeY;
        }
        //если повернуть можно, то задаем новые параметры фигуры
        if (checkRotate(horizontalPlace, newVerticalPlace, field, newFigureShape, newSizeX, newSizeY)) {
            this.figureShape = newFigureShape;
            this.sizeX = newSizeX;
            this.sizeY = newSizeY;
            //если в момент поворота достигли дна, фигура считается упавшей
            if (horizontalPlace + newSizeX == ROWS) {
                for (int i = 0; i < newSizeX; i++) {
                    for (int j = 0; j < newSizeY; j++) {
                        if (newFigureShape[i][j] == 1) {
                            field[horizontalPlace + i][newVerticalPlace + j] = 1;
                        }
                    }
                }
            }
            return newVerticalPlace;
        }
        return verticalPlace;
    }

    //проверка возможности поворота
    boolean checkRotate(int horizontalPlace, int verticalPlace, int[][] field,
                        int[][] newFigureShape, int newSizeX, int newSizeY) {
        for (int i = 0; i < newSizeX; i++) {
            for (int j = 0; j < newSizeY; j++) {
                if (newFigureShape[i][j] + field[horizontalPlace + i][verticalPlace + j] > 1) {
                    return false;
                }
            }
        }
        return true;
    }
}