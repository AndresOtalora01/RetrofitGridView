package com.example.retrofitgridview;

import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog = ProgressDialog.init(this);


    protected void showProgressDialog() {
        progressDialog.show();
    }

    protected void hideProgressDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public void setDialogProgress(int progress) {
        if (progressDialog != null) {
            progressDialog.setProgress(progress);
        }
    }

    public void setDialogMaxProgress(int max) {
        if (progressDialog != null) {
            progressDialog.setMaxProgress(max);
        }
    }
}
