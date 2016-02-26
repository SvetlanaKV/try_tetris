package com.example.svetlana.try_tetris;

import java.util.Timer;
import java.util.TimerTask;

import static com.example.svetlana.try_tetris.Game.COLUMNS;
import static com.example.svetlana.try_tetris.Game.ROWS;
import static com.example.svetlana.try_tetris.TetrisView.SPEED;

//Задание, которое определяет, что происходит с фигурой
public class ActionTask extends TimerTask {

    private ActionTypes action;
    private TetrisView tetrisView;
    private Game game;

    public ActionTask(ActionTypes action, TetrisView tetrisview) {
        this.action = action;
        this.tetrisView = tetrisview;
        this.game = tetrisview.getGame();
    }

    @Override
    public void run() {
        switch (action) {
            case LEFT:
                //двигаем фигуру налево
                if (game.getVerticalPlace() > 0 && game.checkLeft()) {
                    game.setVerticalPlace(game.getVerticalPlace() - 1);
                }
                break;
            case RIGHT:
                //двигаем фигуру направо
                if (game.getVerticalPlace() < COLUMNS - game.getFigure().getSizeY() && game.checkRight()) {
                    game.setVerticalPlace(game.getVerticalPlace() + 1);
                }
                break;
            case DOWN:
                //фигура просто падает вниз
                if (game.getHorizontalPlace() == (ROWS - game.getFigure().getSizeX()) || !game.checkDown()) {
                    game.rememberFallenFigure(game.getFigure());
                    //обновляем очки
                    int score = game.getScore();
                    tetrisView.getScoreView().setScore(score);
                    if (score > tetrisView.getScoreView().getHighScore().getRecord()) {
                        tetrisView.getScoreView().getHighScore().rememberRecord(score);
                    }
                    tetrisView.getScoreView().postInvalidate();
                    //генерируем новую фигуру
                    game.dropFigure();
                    //показываем будущую фигуру
                    tetrisView.getNextFigureView().setNextFigure(game.getNextFigure());
                    tetrisView.getNextFigureView().postInvalidate();
                    //запускаем таймер
                    tetrisView.getTimer().cancel();
                    tetrisView.setTimer(new Timer());
                    ActionTask task = new ActionTask(ActionTypes.DOWN, tetrisView);
                    tetrisView.getTimer().schedule(task, 300L, SPEED);
                    if (!game.checkCreate()) {
                        game.setGameOver(true);
                        tetrisView.getTimer().cancel();
                    }
                    game.updateField();
                } else {
                    game.setHorizontalPlace(game.getHorizontalPlace() + 1);
                }
                break;
            case FASTDOWN:
                //фигура ускоряется
                tetrisView.getTimer().cancel();
                tetrisView.setTimer(new Timer());
                ActionTask task = new ActionTask(ActionTypes.DOWN, tetrisView);
                tetrisView.getTimer().schedule(task, 0, SPEED / 30);
                game.updateField();
                break;
            case UP:
                //фигура переворачивается
                tetrisView.getGame().setVerticalPlace(tetrisView.getGame().getFigure().
                        rotate(tetrisView.getGame().getHorizontalPlace(), tetrisView.getGame().getVerticalPlace(),
                                tetrisView.getGame().getField(), tetrisView.getGame().isGameOver(), tetrisView.getGame().isGameOn()));
                break;
        }
        //обновляем графику
        tetrisView.postInvalidate();
    }
}
