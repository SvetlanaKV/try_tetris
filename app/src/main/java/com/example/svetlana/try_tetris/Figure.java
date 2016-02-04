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

    //поворот фигуры
    public int rotate (int horizontalPlace, int verticalPlace, int[][] field, int columns, int rows,
                       boolean gameOver) {
        if (gameOver) {
            return verticalPlace;
        }
        //создаем новую фигуру
        int newSizeX = sizeY;
        int newSizeY = sizeX;
        int[][] newFigureShape = new int[newSizeX][newSizeY];
        for (int i=0;i<newSizeX;i++) {
            for (int j=0;j<newSizeY;j++) {
                newFigureShape[i][j]=figureShape[newSizeY-j-1][i];
            }
        }
        //если нет места поворота, то сдвигаемся влево
        int newVerticalPlace = verticalPlace;
        if (horizontalPlace+newSizeX>rows) {
            return verticalPlace;
        }
        if (verticalPlace+newSizeY>columns) {
            newVerticalPlace=columns-newSizeY;
        }
        //если повернуть можно, то задаем новые параметры фигуры
        if (checkRotate(horizontalPlace,newVerticalPlace,field, newFigureShape, newSizeX, newSizeY)) {
            this.figureShape = newFigureShape;
            this.sizeX = newSizeX;
            this.sizeY = newSizeY;
            //если в момент поворота достигли дна, фигура считается упавшей
            if (horizontalPlace+newSizeX==rows) {
                for (int i=0; i<newSizeX;i++) {
                    for (int j=0; j<newSizeY;j++) {
                        if (newFigureShape[i][j]==1) {
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
        int max = 0;
        for (int i = 0; i < newSizeX; i++) {
            for (int j = 0; j < newSizeY; j++) {
                int current = newFigureShape[i][j] + field[horizontalPlace + i][verticalPlace + j];
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