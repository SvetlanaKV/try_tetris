package com.example.svetlana.try_tetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class TetrisActivity extends Activity {

    TetrisView tetrisView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tetris_activity);
        //выводим поле для очков
        ScoreView scoreView = new ScoreView(this);
        FrameLayout scoreLayout = (FrameLayout) findViewById(R.id.scoreLayout);
        scoreLayout.addView(scoreView);
        //выводим поле для игры в тетрис
        tetrisView = new TetrisView(this, scoreView);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        frameLayout.addView(tetrisView);
        //назначаем функцию на кнопку
        Button button = (Button) findViewById(R.id.rotate_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.getGame().setVerticalPlace(tetrisView.getGame().getFigure().
                        rotate(tetrisView.getGame().getHorizontalPlace(), tetrisView.getGame().getVerticalPlace(),
                                tetrisView.getGame().getField(), tetrisView.getGame().isGameOver()));
                tetrisView.postInvalidate();
            }
        });
    }
}
