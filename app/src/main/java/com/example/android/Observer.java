package com.example.android;

/**
 * Created by mkucz on 5/17/2017.
 * this interface defines what each observer needs to do
 */

public interface Observer {
    public void update(); //updates info of observer when called by subject

    public void setSubject(Subject subject); //which subject to watch for updates
}
