package com.example.michael.myapplication.models;

/**
 * Created by michael on 9/4/16.
 */
public class CourseMentor {
    String name, email, phoneNumber, courseName;

    public void setName (String name){
        this.name = name;
    }

    public String getName () {
        return this.name;
    }

    public void setEmail (String email){
        this.email = email;
    }

    public String getEmail () {
        return this.email;
    }

    public void setPhoneNumber (String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber () {
        return this.phoneNumber;
    }

    public void setCourseName (String courseName){
        this.courseName = courseName;
    }

    public String getCourseName () {
        return this.courseName;
    }
}
