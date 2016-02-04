package com.example.svetlana.try_tetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class TetrisActivity extends Activity {
    TetrisView tetrisView;
    FrameLayout frameLayout;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tetrisView = new TetrisView(this);
        setContentView(R.layout.tetris_activity);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        frameLayout.addView(tetrisView);
        button = (Button) findViewById(R.id.rotate_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.verticalPlace
                        = tetrisView.figure.rotate(tetrisView.horizontalPlace,
                        tetrisView.verticalPlace, tetrisView.field, tetrisView.getColumns(),
                        tetrisView.getRows(), tetrisView.isGameOver());
            }
        });
    }
}
