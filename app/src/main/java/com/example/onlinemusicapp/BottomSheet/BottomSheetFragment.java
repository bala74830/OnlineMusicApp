package com.example.onlinemusicapp.BottomSheet;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlinemusicapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);

        SharedPreferences settings;
        SharedPreferences.Editor editor;
        String art,titl;

        settings = this.getActivity().getSharedPreferences("MySharedPref", MODE_PRIVATE);
        editor = settings.edit();
        art=settings.getString("art","");
        titl=settings.getString("tit","");
        // Set your lyrics here
        String lyrics = "Your lyrics go here...";
        //String songTitle = gs.getSongTitle();
        //String artistName = gs.getArtist();
        String songTitle = art;
        String artistName = titl;
        TextView tvLyrics = view.findViewById(R.id.lyricsTextView);
        LyricsScraper.searchAndPrintLyrics(songTitle, artistName, tvLyrics);
        return view;
    }
}