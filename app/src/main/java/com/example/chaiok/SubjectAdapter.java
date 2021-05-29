package com.example.chaiok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SubjectAdapter extends BaseAdapter {
    ArrayList<Subject> subject = new ArrayList<>();
    Context ctx;
    LayoutInflater lInflater;

    SubjectAdapter(Context context, ArrayList<Subject> subject) {
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