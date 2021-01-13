package com.example.retrofitgridview.ui.main;

import androidx.appcompat.app.AppCompatActivity;

import com.example.retrofitgridview.ui.book.BooksManagement;
import com.example.retrofitgridview.ui.book.ProgressDialog;

public class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog = ProgressDialog.init(this);
    protected BooksManagement booksManagement = BooksManagement.init(this);

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
