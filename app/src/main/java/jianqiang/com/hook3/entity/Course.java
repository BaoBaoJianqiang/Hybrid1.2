package jianqiang.com.hook3.entity;

import java.io.Serializable;

public class Course implements Serializable {
    private String courseName;
    private int score;

    public Course(String courseName, int score) {
        this.courseName = courseName;
        this.score = score;
    }
}