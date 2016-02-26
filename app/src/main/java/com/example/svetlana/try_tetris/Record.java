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
    private int record;//сам рекорд
    private Context сontext;

    public Record(Context context) {
        this.сontext = context;
        // читаем из файла цифру с рекордом
        try {
            FileInputStream fileInputStream = сontext.openFileInput(RECORDS_FILE);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            this.record = (int) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            this.record = 0;
        }
    }

    public void rememberRecord(int score) {
        // запоминаем рекорд
        this.record = score;
        try {
            FileOutputStream fileOutputStream = сontext.openFileOutput(RECORDS_FILE, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(score);
            objectOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getRecord() {
        return record;
    }

}
