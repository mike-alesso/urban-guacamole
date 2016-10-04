package com.example.michael.myapplication.models;

import java.util.Date;

/**
 * Created by michael on 9/4/16.
 */
public class Course {
    public Course(int courseId, int termId, String title, Date startDate, Date endDate, Status status, boolean startReminder, boolean endReminder) {
        this.setCourseId(courseId);
        this.setTermId(termId);
        this.setTitle(title);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
        this.setStatus(status);
        this.setStartDateReminder(startReminder);
        this.setEndDateReminder(endReminder);
    }
    //Default constructor
    public Course(){}

    private String title;
    private Date startDate, endDate;
    private int courseId;
    private int termId;

    public enum Status {IN_PROGRESS, COMPLETED, DROPPED, PLAN_TO_TAKE}
    private Status courseStatus;
    private boolean startDateReminder, endDateReminder;

    public void setCourseId (int id){
        this.courseId = id;
    }

    public int getCourseId () {
        return this.courseId;
    }

    public void setTermId (int id){
        this.termId = id;
    }

    public int getTermId () {
        return this.termId;
    }

    public void setTitle (String title){
        this.title = title;
    }

    public String getTitle () {
        return this.title;
    }

    public void setStartDate (Date startDate){
        this.startDate = startDate;
    }

    public Date getStartDate () {
        return this.startDate;
    }

    public void setEndDate (Date endDate){
        this.endDate = endDate;
    }

    public Date getEndDate () {
        return this.endDate;
    }

    public void setStatus (Status status) {this.courseStatus = status;}

    public Status getStatus () {return this.courseStatus;}

    public void setStartDateReminder (boolean startDateReminder){
        this.startDateReminder = startDateReminder;
    }

    public boolean getStartDateReminder () {
        return this.startDateReminder;
    }

    public void setEndDateReminder (boolean endDateReminder){
        this.endDateReminder = endDateReminder;
    }

    public boolean getEndDateReminder () {
        return this.endDateReminder;
    }
}
