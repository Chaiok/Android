package com.example.chaiok;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EditSubjectActivity extends AppCompatActivity {
    int id;

    List<StudentAddActivity.Mark> mark;

    private class submark {
        private Integer id,mark;
        private String name;
        submark(Integer id, String name,Integer mark){
            this.id=id;
            this.mark=mark;
            this.name=name;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getMark() {
            return mark;
        }

        public void setMark(Integer mark) {
            this.mark = mark;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private class SubMarkSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
        private final List<submark> data;
        LayoutInflater lInflater;
        Context ctx;

        public SubMarkSpinnerAdapter(Context context, List<submark> data) {
            this.ctx = context;
            this.data = data;
            lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View recycle, ViewGroup parent) {
            TextView text;
            if (recycle != null) {
                text = (TextView) recycle;
            } else {
                text = (TextView) lInflater.inflate(
                        android.R.layout.simple_dropdown_item_1line, parent, false
                );
            }
            text.setTextColor(Color.BLACK);
            text.setText(data.get(position).getName());
            return text;
        }
    }

    private class SubMarkAdapter extends BaseAdapter {
        ArrayList<submark> subject = new ArrayList<>();
        Context ctx;
        LayoutInflater lInflater;

        SubMarkAdapter(Context context, ArrayList<submark> subject) {
            this.ctx = context;
            this.subject.addAll(subject);
            lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return subject.size();
        }

        @Override
        public Object getItem(int position) {
            return subject.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            view = lInflater.inflate(R.layout.subject_list_layout, parent, false);
            if (subject.isEmpty()) return view;
            ((TextView) view.findViewById(R.id.tvlistsubject)).setText(subject.get(position).getName());
            ((TextView) view.findViewById(R.id.tvlistmark)).setText(subject.get(position).getMark().toString());
            return view;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        mark = new ArrayList<StudentAddActivity.Mark>();
        mark.add(new StudentAddActivity.Mark(2,"Неатестован"));
        mark.add(new StudentAddActivity.Mark(3,"Удовлетворительно"));
        mark.add(new StudentAddActivity.Mark(4,"Хорошо"));
        mark.add(new StudentAddActivity.Mark(5,"Отлично"));

        Bundle arguments = getIntent().getExtras();
        id = arguments.getInt("id");

        Update();

        View.OnClickListener delete = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        };
        ((Button) findViewById(R.id.but_delete_sub)).setOnClickListener(delete);
        View.OnClickListener add = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add();
            }
        };
        ((Button) findViewById(R.id.but_add_sub)).setOnClickListener(add);
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

    private void delete(){
        AlertDialog.Builder subjectDialog;
        subjectDialog = new AlertDialog.Builder(EditSubjectActivity.this);
        subjectDialog.setTitle("Кого удалить");
        subjectDialog.setCancelable(false);

        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.delete, null);
        final Spinner sp = (Spinner) vv.findViewById(R.id.delete_spinner);
        List<submark> subject = new ArrayList<submark>();

        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("chaiok.db", MODE_PRIVATE, null);

        Cursor query = db.rawQuery("SELECT * FROM subject where id_student=?", new String[]{String.valueOf(id)});
        while (query.moveToNext()) {
            String name;
            int mark,id_sub;
            id_sub = query.getInt(0);
            name = query.getString(2);
            mark = query.getInt(3);
            subject.add(new submark(id_sub,name,mark));
        }
        query.close();
        db.close();

        SubMarkSpinnerAdapter spinner = new SubMarkSpinnerAdapter(EditSubjectActivity.this,subject);
        sp.setAdapter(spinner);

        subjectDialog.setView(vv);

        subjectDialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final Spinner sp = (Spinner) vv.findViewById(R.id.delete_spinner);
                submark student = (submark) sp.getSelectedItem();
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("chaiok.db", MODE_PRIVATE, null);
                try {
                    db.delete("subject", "id = ?", new String[]{String.valueOf(student.getId())});
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
        ListView lv = (ListView) findViewById(R.id.subject_listView);
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("chaiok.db", MODE_PRIVATE, null);
        Cursor query = db.rawQuery("SELECT * FROM subject where id_student=?", new String[]{String.valueOf(id)});
        ArrayList<submark> subject = new ArrayList<submark>();
        while (query.moveToNext()) {
            String name;
            int mark,id_sub;
            id_sub = query.getInt(0);
            name = query.getString(2);
            mark = query.getInt(3);
            subject.add(new submark(id_sub,name,mark));
        }
        SubMarkAdapter adapter= new SubMarkAdapter(EditSubjectActivity.this, subject);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                submark sub = (submark) parent.getItemAtPosition(position);
                //Edit(sub.getMark());
                Edit(sub.getId());
                //Edit(Integer.valueOf(((TextView) itemClicked.findViewById(R.id.tvlistid1)).getText().toString()));
            }
        });
        query.close();
        db.close();
    }

    private void Edit(int id_sub){
        AlertDialog.Builder subjectDialog;
        subjectDialog = new AlertDialog.Builder(EditSubjectActivity.this);
        subjectDialog.setTitle("Изменить данные о предмете");
        subjectDialog.setCancelable(false);

        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.subject_layout, null);
        final EditText et = (EditText) vv.findViewById(R.id.etSubject);
        final Spinner sp = (Spinner) vv.findViewById(R.id.spMarks);

        StudentAddActivity.MarkSpinnerAdapter adapter = new StudentAddActivity.MarkSpinnerAdapter(EditSubjectActivity.this, mark);
        sp.setAdapter(adapter);

        subjectDialog.setView(vv);

        subjectDialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues values = new ContentValues();
                String name;
                StudentAddActivity.Mark mark;
                name = et.getText().toString();
                mark = (StudentAddActivity.Mark) sp.getSelectedItem();
                values.put("name", name);
                values.put("mark", mark.getId());

                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("chaiok.db", MODE_PRIVATE, null);
                try {
                    db.update("subject", values, "id = ?", new String[]{String.valueOf(id_sub)});
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

    private void add(){
        AlertDialog.Builder subjectDialog;
        subjectDialog = new AlertDialog.Builder(EditSubjectActivity.this);
        subjectDialog.setTitle("Новый предмет");
        subjectDialog.setCancelable(false);

        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.subject_layout, null);
        final EditText et = (EditText) vv.findViewById(R.id.etSubject);
        final Spinner sp = (Spinner) vv.findViewById(R.id.spMarks);

        StudentAddActivity.MarkSpinnerAdapter adapter = new StudentAddActivity.MarkSpinnerAdapter(EditSubjectActivity.this, mark);
        sp.setAdapter(adapter);

        subjectDialog.setView(vv);

        subjectDialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ContentValues values = new ContentValues();
                String name;
                StudentAddActivity.Mark mark;
                name = et.getText().toString();
                mark = (StudentAddActivity.Mark) sp.getSelectedItem();
                values.put("id_student", id);
                values.put("name", name);
                values.put("mark", mark.getId());
                SQLiteDatabase db = getBaseContext().openOrCreateDatabase("chaiok.db", MODE_PRIVATE, null);
                long newRowId = -1;
                db.execSQL("PRAGMA foreign_keys=ON");
                try {
                    newRowId = db.insert("subject", null, values);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {}
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
}