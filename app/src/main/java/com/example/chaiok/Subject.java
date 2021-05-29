package com.example.chaiok;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Subject implements Parcelable {
    private String name;
    private Integer mark;

    public Subject(String name, Integer mark) {
        this.name = name;
        this.mark = mark;
    }

    public Subject(Parcel in) {
        mark = in.readInt();
        name = in.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMark() {
        return mark;
    }

    public void setMark(Integer mark) {
        this.mark = mark;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mark);
        out.writeString(this.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Subject> CREATOR = new Parcelable.Creator<Subject>() {
        @Override
        public Subject createFromParcel(Parcel source) {
            return new Subject(source);
        }

        @Override
        public Subject[] newArray(int size) {
            return new Subject[size];
        }
    };
}