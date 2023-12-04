package com.example.onlinemusicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.onlinemusicapp.Adapter.JcSongsAdapter;
import com.example.onlinemusicapp.Model.Getsongs;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SongsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ProgressBar progressBar;
    Boolean checkin = false;
    List<Getsongs> mupload;
    JcSongsAdapter jcSongsAdapter;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    JcPlayerView jcPlayerView;
    ArrayList<JcAudio> jcAudios = new ArrayList<>();
    private int currentindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs);

        recyclerView=findViewById(R.id.recyclerview_id);
        progressBar=findViewById(R.id.progressbarshowsong);
        jcPlayerView=findViewById(R.id.jcplayer);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mupload = new ArrayList<>();
        recyclerView.setAdapter(jcSongsAdapter);
        jcSongsAdapter = new JcSongsAdapter(getApplicationContext(), mupload, new JcSongsAdapter.RecyclerItemClickListener() {
            @Override
            public void onClickListener(Getsongs getsongs, int position) {

                changeselectedsong(position);

                jcPlayerView.playAudio(jcAudios.get(position));
                jcPlayerView.setVisibility(View.VISIBLE);
                jcPlayerView.createNotification();

            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("songs");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                mupload.clear();
                for (DataSnapshot dss : snapshot.getChildren()){
                    Getsongs getsongs = dss.getValue(Getsongs.class);
                    getsongs.setMkey(dss.getKey());
                    currentindex = 0;
                    final String s = getIntent().getExtras().getString("songsCategory");
                    if (s.equals(getsongs.getSongscategory())){
                        mupload.add(getsongs);
                        checkin=true;
                        jcAudios.add(JcAudio.createFromURL(getsongs.getSongTitle(),getsongs.getSonglink()));
                    }
                }

                jcSongsAdapter.setSelectedPosition(0);
                recyclerView.setAdapter(jcSongsAdapter);
                jcSongsAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);

                if (checkin){
                    jcPlayerView.initPlaylist(jcAudios,null);
                }
                else {
                    Toast.makeText(SongsActivity.this, "there is no songs", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                progressBar.setVisibility(View.GONE);

            }
        });
    }

    public void changeselectedsong(int index){
        jcSongsAdapter.notifyItemChanged(jcSongsAdapter.getSelectedPosition());
        currentindex = index;
        jcSongsAdapter.setSelectedPosition(currentindex);
        jcSongsAdapter.notifyItemChanged(currentindex);

    }
}