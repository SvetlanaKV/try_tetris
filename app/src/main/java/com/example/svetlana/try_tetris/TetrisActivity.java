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
    private Button playPauseButton;
    private Button rotateButton;

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
        playPauseButton = (Button) findViewById(R.id.play_pause_button);
        rotateButton = (Button) findViewById(R.id.rotate_button);
        //назначаем действия на кнопки
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActionTask task = new ActionTask(ActionTypes.UP, tetrisView);
                task.run();
            }
        });
        playPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tetrisView.getGame().isGameOn()) {
                    tetrisView.getGame().setGameOn(false);
                    tetrisView.getTimer().cancel();
                    playPauseButton.setText("Play");
                } else {
                    tetrisView.playGame();
                    playPauseButton.setText("Pause");
                }
            }
        });
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tetrisView.getGame().setGameOver(true);
                tetrisView.playGame();
                playPauseButton.setText("Pause");
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
