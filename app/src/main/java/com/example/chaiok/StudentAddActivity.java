package com.example.chaiok;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
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

public class StudentAddActivity extends AppCompatActivity {
    ArrayList<Subject> subject = new ArrayList<>();

    List<Mark> mark;

    static class Mark {
        private Integer id;
        private String mark;
        Mark(Integer id, String mark){
            this.id=id;
            this.mark=mark;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getMark() {
            return mark;
        }

        public void setMark(String mark) {
            this.mark = mark;
        }
    }

    static class MarkSpinnerAdapter extends BaseAdapter implements SpinnerAdapter{
        private final List<Mark> data;
        LayoutInflater lInflater;
        Context ctx;

        public MarkSpinnerAdapter(Context context, List<Mark> data) {
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
            if(data.get(position).getId()<=2) {
                text.setTextColor(Color.RED);}
            else{
                text.setTextColor(Color.BLACK);
            }
            text.setText(data.get(position).getMark());
            return text;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.studentactivity);

        EditText ed1 = findViewById(R.id.studentfirstname);
        ed1.addTextChangedListener(tw);
        EditText ed2 = findViewById(R.id.studentlastname);
        ed2.addTextChangedListener(tw);
        EditText ed3 = findViewById(R.id.studentmidname);
        ed3.addTextChangedListener(tw);
        EditText ed4 = findViewById(R.id.studentgroup);
        ed4.addTextChangedListener(tw);

        mark = new ArrayList<Mark>();
        mark.add(new Mark(2,"Неатестован"));
        mark.add(new Mark(3,"Удовлетворительно"));
        mark.add(new Mark(4,"Хорошо"));
        mark.add(new Mark(5,"Отлично"));


        View.OnClickListener cancel = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        };
        View.OnClickListener finish_add = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish_add();
            }
        };
        View.OnClickListener add_subject = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_subject();
            }
        };
        ((Button) findViewById(R.id.but_add_subject)).setOnClickListener(add_subject);
        ((Button) findViewById(R.id.but_cancel)).setOnClickListener(cancel);
        ((Button) findViewById(R.id.but_finish)).setOnClickListener(finish_add);
    }

    TextWatcher tw = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (((EditText) findViewById(R.id.studentfirstname)).getText().toString().equals("") ||
                    ((EditText) findViewById(R.id.studentlastname)).getText().toString().equals("") ||
                    ((EditText) findViewById(R.id.studentmidname)).getText().toString().equals("") ||
                    ((EditText) findViewById(R.id.studentgroup)).getText().toString().equals("")) {
                ((Button) findViewById(R.id.but_finish)).setVisibility(View.INVISIBLE);
            } else {
                ((Button) findViewById(R.id.but_finish)).setVisibility(View.VISIBLE);
            }
        }
    };

    public void add_subject() {
        AlertDialog.Builder subjectDialog;
        subjectDialog = new AlertDialog.Builder(StudentAddActivity.this);
        subjectDialog.setTitle("");
        subjectDialog.setCancelable(false);

        View vv = (LinearLayout) getLayoutInflater().inflate(R.layout.subject_layout, null);
        final EditText et = (EditText) vv.findViewById(R.id.etSubject);
        final Spinner sp = (Spinner) vv.findViewById(R.id.spMarks);

        MarkSpinnerAdapter adapter = new MarkSpinnerAdapter(StudentAddActivity.this, mark);
        sp.setAdapter(adapter);

        subjectDialog.setView(vv);

        subjectDialog.setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name;
                Mark mark;
                name = et.getText().toString();
                mark = (Mark) sp.getSelectedItem();
                subject.add( new Subject(name, mark.getId()));
                Update();
            }
        }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        subjectDialog.show();
    }

    SubjectAdapter olAdapter;
    public void Update() {
        olAdapter = new SubjectAdapter(StudentAddActivity.this, subject);
        ListView lvmain = (ListView) findViewById(R.id.lbsubjects);
        lvmain.setAdapter(olAdapter);
        lvmain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                showPopupMenu(itemClicked.findViewById(R.id.tvlistmark), position);
            }
        });
    }

    private void showPopupMenu(View v, int position) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popup_marks);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Integer mark;
                switch (item.getItemId()) {
                    case R.id.popup5: {
                        mark = 5;
                        break;
                    }
                    case R.id.popup4: {
                        mark = 4;
                        break;
                    }
                    case R.id.popup3: {
                        mark = 3;
                        break;
                    }
                    case R.id.popup2: {
                        mark = 2;
                        break;
                    }
                    default:
                        return false;
                }
                subject.get(position).setMark(mark);
                olAdapter.notifyDataSetChanged();
                return true;
            }
        });
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }

    private void cancel(){
        finish();
    }

    private void finish_add(){
        SQLiteDatabase db = getBaseContext().openOrCreateDatabase("chaiok.db", MODE_PRIVATE, null);
        ContentValues values = new ContentValues();

        EditText ed1 = findViewById(R.id.studentfirstname);
        EditText ed2 = findViewById(R.id.studentlastname);
        EditText ed3 = findViewById(R.id.studentmidname);
        EditText ed4 = findViewById(R.id.studentgroup);
        values.put("first_name", ed1.getText().toString());
        values.put("last_name", ed2.getText().toString());
        values.put("middle_name", ed3.getText().toString());
        values.put("gang", ed4.getText().toString());
        long newRowId = -1;
        db.execSQL("PRAGMA foreign_keys=ON");
            try {
                newRowId = db.insert("student", null, values);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (newRowId != -1){
                    for(int i=0;i<subject.size();i=i+1){
                        ContentValues value = new ContentValues();
                        value.put("id_student", newRowId);
                        value.put("name", subject.get(i).getName());
                        value.put("mark", subject.get(i).getMark());
                        db.insert("subject", null, value);
                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "Произошла ошибка", Toast.LENGTH_LONG).show();
            }
        db.close();
        finish();
    }
}
