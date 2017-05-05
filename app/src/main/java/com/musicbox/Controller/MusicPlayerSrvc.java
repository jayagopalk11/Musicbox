package com.musicbox.Controller;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MusicPlayerSrvc extends Service {

    private final IBinder musicBinder = new customLocalBinder();

    public MusicPlayerSrvc() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBinder;
    }


    public class customLocalBinder extends Binder{
        public MusicPlayerSrvc getService(){
            return MusicPlayerSrvc.this;
        }
    }

}
