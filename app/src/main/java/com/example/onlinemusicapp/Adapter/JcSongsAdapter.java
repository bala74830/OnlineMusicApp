package com.example.onlinemusicapp.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.onlinemusicapp.Model.Getsongs;
import com.example.onlinemusicapp.Model.Utility;
import com.example.onlinemusicapp.R;

import java.util.List;

public class JcSongsAdapter extends RecyclerView.Adapter<JcSongsAdapter.SongAdapterViewHolder>{

    private int selectedPosition;
    Context context;
    List<Getsongs> arraylistsongs;
    private RecyclerItemClickListener listener;

    public JcSongsAdapter(Context context, List<Getsongs> arraylistsongs, RecyclerItemClickListener listener) {
        this.context = context;
        this.arraylistsongs = arraylistsongs;
        this.listener = listener;
    }

    @NonNull
    @androidx.annotation.NonNull
    @Override
    public JcSongsAdapter.SongAdapterViewHolder onCreateViewHolder(@NonNull @androidx.annotation.NonNull ViewGroup viewGroup, int i) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view=inflater.inflate(R.layout.songs_row,viewGroup,false);
        return new SongAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @androidx.annotation.NonNull JcSongsAdapter.SongAdapterViewHolder songAdapterViewHolder, int i) {

        Getsongs getsongs =arraylistsongs.get(i);

        if (getsongs != null){
            if (selectedPosition == i){
                songAdapterViewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, com.example.jean.jcplayer.R.color.colorPrimary));
                songAdapterViewHolder.iv_play_active.setVisibility(View.VISIBLE);
            }else {
                songAdapterViewHolder.itemView.setBackgroundColor(ContextCompat.getColor(context, com.example.jean.jcplayer.R.color.colorPrimaryDark));
                songAdapterViewHolder.iv_play_active.setVisibility(View.INVISIBLE);
            }
        }

        songAdapterViewHolder.tv_title.setText(getsongs.getSongTitle());
        songAdapterViewHolder.tv_artist.setText(getsongs.getArtist());
        String duration = Utility.converDuration(Long.parseLong(getsongs.getSongduration()));
        songAdapterViewHolder.tv_duration.setText(duration);

        songAdapterViewHolder.bind(getsongs, listener);

    }

    @Override
    public int getItemCount() {
        return arraylistsongs.size();
    }

    public class SongAdapterViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title,tv_artist,tv_duration;
        ImageView iv_play_active;
        public SongAdapterViewHolder(@NonNull @androidx.annotation.NonNull View itemView) {
            super(itemView);

            tv_artist=itemView.findViewById(R.id.tv_artist);
            tv_duration=itemView.findViewById(R.id.tv_duration);
            tv_title=itemView.findViewById(R.id.tv_title);
            iv_play_active=itemView.findViewById(R.id.play_active);


        }

        public void bind(Getsongs getsongs, RecyclerItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClickListener(getsongs,getAdapterPosition());
                }
            });
        }
    }


    public interface RecyclerItemClickListener {

        void onClickListener(Getsongs getsongs, int position);
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }
}
