package com.example.svetlana.try_tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import static com.example.svetlana.try_tetris.Figure.MAX_X;
import static com.example.svetlana.try_tetris.Figure.MAX_Y;

public class NextFigureView extends View {

    private Figure nextFigure;
    private Paint paint;
    private Rect rect;

    public NextFigureView(Context context) {
        super(context);
        paint = new Paint();
        rect = new Rect();
    }

    public void onDraw(Canvas canvas) {
        canvas.getClipBounds(rect);
        //выводим название блока
        paint.setColor(Color.GREEN);
        int size = rect.width() / 3;
        paint.setTextSize(size);
        canvas.drawText("Next", 0, size, paint);
        canvas.drawText("Figure", 0, size * 2, paint);
        //показываем следующую фигуры
        size = rect.height() / MAX_Y > rect.width() / MAX_X ?
                rect.width() / (MAX_X) : rect.height() / (MAX_Y);
        float startPointX = (rect.width() - size * MAX_X) / 2;
        float startPointY = size * 4;
        int startVerticalPlace = (MAX_Y - nextFigure.getSizeY()) / 2;
        int startHorizontalPlace = (MAX_X - nextFigure.getSizeX()) / 2;
        for (int i = 0; i < nextFigure.getSizeX(); i++) {
            for (int j = 0; j < nextFigure.getSizeY(); j++) {
                if (nextFigure.getFigureShape()[i][j] == 1) {
                    canvas.drawRect(startPointX + (startVerticalPlace + j) * size, startPointY + (startHorizontalPlace + i) * size,
                            startPointX + (startVerticalPlace + j + 1) * size, startPointY + (startHorizontalPlace + i + 1) * size, paint);
                }
            }
        }
        //отрисовываем поле для будущей фигуры
        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(1);
        for (int i = 0; i < MAX_X + 1; i++) {
            canvas.drawLine(startPointX, startPointY + i * size, startPointX + size * MAX_Y, startPointY + i * size, paint);
        }
        for (int i = 0; i < MAX_Y + 1; i++) {
            canvas.drawLine(startPointX + i * size, startPointY, startPointX + i * size, startPointY + size * MAX_X, paint);
        }
    }

    public void setNextFigure(Figure nextFigure) {
        this.nextFigure = nextFigure;
    }
}
