package com.musicbox.Controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.musicbox.Model.songItem;
import com.musicbox.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jai on 5/8/2017.
 */

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.SongListHolder>{


    public ItemClickCallback ItemClickCallback;



    public interface ItemClickCallback{
        void onImageClick(int p);
        void onItemClick(int p);
    }

    public void setItemClickCallback(final ItemClickCallback ItemClickCallback){
        this.ItemClickCallback = ItemClickCallback;
    }

    public List<songItem> songList;

    private LayoutInflater inflater;
    public SongListAdapter(ArrayList<songItem> songList, Context c) {
        this.inflater = LayoutInflater.from(c);
        this.songList = songList;
    }

    @Override
    public SongListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.song_item_layout, parent, false);
        return new SongListHolder(view);
    }

    @Override
    public void onBindViewHolder(SongListHolder holder, int position) {
        songItem s = songList.get(position);
        holder.albumName.setText(s.getAlbum());
        holder.songName.setText(s.getTitle());


        if(s.getAlbumArt() != null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            //Bitmap bitmap = BitmapFactory.decodeByteArray(value.arr, 0, value.arr.length, options);
            Bitmap bitmap = BitmapFactory.decodeFile(s.getAlbumArt(), options);

            holder.albumArt.setImageBitmap(bitmap);
        }
        else{
            holder.albumArt.setImageResource(R.drawable.music);
        }

    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    class SongListHolder extends RecyclerView.ViewHolder{

        public TextView albumName;
        public TextView songName;
        public ImageView albumArt;
        public View container;

        public SongListHolder(View itemView) {
            super(itemView);
            albumName = (TextView)itemView.findViewById(R.id.albumName);
            songName = (TextView)itemView.findViewById(R.id.songName);
            albumArt = (ImageView) itemView.findViewById(R.id.songAlbumArt);
            container = itemView.findViewById(R.id.song_root);

            albumArt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemClickCallback.onImageClick(getAdapterPosition());
                }
            });

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ItemClickCallback.onItemClick(getAdapterPosition());
                }
            });

        }


    }


}
