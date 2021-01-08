package com.example.retrofitgridview.ui.book;

import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.network.LoaderListener;
import com.example.retrofitgridview.R;
import com.example.retrofitgridview.ui.main.BaseActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

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
        long startTime = new Date().getTime();
        Intent intent = getIntent();
        showProgressDialog();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); // this allows to load content from "unsecure" websites.
        StrictMode.setThreadPolicy(policy);

        if (intent.getExtras() != null) {
            book = (Book) intent.getSerializableExtra("data");
            setTitle(book.getTitle());
        }
        ViewPager vpPager = (ViewPager) findViewById(R.id.vpContent);
        String content = "";
        if (book.getFormats().getTextPlain() != null && book.getFormats().getTextPlain().endsWith(".txt") ) {
            content = book.getFormats().getTextPlain();
        } else if (book.getFormats().getTextPlainAscii() != null && book.getFormats().getTextPlainAscii().endsWith(".txt")) {
            content = book.getFormats().getTextPlainAscii();
        } else if (book.getFormats().getTextPlainIso() != null && book.getFormats().getTextPlainIso().endsWith(".txt") ) {
            content = book.getFormats().getTextPlainIso();
        }
        new DownloadFileFromURL(this, content, new LoaderListener() {
            @Override
            public void onLoaded(String result) {
                hideProgressDialog();
                adapterViewPager = new PageFragmentAdapter(getSupportFragmentManager(), 600, result, book.getFormats().getImage());
                vpPager.setAdapter(adapterViewPager);
            }
            @Override
            public void onFailure(String error) {
                hideProgressDialog();
                Toast.makeText(getApplicationContext(), "ERROR   " + error, Toast.LENGTH_LONG).show();
            }
        }).execute();
        long endTime = new Date().getTime();
        Log.d("tiempo2",  endTime - startTime +"");
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
            String line = "", result = "";
            try {
                int progress = 0;
                URL url = new URL(urlString);
                URLConnection urlConnection = url.openConnection();
                InputStream is = urlConnection.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                int bookSize = urlConnection.getContentLength();
                baseActivity.setDialogMaxProgress(bookSize);
                while (line != null) {
                    line = br.readLine();
                    if (line != null) {
                        if (line.isEmpty()) result += "\n";
                        else result += " " + line;
                        progress = (result.length());
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
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}