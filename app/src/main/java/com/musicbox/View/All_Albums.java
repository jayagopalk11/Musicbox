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

import com.musicbox.Controller.AlbArtistListAdapter;
import com.musicbox.Controller.SongListAdapter;
import com.musicbox.R;


public class All_Albums extends Fragment implements AlbArtistListAdapter.AlbumClickCallBack{

    private RecyclerView Rec;
    private AlbArtistListAdapter albumListAdapter;

    @Override
    public void onAlbArtistItemClick() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all__albums, container, false);
        final FragmentActivity c = getActivity();
        Rec = (RecyclerView)rootView.findViewById(R.id.albumsRecView);
        Rec.setLayoutManager(new GridLayoutManager(c,2));




        albumListAdapter = new AlbArtistListAdapter(NowPlaying.allAlbumList,c);
        albumListAdapter.setAlbumClickCallBack(this);
        Rec.setAdapter(albumListAdapter);

        return rootView;
    }


}
