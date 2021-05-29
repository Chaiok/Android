package com.example.chaiok;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class Student implements Parcelable {
    private String first_name, last_name, middle_name,gang;
    private Integer id;

    public Student(Integer id, String firstname, String lastname, String midname, String gang) {
        this.id = id;
        this.first_name = firstname;
        this.last_name = lastname;
        this.middle_name = midname;
        this.gang = gang;
    }

    public Student(Parcel in) {
        id=in.readInt();
        first_name = in.readString();
        last_name = in.readString();
        middle_name = in.readString();
        gang = in.readString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMiddle_name() {
        return middle_name;
    }

    public void setMiddle_name(String middle_name) {
        this.middle_name = middle_name;
    }

    public String getGang() {
        return gang;
    }

    public void setGang(String gang) {
        this.gang = gang;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeString(this.first_name);
        out.writeString(this.last_name);
        out.writeString(this.middle_name);
        out.writeString(this.gang);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Student> CREATOR = new Parcelable.Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}