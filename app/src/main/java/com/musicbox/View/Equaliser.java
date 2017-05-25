package com.musicbox.View;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.media.audiofx.BassBoost;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.musicbox.Controller.MusicPlayerSrvc;
import com.musicbox.R;

import java.util.ArrayList;
import java.util.List;

public class Equaliser extends AppCompatActivity {

    MusicPlayerSrvc musicPlayerService;
    Boolean isBound = false;
    public static Switch enabler;
    List<SeekBar> allSeeks = new ArrayList<SeekBar>();
    public static Spinner presetValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equaliser);

        Intent n = new Intent(this, MusicPlayerSrvc.class);
        bindService(n,musicBoxConnection, Context.BIND_AUTO_CREATE);
        enabler = (Switch)findViewById(R.id.enableEq);
        enabler.setChecked(false);



        enabler.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(getApplicationContext(),"Turned on",Toast.LENGTH_SHORT).show();
                    for(int i=0; i < allSeeks.size(); i++){
                        allSeeks.get(i).setEnabled(true);
                    }
                    musicPlayerService.myEq.setEnabled(true);
                    presetValues.setEnabled(true);
                }else{
                    for(int i=0; i < allSeeks.size(); i++){
                        allSeeks.get(i).setEnabled(false);
                    }
                    musicPlayerService.myEq.setEnabled(false);
                    presetValues.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"Turned off",Toast.LENGTH_SHORT).show();
                }

            }
        });

        presetValues = (Spinner) findViewById(R.id.presets);
        List<String> categories = new ArrayList<String>();
        categories.add("Automobile");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        presetValues.setAdapter(dataAdapter);



    }


    ServiceConnection musicBoxConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            MusicPlayerSrvc.customLocalBinder binder = (MusicPlayerSrvc.customLocalBinder) iBinder;
            musicPlayerService = binder.getService();
            isBound = true;
            drawEqualiser();
            Log.i("Binder log: ","Successfully binded");


        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound=false;

        }
    };

    public void drawEqualiser(){
        LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.eqLinearLayout);


        TextView EqHeading = new TextView(this);
        EqHeading.setText("Equalizer");
        EqHeading.setTextSize(22);
        EqHeading.setGravity(Gravity.CENTER_HORIZONTAL);
        //mLinearLayout.addView(EqHeading);

        final short numberOfFreqBands = musicPlayerService.getEqNumberOfBands();
        final short lowerEqualizerBandLevel = musicPlayerService.getBandLevelRange()[0];
        final short upperEqualizerBandLevel = musicPlayerService.getBandLevelRange()[1];

        for (short a=0;a<numberOfFreqBands;a++){
            int centerFreq = musicPlayerService.myEq.getCenterFreq(a);
            String centerFreqStr;
            centerFreq = centerFreq/1000;
            if(centerFreq > 1000){
                centerFreqStr = String.valueOf(centerFreq/1000)+"."+
                        String.valueOf(centerFreq%1000).substring(0,1)+" KHz";
            }else{
                centerFreqStr = String.valueOf(centerFreq)+" Hz";
            }


            TextView freqTitle = new TextView(this);
            freqTitle.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            freqTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            freqTitle.setText(centerFreqStr);
            freqTitle.setTextSize(15);
            freqTitle.setId(122+a);
            mLinearLayout.addView(freqTitle);
            Log.i("loop log: ","text view added"+String.valueOf(a));

            LinearLayout seekBarRowLayout = new LinearLayout(this);
            seekBarRowLayout.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams seekBarLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            seekBarLayoutParams.setMargins(30,10,30,0);
            seekBarRowLayout.setLayoutParams(seekBarLayoutParams);

            Log.i("loop log: ","linear layout created");

            TextView lowLimitBandText = new TextView(this);
            lowLimitBandText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            lowLimitBandText.setText((lowerEqualizerBandLevel/100)+" dB");
            Log.i("loop log: ","low limit created");


            TextView upLimitBandText = new TextView(this);
            upLimitBandText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            upLimitBandText.setText((upperEqualizerBandLevel/100)+" dB");
            Log.i("loop log: ","up limit created");

            //String x = "seekBar"+String.valueOf(a)
            SeekBar seekBar = new SeekBar(this);
            seekBar.setId(444+a);

            LinearLayout.LayoutParams seekbarParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);

            seekbarParams.weight = 1;
            seekBar.setLayoutParams(seekbarParams);
            seekBar.setMax(upperEqualizerBandLevel - lowerEqualizerBandLevel);
            seekBar.setProgress(musicPlayerService.myEq.getBandLevel(a)-lowerEqualizerBandLevel);

            Log.i("Seekbar current level: ",String.valueOf(musicPlayerService.myEq.getBandLevel(a)));

            final short bandLevel = a;
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    musicPlayerService.myEq.setBandLevel(bandLevel,(short)(i+lowerEqualizerBandLevel));
                    Log.i("Seekbar change: ",String.valueOf((int)i+lowerEqualizerBandLevel));
                    Log.i("Seekbar current value: ",String.valueOf(musicPlayerService.myEq.getBandLevel(bandLevel)));

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            allSeeks.add(seekBar);
            seekBarRowLayout.addView(lowLimitBandText);
            seekBarRowLayout.addView(seekBar);
            seekBarRowLayout.addView(upLimitBandText);

            mLinearLayout.addView(seekBarRowLayout);
            musicPlayerService.myEq.setEnabled(true);

            int presetCount = musicPlayerService.myEq.getNumberOfPresets();
            for(short m = 0; m<presetCount ; m++) {
                Log.i("preset name", musicPlayerService.myEq.getPresetName(m));
            }
        }

    }

}
