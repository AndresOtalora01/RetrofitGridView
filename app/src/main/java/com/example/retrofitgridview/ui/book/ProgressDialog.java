package com.example.retrofitgridview.ui.book;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.retrofitgridview.R;

public class ProgressDialog {
    private Dialog dialog;
    private static ProgressDialog progressDialog;
    private Context context;
    private int maxProgress;

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
        }
        dialog.show();
        return this;
    }

    public void dismiss() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void setProgress(int progress) {
        if (dialog != null) {
            int progressValue = progress * 100 / maxProgress;
            ProgressBar progressBar = (ProgressBar) dialog.findViewById(R.id.progressBar);
            TextView tvProgressKb = (TextView) dialog.findViewById(R.id.progressKb);
            progressBar.setProgress(progressValue);
            tvProgressKb.setText(toKbConverter(progress) + "/" + toKbConverter(maxProgress) + "Kbs");
            tvProgressKb.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);

        }
    }

    public void setMaxProgress(int max) {
        this.maxProgress = max;
    }

    public int toKbConverter(int value) {
        value = ((value != 0) ? value / 1024 : 0);
        return value;
    }

}
