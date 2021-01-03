package com.example.retrofitgridview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class BookActivity extends AppCompatActivity {

    private Book book;
    private SeekBar sbTextSize;
    private PageFragmentAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            book = (Book) intent.getSerializableExtra("data");
            String message = "Título: " + book.getTitle();
            Log.d("Buscaminas", message);
        }

        Button btnReturn = (Button) findViewById(R.id.returnButton);
        btnReturn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        ViewPager vpPager = (ViewPager) findViewById(R.id.vpContent);


        Log.d("cacaInfo", "onCreate");
        new DownloadFileFromURL(new LoaderListener() {
            @Override
            public void onLoaded(String result) {
                Log.d("cacaInfo", String.valueOf(result.length()));
                adapterViewPager = new PageFragmentAdapter(getSupportFragmentManager(), 600, result);
                vpPager.setAdapter(adapterViewPager);
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getApplicationContext(), "ERROR   " + error, Toast.LENGTH_LONG).show();
            }
        }).execute(book.getFormats().getTextPlain());

        sbTextSize = (SeekBar) findViewById(R.id.sbTextSize);
        sbTextSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (adapterViewPager != null)
                    adapterViewPager.setNewSize(progress + 12);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }


    public static class DownloadFileFromURL extends AsyncTask<String, String, String> {
        private LoaderListener listener;

        public DownloadFileFromURL(LoaderListener listener) {
            this.listener = listener;
        }

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            Log.d("cacaInfo", "onPreExcute");
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... urls) {
            Log.d("cacaInfo", "doInBackground");
            String line = "", result = "";
            try {
                URL url = new URL(urls[0]);
                URLConnection urlConnection = url.openConnection();
                InputStream is = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));


                while (line != null) {
                    line = br.readLine();
                    if (line != null) {
                        if (line.isEmpty()) result += "\n";
                        else result += " " + line;
                        Log.d("cacaInfo", line);
                    }
                }
                br.close();
                return result;
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
                listener.onFailure(e.getMessage());
            }
            return result;
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String content) {
            // dismiss the dialog after the file was downloaded
            listener.onLoaded(content);
        }
    }

    public void returnMenu(View view) {

    }

}