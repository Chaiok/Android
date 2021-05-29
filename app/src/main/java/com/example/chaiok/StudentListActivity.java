package com.example.chaiok;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class StudentListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        Update();

        View.OnClickListener delete_student = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_student();
            }
        };
        ((Button) findViewById(R.id.but_delete_student)).setOnClickListener(delete_student);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(1, 0, 0, "Выход");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case 0:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void EditStudent(int id){
        Intent intent = new Intent(StudentListActivity.this, EditSubjectActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    private void delete_student(){
        AlertDialog.Builder subjectDialog;
        subjectDialog = new AlertDialog.Builder(StudentListActivity.this);
        subjectDialog.setTitle("Кого удалить");
        subjectDialog.setCancelable(false);

        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.delete, null);
        final Spinner sp = (Spinner) vv.findViewById(R.id.delete_spinner);
        List<Student> student = new ArrayList<Student>();

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("chaiok.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM student", null);
        while (query.moveToNext()) {
            String first_name, last_name, middle_name,gang;
            Integer id;
            id = query.getInt(0);
            first_name = query.getString(1);
            last_name = query.getString(2);
            middle_name = query.getString(3);
            gang = query.getString(4);
            student.add(new Student(id, first_name, last_name, middle_name, gang));
        }
        query.close();
        db.close();

        StudentSpinner spinner = new StudentSpinner(StudentListActivity.this,student);
        sp.setAdapter(spinner);

        subjectDialog.setView(vv);

        subjectDialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Spinner sp = (Spinner) vv.findViewById(R.id.delete_spinner);
                Student student = (Student) sp.getSelectedItem();
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("chaiok.db", MODE_PRIVATE, null);
                try {
                    db.delete("student", "id = ?", new String[]{String.valueOf(student.getId())});
                    db.delete("subject", "id_student = ?", new String[]{String.valueOf(student.getId())});
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_LONG).show();
                } finally {
                }
                db.close();
                Update();
            }
        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        subjectDialog.show();
    }

    private void Update(){
        ListView lv = (ListView) findViewById(R.id.lvstudent);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("chaiok.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM student", null);
        ArrayList<Student> student = new ArrayList<Student>();
        while (query.moveToNext()) {
            String first_name, last_name, middle_name,gang;
            Integer id;
            id = query.getInt(0);
            first_name = query.getString(1);
            last_name = query.getString(2);
            middle_name = query.getString(3);
            gang = query.getString(4);
            student.add(new Student(id, first_name, last_name, middle_name, gang));
        }
        StudentAdapter adapter= new StudentAdapter(StudentListActivity.this, student);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                EditStudent(Integer.valueOf(((TextView) itemClicked.findViewById(R.id.tvlistid)).getText().toString()));
            }
        });
        query.close();
        db.close();
    }
}