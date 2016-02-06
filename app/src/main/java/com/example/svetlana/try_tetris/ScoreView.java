package com.example.svetlana.try_tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class ScoreView extends View {

    private int score;
    private Paint paint;
    private Rect rect;

    public ScoreView(Context context) {
        super(context);
        paint = new Paint();//создаем то, чем будем рисовать
        rect = new Rect();//создаем место, где будем рисовать
    }

    public void onDraw(Canvas canvas) {
        canvas.getClipBounds(rect);
        //выводим текущие очки
        paint.setColor(Color.GREEN);
        int size = rect.width() / 3;
        paint.setTextSize(size);
        canvas.drawText("Score", 0, size, paint);
        canvas.drawText(Integer.toString(score), 0, size * 2, paint);
    }

    public void setScore(int score) {
        this.score = score;
    }
}
