package com.example.michael.myapplication.models;

import java.util.Date;

/**
 * Created by michael on 9/4/16.
 */
public class Term {
    private int id;
    private String title;
    private Date startDate, endDate;


    public Term() {
    }

    public Term(int id, String title, Date startDate, Date endDate) {
        this.setId(id);
        this.setTitle(title);
        this.setStartDate(startDate);
        this.setEndDate(endDate);
    }

    public void setId (int id){
        this.id = id;
    }

    public int getId () {
        return this.id;
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
}
