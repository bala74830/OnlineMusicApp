package com.example.onlinemusicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.arges.sepan.argmusicplayer.Models.ArgAudio;
import com.arges.sepan.argmusicplayer.Models.ArgAudioList;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerLargeView;
import com.arges.sepan.argmusicplayer.PlayerViews.ArgPlayerSmallView;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.onlinemusicapp.Adapter.JcSongsAdapter;
import com.example.onlinemusicapp.Adapter.RecyclerViewAdapter;
import com.example.onlinemusicapp.Model.Getsongs;
import com.example.onlinemusicapp.Model.Upload;
import com.example.onlinemusicapp.OfflineMusicPlayer.OfflineMusicPlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ru.github.igla.ferriswheel.FerrisWheelView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    JcSongsAdapter jcSongsAdapter;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private List<Getsongs> getsongs;
    //JcPlayerView jcPlayerView;
    ArgPlayerLargeView musicPlayer;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();

    ArrayList<ArgAudio> argaudios = new ArrayList<>();
    ArgAudioList myplaylist = new ArgAudioList(true);
    int currentindex;
    Boolean checkin = false;
    FerrisWheelView ferrisWheelView;
    Button offlinebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recyclerview_id);
        //jcPlayerView=findViewById(R.id.jcplayer);
        musicPlayer=findViewById(R.id.argmusicplayer);
        ferrisWheelView=findViewById(R.id.ferrisWheelView);
        offlinebtn=findViewById(R.id.offlinebtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        //progressDialog = new ProgressDialog(this);
        getsongs = new ArrayList<>();
        ferrisWheelView.setVisibility(View.VISIBLE);
        ferrisWheelView.startAnimation();
        ferrisWheelView.setClickable(false);
        //progressDialog.setMessage("please wait...");
        //progressDialog.show();
        recyclerView.setAdapter(jcSongsAdapter);
        jcSongsAdapter = new JcSongsAdapter(getApplicationContext(), getsongs, new JcSongsAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(Getsongs getsongs, int position) {
                changeselectedsong(position);

//                jcPlayerView.playAudio(jcAudios.get(position));
//                jcPlayerView.setVisibility(View.VISIBLE);
//                jcPlayerView.createNotification();
                musicPlayer.play(argaudios.get(position));
                musicPlayer.setVisibility(View.VISIBLE);
                musicPlayer.enableNotification(MainActivity.this);
                myplaylist.add(argaudios.get(position));
            }
        });
        databaseReference = FirebaseDatabase.getInstance().getReference("songs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getsongs.clear();
                for (DataSnapshot dss : snapshot.getChildren()){
                    Getsongs gsongs = dss.getValue(Getsongs.class);
                    gsongs.setMkey(dss.getKey());
                    currentindex=0;
                    getsongs.add(gsongs);
                    checkin=true;
                   // jcAudios.add(JcAudio.createFromURL(gsongs.getSongTitle(),gsongs.getSonglink()));
                    argaudios.add(ArgAudio.createFromURL(gsongs.getArtist(),gsongs.getSongTitle(),gsongs.getSonglink()));
                }
                jcSongsAdapter.setSelectedPosition(0);
                recyclerView.setAdapter(jcSongsAdapter);
                jcSongsAdapter.notifyDataSetChanged();
                //progressDialog.dismiss();
                ferrisWheelView.stopAnimation();
                ferrisWheelView.setVisibility(View.GONE);
                if(checkPermission() == false){
                    requestPermission();
                    return;
                }
                if (checkin){
                    //jcPlayerView.initPlaylist(jcAudios,null);
                    musicPlayer.loadPlaylist(myplaylist);
                }
                else {
                    Toast.makeText(MainActivity.this, "there is no songs", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //progressDialog.dismiss();
                ferrisWheelView.stopAnimation();
                ferrisWheelView.setVisibility(View.GONE);
            }
        });
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                getsongs.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren())
//                {
//                    Getsongs upload = dataSnapshot.getValue(Getsongs.class);
//                    getsongs.add(upload);
//                }
//                jcSongsAdapter = new JcSongsAdapter(MainActivity.this,getsongs);
//                recyclerView.setAdapter(jcSongsAdapter);
//                jcSongsAdapter.notifyDataSetChanged();
//                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//                progressDialog.dismiss();
//
//            }
//        });

        offlinebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, OfflineMusicPlayer.class);
                startActivity(i);
                finishAffinity();
            }
        });
    }
    public void changeselectedsong(int index){
        jcSongsAdapter.notifyItemChanged(jcSongsAdapter.getSelectedPosition());
        currentindex = index;
        jcSongsAdapter.setSelectedPosition(currentindex);
        jcSongsAdapter.notifyItemChanged(currentindex);

    }

    boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if(result == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            Toast.makeText(MainActivity.this,"READ PERMISSION IS REQUIRED,PLEASE ALLOW FROM SETTINGS",Toast.LENGTH_SHORT).show();
        }else
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
    }
}