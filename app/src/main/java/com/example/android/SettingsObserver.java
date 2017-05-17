package com.example.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.*;

public class SettingsObserver implements Observer {

    private boolean[] settings ={false, false, false, false, false};
    //dejaMode, location, time, day, karma

    private Subject setting;

    public SettingsObserver(boolean [] settings) {
        this.settings = settings;
    }

    @Override
    public void update() {
        //boolean[] settings = (boolean[]) setting.getUpdate();
        //TODO
    }

    @Override
    public void setSubject(Subject subject) {
        this.setting = subject;
    }
}
