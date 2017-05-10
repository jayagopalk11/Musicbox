package com.musicbox.Controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musicbox.Model.albumArtistItem;
import com.musicbox.Model.songItem;
import com.musicbox.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Jai on 5/10/2017.
 */

public class AlbArtistListAdapter extends RecyclerView.Adapter<AlbArtistListAdapter.AlbArtistHolder> {


    public AlbumClickCallBack albumClickCallBack;

    public interface AlbumClickCallBack{
        void onAlbArtistItemClick();
    }

    public void setAlbumClickCallBack(AlbumClickCallBack callBack){
        this.albumClickCallBack = callBack ;
    }

    public List<albumArtistItem> itemList;

    private LayoutInflater inflater;

    public AlbArtistListAdapter(ArrayList<albumArtistItem> list, Context c) {
        this.itemList = list;
        this.inflater = LayoutInflater.from(c);
    }

    @Override
    public AlbArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.alb_artist_item_layout, parent, false );
        return new AlbArtistHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbArtistHolder holder, int position) {
        albumArtistItem a = itemList.get(position);
        holder.itemName.setText(a.getAlbumName());
        holder.songsCount.setText(a.getSongsInAlbum()+" Song(s)");
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class AlbArtistHolder extends RecyclerView.ViewHolder{

        public TextView itemName;
        public TextView songsCount;
        public View container;

        public AlbArtistHolder(View itemView) {
            super(itemView);
            itemName = (TextView)itemView.findViewById(R.id.itemName);
            songsCount = (TextView)itemView.findViewById(R.id.songsCount);
            container = itemView.findViewById(R.id.alb_art_root);
        }
    }
}
