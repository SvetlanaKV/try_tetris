package com.example.svetlana.try_tetris;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;

public class TetrisActivity extends Activity {

    private TetrisView tetrisView;
    private SavedGame savedGame;
    private Button restartButton;
    private Button playButton;
    private Button pauseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tetris_activity);
        savedGame = new SavedGame(this);
        //выводим поле для очков
        ScoreView scoreView = new ScoreView(this);
        FrameLayout scoreLayout = (FrameLayout) findViewById(R.id.scoreLayout);
        scoreLayout.addView(scoreView);
        //выводим поле для отображения следующей фигуры
        NextFigureView nextFigureView = new NextFigureView(this);
        FrameLayout nextFigureLayout = (FrameLayout) findViewById(R.id.nextFigureLayout);
        nextFigureLayout.addView(nextFigureView);
        //выводим поле для игры в тетрис
        tetrisView = new TetrisView(this, scoreView, nextFigureView, savedGame.getGame());
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        frameLayout.addView(tetrisView);
        //добавляем свайпы
        TableLayout mainLayout = (TableLayout) findViewById(R.id.mainLayout);
        mainLayout.setOnTouchListener(new OnSwipeTouchListener(TetrisActivity.this, tetrisView));
        //задаем кнопки
        restartButton = (Button) findViewById(R.id.restart_button);
        pauseButton = (Button) findViewById(R.id.pause_button);
        pauseButton.setEnabled(false);
        playButton = (Button) findViewById(R.id.play_button);
        //назначаем действия на кнопки
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.getGame().setGameOn(false);
                tetrisView.getTimer().cancel();
                pauseButton.setEnabled(false);
                playButton.setEnabled(true);
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.playGame();
                pauseButton.setEnabled(true);
                playButton.setEnabled(false);
            }
        });
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.getGame().setGameOver(true);
                tetrisView.playGame();
                pauseButton.setEnabled(true);
                playButton.setEnabled(false);
            }
        });
    }

    //сохраняем игру при выходе
    @Override
    public void onStop() {
        tetrisView.getGame().setGameOn(false);
        tetrisView.getTimer().cancel();
        savedGame.rememberGame(tetrisView.getGame());
        super.onStop();
    }

}
