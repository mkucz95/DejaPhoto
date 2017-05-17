package com.example.android;

import android.util.Log;

import java.util.*;
import java.util.Observer;

/**
 * Created by mkucz on 5/17/2017.
 */

public class ViewSettings implements Subject {
    private ArrayList<Observer> observers; //what is watching the settings object

    public static boolean[] settings = {false, false, false, false, false};
    private boolean changedState;

    public ViewSettings(){
        this.observers = new ArrayList<>();
    }


    public void sendState(boolean flag, int index){ //set only one specific setting
        this.settings[index] = flag;
        this.changedState = true;
        notifyObs();
    }

    public void sendState(boolean[] settings){ //set all new settings
        this.settings = settings;
        this.changedState = true;
        notifyObs();
    }

    @Override
    public void notifyObs() {
        if(!changedState) return; //do nothing
       // this.changedState = false;

    //for(Observer observer : observers) observer.update();  TODO
    }

    @Override
    public void register(Observer observer) {
        if(observer == null) Log.e("ViewSettings", "Observer Is NULL");

        if(!observers.contains(observer)) observers.add(observer); //add observer to our list
    }

    @Override
    public void unregister(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public Object getUpdate(Observer observer) {
        return this.settings;
    }
}
