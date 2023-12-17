package com.example.onlinemusicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.example.onlinemusicapp.Adapter.JcSongsAdapter;
import com.example.onlinemusicapp.Adapter.OnlineSongAdapter;
import com.example.onlinemusicapp.Model.Getsongs;
import com.example.onlinemusicapp.OfflineMusicPlayer.OfflineMusicPlayer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import ru.github.igla.ferriswheel.FerrisWheelView;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    JcSongsAdapter jcSongsAdapter;
    OnlineSongAdapter onlineSongAdapter;
    DatabaseReference databaseReference;
    ProgressDialog progressDialog;
    private ArrayList<Getsongs> getsongs;
    int currentindex;
    Boolean checkin = false;
    FerrisWheelView ferrisWheelView;
    Button offlinebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.primarydark));
        }

        ferrisWheelView=findViewById(R.id.ferrisWheelView);
        recyclerView=findViewById(R.id.recyclerview_id);
        offlinebtn=findViewById(R.id.offlinebtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //progressDialog = new ProgressDialog(this);
        getsongs = new ArrayList<>();
        ferrisWheelView.setVisibility(View.VISIBLE);
        ferrisWheelView.startAnimation();
        ferrisWheelView.setClickable(false);
        //progressDialog.setMessage("please wait...");
        //progressDialog.show();
        recyclerView.setAdapter(onlineSongAdapter);
        onlineSongAdapter = new OnlineSongAdapter(getsongs,getApplicationContext());
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

                }
                recyclerView.setAdapter(onlineSongAdapter);
                //progressDialog.dismiss();
                ferrisWheelView.stopAnimation();
                ferrisWheelView.setVisibility(View.GONE);
                if(checkPermission() == false){
                    requestPermission();
                    return;
                }
                if (checkin){
                    recyclerView.setAdapter(onlineSongAdapter);
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

        offlinebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(MainActivity.this, OfflineMusicPlayer.class);
                startActivity(i);
            }
        });
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