package com.example.android;

import android.util.Log;

import java.util.*;
import java.util.Observer;

/**
 * Created by mkucz on 5/17/2017.
 */

public class ViewSettings extends Observable implements Subject {
    private ArrayList<Observer> observers; //what is watching the settings object

    public static boolean[] settings = {false, false, false, false, false};
    private boolean changedState;

    public ViewSettings(boolean[] settings){
        this.observers = new ArrayList<>();
        this.settings = settings;
    }

    public boolean getState(String type){
        if(type.equals("dejaVu")) return settings[0];
        if(type.equals("location")) return settings[1];
        if(type.equals("time")) return settings[2];
        if(type.equals("day")) return settings[3];

       return settings[4];
    }


    public void sendState(boolean flag, int index){ //set only one specific setting
        this.settings[index] = flag;
        this.changedState = true;
    }

    public void sendState(boolean[] settings){ //set all new settings
        this.settings = settings;
        this.changedState = true;
    }

    @Override
    public void notifyObs() {
        if(!changedState) return; //do nothing
       // this.changedState = false;

    for(Observer observer : observers) observer.update(this, settings);
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
