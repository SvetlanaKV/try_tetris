package com.example.svetlana.try_tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

//вью для отображения очков и рекорда
public class ScoreView extends View {

    private int score;
    private Record highScore;
    private Paint paint;
    private Rect rect;

    public ScoreView(Context context) {
        super(context);
        paint = new Paint();//создаем то, чем будем рисовать
        rect = new Rect();//создаем место, где будем рисовать
        highScore = new Record(context); //рекорд
    }

    public void onDraw(Canvas canvas) {
        canvas.getClipBounds(rect);
        paint.setColor(Color.GREEN);
        int size = rect.width() / 3;
        paint.setTextSize(size);
        canvas.drawText("High", 0, size, paint);
        canvas.drawText("Score", 0, size * 2, paint);
        canvas.drawText(Integer.toString(highScore.getRecord()), 0, size * 3, paint);
        canvas.drawText("Score", 0, size * 5, paint);
        canvas.drawText(Integer.toString(score), 0, size * 6, paint);
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Record getHighScore() {
        return highScore;
    }
}
