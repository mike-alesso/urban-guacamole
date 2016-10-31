package com.example.michael.myapplication.models;

/**
 * Created by michael on 9/4/16.
 */
public class CourseMentor {
    public CourseMentor(int courseMentorId, int courseId, String name, String phone, String email) {
        this.setMentorId(courseMentorId);
        this.setCourseId(courseId);
        this.setName(name);
        this.setPhoneNumber(phone);
        this.setEmail(email);
    }
    String name, email, phoneNumber, courseName;
    int courseId, mentorId;

    public CourseMentor(){}

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

    public void setCourseId (int courseId){
        this.courseId = courseId;
    }

    public int getCourseId () {
        return this.courseId;
    }

    public void setMentorId (int mentorId){
        this.mentorId = mentorId;
    }

    public int getMentorId () {
        return this.mentorId;
    }
}
