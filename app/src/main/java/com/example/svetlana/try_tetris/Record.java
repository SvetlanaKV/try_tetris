package com.example.svetlana.try_tetris;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

//сохраняем рекорд в игре
public class Record {

    // Название файла, в который будем сохранять рекорды
    private static String RECORDS_FILE = "records";
    private int recordScore;//сам рекорд
    private int recordLevel;
    private Context сontext;

    public Record(Context context) {
        this.сontext = context;
    }

    public void rememberRecord(int score, int level) {
        // запоминаем рекорд
        this.recordScore = score;
        this.recordLevel = level;
        try {
            FileOutputStream fileOutputStream = сontext.openFileOutput(RECORDS_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(score);
            objectOutputStream.writeObject(level);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //читаем значения из файла
    public void readFile() {
        try {
            FileInputStream fileInputStream = сontext.openFileInput(RECORDS_FILE);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.recordScore = (int) objectInputStream.readObject();
            this.recordLevel = (int) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            this.recordScore = 0;
            this.recordLevel = 0;
        }
    }

    public int getRecordScore() {
        readFile();
        return recordScore;
    }

    public int getRecordLevel() {
        readFile();
        return recordLevel;
    }
}
