package com.example.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ChangeMediator extends Service {
    public ChangeMediator() {

        Observer settingsObserver  = new SettingsObserver
                (new boolean[]{false, false, false, false, false});

        //TODO
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
