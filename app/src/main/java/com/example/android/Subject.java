package com.example.android;

import java.util.ArrayList;
import java.util.Observer;

/**
 * Created by mkucz on 5/17/2017.
 * interface defines what each subject (the thing that is being watched and then
 * sends info to observers) will need to implement
 */


public interface Subject {
    public void notifyObs();
    public void register(Observer observer);
    public void unregister(Observer observer);
    public Object getUpdate(Observer observer);
}
