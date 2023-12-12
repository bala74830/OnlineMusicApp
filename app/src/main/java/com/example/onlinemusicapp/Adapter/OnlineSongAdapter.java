package com.example.onlinemusicapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlinemusicapp.Model.Getsongs;
import com.example.onlinemusicapp.Model.Utility;
import com.example.onlinemusicapp.OfflineMusicPlayer.AudioModel;
import com.example.onlinemusicapp.OfflineMusicPlayer.MusicPlayerActivity;
import com.example.onlinemusicapp.OfflineMusicPlayer.MyMediaPlayer;
import com.example.onlinemusicapp.OnlineMediaPlayer;
import com.example.onlinemusicapp.R;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OnlineSongAdapter extends RecyclerView.Adapter<OnlineSongAdapter.SongAdapterViewHolder>{

    ArrayList<Getsongs> songsList;
    Context context;
    StorageReference storageReference,mreference;

    public OnlineSongAdapter(ArrayList<Getsongs> songsList, Context context) {
        this.songsList = songsList;
        this.context = context;
    }

    @NonNull
    @Override
    public SongAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view=inflater.inflate(R.layout.songs_row,parent,false);
        return new SongAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapterViewHolder holder, int position) {

        Getsongs getsongs =songsList.get(position);
        holder.tv_title.setText(getsongs.getSongTitle());
        holder.tv_artist.setText(getsongs.getArtist());
        String duration = Utility.converDuration(Long.parseLong(getsongs.getsongDuration()));
        holder.tv_duration.setText(duration);

        if(MyMediaPlayer.currentIndex==position){
            holder.tv_title.setTextColor(Color.parseColor("#1DB954"));
            holder.tv_artist.setTextColor(Color.parseColor("#1DB954"));
            holder.iv_play_active.setVisibility(View.VISIBLE);
        }else{
            holder.tv_title.setTextColor(Color.parseColor("#FFFFFF"));
            holder.tv_artist.setTextColor(Color.parseColor("#FFFFFF"));
            holder.iv_play_active.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                Intent intent = new Intent(context, OnlineMediaPlayer.class);
                intent.putExtra("LIST",songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class SongAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView tv_title,tv_artist,tv_duration;
        ImageView iv_play_active,downloadsong;
        RelativeLayout songlayout;
        public SongAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_artist=itemView.findViewById(R.id.tv_artist);
            tv_duration=itemView.findViewById(R.id.tv_duration);
            tv_title=itemView.findViewById(R.id.tv_title);
            iv_play_active=itemView.findViewById(R.id.play_active);
            songlayout=itemView.findViewById(R.id.song_layout);
            downloadsong=itemView.findViewById(R.id.downloadsong);
        }
    }
}
