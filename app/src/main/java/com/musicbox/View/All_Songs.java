package com.musicbox.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musicbox.Controller.MainActivity;
import com.musicbox.Controller.SongListAdapter;
import com.musicbox.Controller.songsSqlHandler;
import com.musicbox.R;

import java.util.ArrayList;


public class All_Songs extends Fragment implements SongListAdapter.ItemClickCallback{

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




        myAdapter = new SongListAdapter(NowPlaying.allSongsList,c);
        myAdapter.setItemClickCallback(this);
        Rec.setAdapter(myAdapter);

        return rootView;
    }


    @Override
    public void onImageClick(int p) {

    }

    @Override
    public void onItemClick(int p) {

    }
}
