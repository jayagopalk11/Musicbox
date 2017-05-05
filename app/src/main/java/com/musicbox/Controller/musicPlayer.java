package com.musicbox.Controller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class musicPlayer extends Service {

    private final IBinder musicBinder = new customLocalBinder();

    public musicPlayer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return musicBinder;
    }


    public class customLocalBinder extends Binder{
        musicPlayer getService(){
            return musicPlayer.this;
        }
    }

}
