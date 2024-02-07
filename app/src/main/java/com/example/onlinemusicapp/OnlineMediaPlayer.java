package com.example.onlinemusicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.onlinemusicapp.BottomSheet.BottomSheetFragment;
import com.example.onlinemusicapp.Model.Getsongs;
import com.example.onlinemusicapp.OfflineMusicPlayer.AudioModel;
import com.example.onlinemusicapp.OfflineMusicPlayer.MusicPlayerActivity;
import com.example.onlinemusicapp.OfflineMusicPlayer.MyMediaPlayer;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class OnlineMediaPlayer extends AppCompatActivity {

    TextView titleTv,currentTimeTv,totalTimeTv;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,previousBtn,musicIcon;
    ArrayList<Getsongs> songsList;
    Getsongs currentSong;
    ProgressBar progressBar;
    Button bottomsheet;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();
    int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_media_player);
        //getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.colorAccent));
        }

        titleTv = findViewById(R.id.song_titleol);
        currentTimeTv = findViewById(R.id.current_timeol);
        totalTimeTv = findViewById(R.id.total_timeol);
        seekBar = findViewById(R.id.seek_barol);
        pausePlay = findViewById(R.id.pause_playol);
        nextBtn = findViewById(R.id.nextol);
        previousBtn = findViewById(R.id.previousol);
        musicIcon = findViewById(R.id.music_icon_bigol);
        progressBar=findViewById(R.id.playsongsprgrbar);
        bottomsheet=findViewById(R.id.btnShowBottomSheet);

        titleTv.setSelected(true);
        songsList = (ArrayList<Getsongs>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();

        OnlineMediaPlayer.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMSS(mediaPlayer.getCurrentPosition()+""));

                    if(mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.ic_pause);
                        musicIcon.setRotation(x++);
                    }else{
                        pausePlay.setImageResource(R.drawable.ic_play);
                        musicIcon.setRotation(0);
                    }

                }
                new Handler().postDelayed(this,100);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bottomsheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });

    }

    void setResourcesWithMusic(){
        progressBar.setVisibility(View.VISIBLE);
        pausePlay.setVisibility(View.GONE);
        currentSong = songsList.get(MyMediaPlayer.currentIndex);

        titleTv.setText(currentSong.getSongTitle());

        totalTimeTv.setText(convertToMMSS(currentSong.getsongDuration()));

        pausePlay.setOnClickListener(v-> pausePlay());
        nextBtn.setOnClickListener(v-> playNextSong());
        previousBtn.setOnClickListener(v-> playPreviousSong());

        playMusic();


    }


    private void playMusic(){
        if (mediaPlayer == null) {
            mediaPlayer = MyMediaPlayer.getInstance();
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getSonglink());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    progressBar.setVisibility(View.GONE);
                    pausePlay.setVisibility(View.VISIBLE);
                    mediaPlayer.start();
                    seekBar.setProgress(0);
                    seekBar.setMax(mediaPlayer.getDuration());
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void playNextSong(){

        if(MyMediaPlayer.currentIndex== songsList.size()-1)
            return;
        MyMediaPlayer.currentIndex +=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void playPreviousSong(){
        if(MyMediaPlayer.currentIndex== 0)
            return;
        MyMediaPlayer.currentIndex -=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}