package com.musicbox.View;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.musicbox.Controller.AlbArtistListAdapter;
import com.musicbox.R;


public class All_Artists extends Fragment implements AlbArtistListAdapter.AlbumClickCallBack{

    private RecyclerView recArtist;
    private AlbArtistListAdapter albumListAdapter;

    public All_Artists() {
        // Required empty public constructor
    }
    @Override
    public void onAlbArtistItemClick() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all__artists, container, false);
        final FragmentActivity c = getActivity();
        recArtist = (RecyclerView)rootView.findViewById(R.id.artistsRecView);
        recArtist.setLayoutManager(new GridLayoutManager(c,2));




        albumListAdapter = new AlbArtistListAdapter(NowPlaying.allArtistList,c);
        albumListAdapter.setAlbumClickCallBack(this);
        recArtist.setAdapter(albumListAdapter);

        return rootView;
    }



}
