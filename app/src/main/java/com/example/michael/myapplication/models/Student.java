package com.example.michael.myapplication.models;

/**
 * Created by michael on 9/3/16.
 */
public class Student {
    String name, email, uname, pass;


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

    public void setUsername (String username){
        this.uname = username;
    }

    public String getUsername () {
        return this.uname;
    }

    public void setPassword (String password){
        this.pass = password;
    }

    public String getPassword () {
        return this.pass;
    }
}
