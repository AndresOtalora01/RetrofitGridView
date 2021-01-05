package com.example.retrofitgridview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BookActivity extends BaseActivity {

    private Book book;
    private SeekBar sbTextSize;
    private PageFragmentAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Intent intent = getIntent();
        showProgressDialog();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (intent.getExtras() != null) {
            book = (Book) intent.getSerializableExtra("data");
            setTitle(book.getTitle());
        }


        ViewPager vpPager = (ViewPager) findViewById(R.id.vpContent);


        Log.d("cacaInfo", "onCreate");

        if (book.getFormats().getTextPlain() != null) {
            new DownloadFileFromURL(this, book.getFormats().getTextPlain(), new LoaderListener() {
                @Override
                public void onLoaded(String result) {
                    hideProgressDialog();
                    Log.d("cacaInfo", String.valueOf(result.length()));
                    adapterViewPager = new PageFragmentAdapter(getSupportFragmentManager(), 600, result, book.getFormats().getImage());
                    vpPager.setAdapter(adapterViewPager);
                }

                @Override
                public void onFailure(String error) {
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), "ERROR   " + error, Toast.LENGTH_LONG).show();
                }
            }).execute();

        }

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

        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    sbTextSize.setVisibility(View.GONE);
                else
                    sbTextSize.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public static class DownloadFileFromURL extends AsyncTask<String, Integer, String> {
        private LoaderListener listener;
        private BaseActivity baseActivity;
        private String urlString = "";


        public DownloadFileFromURL(BaseActivity baseActivity, String url, LoaderListener listener) {
            this.listener = listener;
            this.baseActivity = baseActivity;
            this.urlString = url;
        }


        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            Log.d("cacaInfo", "onPreExcute");
            super.onPreExecute();
            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... urls) {
            Log.d("cacaInfo", "doInBackground");
            String line = "", result = "";
            try {
                int progress = 0;
                URL url = new URL(urlString);
                URLConnection urlConnection = url.openConnection();
                InputStream is = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                int bookSize = urlConnection.getContentLength();
                while (line != null) {
                    line = br.readLine();
                    if (line != null) {
                        if (line.isEmpty()) result += "\n";

                        else result += " " + line;
                        progress = ((result.length() * 100) / bookSize);
                        publishProgress(progress);
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

        protected void onProgressUpdate(Integer... progress) {
            baseActivity.setDialogProgress(progress[0]);
        }
    }

    public void returnMenu(View view) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}