package com.example.onlinemusicapp.Adapter;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class OnlineSongAdapter extends RecyclerView.Adapter<OnlineSongAdapter.SongAdapterViewHolder>{

    ArrayList<Getsongs> songsList;
    Context context;
    StorageReference storageReference,mreference;
    int index;

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
            holder.iv_play_active.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = position;
                index=position;
                notifyDataSetChanged();
                if (index == position) {
                    holder.tv_title.setTextColor(Color.parseColor("#1DB954"));
                    holder.tv_artist.setTextColor(Color.parseColor("#1DB954"));
                    holder.iv_play_active.setVisibility(View.VISIBLE);
                }
                else{
                holder.tv_title.setTextColor(Color.parseColor("#FFFFFF"));
                holder.tv_artist.setTextColor(Color.parseColor("#FFFFFF"));
                holder.iv_play_active.setVisibility(View.INVISIBLE);}

                Intent intent = new Intent(context, OnlineMediaPlayer.class);
                intent.putExtra("LIST",songsList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.downloadsong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storageReference= FirebaseStorage.getInstance().getReference().child("songs");
                mreference = storageReference.child(songsList.get(position).getSongTitle()+".mp3");
                mreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        downloadFile(context, songsList.get(position).getSongTitle(), ".mp3", DIRECTORY_DOWNLOADS, url);
                        Toast.makeText(context, "Song Downloading...", Toast.LENGTH_LONG).show();
                        holder.downloadsong.setImageResource(R.drawable.check);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Download failed", Toast.LENGTH_LONG).show();
                    }
                });
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

    private void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {

        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }
}
