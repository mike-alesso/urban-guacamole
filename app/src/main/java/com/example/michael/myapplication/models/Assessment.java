package com.example.michael.myapplication.models;

import java.util.Date;

/**
 * Created by michael on 9/4/16.
 */
public class Assessment {
    private String assessmentName, courseName;
    private int courseId;
    private int termId;
    private int assessmentId;

    public enum AssessmentType {OBJECTIVE, PERFORMANCE}
    private AssessmentType type;
    private Date goalDate;
    private boolean reminder;

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

    public void setAssessmentId (int id){
        this.assessmentId = id;
    }

    public int getAssessmentId () {
        return this.assessmentId;
    }

    public void setAssessmentName (String name){this.assessmentName = name;}

    public String getAssessmentName () {return this.assessmentName;}

    public void setCourseName (String name) {this.courseName = name;}

    public String getCourseName () {return this.courseName;}

    public void setType (AssessmentType type){this.type = type;}

    public AssessmentType getType (){return this.type;}

    public Date getGoalDate () {return this.goalDate;}

    public void setGoalDate (Date goalDate){this.goalDate = goalDate;}

    public void setReminder (boolean reminder) {this.reminder = reminder;}

    public boolean getReminder () {return this.reminder;}
}
