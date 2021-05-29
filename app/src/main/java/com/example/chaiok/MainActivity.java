package com.example.chaiok;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("chaiok.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student " +
                "(id integer primary key autoincrement, first_name TEXT, last_name TEXT, middle_name TEXT, gang TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS subject " +
                "(id integer primary key autoincrement, id_student integer, name TEXT, mark integer, " +
                "FOREIGN KEY (id_student) REFERENCES student (id))");
        db.close();

        View.OnClickListener add_student = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_student();
            }
        };
        View.OnClickListener list_student = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list_student();
            }
        };
        ((Button) findViewById(R.id.but_add_student)).setOnClickListener(add_student);
        ((Button) findViewById(R.id.but_student_list)).setOnClickListener(list_student);
    }

    protected void add_student() {
        Intent intent = new Intent(MainActivity.this, StudentAddActivity.class);
        startActivity(intent);
    }

    protected void list_student() {
        Intent intent = new Intent(MainActivity.this, StudentListActivity.class);
        startActivity(intent);
    }
}