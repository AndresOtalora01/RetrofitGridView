package com.example.retrofitgridview;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import androidx.annotation.InspectableProperty;
import androidx.recyclerview.widget.DefaultItemAnimator;

public class ProgressDialog {
    private Dialog dialog;
    private static ProgressDialog progressDialog;
    private Context context;

    public static ProgressDialog init(Context context) {
        progressDialog = new ProgressDialog();
        progressDialog.context = context;
        return progressDialog;
    }

    public ProgressDialog show() {
        if (dialog == null) {
            dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.loading_book);
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }
        return this;
    }

    public void dismiss() {
        if(dialog!= null) {
            dialog.dismiss();
        }
    }

    public void setProgress(int progress) {
        if(dialog != null) {
           ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
           progressBar.setVisibility(View.VISIBLE);
           progressBar.setProgress(progress);
        }
    }

    public void setMaxProgress(int max) {
        if(dialog != null) {
            ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(max);
        }
    }

}
