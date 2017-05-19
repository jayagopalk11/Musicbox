package com.musicbox.View;

import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.musicbox.Controller.MainActivity;
import com.musicbox.Controller.MusicPlayerSrvc;
import com.musicbox.Controller.SongListAdapter;
import com.musicbox.Controller.songsSqlHandler;
import com.musicbox.Model.songItem;
import com.musicbox.R;




public class All_Songs extends Fragment implements SongListAdapter.ItemClickCallback{

    MusicPlayerSrvc musicPlayerService;
    Boolean isBound = false;
    private RecyclerView Rec;
    private SongListAdapter myAdapter;
    public All_Songs() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_all__songs, container, false);
        final FragmentActivity c = getActivity();
        Rec = (RecyclerView)rootView.findViewById(R.id.recView);
        Rec.setLayoutManager(new LinearLayoutManager(c));

        Intent n = new Intent(getActivity(), MusicPlayerSrvc.class);
        //getActivity().startService(n);
        getActivity().bindService(n,musicBoxConnection, Context.BIND_AUTO_CREATE);


        myAdapter = new SongListAdapter(NowPlaying.allSongsList,c);
        myAdapter.setItemClickCallback(this);
        Rec.setAdapter(myAdapter);

        return rootView;
    }


    @Override
    public void onImageClick(int p) {
        songItem i = NowPlaying.allSongsList.get(p);
        Toast.makeText(getActivity().getApplicationContext(),i.getId(),Toast.LENGTH_SHORT).show();

        Boolean success = false;
        SharedPreferences sharedPref = getContext().getSharedPreferences("Music",Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("songIndex",i.getId());
        editor.apply();
        NowPlaying.wItem = i;
        musicPlayerService.mp.pause();

        Log.i("mp stat","mp paused from playlist");
        musicPlayerService.mp.stop();
        Log.i("mp stat","mp stopped from playlist");
        musicPlayerService.mp.reset();
        Log.i("mp stat","mp reset from playlist");
        musicPlayerService.isPaused = false;
        musicPlayerService.isPlaying = false;


        try {
            Uri trackUri = ContentUris.withAppendedId(
                    android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    Long.valueOf(i.getId()));

            musicPlayerService.mp.setDataSource(getActivity().getApplicationContext(), trackUri);
            success = true;
        }catch (Exception e){
            Log.i("Exception","Exception handled"+e.toString());
        }
        if(!success) {
            try {
                Uri trackUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
                        Long.valueOf(i.getId()));

                musicPlayerService.mp.setDataSource(getActivity().getApplicationContext(), trackUri);
            } catch (Exception e) {
                Log.i("Exception", "Exception handled" + e.toString());
            }
        }

        musicPlayerService.playMusic();
        NowPlaying.songName.setText(i.getTitle());
        NowPlaying.albumName.setText(i.getAlbum());


    }

    @Override
    public void onDestroyView() {
        getActivity().unbindService(musicBoxConnection);
        super.onDestroyView();
    }

    @Override
    public void onItemClick(int p) {
        songItem i = NowPlaying.allSongsList.get(p);
        Toast.makeText(getContext(),i.getId(),Toast.LENGTH_SHORT).show();
    }

    ServiceConnection musicBoxConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            MusicPlayerSrvc.customLocalBinder binder = (MusicPlayerSrvc.customLocalBinder) iBinder;
            musicPlayerService = binder.getService();
            isBound = true;
            Log.i("Binder log: ","Successfully binded");
            Log.i("play status: ",String.valueOf(musicPlayerService.isPlaying()));
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound=false;

        }
    };
}
