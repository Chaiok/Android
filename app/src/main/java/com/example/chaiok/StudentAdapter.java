package com.example.chaiok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class StudentAdapter extends BaseAdapter {
    ArrayList<Student> students = new ArrayList<>();
    Context ctx;
    LayoutInflater lInflater;

    StudentAdapter(Context context, ArrayList<Student> students) {
        this.ctx = context;
        //this.subjects = subjects;
        this.students.addAll(students);
        lInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public Object getItem(int position) {
        return students.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        view = lInflater.inflate(R.layout.student_list_layout, parent, false);
        if (students.isEmpty()) return view;
        ((TextView) view.findViewById(R.id.tvlistfname)).setText(students.get(position).getFirst_name()+" "+students.get(position).getLast_name().substring(0,1)+" "+students.get(position).getMiddle_name().substring(0,1));
        //((TextView) view.findViewById(R.id.tvlistlname)).setText(students.get(position).getLast_name());
        //((TextView) view.findViewById(R.id.tvlistmname)).setText(students.get(position).getMiddle_name());
        ((TextView) view.findViewById(R.id.tvlistgang)).setText(students.get(position).getGang().toString());
        return view;
    }
}