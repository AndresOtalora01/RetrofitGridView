package com.example.retrofitgridview.ui.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.retrofitgridview.R;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.Calendar;

public class FilterDialog extends AppCompatDialogFragment {
    private SwitchMaterial swCopyRight;
    private TextView tvFromYear;
    private TextView tvToYear;
    private FilterDialogListener listener;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.filter_dialog, null);
        swCopyRight = view.findViewById(R.id.switchCopyright);
        tvFromYear = view.findViewById(R.id.tvFromYear);
        tvToYear = view.findViewById(R.id.tvToYear);

        clickListener(tvFromYear);
        clickListener(tvToYear);

        builder.setView(view)
                .setTitle("Filtros")
                .setNegativeButton("Cancelar", (dialog, which) -> {

                })
                .setPositiveButton("Aplicar", (dialog, which) -> {
                    Boolean copyright = swCopyRight.isChecked();
                    String fromYear = tvFromYear.getText().toString();
                    String toYear = tvToYear.getText().toString();
                    listener.getFilters(copyright, fromYear, toYear);
                });


        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (FilterDialogListener) context;
    }

    public interface FilterDialogListener {
        void getFilters(Boolean copyright, String fromYear, String toYear);
    }

    private DatePickerDialog createDialogWithoutDateField(TextView tvYear) {
        DatePickerDialog dpd = new DatePickerDialog(getContext());

        dpd.getDatePicker().getTouchables().get(0).performClick();
        Calendar calendar = Calendar.getInstance();
        dpd.getDatePicker().init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                (view, year, monthOfYear, dayOfMonth) ->
                {
                    Log.d("fecha", year + "");
                    tvYear.setText(year + "");
                    dpd.dismiss();
                });

        return dpd;
    }

    private void clickListener(View view) {
        view.setOnClickListener(v -> {
            createDialogWithoutDateField((TextView) v).show();
        });
    }

}
