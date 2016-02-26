package com.example.svetlana.try_tetris;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//сохранение текущей игры
public class SavedGame {

    // Название файла, в который будем сохранять игру
    private static String GAME_FILE = "savedGame";
    private Game game;//сама игра
    private Context сontext;

    public SavedGame(Context context) {
        this.сontext = context;
        // читаем из файла сохраненную игру
        try {
            FileInputStream fileInputStream = сontext.openFileInput(GAME_FILE);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.game = (Game) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            this.game = new Game();
            this.game.dropFigure();
        }
    }

    public void rememberGame(Game game) {
        // запоминаем текущую игру
        this.game = game;
        try {
            FileOutputStream fileOutputStream = сontext.openFileOutput(GAME_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(game);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Game getGame() {
        return game;
    }

}
