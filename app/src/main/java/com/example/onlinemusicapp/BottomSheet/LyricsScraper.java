package com.example.onlinemusicapp.BottomSheet;

import static com.airbnb.lottie.L.TAG;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LyricsScraper {

    private static final String apiKey = "017f4c118fdaba5612b549a5d542d851";
    private TextView textView;

    public static void searchAndPrintLyrics(String songTitle, String artistName, TextView textView) {
        new LyricsAsyncTask(textView).execute(songTitle, artistName);
    }

    private static class LyricsAsyncTask extends AsyncTask<String, Void, String> {
        private TextView textView;
        public LyricsAsyncTask(TextView textView) {
            this.textView = textView;
        }

        @Override
        protected String doInBackground(String... params) {
            String songTitle = params[0];
            String artistName = params[1];

            try {
                // Construct the Musixmatch API URL
                String apiUrl = "https://api.musixmatch.com/ws/1.1/matcher.lyrics.get?" +
                        "q_track=" + songTitle +
                        "&q_artist=" + artistName +
                        "&apikey=" + apiKey;

                URL url = new URL(apiUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }

                    bufferedReader.close();

                    JSONObject jsonResponse = new JSONObject(stringBuilder.toString());
                    JSONObject message = jsonResponse.getJSONObject("message");
                    JSONObject body = message.getJSONObject("body");
                    JSONObject lyrics = body.getJSONObject("lyrics");

                    return lyrics.getString("lyrics_body");

                } finally {
                    urlConnection.disconnect();
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (textView != null) {
                if (result != null) {
                    textView.setText(result);
                } else {
                    textView.setText("Lyrics not found");
                }
            } else {
                // Handle the case where textView is null
                Log.e(TAG, "TextView is null");
            }
        }
    }
}
