package com.example.svetlana.try_tetris;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //описываем кнопку старта: переход к игре
        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TetrisActivity.class);
                startActivity(intent);
            }
        });
        //описываем кнопку рекордов через диалоговое окно
        final AlertDialog.Builder recordsAlertBox = new AlertDialog.Builder(this);
        final Record record = new Record(this);
        Button recordsButton = (Button) findViewById(R.id.records_button);
        recordsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // показываем окно
                recordsAlertBox.setTitle("Your best result is:");
                String TextToast = "Level " + Integer.toString(record.getRecordLevel()) +
                        "\nScore " + Integer.toString(record.getRecordScore());
                recordsAlertBox.setMessage(TextToast);
                recordsAlertBox.show();
            }
        });
        //описываем кнопку об игре через диалоговое окно
        final AlertDialog.Builder aboutAlertBox = new AlertDialog.Builder(this);
        Button aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aboutAlertBox.setTitle("About: rules");
                String TextToast = "Swipe Right or Left moves figure\n" +
                        "Swipe Up / Button Rotate rotate figure\n" +
                        "Swipe down fasten figure\n" +
                        "Level and speed up every 1500 points";
                aboutAlertBox.setMessage(TextToast);
                aboutAlertBox.show();
            }
        });
    }
}
